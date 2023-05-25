package com.yanhuanxy.multifunservice.expression;

import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifunservice.expression.enums.FunctionTypeEnums;
import com.yanhuanxy.multifunservice.expression.enums.ParamDataTypeEnums;
import com.yanhuanxy.multifunservice.expression.enums.SpecialCharTypeEnums;
import com.yanhuanxy.multifunservice.expression.enums.TimeKeyWordEnums;
import com.yanhuanxy.multifunservice.expression.vo.DefaultDataColumnVO;
import com.yanhuanxy.multifunservice.expression.vo.ExpressionParams;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 表达式转换识别
 */
public class InitExpressionParser {
    protected static final Integer DEF_INDEX = 0;
    protected static final String DEF_NULL_STR = " ";

    private static final Logger logger = LoggerFactory.getLogger(InitExpressionParser.class);

    private final List<DefaultDataColumnVO> dpuDataColumnVOS;

    private final Map<String, ExpressionParams> expressionTokenizer = new HashMap<>(16);

    public InitExpressionParser(List<DefaultDataColumnVO> dpuDataColumnVOS) {
        this.dpuDataColumnVOS = dpuDataColumnVOS;
    }


    /**
     * 处理公式
     * @param uid 重复公式
     * @param expressionVal 自定义公式字符串
     * @param columnDataType 表达式结果类型
     */
    public ExpressionParams initExpression(Long uid, String expressionVal, ParamDataTypeEnums columnDataType){
        if(ObjectUtils.isEmpty(expressionVal)){
            throw new BaseRuntimeException("自定义公式为空！");
        }
        Set<String> expressionKeys = expressionTokenizer.keySet();

        return expressionKeys.stream().filter(item -> {
            int endIndex = item.lastIndexOf("_");
            String oldExpressionVal = item.substring(0, endIndex);
            return expressionVal.equals(oldExpressionVal);
        }).findAny().map(expressionTokenizer::get).orElse(doParserExpression(uid, expressionVal, columnDataType));
    }

    /**
     * 处理表达式
     * @param uid  唯一
     * @param expressionVal 表达式
     * @param columnDataType 表达式数据类型
     */
    private ExpressionParams doParserExpression (Long uid, String expressionVal, ParamDataTypeEnums columnDataType){

        // 转换解析 表达式
        ExpressionParams expressionParams = doParserExpression(expressionVal, columnDataType);

        // 存储当前公式 信息
        String expressionKey = expressionVal+"_" + uid;
        expressionTokenizer.put(expressionKey, expressionParams);
        return expressionParams;
    }

    /**
     * 处理表达式
     * @param expressionVal 表达式
     * @param columnDataType 表达式数据类型
     */
    private ExpressionParams doParserExpression(String expressionVal, ParamDataTypeEnums columnDataType){
        ExpressionParams expressionParams = new ExpressionParams();
        expressionParams.setExpression(expressionVal);
        // 输出值的类型
        expressionParams.setExpressionDataType(columnDataType);
        // 公式格式化
        String formatExpression = handleExpressionFormat(expressionVal);
        expressionParams.setFormatExpression(formatExpression);
        // 转换为 后缀表达式
        String expressionSuffix = handleExpressionConvertSuffix(formatExpression);
        // 切割 后缀表达式
        List<String> expressionSuffixList = Arrays.asList(expressionSuffix.split(DEF_NULL_STR));
        expressionParams.setExpressionSuffixList(expressionSuffixList);
        // 处理 后缀表达式 获取 字段对应的数据类型
        List<String> expressionField = expressionSuffixList.stream().filter(item -> !SpecialCharTypeEnums.checkCharIsSymbol(item)).collect(Collectors.toList());

        Map<String, ExpressionParams> columnDataTypes = doParserExpressionColumn(expressionField);
        // 处理 解析函数
        expressionParams.setExpressionParams(columnDataTypes);
        return expressionParams;
    }

    /**
     * 格式化自定义表达式
     *
     * @param expressionVal 表达式
     * @return String
     */
    protected String handleExpressionFormat(String expressionVal) {
        expressionVal = expressionVal.replaceAll(DEF_NULL_STR, "");
        char[] tmpValChars = expressionVal.toCharArray();
        StringBuilder tmpField = new StringBuilder();
        Stack<String> stack = new Stack<>();
        // 解析 自定义表达式
        for (int i = 0, len = tmpValChars.length; i < len; i++) {
            char tmpChar = tmpValChars[i];
            SpecialCharTypeEnums expressionType = SpecialCharTypeEnums.getExpressionType(String.valueOf(tmpChar));
            // 不能转换成操作符 添加StringBuilder
            switch (expressionType){
                case OTHER:
                    tmpField.append(tmpChar);
                    break;
                case SUBTRACT:
                    boolean addToField = (i == 0) || (!stack.empty() && !SpecialCharTypeEnums.checkSubtractIsNegative(String.valueOf(tmpValChars[i - 1])));
                    if(addToField){
                        // 是减号 添加到 tmpField
                        tmpField.append(tmpChar);
                        break;
                    }
                case LPARENTHESES:
                    if(FunctionTypeEnums.checkFormulaIsExit(tmpField.toString())){
                        // 一组 函数配置
                        i = handleExpressionFunField(tmpValChars, i, tmpField);
                        stack.push(tmpField.toString());
                        tmpField.setLength(DEF_INDEX);
                        break;
                    }
                default:
                    // 字段入栈 清空 tmpField
                    handleExpressionField(stack, tmpField);
                    // 是操作符 直接入栈
                    stack.push(String.valueOf(tmpChar));
                    break;
            }
            if (i + 1 == len) {
                handleExpressionField(stack, tmpField);
            }
        }
        return String.join(DEF_NULL_STR, stack);
    }

    /**
     * 处理字段
     *
     * @param stack    字段栈
     * @param tmpField 字段信息
     */
    private void handleExpressionField(Stack<String> stack, StringBuilder tmpField) {
        if (tmpField.length() > 0) {
            // 字段入栈
            String fieldVal = tmpField.toString().trim();
            stack.push(fieldVal);
            tmpField.setLength(DEF_INDEX);
        }
    }

    /**
     * 处理函数 将函数整体从表达式内提取出来
     *
     * @return int
     */
    private int handleExpressionFunField(char[] originValChars, int currentIndex, StringBuilder tmpField) {
        // 寻找 闭环 右括号
        int lem = originValChars.length;
        int tmpIndex = currentIndex;
        Stack<String> symbolStack = new Stack<>();
        while (tmpIndex < lem) {
            String tmpVal = String.valueOf(originValChars[tmpIndex]);
            tmpField.append(originValChars[tmpIndex]);
            SpecialCharTypeEnums specialCharType = SpecialCharTypeEnums.getExpressionType(tmpVal);
            switch (specialCharType){
                case LPARENTHESES:
                    symbolStack.push(tmpVal);
                    break;
                case RPARENTHESES:
                    symbolStack.pop();
                    break;
                default:
                    break;
            }
            if (symbolStack.empty()) {
                break;
            }
            ++tmpIndex;
        }

        if (symbolStack.isEmpty()) {
            return tmpIndex;
        } else {
            throw new BaseRuntimeException("自定义公式结构错误！");
        }
    }


    /**
     * 处理自定义公式 转为 后缀表达式
     *
     * @param expressionVal 表达式
     */
    protected String handleExpressionConvertSuffix(String expressionVal) {
        // 格式化 表达式
        StringTokenizer expressionValTokenizer = new StringTokenizer(expressionVal);
        Stack<String> symbolStack = new Stack<>();
        StringBuilder suffixBuf = new StringBuilder();
        while (expressionValTokenizer.hasMoreTokens()) {
            String tmpVal = expressionValTokenizer.nextToken();

            SpecialCharTypeEnums expressionType = SpecialCharTypeEnums.getExpressionType(tmpVal);
            switch (expressionType){
                case OTHER:
                    // 除枚举外的 数值 、字母
                    suffixBuf.append(tmpVal).append(DEF_NULL_STR);
                    break;
                case LPARENTHESES:
                    symbolStack.push(tmpVal);
                    break;
                case RPARENTHESES:
                    String topItem;
                    while (!symbolStack.empty() && !(topItem = symbolStack.pop()).equals(SpecialCharTypeEnums.LPARENTHESES.getSymbol())) {
                        suffixBuf.append(topItem).append(DEF_NULL_STR);
                    }
                    break;
                default:
                    while (!symbolStack.empty() && SpecialCharTypeEnums.getPriority(tmpVal) <= SpecialCharTypeEnums.getPriority(symbolStack.peek())) {
                        suffixBuf.append(symbolStack.pop()).append(DEF_NULL_STR);
                    }
                    symbolStack.push(tmpVal);
                    break;
            }
        }
        // 将特殊字符 按 空格 关联 添加到表达式最后
        String specialChars = String.join(DEF_NULL_STR, symbolStack);
        symbolStack.clear();
        suffixBuf.append(specialChars);

        return suffixBuf.toString();
    }


    /**
     * 获取表达式内使用到的字段 添加字段数据类型
     * @param columnFields 后缀表达式
     * @return map
     */
    private Map<String, ExpressionParams> doParserExpressionColumn(List<String> columnFields){
        Map<String, ExpressionParams> columnDataTypes = new HashMap<>(16);

        for (String columnName : columnFields) {
            boolean isNumber = NumberUtils.isCreatable(columnName);
            if (isNumber) {
                columnDataTypes.put(columnName, new ExpressionParams(columnName, ParamDataTypeEnums.NUMBER));
                continue;
            }
            //时间关键字
            if (TimeKeyWordEnums.exist(columnName)) {
                columnDataTypes.put(columnName, new ExpressionParams(columnName, ParamDataTypeEnums.DATE));
                continue;
            }
            // 判断为表达式 整个表达式计算结果的数据类型
            FunctionTypeEnums formulaTypeEnum = FunctionTypeEnums.getFormulaDataType(columnName);
            if (!FunctionTypeEnums.OTHER.equals(formulaTypeEnum)) {
                ExpressionParams funExpressionParam = doExpressionFunField(columnName, formulaTypeEnum);
                columnDataTypes.put(columnName, funExpressionParam);
                continue;
            }
            // 字段
            ParamDataTypeEnums formulaDataType;
            DefaultDataColumnVO tmpDpuDataColumnVO = dpuDataColumnVOS.stream().filter(dpuDataColumnVO -> Objects.equals(dpuDataColumnVO.getColumnName(), columnName)).findAny().orElseThrow();
            if(ParamDataTypeEnums.isNumber(tmpDpuDataColumnVO.getColumnDataType())){
                formulaDataType = ParamDataTypeEnums.NUMBER;
            }else if(ParamDataTypeEnums.isDate(tmpDpuDataColumnVO.getColumnDataType())){
                formulaDataType = ParamDataTypeEnums.DATE;
            }else {
                throw new BaseRuntimeException("自定义公式字段异常！字段数据类型只能是时间或数值");
            }
            columnDataTypes.put(columnName, new ExpressionParams(columnName, Boolean.TRUE, formulaDataType));
        }

        return columnDataTypes;
    }


    /**
     * 处理函数表达式
     * @param expression 表达式
     */
    private ExpressionParams doExpressionFunField(String expression, FunctionTypeEnums formulaTypeEnum){
        char[] tmpValChars = expression.toCharArray();
        String formulaType = formulaTypeEnum.toString();
        int formulaLen = formulaType.length();
        // 解析 函数表达式
        ExpressionParams expressionParams = new ExpressionParams();
        expressionParams.setExpression(expression);
        // 获取函数参数字段表达式
        List<String> funField = getFunctionExpressionField(tmpValChars, formulaLen);
        expressionParams.setFormatExpression(expression);
        expressionParams.setExpressionDataType(formulaTypeEnum.getDpuDataTypeEnum());
        expressionParams.setExpressionSuffixList(funField);
        // 解析函数参数表达式
        Map<String, ExpressionParams> funExpressionParams = new HashMap<>(funField.size());
        for (String funFieldExpressionVal : funField) {
            // 处理函数 参数表达式
            ExpressionParams funFieldExpressionParams = doParserExpression(funFieldExpressionVal, null);
            funExpressionParams.put(funFieldExpressionVal, funFieldExpressionParams);
        }
        expressionParams.setExpressionParams(funExpressionParams);

        return expressionParams;
    }


    /**
     * 处理函数 将函数参数整体从表达式内提取出来
     * 1、单个函数
     * 2、函数嵌套  sum(a+b)  ||  trunc(trunc(1.111m,4) /3, 1)
     *
     * @return int
     */
    private List<String> getFunctionExpressionField(char[] originValChars, int currentIndex) {
        // 寻找 闭环 右括号
        int lem = originValChars.length;
        int tmpIndex = currentIndex;
        // 用于存储 括号数 校验表达式是否正常
        Stack<String> symbolStack = new Stack<>();
        symbolStack.push(String.valueOf(originValChars[currentIndex]));
        StringBuilder tmpField = new StringBuilder();
        // 函数含有参数量
        List<String> resultFunField = new ArrayList<>();
        while (tmpIndex + 1 < lem) {
            ++tmpIndex;
            String tmpVal = String.valueOf(originValChars[tmpIndex]);
            if (Objects.equals(SpecialCharTypeEnums.LPARENTHESES.getSymbol(), tmpVal)) {
                symbolStack.push(tmpVal);
                if (FunctionTypeEnums.checkFormulaIsExit(tmpField.toString())) {
                    tmpIndex = handleExpressionFunField(originValChars, tmpIndex, tmpField);
                    tmpField.deleteCharAt(tmpField.length() -1);
                    tmpVal = String.valueOf(originValChars[tmpIndex]);
                }
            }

            if (Objects.equals(SpecialCharTypeEnums.RPARENTHESES.getSymbol(), tmpVal)) {
                symbolStack.pop();
                if (symbolStack.empty()) {
                    resultFunField.add(convertFunParamField(tmpField));
                    break;
                }
            }

            if (Objects.equals(tmpVal, ",") && symbolStack.size() == 1) {
                resultFunField.add(convertFunParamField(tmpField));
                continue;
            }
            tmpField.append(originValChars[tmpIndex]);
        }
        if (symbolStack.isEmpty()) {
            return resultFunField;
        } else {
            throw new BaseRuntimeException("自定义公式结构错误！");
        }
    }

    /**
     * 转换函数参数
     * @param tmpField              函数参数
     */
    private String convertFunParamField(StringBuilder tmpField) {
        String tmpExpression = tmpField.toString().trim();
        tmpField.setLength(DEF_INDEX);
        return tmpExpression;
    }
}

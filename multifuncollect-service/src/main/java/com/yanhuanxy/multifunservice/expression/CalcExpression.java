package com.yanhuanxy.multifunservice.expression;

import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifunservice.expression.enums.ParamDataTypeEnums;
import com.yanhuanxy.multifunservice.expression.vo.DefaultDataColumnVO;
import com.yanhuanxy.multifunservice.expression.vo.ExpressionConfigVO;
import com.yanhuanxy.multifunservice.expression.vo.ExpressionParams;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 计算表达式
 */
public class CalcExpression {

    private static final Logger logger = LoggerFactory.getLogger(CalcExpression.class);

    private final List<DefaultDataColumnVO> dpuDataColumnVOS;

    public CalcExpression(List<DefaultDataColumnVO> dpuDataColumnVOS) {
        this.dpuDataColumnVOS = dpuDataColumnVOS;
    }

    /**
     * 校验自定义公式格式是否正确
     * @param expressionVal 自定义公式
     */
    public void checkExpressionIsRight(String columnName, String columnDataType,String expressionVal){
        // 根据步骤ID查询对应表的字段信息
        boolean isExist = dpuDataColumnVOS.stream().anyMatch(dataColumnVO -> Objects.equals(dataColumnVO.getColumnName(), columnName));
        if(!isExist){
            throw new BaseRuntimeException("自定义公式目标字段不存在！");
        }
        InitExpressionParser dpuExpressionParser = new InitExpressionParser(dpuDataColumnVOS);
        ParamDataTypeEnums paramDataTypeEnum = ParamDataTypeEnums.getParamDataTypeEnum(columnDataType);
        ExpressionParams expressionParams = dpuExpressionParser.initExpression(0L, expressionVal, paramDataTypeEnum);
        executeCheckExpression(expressionParams);
    }

    /**
     * 预执行一次表达式
     * @param expressionParams 表达式键
     */
    private void executeCheckExpression(ExpressionParams expressionParams){
        // 检查自定义公式字段是否符合要求
        Map<String, ParamDataTypeEnums> columnDataTypes = expressionParams.getTableColumnList();
        // 生成模拟数据
        long currentSysDate = System.currentTimeMillis();
        Map<String,Object> tmpRowData = new HashMap<>(16);
        columnDataTypes.forEach((columnName, expressionDataType)->{
            if(ParamDataTypeEnums.DATE.equals(expressionDataType)){
                tmpRowData.put(columnName, new Timestamp(currentSysDate));
            }else if(ParamDataTypeEnums.NUMBER.equals(expressionDataType)) {
                tmpRowData.put(columnName, new BigDecimal("0"));
            }else{
                tmpRowData.put(columnName, "");
            }
        });
        try{
            // 预执行一次后缀表达式 出错 抛出表达式错误
            preExecuteCheckExpression(currentSysDate, tmpRowData, expressionParams);
        }catch (BaseRuntimeException e){
            String message = e.getMessage();
            String lastMessage = message.substring(message.lastIndexOf("！"));
            logger.error("自定义公式异常！",e);
            throw new BaseRuntimeException("自定义公式异常！"+lastMessage);
        }
    }

    /**
     * 根据自定义公式处理数据
     * @param dataList 数据
     * @param dpuExpressionConfigs 公式配置
     */
    public void handleDataByExpression(List<Map<String, Object>> dataList, List<ExpressionConfigVO> dpuExpressionConfigs) {
        // 根据步骤ID查询对应表的字段信息
        long currentSysDate = System.currentTimeMillis();
        try{
            InitExpressionParser dpuExpressionParser = new InitExpressionParser(dpuDataColumnVOS);
            Map<String, Object> tmpRowData = dataList.get(0);
            dpuExpressionConfigs.stream().map(dpuExpressionConfig->{
                String columnName = dpuExpressionConfig.getColumnName();
                if(tmpRowData.containsKey(columnName)){
                    // 初始化表达式
                    ParamDataTypeEnums paramDataTypeEnum = ParamDataTypeEnums.getParamDataTypeEnum(dpuExpressionConfig.getColumnDataType());
                    ExpressionParams expressionParams = dpuExpressionParser.initExpression(dpuExpressionConfig.getId(), dpuExpressionConfig.getExpression(), paramDataTypeEnum);
                    expressionParams.setExpression(columnName);
                    // 预执行一次后缀表达式 出错 后续数据就不用重复执行
                    preExecuteCheckExpression(currentSysDate, tmpRowData, expressionParams);
                    return expressionParams;
                }else{
                    throw new BaseRuntimeException("自定义公式字段不存在！");
                }
            }).forEach(expressionParams->{
                // 根据表达式处理所有数据
                dataList.forEach(rowData-> {
                    Object expressionVal = handleRowDataExecuteExpression(currentSysDate, rowData, expressionParams);
                    rowData.put(expressionParams.getExpression(), expressionVal);
                });
            });
        }catch (BaseRuntimeException e){
            logger.error("处理数据时自定义公式异常！->{}", e.getMessage(), e);
            throw new BaseRuntimeException(e.getMessage());
        }
    }

    /**
     * 预执行一次后缀表达式 校验表达式是否符合要求
     *
     * @param currentSysDate 当前时间
     * columnDataType 列类型
     * @param tmpRowData 数据
     * expressionSuffixList 后缀表达式
     * columnDataTypes 公式使用到的列类型集合
     */
    private void preExecuteCheckExpression(long currentSysDate, Map<String, Object> tmpRowData, ExpressionParams expressionParams){
        // 预执行一次后缀表达式 出错 后续数据就不用重复执行
        Object expressionVal = handleRowDataExecuteExpression(currentSysDate, tmpRowData, expressionParams);
        if(ParamDataTypeEnums.isNumber(expressionParams.getExpressionDataType().getDataType())){
            if(!(expressionVal instanceof BigDecimal) && !(expressionVal instanceof Long)){
                String message = "字符串";
                if(expressionVal instanceof Timestamp){
                    message = "时间";
                }
                throw new BaseRuntimeException("数据类型不一致！应为数值获取的却是"+message);
            }
            return;
        }

        if(ParamDataTypeEnums.isDate(expressionParams.getExpressionDataType().getDataType())){
            if(!(expressionVal instanceof Timestamp)){
                String message = "字符串";
                if(expressionVal instanceof BigDecimal){
                    message = "数值";
                }
                throw new BaseRuntimeException("数据类型不一致！应为时间获取的却是"+ message);
            }
        }
    }

    /**
     * 处理单行数据
     * @param currentSysDate 当前时间
     * @param rowData 行数据
     *  expressionSuffixList 自定义公式
     *  columnDataType 字段集合
     */
    public Object handleRowDataExecuteExpression(long currentSysDate, Map<String,Object> rowData, ExpressionParams expressionParams){
        Map<String, ExpressionParams> tmpExpressionParams = expressionParams.getExpressionParams();
        List<String> expressionSuffixList = expressionParams.getExpressionSuffixList();
        Stack<DPUExpressionStackItem> decimalStack = new Stack<>();
        // 1 日期只能相减
        // 2 日期相减 = 数值
        // 3 日期 + 数值 = 日期
        try {
            for (String tmpVal : expressionSuffixList) {
                if (DPUExpressionTypeEnum.checkCharIsSymbol(tmpVal)) {
                    DPUExpressionStackItem tmpOne = decimalStack.pop();
                    DPUExpressionStackItem tmpTwo = decimalStack.pop();
                    boolean isPlus = Objects.equals(DPUExpressionTypeEnum.PLUS.getSymbol(), tmpVal);
                    boolean isSubtract = Objects.equals(DPUExpressionTypeEnum.SUBTRACT.getSymbol(), tmpVal);
                    boolean isDateOne = Objects.equals(DPUDataTypeEnums.DATE.getDataType(), tmpOne.getColumnDataType());
                    boolean isDateTwo = Objects.equals(DPUDataTypeEnums.DATE.getDataType(), tmpTwo.getColumnDataType());
                    if (!isSubtract && isDateOne && isDateTwo) {
                        throw new KelvenRuntimeException("自定义公式异常！多个时间类型字段只能相减");
                    } else if ((!isPlus && !isSubtract) && (isDateOne || isDateTwo)) {
                        throw new KelvenRuntimeException("自定义公式异常！时间类型字段只能加减");
                    }
                    if(Objects.isNull(tmpTwo.getColumnVal()) || Objects.isNull(tmpOne.getColumnVal())){
                        decimalStack.push(CalcDataTypeHasDateExpression.COLUMN_VALUE_NULL.calcExpression(tmpVal, tmpTwo, tmpOne));
                        continue;
                    }
                    if (isDateOne || isDateTwo) {
                        DPUExpressionStackItem dpuExpressionStackItem;
                        if (isDateOne && isDateTwo) {
                            dpuExpressionStackItem = CalcDataTypeHasDateExpression.DATE_DATE.calcExpression(tmpVal, tmpTwo, tmpOne);
                        } else {
                            dpuExpressionStackItem = CalcDataTypeHasDateExpression.DATE_NUMBER.calcExpression(tmpVal, tmpTwo, tmpOne);
                        }
                        decimalStack.push(dpuExpressionStackItem);
                    }else {
                        decimalStack.push(CalcDataTypeHasDateExpression.NUMBER_NUMBER.calcExpression(tmpVal, tmpTwo, tmpOne));
                    }
                } else {
                    DPUExpressionStackItem dpuExpressionStackItem = expressionSuffixParamsList.stream().filter(item -> Objects.equals(tmpVal, item.getColumnName())).findAny().orElse(new DPUExpressionStackItem());
                    if(ObjectUtils.isEmpty(dpuExpressionStackItem.getColumnName())){
                        throw new KelvenRuntimeException("字段不存在！");
                    }
                    decimalStack.push(dpuExpressionStackItem);
                }
            }
            // 根据字段数据类型 返回对应的类型数据
            DPUExpressionStackItem expressionStackItem = decimalStack.pop();
            BigDecimal columnVal = expressionStackItem.getColumnVal();
            Object result;
            if (Objects.equals(DPUDataTypeEnums.DATE.getDataType(), expressionStackItem.getColumnDataType())) {
                result = Objects.isNull(columnVal) ?  null : new Timestamp(columnVal.longValue());
            } else {
                if (DPUDataTypeEnums.isInteger(resultDataType)) {
                    result = columnVal.longValue();
                } else {
                    result = columnVal;
                }
            }
            return result;
        }catch(Exception e){
            throw new KelvenRuntimeException("自定义公式 后缀表达式执行异常！" + e.getMessage(), e);
        }

        return null;
    }
}

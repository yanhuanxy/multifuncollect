package com.yanhuanxy.multifunservice.expression.vo;

import com.yanhuanxy.multifunservice.expression.enums.ParamDataTypeEnums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据矩阵 自定义公式 相关
 * @author yym
 */
public class ExpressionParams {

    /**
     * 表达式
     */
    private String expression;

    /**
     * 表字段
     */
    private boolean columnField;

    /**
     * 列类型
     */
    private ParamDataTypeEnums expressionDataType;

    /**
     * 格式化后的表达式
     */
    private String formatExpression;

    /**
     * 列名
     */
    private List<String> expressionSuffixList;

    /**
     * 子级函数参数配置
     */
    private Map<String, ExpressionParams> expressionParams;


    public ExpressionParams() {
        super();
    }

    public ExpressionParams(String expression, ParamDataTypeEnums expressionDataType) {
        this.expression = expression;
        this.expressionDataType = expressionDataType;
    }

    public ExpressionParams(String expression, boolean columnField, ParamDataTypeEnums expressionDataType) {
        this.expression = expression;
        this.columnField = columnField;
        this.expressionDataType = expressionDataType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public boolean isColumnField() {
        return columnField;
    }

    public void setColumnField(boolean columnField) {
        this.columnField = columnField;
    }

    public ParamDataTypeEnums getExpressionDataType() {
        return expressionDataType;
    }

    public void setExpressionDataType(ParamDataTypeEnums expressionDataType) {
        this.expressionDataType = expressionDataType;
    }

    public String getFormatExpression() {
        return formatExpression;
    }

    public void setFormatExpression(String formatExpression) {
        this.formatExpression = formatExpression;
    }

    public List<String> getExpressionSuffixList() {
        return expressionSuffixList;
    }

    public void setExpressionSuffixList(List<String> expressionSuffixList) {
        this.expressionSuffixList = expressionSuffixList;
    }

    public Map<String, ExpressionParams> getExpressionParams() {
        return expressionParams;
    }

    public void setExpressionParams(Map<String, ExpressionParams> expressionParams) {
        this.expressionParams = expressionParams;
    }


    /**
     * 获取 所有字段
     */
    public Map<String, ParamDataTypeEnums> getTableColumnList(){
        Map<String, ExpressionParams> expressionParams = this.getExpressionParams();
        Map<String, ParamDataTypeEnums> resultList = new HashMap<>(16);
        expressionParams.values().forEach(item->{
            if(item.isColumnField()){
                resultList.put(item.getExpression(), item.getExpressionDataType());
            }
            Map<String, ParamDataTypeEnums> tableColumnMap = item.getTableColumnList();
            resultList.putAll(tableColumnMap);
        });
        return resultList;
    }

}

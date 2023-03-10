package com.yanhuanxy.multifunexport.tools.domain.origin.dto;

/**
 * 根据筛选条件 VO
 * @author yym
 * @date 2021/8/5
 */
public class QueryParamDto {

    /**
     * 字段
     */
    private String fieldName;

    /**
     * 字段值 时间类型字段必须使用 java.sql.Date
     * in 时 多个值用 逗号[,]做间隔
     */
    private Object fieldVal;

    /**
     * BETWEEN 时 需要使用到多个值
     */
    private Object fieldValSecond;

    /**
     * 默认操作符 等于
     */
    private String operateType = "=";

    public QueryParamDto() {
    }

    public QueryParamDto(String fieldName, Object fieldVal) {
        this.fieldName = fieldName;
        this.fieldVal = fieldVal;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(Object fieldVal) {
        this.fieldVal = fieldVal;
    }

    public Object getFieldValSecond() {
        return fieldValSecond;
    }

    public void setFieldValSecond(Object fieldValSecond) {
        this.fieldValSecond = fieldValSecond;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }
}

package com.yanhuanxy.multifunexport.tools.domain.origin.vo;


import com.yanhuanxy.multifunexport.tools.domain.emuns.origin.BiOperateTypeEnum;

/**
 * 根据筛选条件 VO
 * @author yym
 * @date 2021/8/5
 */
public class QueryParameterVo {

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
     * 是否是主键 默认false
     */
    private boolean isPrimary = false;

    /**
     * 默认操作符 等于
     */
    private BiOperateTypeEnum biOperateTypeEnum = BiOperateTypeEnum.EQ;

    public QueryParameterVo() {
    }

    public QueryParameterVo(String fieldName, Object fieldVal) {
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

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public BiOperateTypeEnum getBiOperateTypeEnum() {
        return biOperateTypeEnum;
    }

    public void setBiOperateTypeEnum(BiOperateTypeEnum biOperateTypeEnum) {
        this.biOperateTypeEnum = biOperateTypeEnum;
    }
}

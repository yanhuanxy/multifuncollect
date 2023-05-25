package com.yanhuanxy.multifunservice.expression.vo;

import java.io.Serializable;

/**
 * 数据矩阵-列
 */
public class DefaultDataColumnVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**列名*/
    private String columnName;

    /**列注释*/
    private String columnComment;

    /**列数据类型（数据库类型）*/
    private String columnDataType;

    /**参数类型（decimal：小数、integer：整数、string：字符、datetime：日期时间、other：其他类型）*/
    private String columnParamType;

    /**数据类型*/
    private String dataType;

    /**总长度*/
    private Integer length;

    /**小数位精度*/
    private Integer precision;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getColumnParamType() {
        return columnParamType;
    }

    public void setColumnParamType(String columnParamType) {
        this.columnParamType = columnParamType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }
}

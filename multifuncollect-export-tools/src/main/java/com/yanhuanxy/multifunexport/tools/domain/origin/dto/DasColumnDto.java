package com.yanhuanxy.multifunexport.tools.domain.origin.dto;

/**
 * 原始jdbc字段对象
 *
 * @author yym
 * @since 2020/8/27
 */
public class DasColumnDto {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列类型
     */
    private String columnTypeName;

    /**
     * 列显示大小
     */
    private Integer columnDisplaySize;

    /**
     * 列类名
     */
    private String columnClassName;

    /**
     * 注释
     */
    private String columnComment;

    /**
     * 是否为null
     */
    private int isNull;

    /**
     * 是否是主键
     */
    private boolean isprimaryKey;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public Integer getColumnDisplaySize() {
        return columnDisplaySize;
    }

    public void setColumnDisplaySize(Integer columnDisplaySize) {
        this.columnDisplaySize = columnDisplaySize;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public int getIsNull() {
        return isNull;
    }

    public void setIsNull(int isNull) {
        this.isNull = isNull;
    }

    public boolean isIsprimaryKey() {
        return isprimaryKey;
    }

    public void setIsprimaryKey(boolean isprimaryKey) {
        this.isprimaryKey = isprimaryKey;
    }
}

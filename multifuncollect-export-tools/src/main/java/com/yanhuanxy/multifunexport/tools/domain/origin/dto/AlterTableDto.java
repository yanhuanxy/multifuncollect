package com.yanhuanxy.multifunexport.tools.domain.origin.dto;

/**
 * ddl 语句 拼装条件dto
 * @author yym
 */
public class AlterTableDto {
    /**
     * 表名
     */
    private String tableName;

    /**
     * 旧的字段名
     */
    private String newColumnName;

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段属性 datatype [default value][null/not null]
     */
    private String columnField;

    /**
     * 将字段添加 到某个字段之后 只适用于 mysql
     */
    private String afterColumn;

    /**
     * 字段注释
     */
    private String columnComment;

    /**
     * 字段类型 如：int、varchar(10)
     */
    private String columnType;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getNewColumnName() {
        return newColumnName;
    }

    public void setNewColumnName(String newColumnName) {
        this.newColumnName = newColumnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnField() {
        return columnField;
    }

    public void setColumnField(String columnField) {
        this.columnField = columnField;
    }

    public String getAfterColumn() {
        return afterColumn;
    }

    public void setAfterColumn(String afterColumn) {
        this.afterColumn = afterColumn;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }
}

package com.yanhuanxy.multifunexport.tools.domain.origin.dto;

import java.util.List;

/**
 * 数据表别名对照 dto
 * @author yym
 * @date 2020/9/27
 */
public class TableAliasDto {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 下标
     */
    private int tableIndex;

    /**
     * 拥有的字段
     */
    private List<String> fields;

    public TableAliasDto() {
    }

    public TableAliasDto(String tableName, String tableAlias, int tableIndex) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableIndex = tableIndex;
    }

    public TableAliasDto(String tableName, String tableAlias, int tableIndex, List<String> fields) {
        this.tableName = tableName;
        this.tableAlias = tableAlias;
        this.tableIndex = tableIndex;
        this.fields = fields;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }
}

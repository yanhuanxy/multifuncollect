package com.yanhuanxy.multifundomain.excelupload.dto;

import java.util.Map;

public class UploadTableConfDTO {

    /**
     * sheet页下标
     */
    private Integer confSheetIndex;

    /**
     * sheet页名称
     */
    private String confSheetName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表字段 、字段别名
     */
    private Map<String, String> tableFields;

    public UploadTableConfDTO() {
    }

    public Integer getConfSheetIndex() {
        return confSheetIndex;
    }

    public void setConfSheetIndex(Integer confSheetIndex) {
        this.confSheetIndex = confSheetIndex;
    }

    public String getConfSheetName() {
        return confSheetName;
    }

    public void setConfSheetName(String confSheetName) {
        this.confSheetName = confSheetName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, String> getTableFields() {
        return tableFields;
    }

    public void setTableFields(Map<String, String> tableFields) {
        this.tableFields = tableFields;
    }
}

package com.yanhuanxy.multifundomain.excelupload.dto;

public class QueryTableDataDTO{

    private String tableName;

    private String uploadDate;

    public QueryTableDataDTO() {
        super();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }
}

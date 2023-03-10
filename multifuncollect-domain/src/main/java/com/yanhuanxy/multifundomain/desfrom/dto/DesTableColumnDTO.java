package com.yanhuanxy.multifundomain.desfrom.dto;

/**
 * @author
 * @date 2021/10/19
 */

public class DesTableColumnDTO {
    private String columnName;
    private String dataType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "DesTableColumnDTO{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}

package com.yanhuanxy.multifundomain.excelupload.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2021/1/20
 */
public class UploadDataDTO {

    /**
     * 表头
     */
    private List<String> titleData = new ArrayList<>();
    /**
     * 数据
     */
    private List<List> dataData= new ArrayList<>();

    /**
     * 是否下一条
     */
    private boolean isNext = true;

    /**
     * 表格是否存在
     */
    private boolean isTable = true;

    private UploadMoreDataConfDTO uploadMoreDataConf;

    public UploadDataDTO() {
    }

    public UploadDataDTO(List<String> titleData, List<List> dataData, boolean isNext,
                         boolean isTable, UploadMoreDataConfDTO uploadMoreDataConf) {
        this.titleData = titleData;
        this.dataData = dataData;
        this.isNext = isNext;
        this.isTable = isTable;
        this.uploadMoreDataConf = uploadMoreDataConf;
    }

    public List<String> getTitleData() {
        return titleData;
    }

    public void setTitleData(List<String> titleData) {
        this.titleData = titleData;
    }

    public List<List> getDataData() {
        return dataData;
    }

    public void setDataData(List<List> dataData) {
        this.dataData = dataData;
    }

    public boolean isNext() {
        return isNext;
    }

    public void setNext(boolean next) {
        isNext = next;
    }

    public boolean isTable() {
        return isTable;
    }

    public void setTable(boolean table) {
        isTable = table;
    }

    public UploadMoreDataConfDTO getUploadMoreDataConf() {
        return uploadMoreDataConf;
    }

    public void setUploadMoreDataConf(UploadMoreDataConfDTO uploadMoreDataConf) {
        this.uploadMoreDataConf = uploadMoreDataConf;
    }


    /**
     *  获取格式：( "id", "name", "address", "age" )
     * @return
     */
    public String getTitleFieldSql(){
        StringBuilder sql = new StringBuilder();
        for (String titleDatum : titleData) {
            sql.append("\"").append(titleDatum.trim()).append("\",");
        }
        return sql.substring(0, sql.length()-1);
    }
}

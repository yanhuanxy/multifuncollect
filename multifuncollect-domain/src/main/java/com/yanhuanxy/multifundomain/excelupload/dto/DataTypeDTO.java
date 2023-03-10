package com.yanhuanxy.multifundomain.excelupload.dto;

import java.util.List;

/**
 * @date 2021/1/22
 */
public class DataTypeDTO {
    private  List<List> insertData;
    private List<List> updateData;

    public DataTypeDTO(List<List> insertData, List<List> updateData) {
        this.insertData = insertData;
        this.updateData = updateData;
    }

    public List<List> getInsertData() {
        return insertData;
    }

    public void setInsertData(List<List> insertData) {
        this.insertData = insertData;
    }

    public List<List> getUpdateData() {
        return updateData;
    }

    public void setUpdateData(List<List> updateData) {
        this.updateData = updateData;
    }
}

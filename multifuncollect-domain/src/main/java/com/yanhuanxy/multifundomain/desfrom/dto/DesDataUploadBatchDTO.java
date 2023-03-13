package com.yanhuanxy.multifundomain.desfrom.dto;

import java.util.List;

/**
 * @author
 * @date 2021/10/19
 */

public class DesDataUploadBatchDTO {
    /**
     * 字段列默认大写 小写->标识符无效
     */
    private List<String> columns;
    private List<Object> values;

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "DesDataUploadBatchDTO{" +
                "columns=" + columns +
                ", values=" + values +
                '}';
    }
}

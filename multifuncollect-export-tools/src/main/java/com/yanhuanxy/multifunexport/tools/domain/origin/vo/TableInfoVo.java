package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

import java.util.List;

/**
 * 表信息
 *
 * @author yym
 * @since 2020/08/27
 */
public class TableInfoVo {
    /**
     * 表名
     */
    private String name;

    /**
     * 注释
     */
    private String comment;
    /**
     * 所有列
     */
    private List<ColumnInfoVo> columns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnInfoVo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfoVo> columns) {
        this.columns = columns;
    }
}

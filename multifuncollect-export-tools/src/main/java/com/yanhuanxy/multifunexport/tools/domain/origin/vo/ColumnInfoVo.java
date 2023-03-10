package com.yanhuanxy.multifunexport.tools.domain.origin.vo;


/**
 * 字段信息
 *
 * @author yym
 * @date 2020/8/27
 */
public class ColumnInfoVo {
    /**
     * 字段名称
     */
    private String name;
    /**
     * 注释
     */
    private String comment;
    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段长度
     */
    private Integer length;

    /**
     * 是否是主键列
     */
    private Boolean isPrimaryKey;
    /**
     * 是否可为null 0 不可为空  1 可以为null
     */
    private int isnull;

    /**
     * 是否敏感 0不敏感 1 敏感
     */
    private int issensitive = 0;

    /**
     * 是否排序 true asc false desc null
     */
    private Boolean isSort;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getIsPrimaryKey() {
        return isPrimaryKey;
    }

    public void setIsPrimaryKey(Boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public int getIsnull() {
        return isnull;
    }

    public void setIsnull(int isnull) {
        this.isnull = isnull;
    }

    public int getIssensitive() {
        return issensitive;
    }

    public void setIssensitive(int issensitive) {
        this.issensitive = issensitive;
    }

    public Boolean getSort() {
        return isSort;
    }

    public void setSort(Boolean sort) {
        isSort = sort;
    }
}
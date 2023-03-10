package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

import java.util.List;

/**
 * 筛选条件分页多表联合查询数据 子级VO
 * @author yym
 * @date 2021/8/30
 */
public class QueryTableMultSubDataVo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 显示的列
     * name 字段
     * comment 字段别名
     */
    private List<ColumnInfoVo> fields;

    /**
     * 条件集合
     */
    private List<QueryParameterVo> parameters;

    /**
     * 关联类型
     */
    private Integer joinType;

    /**
     * 前个表名
     */
    private String beforeTableName;

    /**
     * 前个表的关联指针
     */
    private List<String> beforeJoinFields;

    /**
     * 后个表的关联指针
     */
    private List<String> afterJoinFields;


    public QueryTableMultSubDataVo() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnInfoVo> getFields() {
        return fields;
    }

    public void setFields(List<ColumnInfoVo> fields) {
        this.fields = fields;
    }

    public List<QueryParameterVo> getParameters() {
        return parameters;
    }

    public void setParameters(List<QueryParameterVo> parameters) {
        this.parameters = parameters;
    }

    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }

    public List<String> getAfterJoinFields() {
        return afterJoinFields;
    }

    public void setAfterJoinFields(List<String> afterJoinFields) {
        this.afterJoinFields = afterJoinFields;
    }

    public String getBeforeTableName() {
        return beforeTableName;
    }

    public void setBeforeTableName(String beforeTableName) {
        this.beforeTableName = beforeTableName;
    }

    public List<String> getBeforeJoinFields() {
        return beforeJoinFields;
    }

    public void setBeforeJoinFields(List<String> beforeJoinFields) {
        this.beforeJoinFields = beforeJoinFields;
    }
}

package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

import com.yanhuanxy.multifunexport.tools.base.PageVO;

import java.util.List;

/**
 * 根据表名、筛选条件分页多表联合查询数据 VO
 * @author yym
 * @date 2021/8/30
 */
public class QueryTableMultDataVo extends PageVO {

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
     * 条件集合
     */
    private List<String> parameterStrs;
    /**
     * 关联表集合
     */
    private List<QueryTableMultSubDataVo> childrenTable;

    public QueryTableMultDataVo() {
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

    public List<String> getParameterStrs() {
        return parameterStrs;
    }

    public void setParameterStrs(List<String> parameterStrs) {
        this.parameterStrs = parameterStrs;
    }

    public void setParameters(List<QueryParameterVo> parameters) {
        this.parameters = parameters;
    }

    public List<QueryTableMultSubDataVo> getChildrenTable() {
        return childrenTable;
    }

    public void setChildrenTable(List<QueryTableMultSubDataVo> childrenTable) {
        this.childrenTable = childrenTable;
    }
}

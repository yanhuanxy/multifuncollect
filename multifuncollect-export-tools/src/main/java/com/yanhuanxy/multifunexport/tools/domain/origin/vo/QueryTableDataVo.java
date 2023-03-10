package com.yanhuanxy.multifunexport.tools.domain.origin.vo;


import com.yanhuanxy.multifunexport.tools.base.PageVO;

import java.util.List;

/**
 * 根据表名、筛选条件分页查询数据 VO
 * 可用于 查询、删除、新增
 * @author yym
 * @date 2021/8/5
 */
public class QueryTableDataVo extends PageVO {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 条件集合
     */
    private List<QueryParameterVo> parameters;

    /**
     * 主键策略 1 uuid,2  最大ID值
     */
    private Integer pkStrategyType;

    public QueryTableDataVo() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<QueryParameterVo> getParameters() {
        return parameters;
    }

    public void setParameters(List<QueryParameterVo> parameters) {
        this.parameters = parameters;
    }

    public Integer getPkStrategyType() {
        return pkStrategyType;
    }

    public void setPkStrategyType(Integer pkStrategyType) {
        this.pkStrategyType = pkStrategyType;
    }
}

package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

import java.util.List;

/**
 * 根据表名、筛选条件 修改数据 VO
 * @author yym
 * @date 2021/8/30
 */
public class UpdateTableDataVo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * set条件集合
     */
    private List<QueryParameterVo> parameters;

    /**
     * where 条件集合
     */
    private List<QueryParameterVo> whereparameters;

    /**
     * 主键策略
     */
    private Integer pkStrategyType;

    public UpdateTableDataVo() {
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

    public List<QueryParameterVo> getWhereparameters() {
        return whereparameters;
    }

    public void setWhereparameters(List<QueryParameterVo> whereparameters) {
        this.whereparameters = whereparameters;
    }
}

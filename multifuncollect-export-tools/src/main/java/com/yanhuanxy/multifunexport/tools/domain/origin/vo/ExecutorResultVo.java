package com.yanhuanxy.multifunexport.tools.domain.origin.vo;

/**
 * 返回执行状态 VO
 * @author yym
 * @date 2021/8/5
 */
public class ExecutorResultVo {

    private boolean isSuccess;

    private String tableName;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

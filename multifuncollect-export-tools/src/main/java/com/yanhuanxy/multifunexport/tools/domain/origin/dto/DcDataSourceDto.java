package com.yanhuanxy.multifunexport.tools.domain.origin.dto;


import com.yanhuanxy.multifunexport.tools.base.PageDTO;

/**
 * 数据源 dto
 * @author yym
 * @date 2020/8/26
 */
public class DcDataSourceDto extends PageDTO {

    private Long id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 所属应用name
     */
    private String applicationName;

    /**
     * 数据库类型 mysql、oracle、sqlserver
     */
    private String type;

    /**
     * IP
     */
    private String ip;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据库版本
     */
    private String dbVersion;

    /**
     * 账号
     */
    private String dbAccount;

    /**
     * 密码
     */
    private String dbPassword;

    /**
     * 状态
     */
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDbVersion() {
        return dbVersion;
    }

    public void setDbVersion(String dbVersion) {
        this.dbVersion = dbVersion;
    }

    public String getDbAccount() {
        return dbAccount;
    }

    public void setDbAccount(String dbAccount) {
        this.dbAccount = dbAccount;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

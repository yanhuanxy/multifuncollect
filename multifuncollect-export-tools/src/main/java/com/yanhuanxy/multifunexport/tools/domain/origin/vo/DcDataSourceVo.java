package com.yanhuanxy.multifunexport.tools.domain.origin.vo;


import com.yanhuanxy.multifunexport.tools.base.PageVO;

/**
 * 数据源 VO
 * @author yym
 * @date 2020/8/26
 */
public class DcDataSourceVo extends PageVO {
    /**
     * 主键
     * id
     */
    private Long id;

    /**
     * 数据源名称
     * name
     */
    private String name;

    /**
     * 数据源编码
     * charset
     */
    private String charset;

    /**
     * 数据库类型 Mysql、Oracle、SqlServer
     * type
     */
    private String type;

    /**
     * IP
     * ip
     */
    private String ip;

    /**
     * 端口
     * port
     */
    private Integer port;

    /**
     * 数据库名称
     * db_name
     */
    private String dbName;

    /**
     * 数据库版本
     * db_version
     */
    private String dbVersion;

    /**
     * 账号
     * db_account
     */
    private String dbAccount;

    /**
     * 密码
     * db_password
     */
    private String dbPassword;

    /**
     * 所属应用id
     * application_id
     */
    private String applicationId;

    /**
     * 所属应用name
     * application_name
     */
    private String applicationName;

    /**
     * 数据来源 1：业务系统、2：数据中心、3：前置库
     * source
     */
    private Integer source;

    /**
     * 状态0 未检测 1：正常 2 异常
     */
    private Integer status;

    /**
     * 备注
     * remarks
     */
    private String remarks;

    public DcDataSourceVo(){
        super();
    }

    public DcDataSourceVo(Long id) {
        this.id = id;
    }

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

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

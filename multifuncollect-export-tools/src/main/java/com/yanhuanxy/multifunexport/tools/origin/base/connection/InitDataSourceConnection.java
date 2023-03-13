package com.yanhuanxy.multifunexport.tools.origin.base.connection;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.util.AESUtil;
import com.yanhuanxy.multifunexport.tools.util.origin.LocalCacheUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 建立数据库连接
 */
public class InitDataSourceConnection {
    protected static final Logger logger = LoggerFactory.getLogger(InitDataSourceConnection.class);

    private DataSource datasource;

    private Connection connection;

    private String dataSourceType;

    private String currentSchema;

    private String dbName;

    public InitDataSourceConnection(DcDataSourceDto dcDataSourceDto)  throws SQLException{
        initDataSource(dcDataSourceDto, Boolean.TRUE);
    }

    public InitDataSourceConnection(DcDataSourceDto dcDataSourceDto, boolean isLocalCache)  throws SQLException{
        initDataSource(dcDataSourceDto, isLocalCache);
    }

    public DataSource getDatasource() {
        return datasource;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getDataSourceType() {
        return dataSourceType;
    }

    public String getCurrentSchema() {
        return currentSchema;
    }

    public String getDbName() {
        return dbName;
    }

    /**
     * 初始话数据源
     * @param dcDataSourceDto 数据源配置
     * @param isLocalCache 是否缓存
     * @throws SQLException
     */
    private void initDataSource(DcDataSourceDto dcDataSourceDto, boolean isLocalCache) throws SQLException{
        Long id = dcDataSourceDto.getId();
        if (id == null){
            id = System.currentTimeMillis();
        }
        String localCacheKey = dcDataSourceDto.getDbName() + "" + id;
        if (LocalCacheUtil.get(localCacheKey) == null) {
            getDataSourceAndConn(dcDataSourceDto);
        } else {
            this.connection = (Connection) LocalCacheUtil.get(localCacheKey);
            if (!this.connection.isValid(500)) {
                LocalCacheUtil.remove(localCacheKey);
                getDataSourceAndConn(dcDataSourceDto);
            }
        }
        this.dbName = dcDataSourceDto.getDbName();
        this.currentSchema = getSchema(dcDataSourceDto.getDbAccount());
        this.dataSourceType = dcDataSourceDto.getType();
        if(isLocalCache){
            long cacheTime = (long) 4 * 60 * 60 * 1000;
            LocalCacheUtil.set(localCacheKey, this.connection, cacheTime);
        }
    }


    /**
     * 连接数据库
     * @param dcDataSourceDto 数据源
     * @throws SQLException sql异常
     */
    private void getDataSourceAndConn(DcDataSourceDto dcDataSourceDto) throws SQLException {
        String userName = AESUtil.decrypt(dcDataSourceDto.getDbAccount());
        String userPassword = AESUtil.decrypt(dcDataSourceDto.getDbPassword());
        String type = dcDataSourceDto.getType();
        String jdbcUrl = getJdbcUrl(type, dcDataSourceDto.getIp(), dcDataSourceDto.getPort(),dcDataSourceDto.getDbName()) ;
        logger.info("jdbcUrl: ------> " + jdbcUrl);
        String jdbcDriver = getJdbcDriverClassName(dcDataSourceDto.getDbVersion());
        //这里默认使用 hikari 数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(userName);
        dataSource.setPassword(userPassword);
        dataSource.setJdbcUrl(jdbcUrl);
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setMaximumPoolSize(5);
        dataSource.setMinimumIdle(1);
        dataSource.setConnectionTimeout(30000);
        this.datasource = dataSource;
        this.connection = dataSource.getConnection();
    }

    /**
     * 获取 jdbcUrl
     * @param type 类型
     * @param ip ip
     * @param port 端口
     * @param dbname 数据库名
     * @return String
     */
    private String getJdbcUrl(String type,String ip,Integer port,String dbname){
        StringBuilder jdbcurl = new StringBuilder();
        switch (type){
            case JdbcConstants.MYSQL:
                jdbcurl.append("jdbc:mysql://");
                jdbcurl.append(ip).append(":");
                jdbcurl.append(port).append("/");
                jdbcurl.append(dbname);
                jdbcurl.append("?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
                        "serverTimezone=Asia%2FShanghai&characterEncoding=utf-8&useSSL=false");
                break;
            case JdbcConstants.ORACLE:
                jdbcurl.append("jdbc:oracle:thin:@");
                jdbcurl.append(ip).append(":");
                jdbcurl.append(port).append("/");
                jdbcurl.append(dbname);
                break;
            case JdbcConstants.SQL_SERVER:
                jdbcurl.append("jdbc:sqlserver://");
                jdbcurl.append(ip).append(":");
                jdbcurl.append(port).append(";");
                jdbcurl.append("DatabaseName=").append(dbname);
                break;
            case JdbcConstants.DM:
                jdbcurl.append("jdbc:dm://");
                jdbcurl.append(ip).append(":");
                jdbcurl.append(port).append(":");
                jdbcurl.append(dbname);
                break;
            default:
                break;
        }
        return jdbcurl.toString();
    }

    /**
     * 获取 dirverclassname
     * @param dbversion 版本
     * @return String
     */
    private String getJdbcDriverClassName(String dbversion){
        if("Mysql5.X".equals(dbversion) || "Mysql4.X".equals(dbversion) || "Mysql3.X".equals(dbversion)){
            return JdbcConstants.MYSQL_DRIVER_6;
        }
        if("Mysql8.X".equals(dbversion)){
            return JdbcConstants.MYSQL_DRIVER_6;
        }
        if("Oracle8i".equals(dbversion)){
            return JdbcConstants.ORACLE_DRIVER;
        }
        if("Oracle9i".equals(dbversion) || "Oracle10g".equals(dbversion) || "Oracle11g".equals(dbversion)){
            return JdbcConstants.ORACLE_DRIVER2;
        }
        if("SqlServer2008".equals(dbversion)){
            return JdbcConstants.SQL_SERVER_DRIVER_SQLJDBC4;
        }

        if("DM6.X".equals(dbversion)){
            return JdbcConstants.DM_DRIVER;
        }
        return null;
    }

    /**
     * 根据connection获取schema
     * @param jdbcUsername 连接
     * @return String
     */
    protected String getSchema(String jdbcUsername) {
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                logger.error("[SQLException getSchema Exception] --> the exception message is:{}" , e1.getMessage());
            }
            logger.error("[getSchema Exception] --> the exception message is:{}" , e.getMessage());
        }
        // 如果res是null，则将用户名当作 schema
        if (StringUtils.isBlank(res) && StringUtils.isNotBlank(jdbcUsername)) {
            res = jdbcUsername.toUpperCase();
        }
        return res;
    }

}

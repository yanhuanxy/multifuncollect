package com.yanhuanxy.multifunexport.tools.origin.base;


import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;
import com.yanhuanxy.multifunexport.tools.origin.dm.DmDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.mysql.MySqlDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.oracle.OracleDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.sqlserver.SqlServerDatabaseMeta;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * meta信息工厂
 *
 * @author yym
 * @date 2020/08/27
 */
public class DatabaseMetaFactory {
    private static final ConcurrentMap<String, DatabaseInterface> cacheDatabaseInterface = new ConcurrentHashMap<String, DatabaseInterface>();

    /**
     * 根据数据库类型返回对应的接口
     * @param dbType 数据库类型
     * @return DatabaseInterface
     */
    public static DatabaseInterface getByDbType(String dbType) {

        if (JdbcConstants.MYSQL.equals(dbType)) {
            return MySqlDatabaseMeta.getInstance();
        } else if (JdbcConstants.ORACLE.equals(dbType)) {
            return OracleDatabaseMeta.getInstance();
        } else if (JdbcConstants.SQL_SERVER.equals(dbType)) {
            return SqlServerDatabaseMeta.getInstance();
        }else if(JdbcConstants.DM.equals(dbType)){
            return DmDatabaseMeta.getInstance();
        } else {
            throw new UnsupportedOperationException("暂不支持的类型：".concat(dbType));
        }
    }
}

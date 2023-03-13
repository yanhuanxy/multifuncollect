package com.yanhuanxy.multifunexport.tools.origin.base;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.dm.DmQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.mysql.MySqlQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.oracle.OracleQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.sqlserver.SqlServerQueryTool;

/**
 * 工具类，获取单例实体
 *
 * @author yym
 * @since 2020/08/27
 */
public class QueryToolFactory {

    public static BaseQueryTool getByDbType(InitDataSourceConnection dcDataSourceDto) {
        //获取dbType
        String datasource = dcDataSourceDto.getDbName();
        if (JdbcConstants.MYSQL.equals(datasource)) {
            return getMySqlQueryToolInstance(dcDataSourceDto);
        } else if (JdbcConstants.ORACLE.equals(datasource)) {
            return getOracleQueryToolInstance(dcDataSourceDto);
        } else if (JdbcConstants.SQL_SERVER.equals(datasource)) {
            return getSqlserverQueryToolInstance(dcDataSourceDto);
        } else if (JdbcConstants.DM.equals(datasource)) {
            return getDmQueryToolInstance(dcDataSourceDto);
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(datasource));
    }

    private static BaseQueryTool getMySqlQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
        return new MySqlQueryTool(dcDataSourceDto);
    }

    private static BaseQueryTool getOracleQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
        return new OracleQueryTool(dcDataSourceDto);
    }

    private static BaseQueryTool getSqlserverQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
        return new SqlServerQueryTool(dcDataSourceDto);
    }

    private static BaseQueryTool getDmQueryToolInstance(InitDataSourceConnection dcDataSourceDto) {
        return new DmQueryTool(dcDataSourceDto);
    }
}

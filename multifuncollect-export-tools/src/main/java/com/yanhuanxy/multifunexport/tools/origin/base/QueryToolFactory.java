package com.yanhuanxy.multifunexport.tools.origin.base;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.exception.origin.RdbmsException;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.dm.DMQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.mysql.MySQLQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.oracle.OracleQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.sqlserver.SqlServerQueryTool;

import java.sql.SQLException;

/**
 * 工具类，获取单例实体
 *
 * @author yym
 * @since 2020/08/27
 */
public class QueryToolFactory {

    public static BaseQueryTool getByDbType(DcDataSourceDto dcDataSourceDto) {
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

    private static BaseQueryTool getMySqlQueryToolInstance(DcDataSourceDto dcDataSourceDto) {
        try {
            return new MySQLQueryTool(dcDataSourceDto);
        } catch (Exception e) {
            throw RdbmsException.asConnException(JdbcConstants.MYSQL,
                    e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
        }
    }

    private static BaseQueryTool getOracleQueryToolInstance(DcDataSourceDto dcDataSourceDto) {
        try {
            return new OracleQueryTool(dcDataSourceDto);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.ORACLE,
                    e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
        }
    }

    private static BaseQueryTool getSqlserverQueryToolInstance(DcDataSourceDto dcDataSourceDto) {
        try {
            return new SqlServerQueryTool(dcDataSourceDto);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.SQL_SERVER,
                    e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
        }
    }

    private static BaseQueryTool getDmQueryToolInstance(DcDataSourceDto dcDataSourceDto) {
        try {
            return new DMQueryTool(dcDataSourceDto);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(JdbcConstants.DM,
                    e,dcDataSourceDto.getDbAccount(),dcDataSourceDto.getDbName());
        }
    }
}

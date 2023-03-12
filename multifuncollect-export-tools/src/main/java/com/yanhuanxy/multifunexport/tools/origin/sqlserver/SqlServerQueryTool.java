package com.yanhuanxy.multifunexport.tools.origin.sqlserver;

import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * sql server
 *
 * @author yym
 * @date 2020/9/10
 */
public class SqlServerQueryTool extends BaseQueryTool implements QueryToolInterface {

    public SqlServerQueryTool(InitDataSourceConnection dcDataSourceDto) {
        super(dcDataSourceDto);
    }

    @Override
    public List<Map<String, Object>> getTableNamesComment() {
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlBuilder.getSqlQueryTablesNameComments(), null);
        } catch (SQLException e) {
            logger.error("[getTableNamesComment Exception] --> the exception message is:{}",e.getMessage());
        }
        return res;
    }
}

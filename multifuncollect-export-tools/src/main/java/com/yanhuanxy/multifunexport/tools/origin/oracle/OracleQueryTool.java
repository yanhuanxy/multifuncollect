package com.yanhuanxy.multifunexport.tools.origin.oracle;

import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Oracle数据库使用的查询工具
 *
 * @author yym
 * @date 2020/9/10
 */
public class OracleQueryTool extends BaseQueryTool implements QueryToolInterface {

    public OracleQueryTool(InitDataSourceConnection dcDataSourceDto) {
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

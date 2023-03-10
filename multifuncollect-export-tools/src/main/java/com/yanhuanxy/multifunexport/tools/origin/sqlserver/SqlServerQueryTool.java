package com.yanhuanxy.multifunexport.tools.origin.sqlserver;

import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;

import java.sql.SQLException;

/**
 * sql server
 *
 * @author yym
 * @date 2020/9/10
 */
public class SqlServerQueryTool extends BaseQueryTool implements QueryToolInterface {
    public SqlServerQueryTool(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }
}

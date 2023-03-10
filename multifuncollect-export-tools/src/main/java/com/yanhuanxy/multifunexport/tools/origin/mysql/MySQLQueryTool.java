package com.yanhuanxy.multifunexport.tools.origin.mysql;

import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;

import java.sql.SQLException;

/**
 * mysql数据库使用的查询工具
 *
 * @author yym
 * @date 2020/08/27
 */
public class MySQLQueryTool extends BaseQueryTool implements QueryToolInterface {

    public MySQLQueryTool(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }
}

package com.yanhuanxy.multifunexport.tools.origin.oracle;

import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;

import java.sql.SQLException;

/**
 * Oracle数据库使用的查询工具
 *
 * @author yym
 * @date 2020/9/10
 */
public class OracleQueryTool extends BaseQueryTool implements QueryToolInterface {

    public OracleQueryTool(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }

}

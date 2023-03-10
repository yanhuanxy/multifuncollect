package com.yanhuanxy.multifunexport.tools.origin.dm;

import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;

import java.sql.SQLException;

public class DMQueryTool extends BaseQueryTool implements QueryToolInterface {

    public DMQueryTool(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }

}

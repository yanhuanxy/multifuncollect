package com.yanhuanxy.multifunexport.tools.origin.dm;

import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;

public class DmQueryTool extends BaseQueryTool implements QueryToolInterface {

    public DmQueryTool(InitDataSourceConnection dcDataSourceDto) {
        super(dcDataSourceDto);
    }

}

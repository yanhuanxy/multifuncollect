package com.yanhuanxy.multifunexport.tools.origin.dm;

import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SqlExecutorInterface;

/**
 * 达梦数据库
 */
public class DmExecutor extends BaseSqlExecutor implements SqlExecutorInterface {

    public DmExecutor(InitDataSourceConnection dcDataSourceDto) {
        super(dcDataSourceDto);
    }
}

package com.yanhuanxy.multifunexport.tools.origin.dm;

import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSQLExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SQLExecutorInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;

import java.sql.SQLException;

/**
 * 达梦数据库
 */
public class DmExecutor extends BaseSQLExecutor implements SQLExecutorInterface {

    public DmExecutor(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }


    public DmExecutor(DcDataSourceDto dcDataSourceDto, Boolean isLocalCache) throws SQLException{
        super(dcDataSourceDto,isLocalCache);
    }


}

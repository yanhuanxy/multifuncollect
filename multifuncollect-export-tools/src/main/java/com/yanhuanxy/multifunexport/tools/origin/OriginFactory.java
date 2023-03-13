package com.yanhuanxy.multifunexport.tools.origin;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.exception.origin.RdbmsException;
import com.yanhuanxy.multifunexport.tools.origin.base.QueryToolFactory;
import com.yanhuanxy.multifunexport.tools.origin.base.SqlExecutorFactory;
import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;

import java.sql.SQLException;

/**
 * 数据源 工厂类
 */
public class OriginFactory {
    /**
     * 初始化数据库连接
     */
    private final InitDataSourceConnection initDataSource;

    public OriginFactory(DcDataSourceDto dcDataSourceDto) {
        try {
            this.initDataSource = new InitDataSourceConnection(dcDataSourceDto);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(dcDataSourceDto.getDbName(), e,dcDataSourceDto.getDbAccount(), dcDataSourceDto.getDbName());
        }
    }

    public OriginFactory(DcDataSourceDto dcDataSourceDto, boolean isLocalCache ){
        try {
            this.initDataSource = new InitDataSourceConnection(dcDataSourceDto, isLocalCache);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(dcDataSourceDto.getDbName(), e,dcDataSourceDto.getDbAccount(), dcDataSourceDto.getDbName());
        }
    }

    /**
     * 获取 DDL 语句执行器
     */
    public BaseSqlExecutor getSqlExecutor(){
        return SqlExecutorFactory.getByDbType(initDataSource);
    }


    /**
     * 获取 DML 语句执行器
     */
    public BaseQueryTool getSqlQueryTool(){
        return QueryToolFactory.getByDbType(initDataSource);
    }

}

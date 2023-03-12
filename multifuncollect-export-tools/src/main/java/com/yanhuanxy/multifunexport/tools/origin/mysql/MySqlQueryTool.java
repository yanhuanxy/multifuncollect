package com.yanhuanxy.multifunexport.tools.origin.mysql;

import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.origin.base.query.QueryToolInterface;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mysql数据库使用的查询工具
 *
 * @author yym
 * @date 2020/08/27
 */
public class MySqlQueryTool extends BaseQueryTool implements QueryToolInterface {

    public MySqlQueryTool(InitDataSourceConnection dcDataSourceDto) {
        super(dcDataSourceDto);
    }


    @Override
    public Boolean checkTableIsExist(String tableName) {
        String sqlQueryIsExist = getSqlQueryIsExist(tableName);
        try {
            String schema = connection.getCatalog();
            if(StringUtils.isBlank(schema)){
                schema = connection.getSchema();
            }
            if(!StringUtils.isBlank(schema)){
                logger.info(sqlQueryIsExist);
                try (PreparedStatement statement = connection.prepareStatement(sqlQueryIsExist)){
                    statement.setObject(1,schema);
                    try(ResultSet resultSet = statement.executeQuery()) {
                        resultSet.next();
                        long count = resultSet.getLong(1);
                        if (count > 0) {
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("[checkTableIsExist Exception] --> the exception message is:{}",e.getMessage());
        }
        return false;
    }

}

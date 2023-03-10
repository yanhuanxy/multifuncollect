package com.yanhuanxy.multifunexport.tools.origin.mysql;

import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSQLExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SQLExecutorInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ddl mysql sql执行器
 *
 * @author yym
 * @since 2020/08/27
 */
public class MySqlExecutor extends BaseSQLExecutor implements SQLExecutorInterface {

    public MySqlExecutor(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }

    public MySqlExecutor(DcDataSourceDto dcDataSourceDto,Boolean isLocalCache) throws SQLException {
        super(dcDataSourceDto,isLocalCache);
    }

    @Override
    public Boolean checkTableIsExist(String tableName) {
        String sqlQueryIsExist = getSqlQueryIsExist(tableName);
        try {
            Connection connection = getConnection();
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

    @Override
    public List<ColumnInfoDto> getSqlTableDesc(String tableName) {
        String sql = getSqlTableDescCmd(tableName,currentSchema);
        logger.info("===========getSqlTableDesc=================\n\n\n\n");
        logger.info("SQL:::"+sql);
        List<ColumnInfoDto> result = new ArrayList<>();
        try {
            List<Map<String, Object>> maps = JdbcUtils.executeQuery(getConnection(), sql,null);
            if(maps.size() > 0){
                maps.forEach(item->{
                    ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                    columnInfoDto.setName(item.get("COLUMN_NAME").toString());
                    columnInfoDto.setComment(item.get("COLUMN_COMMENT").toString());
                    columnInfoDto.setType(item.get("COLUMN_TYPE").toString());
                    columnInfoDto.setIsnull("NO".equals(item.get("IS_NULLABLE").toString())?0:1);
                    try {
                        columnInfoDto.setIfPrimaryKey("PRI".equals(item.get("COLUMN_KEY").toString()));
                    }catch (Exception e){
                        columnInfoDto.setIfPrimaryKey(false);
                    }
                    result.add(columnInfoDto);
                });
            }
        } catch (SQLException e) {
            logger.error("[getMaxIdVal Exception] --> the exception message is:{}",e.getMessage());
        }
        return result;
    }
}

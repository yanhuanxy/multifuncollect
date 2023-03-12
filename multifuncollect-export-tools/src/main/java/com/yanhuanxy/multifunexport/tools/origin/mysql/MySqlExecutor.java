package com.yanhuanxy.multifunexport.tools.origin.mysql;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSqlExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SqlExecutorInterface;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;

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
public class MySqlExecutor extends BaseSqlExecutor implements SqlExecutorInterface {

    public MySqlExecutor(InitDataSourceConnection dcDataSourceDto) {
        super(dcDataSourceDto);
    }


    @Override
    public List<ColumnInfoDto> getSqlTableDesc(String tableName) {
        String sql = sqlBuilder.getSqlTableDesc(tableName, currentSchema);
        logger.info("===========getSqlTableDesc=================\n\n\n\n");
        logger.info("SQL:::"+sql);
        List<ColumnInfoDto> result = new ArrayList<>();
        try {
            List<Map<String, Object>> maps = JdbcUtils.executeQuery(connection, sql,null);
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

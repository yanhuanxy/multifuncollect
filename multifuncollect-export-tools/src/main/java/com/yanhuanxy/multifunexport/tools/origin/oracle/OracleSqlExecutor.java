package com.yanhuanxy.multifunexport.tools.origin.oracle;

import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSQLExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SQLExecutorInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ddl Oracle sql执行器
 *
 * @author yym
 * @since 2020/08/27
 */
public class OracleSqlExecutor extends BaseSQLExecutor implements SQLExecutorInterface {

    public OracleSqlExecutor(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }
    public OracleSqlExecutor(DcDataSourceDto dcDataSourceDto,Boolean localCache) throws SQLException {
        super(dcDataSourceDto,localCache);
    }

    @Override
    public List<Map<String, Object>> getTableNamesComment() {
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(getConnection(), getSqlQueryTablesNameComments(), null);
        } catch (SQLException e) {
            logger.error("[getTableNamesComment Exception] --> the exception message is:{}",e.getMessage());
        }
        return res;
    }

    @Override
    public List<ColumnInfoDto> getSqlTableDesc(String tableName) {
        String sql = getSqlTableDescCmd(tableName,currentSchema);
        List<ColumnInfoDto> result = new ArrayList<>();
        try {
            List<Map<String, Object>> maps = JdbcUtils.executeQuery(getConnection(), sql,null);
            if(maps.size() > 0){
                maps.forEach(item->{
                    ColumnInfoDto columnInfoDto = new ColumnInfoDto();
                    columnInfoDto.setName(item.get("COLUMN_NAME").toString());
                    columnInfoDto.setComment(item.get("COMMENTS").toString());
                    columnInfoDto.setType(item.get("DATA_TYPE").toString());
                    columnInfoDto.setIsnull("N".equals(item.get("NULLABLE").toString())?0:1);
                    try {
                        columnInfoDto.setIfPrimaryKey("PRI".equals(item.get("COLUMN_KEY").toString()));
                    }catch (Exception e){
                        columnInfoDto.setIfPrimaryKey(false);
                    }
                    result.add(columnInfoDto);
                });
            }
        } catch (SQLException e) {
            logger.error("[getSqlTableDesc Exception] --> the exception message is:{}",e.getMessage());
        }
        return result;
    }
}

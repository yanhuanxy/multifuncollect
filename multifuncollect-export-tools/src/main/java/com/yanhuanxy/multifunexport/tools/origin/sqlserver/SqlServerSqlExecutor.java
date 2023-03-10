package com.yanhuanxy.multifunexport.tools.origin.sqlserver;

import com.yanhuanxy.multifunexport.tools.origin.base.executor.BaseSQLExecutor;
import com.yanhuanxy.multifunexport.tools.origin.base.executor.SQLExecutorInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;
import org.apache.commons.lang3.StringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ddl sqlserver sql执行器
 *
 * @author yym
 * @since 2020/08/27
 */
public class SqlServerSqlExecutor extends BaseSQLExecutor implements SQLExecutorInterface {

    public SqlServerSqlExecutor(DcDataSourceDto dcDataSourceDto) throws SQLException {
        super(dcDataSourceDto);
    }

    public SqlServerSqlExecutor(DcDataSourceDto dcDataSourceDto,Boolean localCache) throws SQLException {
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

    /**
     * 检查 是否有注解
     * @param tableName 表名
     * @return Integer
     */
    private Integer checkTableCommentOrColumnCommentExists(String tableName,String columnName) {
        PreparedStatement stmt = null;
        Integer count = null;
        boolean flag = !StringUtils.isBlank(columnName);
        String checkTableCommentExistsSql = checkTableCommentExistsSql(flag);
        try {
            stmt = getConnection().prepareStatement(checkTableCommentExistsSql);
            stmt.setString(1,tableName);
            if(flag){
                stmt.setString(2,columnName);
            }
            try(ResultSet resultSet = stmt.executeQuery()){
                resultSet.next();
                count = Integer.valueOf(resultSet.getString(1));
            }
        } catch (SQLException e) {
            logger.error("[updateTableComment Exception] --> the exception message is:{}",e.getMessage());
        } finally {
            JdbcUtils.close(stmt);
        }
        return count;
    }

    protected String checkTableCommentExistsSql(boolean flag){
        if(!flag){
            return "SELECT COUNT(*) as count FROM ::fn_listextendedproperty ('MS_Description','SCHEMA','dbo','TABLE',?,NULL,NULL)";
        }
        return "SELECT COUNT(*) as count FROM ::fn_listextendedproperty ('MS_Description','SCHEMA','dbo','TABLE',?,'COLUMN',?)";
    }


    @Override
    public boolean updateTableComment(String tableName, String tableComment) {
        if(StringUtils.isBlank(tableName) || StringUtils.isBlank(tableComment)){
            return false;
        }
        Integer count = checkTableCommentOrColumnCommentExists(tableName,null);
        String sqlupdateTableComment = getSqlServerUpdateTableComment(tableName,tableComment,count);
        Statement stmt = null;
        boolean issuccess = true;
        try {
            stmt = getConnection().createStatement();
            stmt.executeUpdate(sqlupdateTableComment);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[updateTableComment Exception] --> the exception message is:{}",e.getMessage());
        } finally {
            JdbcUtils.close(stmt);
        }
        return issuccess;
    }

    protected String getSqlServerUpdateTableComment(String tableName, String tableComment,Integer count){
        StringBuilder buffer = new StringBuilder();
        if(count == null || count.equals(0)){
            buffer.append(" execute sp_addextendedproperty 'MS_Description',");
        }else{
            buffer.append(" execute sp_updateextendedproperty 'MS_Description',");
        }
        buffer.append("'").append(tableComment).append("',");
        buffer.append("'SCHEMA','dbo','TABLE',");
        buffer.append("'").append(tableName).append("'");
        logger.info(buffer.toString());
        return buffer.toString();
    }

    @Override
    public boolean updateTableColumnComment(AlterTableDto alterTableDto) {
        if(StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())
                || StringUtils.isBlank(alterTableDto.getColumnComment())){
            return false;
        }
        Integer count = checkTableCommentOrColumnCommentExists(alterTableDto.getTableName(),alterTableDto.getColumnName());
        String sqlupdateTableColumnComment = getSqlServerUpdateTableColumnComment(alterTableDto,count);
        Statement stmt = null;
        boolean issuccess = true;
        try {
            stmt = getConnection().createStatement();
            stmt.executeUpdate(sqlupdateTableColumnComment);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[updateTableComment Exception] --> the exception message is:{}",e.getMessage());
        } finally {
            JdbcUtils.close(stmt);
        }
        return issuccess;
    }

    protected String getSqlServerUpdateTableColumnComment(AlterTableDto alterTableDto,Integer count){
        StringBuilder buffer = new StringBuilder();
        if(count == null || count.equals(0)){
            buffer.append(" execute sp_addextendedproperty 'MS_Description',");
        }else {
            buffer.append(" execute sp_updateextendedproperty 'MS_Description',");
        }
        buffer.append("'").append(alterTableDto.getColumnComment()).append("',");
        buffer.append("'SCHEMA','dbo','TABLE',");
        buffer.append("'").append(alterTableDto.getTableName()).append("',");
        buffer.append("'COLUMN',");
        buffer.append("'").append(alterTableDto.getColumnName()).append("'");
        logger.info(buffer.toString());
        return buffer.toString();
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
                        columnInfoDto.setIfPrimaryKey("PRI".equals(item.get("PRI").toString()));
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

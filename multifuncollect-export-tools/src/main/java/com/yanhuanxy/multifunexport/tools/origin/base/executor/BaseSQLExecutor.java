package com.yanhuanxy.multifunexport.tools.origin.base.executor;

import com.google.gson.Gson;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.DcDataSourceDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.QueryParamDto;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.ParserMultipleTableQuerySql;
import com.yanhuanxy.multifunexport.tools.origin.base.query.BaseQueryTool;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ddl sql执行器 基础类
 *
 * @author yym
 * @since 2020/08/27
 */
public class BaseSQLExecutor extends BaseQueryTool implements SQLExecutorInterface {

    protected static final Logger logger = LoggerFactory.getLogger(BaseSQLExecutor.class);

    /**
     * 构造方法
     *
     * @param dcDataSourceDto 数据源
     */
    protected BaseSQLExecutor(DcDataSourceDto dcDataSourceDto) throws SQLException{
        super(dcDataSourceDto);
    }

    /**
     * 是否缓存数据连接
     * @param dcDataSourceDto 数据源
     * @param localCache 缓存集合
     */
    protected BaseSQLExecutor(DcDataSourceDto dcDataSourceDto,Boolean localCache) throws SQLException{
        super(dcDataSourceDto,localCache);
    }

    @Override
    public Date getMaxDateVal(String tableName, String filedName) {
        Statement stmt = null;
        ResultSet rs = null;
        Date maxVal = null;
        try {
            stmt = getConnection().createStatement();
            String sql = String.format("select max(%s) from %s",filedName,tableName);
            logger.info(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            long maxValL = rs.getTimestamp(1).getTime();
            maxVal = new Date(maxValL);
        } catch (SQLException e) {
            logger.error("[getMaxDateVal Exception] --> the exception message is:{}",e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return maxVal;
    }

    @Override
    public boolean createTable(Map<String, String> columnmap, String tableName) {
        if (columnmap.isEmpty() || StringUtils.isBlank(tableName)) {
            return false;
        }
        StringBuffer sqlbuf = new StringBuffer();
        sqlbuf.append("create table ").append(tableName).append(" (");
        columnmap.forEach((key,val)-> sqlbuf.append(key).append(" ").append(val).append(","));
        sqlbuf.deleteCharAt(sqlbuf.length()-1);
        sqlbuf.append(")");

        return createTable(sqlbuf.toString());
    }

    @Override
    public boolean dropTable(String tableName) {
        if (StringUtils.isBlank(tableName)) {
            return false;
        }
        StringBuilder sqlbuf = new StringBuilder();
        sqlbuf.append("drop table ").append(tableName).append(" ");

        return dropTableBySql(sqlbuf.toString());
    }

    @Override
    public boolean renameTable(String oldtableName, String tableName) {
        if (StringUtils.isBlank(tableName) || StringUtils.isBlank(oldtableName)) {
            return false;
        }
        String sqlrenametable = getSqlRenameTableName(oldtableName, tableName);

        return renameTable(sqlrenametable);
    }

    protected String getSqlRenameTableName(String oldtableName, String tableName){
        return getSqlBuilder().getSqlRenameTableName(oldtableName,tableName);
    }


    @Override
    public boolean createTable(String ddl) {
        boolean issuccess = true;
        try {
            executeUpdateNoResult(ddl);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[createTable Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    @Override
    public boolean dropTableBySql(String ddl) {
        boolean issuccess = true;
        try {
            executeUpdateNoResult(ddl);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[dropTable Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    @Override
    public boolean renameTable(String ddl) {
        boolean issuccess = true;
        try {
            executeUpdateNoResult(ddl);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[renameTable Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    @Override
    public boolean addTableColumn(AlterTableDto alterTableDto) {
        if (StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())
                || StringUtils.isBlank(alterTableDto.getColumnField())) {
            return false;
        }

        String sqlAddTableColumn = getSqlAddTableColumn(alterTableDto);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlAddTableColumn);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[addTableColumn Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlAddTableColumn(AlterTableDto alterTableDto){
        return getSqlBuilder().getSqlAddTableColumn(alterTableDto);
    }

    @Override
    public boolean addTableColumns(List<AlterTableDto> alterTableDtolist) {
        if(alterTableDtolist.isEmpty()){
            return false;
        }
        String sqlAddTableColumn = getSqlAddTableColumns(alterTableDtolist);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlAddTableColumn);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[addTableColumns Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist){
        return getSqlBuilder().getSqlAddTableColumns(alterTableDtolist);
    }

    @Override
    public boolean updateTableColumn(AlterTableDto alterTableDto) {
        if (StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())
                || StringUtils.isBlank(alterTableDto.getNewColumnName())) {
            return false;
        }
        String sqlupdateTableColumn = getSqlUpdateTableColumn(alterTableDto);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlupdateTableColumn);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[updateTableColumn Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlUpdateTableColumn(AlterTableDto alterTableDto){
        return getSqlBuilder().getSqlUpdateTableColumn(alterTableDto);
    }

    @Override
    public boolean changeTableColumn(AlterTableDto alterTableDto) {
        if (StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())
                || StringUtils.isBlank(alterTableDto.getNewColumnName()) || StringUtils.isBlank(alterTableDto.getColumnField())) {
            return false;
        }

        String sqlChangeTableColumn = getSqlChangeTableColumn(alterTableDto);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlChangeTableColumn);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[changeTableColumn Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlChangeTableColumn(AlterTableDto alterTableDto){
        return getSqlBuilder().getSqlChangeTableColumn(alterTableDto);
    }

    @Override
    public boolean delTableColumn(AlterTableDto alterTableDto) {
        if (StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())) {
            return false;
        }

        String sqldelTableColumn = getSqlDelTableColumn(alterTableDto);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqldelTableColumn);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[delTableColumn Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlDelTableColumn(AlterTableDto alterTableDto){
        return getSqlBuilder().getSqlDelTableColumn(alterTableDto);
    }

    @Override
    public boolean delTableData(String tableName) {
        if(StringUtils.isBlank(tableName)){
            return false;
        }
        String sqldelTableData = getSqlDelTableData(tableName);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqldelTableData);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[delTableData Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlDelTableData(String tableName){
        return getSqlBuilder().getSqlDelTableData(tableName);
    }

    @Override
    public boolean truncateTableData(String tableName) {
        if(StringUtils.isBlank(tableName)){
            return false;
        }
        String sqltruncateTableData = getSqlTruncateTableData(tableName);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqltruncateTableData);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[truncateTableData Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlTruncateTableData(String tableName){
        return getSqlBuilder().getSqlTruncateTableData(tableName);
    }

    @Override
    public boolean updateTableComment(String tableName, String tableComment) {
        if(StringUtils.isBlank(tableName) || StringUtils.isBlank(tableComment)){
            return false;
        }
        String sqlupdateTableComment = getSqlUpdateTableComment(tableName,tableComment);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlupdateTableComment);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[updateTableComment Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlUpdateTableComment(String tableName, String tableComment){
        return getSqlBuilder().getSqlUpdateTableComment(tableName,tableComment);
    }

    @Override
    public boolean updateTableColumnComment(AlterTableDto alterTableDto) {
        if(StringUtils.isBlank(alterTableDto.getTableName()) || StringUtils.isBlank(alterTableDto.getColumnName())
            || StringUtils.isBlank(alterTableDto.getColumnComment())){
            return false;
        }
        String sqlupdateTableColumnComment = getSqlUpdateTableColumnComment(alterTableDto);
        boolean issuccess = true;
        try {
            executeUpdateNoResult(sqlupdateTableColumnComment);
        } catch (SQLException e) {
            issuccess = false;
            logger.error("[updateTableComment Exception] --> the exception message is:{}",e.getMessage());
        }
        return issuccess;
    }

    protected String getSqlUpdateTableColumnComment(AlterTableDto alterTableDto){
        return getSqlBuilder().getSqlUpdateTableColumnComment(alterTableDto);
    }

    @Override
    public Double distanceFinderDataBaseSizeBySchema(String schema) {
        if(StringUtils.isBlank(schema)){
            return null;
        }
        String sqlbuild = getSqlDistanceFinderDataBaseSizeBySchema(schema);
        if(StringUtils.isBlank(sqlbuild)){
            throw new UnsupportedOperationException();
        }
        double dataLength = 0d;
        try {
            dataLength = executeSqlResultDouble(sqlbuild);
        }catch (SQLException e){
            logger.error("[distanceFinderDataBaseSizeBySchema Exception] --> the exception message is:{}",e.getMessage());
        }
        return dataLength;
    }

    protected String getSqlDistanceFinderDataBaseSizeBySchema(String schema) {
        return getSqlBuilder().getSqlDistanceFinderDataBaseSizeBySchema(schema);
    }

    /**
     * 执行 executeUpdate
     * @param sqlbuild sql语句
     */
    private void executeUpdateNoResult(String sqlbuild) throws SQLException {
        logger.info(sqlbuild);
        Statement stmt = null;
        try{
            stmt = getConnection().createStatement();
            stmt.executeUpdate(sqlbuild);
        }finally {
            JdbcUtils.close(stmt);
        }
    }

    /**
     * 返回double 类型值
     * @param sqlbuild sql语句
     * @return double
     */
    private double executeSqlResultDouble(String sqlbuild) throws SQLException{
        Statement stmt = null;
        ResultSet rs = null;
        double dataLength;
        DecimalFormat df= new DecimalFormat("0.000");
        try {
            stmt = getConnection().createStatement();
            logger.info(sqlbuild);
            rs = stmt.executeQuery(sqlbuild);
            rs.next();
            long dataLengthchar = rs.getLong(1);
            dataLength = Double.parseDouble(df.format((float) dataLengthchar / (1024 * 1024)));
        }finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return dataLength;
    }

    @Override
    public Double distanceFinderTableSizeBySchemaAndTableName(String schema, String tableName) {
        if(StringUtils.isBlank(schema) || StringUtils.isBlank(tableName)){
            return null;
        }
        String sqlbuild = getSqlDistanceFinderTableSizeBySchemaAndTableName(schema,tableName);
        if(StringUtils.isBlank(sqlbuild)){
            throw new UnsupportedOperationException();
        }
        double dataLength = 0d;
        try {
            dataLength = executeSqlResultDouble(sqlbuild);
        }catch (SQLException e){
            logger.error("[distanceFinderTableSizeBySchemaAndTableName Exception] --> the exception message is:{}",e.getMessage());
        }
        return dataLength;
    }

    @Override
    public List<ColumnInfoDto> getSqlTableDesc(String tableName) {
        return null;
    }

    protected String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema,String tableName) {
        return getSqlBuilder().getSqlDistanceFinderTableSizeBySchemaAndTableName(schema,tableName);
    }

    @Override
    public boolean insertToTable(String tableName, Map<String, Object> parameters){
        boolean isExecute = true;
        Connection connection = getConnection();
        try {
            List<Object> data = new ArrayList<>();
            List<String> tmpFields = parameters.entrySet().stream().map(item -> {
                data.add(item.getValue());
                return item.getKey();
            }).collect(Collectors.toList());
            String sql = makeInsertToTableSql(tableName, tmpFields);
            logger.info("[ insertToTable ] ->{}",sql);
            logger.info("[ insertToTable parameters ] ->{}", new Gson().toJson(data));
            JdbcUtils.executeUpdate(connection, sql, data);
        }catch (SQLException e){
            isExecute = false;
            logger.error("[insertToTable Exception] --> the exception message is:{}",e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("插入数据失败时回滚失败！", ex);
            }
        }
        return isExecute;
    }

    /**
     * 组装插入数据 sql 语句
     * @param tableName 表名
     * @param fields 字段
     * @return String
     */
    private String makeInsertToTableSql(String tableName, Collection<String> fields) {
        StringBuilder sql = new StringBuilder().append("insert into ").append(tableName).append("(");
        int nameCount = 0;
        for (String name : fields) {
            if (nameCount > 0) {
                sql.append(",");
            }
            sql.append(name);
            nameCount++;
        }
        sql.append(") values (");
        for (int i = 0; i < nameCount; ++i) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");

        return sql.toString();
    }

    @Override
    public boolean updateToTable(String tableName,Map<String,Object> setParameters, List<QueryParamDto> queryParamDtos){
        boolean isExecute = true;
        Connection connection = getConnection();
        try {
            List<Object> data = new ArrayList<>();
            List<String> setFields = setParameters.entrySet().stream().map(item -> {
                data.add(item.getValue());
                return item.getKey();
            }).collect(Collectors.toList());
            String sql = makeUpdateToTableSql(tableName, setFields, queryParamDtos, data);
            logger.info("[ updateToTable sql ] ->{}",sql);
            logger.info("[ updateToTable parameters ] ->{}",new Gson().toJson(data));
            JdbcUtils.executeUpdate(connection, sql, data);
        }catch (SQLException e){
            isExecute = false;
            logger.error("[updateToTable Exception] --> the exception message is:{}",e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("修改数据失败时回滚失败！", ex);
            }
        }
        return isExecute;
    }



    /**
     * 组装修改数据 sql 语句
     * @param tableName 表名
     * @param setFields 修改字段
     * @param queryParamDtos 条件字段
     * @param tmVals 值集合
     * @return String
     */
    private String makeUpdateToTableSql(String tableName, Collection<String> setFields, Collection<QueryParamDto> queryParamDtos, List<Object> tmVals){
        StringBuilder sql = new StringBuilder().append("update ").append(tableName).append(" set ");
        int fieldCount = 0;
        for (String field : setFields) {
            if(fieldCount>0){
                sql.append(",");
            }
            sql.append(field).append(" = ").append("?");
            fieldCount++;
        }
        if(queryParamDtos.isEmpty()){
            return sql.toString();
        }
        // 添加where sql 片段
        parserQueryParamDtoAddWherePart(queryParamDtos, sql, tmVals);
        return sql.toString();
    }

    @Override
    public boolean deleteToTable(String tableName, List<QueryParamDto> queryParamDtos){
        boolean isExecute = true;
        Connection connection = getConnection();
        try {
            List<Object> data = new ArrayList<>();
            String sql = makeDeleteToTableSql(tableName, queryParamDtos, data);
            logger.info("[ deleteToTable sql ] ->{}",sql);
            logger.info("[ deleteToTable parameters ] ->{}",new Gson().toJson(data));
            JdbcUtils.executeUpdate(connection, sql, data);
        }catch (SQLException e){
            isExecute = false;
            logger.error("[deleteToTable Exception] --> the exception message is:{}",e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException ex) {
                logger.error("删除数据失败时回滚失败！", ex);
            }
        }
        return isExecute;
    }

    /**
     * 组装删除数据 sql 语句
     * @param tableName 表名
     * @param queryParamDtos 条件字段
     * @param tmVals 条件字段值
     * @return String
     */
    private String makeDeleteToTableSql(String tableName, Collection<QueryParamDto> queryParamDtos, List<Object> tmVals){
        StringBuilder sql = new StringBuilder().append("delete from ").append(tableName);
        if(queryParamDtos.isEmpty()){
            return sql.toString();
        }
        // 添加where sql 片段
        parserQueryParamDtoAddWherePart(queryParamDtos, sql, tmVals);
        return sql.toString();
    }

    /**
     * 解析 筛选条件集合 转换成 where sql 语句片段
     * @param queryParamDtos 筛选条件集合
     * @param sql sql
     */
    private void parserQueryParamDtoAddWherePart(Collection<QueryParamDto> queryParamDtos, StringBuilder sql, List<Object> tmVals){
        int fieldCount = 0;
        for (QueryParamDto queryParamDto : queryParamDtos) {
            String wherePart = ParserMultipleTableQuerySql.parserQueryParamDto(queryParamDto, tmVals);
            if(ObjectUtils.isEmpty(wherePart)){
                continue;
            }
            if(fieldCount>0){
                sql.append(" AND ");
            }else{
                sql.append(" WHERE ");
            }
            sql.append(wherePart);
            fieldCount++;
        }
    }
}

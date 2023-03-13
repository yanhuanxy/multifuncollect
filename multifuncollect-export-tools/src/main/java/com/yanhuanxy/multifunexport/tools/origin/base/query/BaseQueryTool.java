package com.yanhuanxy.multifunexport.tools.origin.base.query;

import com.google.gson.Gson;
import com.yanhuanxy.multifunexport.tools.constant.origin.Constants;
import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.*;
import com.yanhuanxy.multifunexport.tools.origin.base.connection.InitDataSourceConnection;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;
import com.yanhuanxy.multifunexport.tools.origin.base.DatabaseMetaFactory;
import com.yanhuanxy.multifunexport.tools.util.AESUtil;
import com.yanhuanxy.multifunexport.tools.util.origin.JdbcUtils;
import com.yanhuanxy.multifunexport.tools.util.origin.LocalCacheUtil;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * 查询工具 基础类
 *
 * @author yym
 * @since 2020/08/27
 */
public abstract class BaseQueryTool implements QueryToolInterface {

    protected static final Logger logger = LoggerFactory.getLogger(BaseQueryTool.class);
    /**
     * 用于获取查询语句
     */
    protected final DatabaseInterface sqlBuilder;

    protected final Connection connection;
    /**
     * 当前数据库名
     */
    protected String currentSchema;

    /**
     * 当前数据库类型
     */
    protected final String currentDatabase;

    public BaseQueryTool(InitDataSourceConnection dcDataSourceDto){
        this.connection = dcDataSourceDto.getConnection();
        this.currentSchema = dcDataSourceDto.getCurrentSchema();
        this.currentDatabase = dcDataSourceDto.getDataSourceType();
        this.sqlBuilder = DatabaseMetaFactory.getByDbType(dcDataSourceDto.getDataSourceType());
    }

    @Override
    public TableInfoDto buildTableInfo(String tableName) {
        //获取表信息
        List<Map<String, Object>> tableInfos = this.getTableInfo(tableName);
        if (tableInfos.isEmpty()) {
            throw new NullPointerException("查询出错! ");
        }

        TableInfoDto tableInfo = new TableInfoDto();
        //表名，注释
        List<Object> tValues = Collections.singletonList(tableInfos.get(0).values());

        tableInfo.setName(tValues.get(0) != null ? tValues.get(0).toString() : "null");
        tableInfo.setComment(Objects.isNull(tValues.get(1)) ? tValues.get(1).toString() : "null");

        //获取所有字段
        List<ColumnInfoDto> fullColumn = getColumns(tableName);
        tableInfo.setColumns(fullColumn);

        //获取主键列
        List<String> primaryKeys = getPrimaryKeys(tableName);
        logger.info("主键列为：{}", primaryKeys);

        //设置ifPrimaryKey标志
        fullColumn.forEach(e -> e.setIfPrimaryKey(primaryKeys.contains(e.getName())));
        return tableInfo;
    }

    /**
     * 无论怎么查，返回结果都应该只有表名和表注释，遍历map拿value值即可
     * @param tableName 表名
     * @return List
     */
    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSqlQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            List<Object> parameters = setParameters(tableName);
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, parameters);
        } catch (SQLException e) {
            logger.error("[getTableInfo Exception] --> the exception message is:{}" , e.getMessage());
        }
        return res;
    }

    /**
     * 添加参数
     * @param tableName 表名
     * @return list
     */
    private List<Object> setParameters(String tableName) throws SQLException{
        List<Object> parameters = new ArrayList<>();
        String oracle = "oracle";
        String databaseProductName = connection.getMetaData().getDatabaseProductName();
        if(databaseProductName.equals(oracle) && ObjectUtils.isNotEmpty(currentSchema)){
            parameters.add(currentSchema);
        }
        if(ObjectUtils.isNotEmpty(tableName)){
            parameters.add(tableName);
        }
        return parameters;
    }

    @Override
    public List<Map<String, Object>> getTables() {
        String sqlQueryTables = sqlBuilder.getSqlQueryTables();
        logger.info(sqlQueryTables);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTables, null);
        } catch (SQLException e) {
            logger.error("[getTables Exception] --> the exception message is:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public List<ColumnInfoDto> getColumns(String tableName) {

        List<ColumnInfoDto> fullColumn = new ArrayList<>();
        //获取指定表的所有字段
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.getSqlQueryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            statement = connection.createStatement();
            resultSet = statement.executeQuery(querySql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<DasColumnDto> res = new ArrayList<>();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                DasColumnDto dasColumn = new DasColumnDto();
                dasColumn.setColumnClassName(metaData.getColumnClassName(i));
                dasColumn.setColumnTypeName(metaData.getColumnTypeName(i));
                dasColumn.setColumnDisplaySize(metaData.getColumnDisplaySize(i));
                dasColumn.setColumnName(metaData.getColumnName(i));
                dasColumn.setIsNull(metaData.isNullable(i));
                res.add(dasColumn);
            }

            List<DasColumnDto> dasColumns = buildDasColumn(tableName, res, statement);

            //构建 fullColumn
            fullColumn = buildFullColumn(dasColumns);

        } catch (SQLException e) {
            logger.error("[getColumns Exception] --> the exception message is:{}", e.getMessage());
        }finally {
            JdbcUtils.close(resultSet);
            JdbcUtils.close(statement);
        }
        return fullColumn;
    }

    private List<ColumnInfoDto> buildFullColumn(List<DasColumnDto> dasColumns) {
        List<ColumnInfoDto> res = new ArrayList<>();
        dasColumns.forEach(e -> {
            ColumnInfoDto columnInfo = new ColumnInfoDto();
            columnInfo.setName(e.getColumnName());
            columnInfo.setComment(e.getColumnComment());
            columnInfo.setType(e.getColumnTypeName());
            columnInfo.setLength(e.getColumnDisplaySize());
            columnInfo.setIfPrimaryKey(e.isIsprimaryKey());
            columnInfo.setIsnull(e.getIsNull());
            res.add(columnInfo);
        });
        return res;
    }

    /**
     * 构建DasColumn对象
     * @param tableName 表名
     * @param statement sql执行器对象
     * @return List
     */
    private List<DasColumnDto> buildDasColumn(String tableName, List<DasColumnDto> res, Statement statement) {
        if (currentDatabase.equals(JdbcConstants.MYSQL) || currentDatabase.equals(JdbcConstants.ORACLE) || currentDatabase.equals(JdbcConstants.SQL_SERVER)) {
            List<String> pricolumns = getPrimaryKeys(tableName);
            res.forEach(e -> {
                if (pricolumns.contains(e.getColumnName())) {
                    e.setIsprimaryKey(true);
                }
            });

            for(DasColumnDto tmpDasColumn : res){
                String sqlQueryComment = getSqlQueryComment(tableName,tmpDasColumn.getColumnName());
                //查询字段注释
                String columncomment = executeQueryResultString(sqlQueryComment, statement);
                tmpDasColumn.setColumnComment(columncomment);
            }
        }
        return res;
    }

    /**
     * 获取指定表的主键，可能是多个，所以用list
     * @param tableName 表名
     * @return list
     */
    private List<String> getPrimaryKeys(String tableName) {
        List<String> res = new ArrayList<>();
        String sqlQueryPrimaryKey = sqlBuilder.getSqlQueryPrimaryKey();
        try {
            List<Object> parameters = setParameters(tableName);
            List<Map<String, Object>> pkColumns = JdbcUtils.executeQuery(connection, sqlQueryPrimaryKey, parameters);
            //返回主键名称即可
            pkColumns.forEach(e -> res.add((String) new ArrayList<>(e.values()).get(0)));
        } catch (SQLException e) {
            logger.error("[getPrimaryKeys Exception] --> the exception message is:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public List<String> getColumnNames(String tableName, String datasource) {

        List<String> res = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            //获取查询指定表所有字段的sql语句
            String querySql = sqlBuilder.getSqlQueryFields(tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(querySql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                if (JdbcConstants.HIVE.equals(datasource)) {
                    if (columnName.contains(Constants.SPLIT_POINT)) {
                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName.substring(columnName.indexOf(Constants.SPLIT_POINT) + 1) + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                    } else {
                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
                    }
                } else {
                    res.add(columnName);
                }
            }
        } catch (SQLException e) {
            logger.error("[getColumnNames Exception] --> the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }

    @Override
    public List<String> getTableNames(String tableSchema) {
        List<String> tables = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSqlQueryTables(tableSchema);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
            tables.sort(Comparator.naturalOrder());
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    @Override
    public List<String> getTableNames() {
        return getTableNames(null);
    }

    public Boolean dataSourceTest() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDatabaseProductName().length() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("[dataSourceTest Exception] --> the exception message is:{}", e.getMessage());
        }
        return false;
    }

    protected String getSqlQueryTables(String tableSchema) {
        if(Objects.isNull(tableSchema)){
            return sqlBuilder.getSqlQueryTables();
        }
        return sqlBuilder.getSqlQueryTables(tableSchema);
    }

    @Override
    public List<String> getColumnsByQuerySql(String querySql) throws SQLException {

        List<String> res = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            querySql = querySql.replace(";", "");
            //拼装sql语句，在后面加上 where 1=0 即可
            String sql = querySql.concat(" where 1=0");
            //判断是否已有where，如果是，则加 and 1=0
            //从最后一个 ) 开始找 where，或者整个语句找
            if (querySql.contains(")")) {
                if (querySql.substring(querySql.indexOf(")")).contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            } else {
                if (querySql.contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            }
            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }

    @Override
    public long getMaxIdVal(String tableName, String primaryKey) {
        long maxVal = 0;
        try {
            //获取sql
            String sql = getSqlMaxId(tableName, primaryKey);
            maxVal = executeQueryResultLong(sql);
        } catch (SQLException e) {
            logger.error("[getMaxIdVal Exception] --> the exception message is:{}", e.getMessage());
        }
        return maxVal;
    }

    /**
     * 获取 最大id sql
     * @param tableName 表名
     * @param primaryKey 主键
     * @return string
     */
    private String getSqlMaxId(String tableName, String primaryKey) {
        return sqlBuilder.getMaxId(tableName, primaryKey);
    }

    @Override
    public List<String> getTableSchema() {
        List<String> schemas = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSqlQueryTableSchema();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                schemas.add(tableName);
            }
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return schemas;
    }

    protected String getSqlQueryTableSchema() {
        return sqlBuilder.getSqlQueryTableSchema();
    }

    @Override
    public List<Map<String, Object>> executeQueryListMap(String sql){
        return executeQueryListMap(sql, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters, List<TableAliasDto> tableAliasDtos){
        logger.info(sql);
        List<Map<String, Object>> res = null;
        try {
            logger.info("[ executeQueryList sql ] ->{}", sql);
            logger.info("[ executeQueryList parameters ] ->{}", new Gson().toJson(parameters));
            res = JdbcUtils.executeQuery(connection, sql, parameters, tableAliasDtos, Boolean.TRUE);
        } catch (SQLException e) {
            logger.error("[executeQueryListMap Exception] -->  the exception message is:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public List<Map<String, Object>> executeQueryListMap(String sql,Integer start,Integer end){

        return executeQueryListMap(sql, new ArrayList<>(), start, end);
    }

    @Override
    public List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters,Integer start,Integer end){
        String sqlQueryPages = getSqlQueryPages(sql,start,end);

        return executeQueryListMap(sqlQueryPages, parameters, new ArrayList<>());
    }

    @Override
    public List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters, List<TableAliasDto> tableAliasDtos, Integer start, Integer end){
        String sqlQueryPages = getSqlQueryPages(sql,start,end);
        return executeQueryListMap(sqlQueryPages, parameters, tableAliasDtos);
    }


    protected String getSqlQueryPages(String sql,Integer start,Integer end){
        return sqlBuilder.getSqlQueryPages(sql,start,end);
    }

    @Override
    public List<List<Object>> executeQueryList(String sql){
        return executeQueryList(sql,new ArrayList<>());
    }

    @Override
    public List<List<Object>> executeQueryList(String sql, List<Object> parameters){
        logger.info(sql);
        List<List<Object>> res = null;
        try {
            logger.info("[ executeQueryList sql ] ->{}", sql);
            logger.info("[ executeQueryList parameters ] ->{}", new Gson().toJson(parameters));
            res = JdbcUtils.executeQueryList(connection, sql, parameters, Boolean.TRUE);
        } catch (SQLException e) {
            logger.error("[noPage executeQueryList Exception] --> the exception message is:{}", e.getMessage());
        }
        return res;
    }

    @Override
    public List<List<Object>> executeQueryList(String sql, List<Object> parameters, Integer start,Integer end){
        String sqlQueryPages = getSqlQueryPages(sql,start,end);
        return executeQueryList(sqlQueryPages, parameters);
    }

    @Override
    public Boolean checkTableIsExist(String tableName) {
        String sqlQueryIsExist = getSqlQueryIsExist(tableName);
        try {
            long count = executeQueryResultLong(sqlQueryIsExist);
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("[checkTableIsExist Exception] --> the exception message is:{}", e.getMessage());
        }
        return false;
    }

    protected String getSqlQueryIsExist(String tableName){
        return sqlBuilder.getSqlQueryIsExist(tableName);
    }

    protected String getSqlTableDescCmd(String tableName,String titleSchema){
        return sqlBuilder.getSqlTableDesc(tableName,titleSchema);

    }
    protected String getSqlQueryComment(String tableName, String columnName){
        return sqlBuilder.getSqlQueryComment(currentSchema,tableName,columnName);
    }

    @Override
    public List<Map<String, Object>> getTableNamesComment() {
        String sqlQueryTables = sqlBuilder.getSqlQueryTablesNameComments();
        logger.info(sqlQueryTables);
        List<Map<String, Object>> res = null;
        try {
            if(currentSchema == null){
                currentSchema = connection.getSchema();
            }
            List<Object> parameters = setParameters("");
            res = JdbcUtils.executeQuery(connection, sqlQueryTables, parameters);
        } catch (SQLException e) {
            logger.error("[getTableNamesComment Exception] --> the exception message is:{}", e.getMessage());
        }
        return res;
    }

    protected String getSqlQueryTablesNameComments(){
        String sqlQueryTablesNameComments = sqlBuilder.getSqlQueryTablesNameComments();
        logger.info(sqlQueryTablesNameComments);
        return sqlQueryTablesNameComments;
    }

    @Override
    public Long getTableTotal(String tableName){
        String sqlQueryTables = sqlBuilder.getTableTotal(tableName);
        return getTableTotal(sqlQueryTables,new ArrayList<>());
    }

    @Override
    public Long getTableTotal(String sql,List<Object> parameters){
        long total = 0L;
        try {
            total = executeQueryResultLong(sql, parameters);
        } catch (SQLException e) {
            logger.error("[getTableTotal Exception] --> the exception message is:{}" , e.getMessage());
        }
        return total;
    }

    /**
     * 只执行sql语句 无参数
     * @param sql  语句
     * @return Long
     * @throws SQLException 异常
     */
    private Long executeQueryResultLong(String sql) throws SQLException{
        return executeQueryResultLong(sql,new ArrayList<>());
    }
    /**
     * 执行sql 获取 Long 类型返回值
     * @param sql 语句
     * @param parameters 参数
     * @return Long
     */
    private Long executeQueryResultLong(String sql,List<Object> parameters) throws SQLException{
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        long total;
        logger.info(sql);
        try {
            statement = connection.prepareStatement(sql);
            setParameters(statement,parameters);
            resultSet = statement.executeQuery();
            resultSet.next();
            total = resultSet.getLong(1);
        } finally {
            JdbcUtils.close(resultSet);
            JdbcUtils.close(statement);
        }
        return total;
    }

    /**
     * 执行sql 获取 String 类型返回值
     * @param sql 语句
     * @return String
     */
    private String executeQueryResultString(String sql,Statement statement) {
        ResultSet resultSet = null;
        String total = null;
        logger.info(sql);
        try {
            resultSet = statement.executeQuery(sql);
            resultSet.next();
            total = resultSet.getString(1);
        }catch (SQLException e){
            logger.error("[executeQueryResultString executeQuery getSQLQueryComment Exception] --> "
                    + "the exception message is:{}", e.getMessage());
        } finally {
            JdbcUtils.close(resultSet);
        }
        return total;
    }

    /**
     * 给 statement 设置参数
     * @param stmt 对象
     * @param parameters 参数
     */
    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }
    }
}

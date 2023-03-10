package com.yanhuanxy.multifunexport.tools.util.origin;

import com.yanhuanxy.multifunexport.tools.constant.origin.JdbcConstants;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.TableAliasDto;
import org.apache.xmlbeans.impl.util.HexBin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.Closeable;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.Date;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * jdbc utils
 *
 * @author yym
 * @date 2020/08/28
 */
public final class JdbcUtils implements JdbcConstants {

    private static Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);


    private static final Properties DRIVER_URL_MAPPING = new Properties();

    private static Boolean mysql_driver_version_6      = null;

    private static final String DATATYPE = "DATE";

    static {
        try {
            ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            if (ctxClassLoader != null) {
                for (Enumeration<URL> e = ctxClassLoader.getResources("META-INF/druid-driver.properties"); e.hasMoreElements();) {
                    URL url = e.nextElement();

                    Properties property = new Properties();

                    InputStream is = null;
                    try {
                        is = url.openStream();
                        property.load(is);
                    } finally {
                        JdbcUtils.close(is);
                    }

                    DRIVER_URL_MAPPING.putAll(property);
                }
            }
        } catch (Exception e) {
            LOG.error("load druid-driver.properties error", e);
        }
    }

    public static void close(Connection x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close connection error", e);
        }
    }

    public static void close(Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close statement error", e);
        }
    }

    public static void close(ResultSet x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close result set error", e);
        }
    }

    public static void close(Closeable x) {
        if (x == null) {
            return;
        }

        try {
            x.close();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }

    public static void close(Blob x) {
        if (x == null) {
            return;
        }

        try {
            x.free();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }

    public static void close(Clob x) {
        if (x == null) {
            return;
        }

        try {
            x.free();
        } catch (Exception e) {
            LOG.debug("close error", e);
        }
    }

    public static void printResultSet(ResultSet rs) throws SQLException {
        printResultSet(rs, System.out);
    }

    public static void printResultSet(ResultSet rs, PrintStream out) throws SQLException {
        printResultSet(rs, out, true, "\t");
    }

    public static void printResultSet(ResultSet rs, PrintStream out, boolean printHeader, String seperator) throws SQLException {
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        if (printHeader) {
            for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }
                out.print(metadata.getColumnName(columnIndex));
            }
        }

        out.println();

        while (rs.next()) {

            for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }

                int type = metadata.getColumnType(columnIndex);

                if (type == Types.VARCHAR || type == Types.CHAR || type == Types.NVARCHAR || type == Types.NCHAR) {
                    out.print(rs.getString(columnIndex));
                } else if (type == Types.DATE) {
                    Date date = rs.getDate(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(date.toString());
                    }
                } else if (type == Types.BIT) {
                    boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Boolean.toString(value));
                    }
                } else if (type == Types.BOOLEAN) {
                    boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Boolean.toString(value));
                    }
                } else if (type == Types.TINYINT) {
                    byte value = rs.getByte(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Byte.toString(value));
                    }
                } else if (type == Types.SMALLINT) {
                    short value = rs.getShort(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Short.toString(value));
                    }
                } else if (type == Types.INTEGER) {
                    int value = rs.getInt(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Integer.toString(value));
                    }
                } else if (type == Types.BIGINT) {
                    long value = rs.getLong(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(Long.toString(value));
                    }
                } else if (type == Types.TIMESTAMP) {
                    out.print(String.valueOf(rs.getTimestamp(columnIndex)));
                } else if (type == Types.DECIMAL) {
                    out.print(String.valueOf(rs.getBigDecimal(columnIndex)));
                } else if (type == Types.CLOB) {
                    out.print(String.valueOf(rs.getString(columnIndex)));
                } else if (type == Types.JAVA_OBJECT) {
                    Object object = rs.getObject(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(String.valueOf(object));
                    }
                } else if (type == Types.LONGVARCHAR) {
                    Object object = rs.getString(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        out.print(String.valueOf(object));
                    }
                } else if (type == Types.NULL) {
                    out.print("null");
                } else {
                    Object object = rs.getObject(columnIndex);

                    if (rs.wasNull()) {
                        out.print("null");
                    } else {
                        if (object instanceof byte[]) {
                            byte[] bytes = (byte[]) object;
                            String text = Arrays.toString(HexBin.encode(bytes));
                            out.print(text);
                        } else {
                            out.print(String.valueOf(object));
                        }
                    }
                }
            }
            out.println();
        }
    }

    public static String getTypeName(int sqlType) {
        switch (sqlType) {
            case Types.ARRAY:
                return "ARRAY";

            case Types.BIGINT:
                return "BIGINT";

            case Types.BINARY:
                return "BINARY";

            case Types.BIT:
                return "BIT";

            case Types.BLOB:
                return "BLOB";

            case Types.BOOLEAN:
                return "BOOLEAN";

            case Types.CHAR:
                return "CHAR";

            case Types.CLOB:
                return "CLOB";

            case Types.DATALINK:
                return "DATALINK";

            case Types.DATE:
                return "DATE";

            case Types.DECIMAL:
                return "DECIMAL";

            case Types.DISTINCT:
                return "DISTINCT";

            case Types.DOUBLE:
                return "DOUBLE";

            case Types.FLOAT:
                return "FLOAT";

            case Types.INTEGER:
                return "INTEGER";

            case Types.JAVA_OBJECT:
                return "JAVA_OBJECT";

            case Types.LONGNVARCHAR:
                return "LONGNVARCHAR";

            case Types.LONGVARBINARY:
                return "LONGVARBINARY";

            case Types.NCHAR:
                return "NCHAR";

            case Types.NCLOB:
                return "NCLOB";

            case Types.NULL:
                return "NULL";

            case Types.NUMERIC:
                return "NUMERIC";

            case Types.NVARCHAR:
                return "NVARCHAR";

            case Types.REAL:
                return "REAL";

            case Types.REF:
                return "REF";

            case Types.ROWID:
                return "ROWID";

            case Types.SMALLINT:
                return "SMALLINT";

            case Types.SQLXML:
                return "SQLXML";

            case Types.STRUCT:
                return "STRUCT";

            case Types.TIME:
                return "TIME";

            case Types.TIMESTAMP:
                return "TIMESTAMP";

            case Types.TINYINT:
                return "TINYINT";

            case Types.VARBINARY:
                return "VARBINARY";

            case Types.VARCHAR:
                return "VARCHAR";

            default:
                return "OTHER";

        }
    }

    public static String getDbType(String rawUrl, String driverClassName) {
        if (rawUrl == null) {
            return null;
        }

        if (rawUrl.startsWith("jdbc:derby:") || rawUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return DERBY;
        } else if (rawUrl.startsWith("jdbc:mysql:") || rawUrl.startsWith("jdbc:cobar:")
                || rawUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return MYSQL;
        } else if (rawUrl.startsWith("jdbc:mariadb:")) {
            return MARIADB;
        } else if (rawUrl.startsWith("jdbc:oracle:") || rawUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return ORACLE;
        } else if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
            return ALI_ORACLE;
        } else if (rawUrl.startsWith("jdbc:microsoft:") || rawUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return SQL_SERVER;
        } else if (rawUrl.startsWith("jdbc:sqlserver:") || rawUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return SQL_SERVER;
        } else if (rawUrl.startsWith("jdbc:sybase:Tds:") || rawUrl.startsWith("jdbc:log4jdbc:sybase:")) {
            return SYBASE;
        } else if (rawUrl.startsWith("jdbc:jtds:") || rawUrl.startsWith("jdbc:log4jdbc:jtds:")) {
            return JTDS;
        } else if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
            return MOCK;
        } else if (rawUrl.startsWith("jdbc:postgresql:") || rawUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return POSTGRESQL;
        } else if (rawUrl.startsWith("jdbc:edb:")) {
            return ENTERPRISEDB;
        } else if (rawUrl.startsWith("jdbc:hsqldb:") || rawUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return HSQL;
        } else if (rawUrl.startsWith("jdbc:odps:")) {
            return ODPS;
        } else if (rawUrl.startsWith("jdbc:db2:")) {
            return DB2;
        } else if (rawUrl.startsWith("jdbc:sqlite:")) {
            return SQLITE;
        } else if (rawUrl.startsWith("jdbc:ingres:")) {
            return "ingres";
        } else if (rawUrl.startsWith("jdbc:h2:") || rawUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return H2;
        } else if (rawUrl.startsWith("jdbc:mckoi:")) {
            return "mckoi";
        } else if (rawUrl.startsWith("jdbc:cloudscape:")) {
            return "cloudscape";
        } else if (rawUrl.startsWith("jdbc:informix-sqli:") || rawUrl.startsWith("jdbc:log4jdbc:informix-sqli:")) {
            return "informix";
        } else if (rawUrl.startsWith("jdbc:timesten:")) {
            return "timesten";
        } else if (rawUrl.startsWith("jdbc:as400:")) {
            return "as400";
        } else if (rawUrl.startsWith("jdbc:sapdb:")) {
            return "sapdb";
        } else if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
            return "JSQLConnect";
        } else if (rawUrl.startsWith("jdbc:JTurbo:")) {
            return "JTurbo";
        } else if (rawUrl.startsWith("jdbc:firebirdsql:")) {
            return "firebirdsql";
        } else if (rawUrl.startsWith("jdbc:interbase:")) {
            return "interbase";
        } else if (rawUrl.startsWith("jdbc:pointbase:")) {
            return "pointbase";
        } else if (rawUrl.startsWith("jdbc:edbc:")) {
            return "edbc";
        } else if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
            return "mimer";
        } else if (rawUrl.startsWith("jdbc:dm:")) {
            return JdbcConstants.DM;
        } else if (rawUrl.startsWith("jdbc:kingbase:")) {
            return JdbcConstants.KINGBASE;
        } else if (rawUrl.startsWith("jdbc:gbase:")) {
            return JdbcConstants.GBASE;
        } else if (rawUrl.startsWith("jdbc:xugu:")) {
            return JdbcConstants.XUGU;
        } else if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return LOG4JDBC;
        } else if (rawUrl.startsWith("jdbc:hive:")) {
            return HIVE;
        } else if (rawUrl.startsWith("jdbc:hive2:")) {
            return HIVE;
        } else if (rawUrl.startsWith("jdbc:phoenix:")) {
            return PHOENIX;
        } else if (rawUrl.startsWith("jdbc:elastic:")) {
            return ELASTIC_SEARCH;
        } else if (rawUrl.startsWith("jdbc:clickhouse:")) {
            return CLICKHOUSE;
        }else if (rawUrl.startsWith("jdbc:presto:")) {
            return PRESTO;
        } else {
            return null;
        }
    }

    public static Driver createDriver(String driverClassName) throws SQLException {
        return createDriver(null, driverClassName);
    }

    public static Driver createDriver(ClassLoader classLoader, String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            } catch (ClassNotFoundException e) {
                // skip
            }
        }

        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }

        try {
            return (Driver) clazz.newInstance();
        } catch (IllegalAccessException e) {
            throw new SQLException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public static int executeUpdate(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        return executeUpdate(dataSource, sql, Arrays.asList(parameters));
    }

    public static int executeUpdate(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return executeUpdate(conn, sql, parameters);
        } finally {
            close(conn);
        }
    }

    public static int executeUpdate(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        int updateCount;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);

            updateCount = stmt.executeUpdate();
        } finally {
            JdbcUtils.close(stmt);
        }

        return updateCount;
    }

    public static void execute(DataSource dataSource, String sql, Object... parameters) throws SQLException {
        execute(dataSource, sql, Arrays.asList(parameters));
    }

    public static void execute(DataSource dataSource, String sql, List<Object> parameters) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            execute(conn, sql, parameters);
        } finally {
            close(conn);
        }
    }

    public static void execute(Connection conn, String sql) throws SQLException {
        execute(conn, sql, Collections.emptyList());
    }

    public static void execute(Connection conn, String sql, List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            setParameters(stmt, parameters);

            stmt.executeUpdate();
        } finally {
            JdbcUtils.close(stmt);
        }
    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, Object... parameters)
            throws SQLException {
        return executeQuery(dataSource, sql, Arrays.asList(parameters));
    }

    public static List<Map<String, Object>> executeQuery(DataSource dataSource, String sql, List<Object> parameters)
            throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return executeQuery(conn, sql, parameters);
        } finally {
            close(conn);
        }
    }

    /**
     * 执行查询语句 返回键值对
     * @param conn 连接
     * @param sql sql语句
     * @param parameters 参数
     * @return List
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters, boolean isTochar) throws SQLException{
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(parameters != null){
                setParameters(stmt, parameters);
            }
            rs = stmt.executeQuery();
            rows = getResultSetMetaDataOfDateIsToChar(rs, new ArrayList<>(),isTochar);
        } finally {
            JdbcUtils.close(rs);
        }
        return rows;
    }
    /**
     * 执行查询语句 返回键值对 多表联查
     * @param conn 连接
     * @param sql sql语句
     * @param parameters 参数
     * @return List
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters, List<TableAliasDto> tableAliasDtos, boolean isTochar) throws SQLException{
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        ResultSet rs = null;
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(parameters != null){
                setParameters(stmt, parameters);
            }
            rs = stmt.executeQuery();
            rows = getResultSetMetaDataOfDateIsToChar(rs, tableAliasDtos, isTochar);
        } finally {
            JdbcUtils.close(rs);
        }
        return rows;
    }


    /**
     * 执行查询语句
     * @param conn 连接
     * @param sql sql语句
     * @param parameters 参数
     * @return List
     * @throws SQLException
     */
    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, List<Object> parameters)
            throws SQLException {
        boolean isTochar = false;
        return executeQuery(conn, sql, parameters, isTochar);
    }

    /**
     * 包含表头字段名
     * 解析 ResultSet 内的数据 根据isTochar 将时间类型的值转成字符串
     * @param rs sql result对象
     * @param isTochar 是否将时间戳转成 字符串
     * @return List
     * @throws SQLException
     */
    private static List<Map<String, Object>> getResultSetMetaDataOfDateIsToChar(ResultSet rs, List<TableAliasDto> tableAliasDtos, boolean isTochar) throws SQLException{
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        ResultSetMetaData rsMeta = rs.getMetaData();
        String symbol = "_";
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<String, Object>();
            if(Objects.isNull(tableAliasDtos) || tableAliasDtos.isEmpty()){
                for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                    int tmpindex = i+1;
                    String columName = rsMeta.getColumnLabel(tmpindex);
                    String columnTypeName = rsMeta.getColumnTypeName(tmpindex);
                    Object tmpval = convertDateToChar(rs.getObject(tmpindex), columnTypeName, isTochar);
                    row.put(columName, tmpval);
                }
            }else {
                int tmpindex = 0;
                for(TableAliasDto tableAliasDto : tableAliasDtos){
                    List<String> fields = tableAliasDto.getFields();
                    String tableAlias = tableAliasDto.getTableAlias();
                    Map<String, Object> tablecolumn = new LinkedHashMap<String, Object>();
                    for(String columnfield: fields){
                        tmpindex++;
                        String columName = rsMeta.getColumnLabel(tmpindex);
                        String columnTypeName = rsMeta.getColumnTypeName(tmpindex);
                        Object tmpval = convertDateToChar(rs.getObject(tmpindex), columnTypeName, isTochar);
                        String tmpcolumn = columnfield + symbol + tableAlias + symbol;
                        if(columName.equalsIgnoreCase(tmpcolumn)){
                            tablecolumn.put(columnfield,tmpval);
                        }
                    }
                    row.put(tableAliasDto.getTableName(), tablecolumn);
                }
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 执行查询语句
     * @param conn 连接
     * @param sql sql语句
     * @param parameters 参数
     * @return List
     * @throws SQLException
     */
    public static List<List<Object>> executeQueryList(Connection conn, String sql, List<Object> parameters) throws SQLException {
        boolean isTochar = false;
        return executeQueryList(conn,sql,parameters,isTochar);
    }

    /**
     * 执行查询语句
     * @param conn 连接
     * @param sql sql语句
     * @param parameters 参数
     * @param isTochar 是否转换时间为 时间字符串
     * @return List
     * @throws SQLException
     */
    public static List<List<Object>> executeQueryList(Connection conn, String sql, List<Object> parameters, boolean isTochar) throws SQLException {
        List<List<Object>> rows = new ArrayList<List<Object>>();
        ResultSet rs = null;
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            if(parameters != null){
                setParameters(stmt, parameters);
            }
            rs = stmt.executeQuery();
            rows = getResultSetMetaDataNoFieldOfDateIsToChar(rs,isTochar);
        } finally {
            JdbcUtils.close(rs);
        }
        return rows;
    }

    /**
     * 解析 ResultSet 内的数据 根据isTochar 将时间类型的值转成字符串
     * @param rs sql result对象
     * @param isTochar 是否将时间戳转成 字符串
     * @return List
     * @throws SQLException
     */
    private static List<List<Object>> getResultSetMetaDataNoFieldOfDateIsToChar(ResultSet rs,boolean isTochar) throws SQLException{
        List<List<Object>> rows = new ArrayList<List<Object>>();
        ResultSetMetaData rsMeta = rs.getMetaData();

        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                int tmpindex = i+1;
                String columnTypeName = rsMeta.getColumnTypeName(tmpindex);
                Object tmpval = convertDateToChar(rs.getObject(tmpindex), columnTypeName, isTochar);
                row.add(tmpval);
            }
            rows.add(row);
        }
        return rows;
    }

    /**
     * 如果是时间则转换成 字符串
     * @param tmpval 值
     * @param columnTypeName 列类型
     * @return Object
     */
    private static Object convertDateToChar(Object tmpval, String columnTypeName, boolean isTochar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(isTochar && DATATYPE.equalsIgnoreCase(columnTypeName)){
            if(tmpval instanceof Timestamp){
                Date val = new Date(((Timestamp) tmpval).getTime());
                return simpleDateFormat.format(val);
            }else if(tmpval instanceof Date){
                Date val = (Date) tmpval;
                return simpleDateFormat.format(val);
            }
        }
        return tmpval;
    }

    /**
     * 给预先申明的通配符添加值
     * @param stmt 表申明对象
     * @param parameters 参数
     * @throws SQLException
     */
    private static void setParameters(PreparedStatement stmt, List<Object> parameters) throws SQLException{
        setParameters(stmt, null, parameters);
    }

    private static void setParameters(PreparedStatement stmt, Connection conn, List<Object> parameters) throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            Object param = parameters.get(i);
            if(param instanceof String[]){
                if(Objects.isNull(conn)){
                    stmt.setObject(i + 1, String.join(",", (String[]) param));
                }else{
                    stmt.setArray(i + 1, conn.createArrayOf("VARCHAR", (String[]) param));
                }
            }else{
                stmt.setObject(i + 1, param);
            }
        }
    }

}

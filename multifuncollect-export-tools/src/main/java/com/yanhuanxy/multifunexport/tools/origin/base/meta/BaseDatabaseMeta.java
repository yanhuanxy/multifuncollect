package com.yanhuanxy.multifunexport.tools.origin.base.meta;

/**
 * meta信息 基础
 *
 * @author yym
 * @date 2020/08/27
 */
public abstract class BaseDatabaseMeta implements DatabaseInterface {

    @Override
    public String getSqlQueryFields(String tableName) {
        return "SELECT * FROM " + tableName + " where 1=0";
    }

    @Override
    public String getSqlQueryTablesNameComments() {
        return "select table_name,table_comment from information_schema.tables where table_schema=?";
    }

    @Override
    public String getSqlQueryTableNameComment() {
        return "select table_name,table_comment from information_schema.tables where table_schema=? and table_name = ?";
    }

    @Override
    public String getSqlQueryPrimaryKey() {
        return null;
    }

    @Override
    public String getSqlQueryComment(String schemaName, String tableName, String columnName) {
        return null;
    }

    @Override
    public String getSqlQueryColumns(String... args) {
        return null;
    }

    @Override
    public String getMaxId(String tableName, String primaryKey) {
        return String.format("select max(%s) from %s",primaryKey,tableName);
    }

    @Override
    public String getSqlQueryTableSchema(String... args) {
        return null;
    }

    @Override
    public String getSqlQueryTables() {
        return null;
    }

    @Override
    public String getSqlQueryTables(String... tableSchema) {
        return null;
    }

    @Override
    public String getSqlQueryPages(String sql,Integer start,Integer end) {
        return null;
    }

    @Override
    public String getSqlDelTableData(String tableName) {
        return String.format("truncate table %s ",tableName);
    }

    @Override
    public String getSqlTruncateTableData(String tableName){
        return String.format("delete from %s ",tableName);
    }

    @Override
    public String getSqlDistanceFinderDataBaseSizeBySchema(String schema) {
        return null;
    }

    @Override
    public String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema, String tableName) {
        return null;
    }

    @Override
    public String getSqlTableDesc(String tableName, String titleSchema) {
        return null;
    }

    @Override
    public String getTableTotal(String tableName) {
        return String.format("SELECT count(*) FROM ('%s')",tableName);
    }
}

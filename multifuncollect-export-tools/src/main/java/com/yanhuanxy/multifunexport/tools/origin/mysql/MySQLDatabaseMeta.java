package com.yanhuanxy.multifunexport.tools.origin.mysql;


import com.yanhuanxy.multifunexport.tools.origin.base.meta.BaseDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;

import java.util.List;

/**
 * MySQL数据库 meta信息查询
 *
 * @author yym
 * @date 2020/08/27
 */
public class MySQLDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private static MySQLDatabaseMeta single;

    private MySQLDatabaseMeta(){
        super();
    }

    public synchronized static MySQLDatabaseMeta getInstance() {
        if (single == null) {
            single = new MySQLDatabaseMeta();
        }
        return single;
    }

    @Override
    public String getSqlQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("SELECT COLUMN_COMMENT FROM information_schema.COLUMNS where TABLE_SCHEMA = '%s' and TABLE_NAME = '%s' and COLUMN_NAME = '%s'", schemaName, tableName, columnName);
    }

    @Override
    public String getSqlQueryPrimaryKey() {
        return "select column_name from information_schema.columns where table_schema=? and table_name=? and column_key = 'PRI'";
    }

    @Override
    public String getSqlQueryTables() {
        return "show tables";
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
    public String getSqlQueryColumns(String... args) {
        return "select column_name from information_schema.columns where table_schema=? and table_name=?";
    }

    @Override
    public String getSqlQueryPages(String sql,Integer start,Integer end){
//        StringBuilder sqlbuf = new StringBuilder(sql);
//        sqlbuf.append(" limit ").append(start).append(",").append(end);
        return String.format(sql + " limit %s , %s ", start, end);
    }

    @Override
    public String getSqlQueryIsExist(String tableName){
        return String.format("select count(1) from information_schema.TABLES where table_name = '%s' and table_schema = ? ",tableName);
    }

    @Override
    public String getSqlAddTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName());
        buffer.append(" add ").append(alterTableDto.getColumnName());
        buffer.append(" ").append(alterTableDto.getColumnField());
        if(alterTableDto.getAfterColumn() != null && "".equals(alterTableDto.getAfterColumn())){
            buffer.append(" after ").append(alterTableDto.getAfterColumn());
        }
        return buffer.toString();
    }

    @Override
    public String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist) {
        StringBuilder buffer = new StringBuilder();
        int size = alterTableDtolist.size();
        for(int i=0;i<size;i++){
            AlterTableDto alterTableDto = alterTableDtolist.get(i);
            if(i == 0){
                buffer.append("alter table ").append(alterTableDto.getTableName());
            }
            buffer.append(" add ").append(alterTableDto.getColumnName());
            buffer.append(" ").append(alterTableDto.getColumnField());
            if(alterTableDto.getAfterColumn() != null && "".equals(alterTableDto.getAfterColumn())){
                buffer.append(" after ").append(alterTableDto.getAfterColumn());
            }
            if(i != (size-1)){
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    @Override
    public String getSqlUpdateTableColumn(AlterTableDto alterTableDto) {
//        StringBuilder buffer = new StringBuilder();
//        buffer.append("alter table ").append(alterTableDto.getTableName());
//        buffer.append(" change ").append(alterTableDto.getColumnName());
//        buffer.append(" ").append(alterTableDto.getNewColumnName());
//        buffer.append(" ").append(alterTableDto.getColumnField());

        return String.format("alter table %s change %s to %s %s", alterTableDto.getTableName(),
                alterTableDto.getColumnName(), alterTableDto.getNewColumnName(), alterTableDto.getColumnField());
    }

    @Override
    public String getSqlChangeTableColumn(AlterTableDto alterTableDto) {
//        StringBuilder buffer = new StringBuilder();
//        buffer.append("alter table ").append(alterTableDto.getTableName());
//        buffer.append(" modify ").append(alterTableDto.getColumnName());
//        buffer.append(" ").append(alterTableDto.getColumnField());
        return String.format("alter table %s modify %s %s",  alterTableDto.getTableName(),
                alterTableDto.getColumnName(), alterTableDto.getColumnField());
    }

    @Override
    public String getSqlDelTableColumn(AlterTableDto alterTableDto) {
//        StringBuilder buffer = new StringBuilder();
//        buffer.append("alter table ").append(alterTableDto.getTableName());
//        buffer.append(" drop ").append(alterTableDto.getColumnName());
        return String.format("alter table %s drop %s ",  alterTableDto.getTableName(),
                alterTableDto.getColumnName());
    }

    @Override
    public String getSqlRenameTableName(String oldtableName, String tableName) {
//        StringBuilder buffer = new StringBuilder();
//        buffer.append("alter table ").append(oldtableName);
//        buffer.append(" rename as ").append(tableName);
        return String.format("alter table %s rename as %s ", oldtableName, tableName);
    }

    @Override
    public String getSqlUpdateTableComment(String tableName, String tableComment) {
        return String.format("alter table %s comment '%s'",tableName,tableComment);
    }

    @Override
    public String getSqlUpdateTableColumnComment(AlterTableDto alterTableDto) {
//        StringBuilder buffer = new StringBuilder();
//        buffer.append("alter table ").append(alterTableDto.getTableName());
//        buffer.append(" modify column ").append(alterTableDto.getColumnName());
//        buffer.append(" ").append(alterTableDto.getColumnType());
//        buffer.append(" comment  '").append(alterTableDto.getColumnComment()).append("'");
        return String.format("alter table %s modify column %s %s comment '%s'",  alterTableDto.getTableName(),
                alterTableDto.getColumnName(), alterTableDto.getColumnType(), alterTableDto.getColumnComment());
    }

    @Override
    public String getSqlDistanceFinderDataBaseSizeBySchema(String schema) {
        return String.format("SELECT sum(DATA_LENGTH)+sum(INDEX_LENGTH) " +
                "FROM information_schema.TABLES WHERE TABLE_SCHEMA='%s'",schema);
    }

    @Override
    public String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema, String tableName) {
        return String.format("SELECT DATA_LENGTH+INDEX_LENGTH " +
                "FROM information_schema.TABLES  WHERE TABLE_SCHEMA='%s' AND TABLE_NAME='%s'",schema,tableName);
    }

    @Override
    public String getSqlTableDesc(String tableName,String titleSchema) {
        return String.format("select * from INFORMATION_SCHEMA.Columns where table_name='%s' and table_schema = '%s' GROUP BY COLUMN_NAME",tableName,titleSchema);
    }
}

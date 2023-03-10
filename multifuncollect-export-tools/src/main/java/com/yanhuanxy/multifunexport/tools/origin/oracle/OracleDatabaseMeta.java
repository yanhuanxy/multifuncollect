package com.yanhuanxy.multifunexport.tools.origin.oracle;


import com.yanhuanxy.multifunexport.tools.origin.base.meta.BaseDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;

import java.util.List;

/**
 * Oracle数据库 meta信息查询
 *
 * @author yym
 * @date 2020/08/27
 */
public class OracleDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private static OracleDatabaseMeta single;

    private OracleDatabaseMeta(){
        super();
    }

    public synchronized static OracleDatabaseMeta getInstance() {
        if (single == null) {
            single = new OracleDatabaseMeta();
        }
        return single;
    }

    @Override
    public String getSqlQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("select B.comments as column_comment \n" +
                "  from user_tab_columns A, user_col_comments B\n" +
                " where a.COLUMN_NAME = b.column_name\n" +
                "   and A.Table_Name = B.Table_Name\n" +
                "   and A.Table_Name = upper('%s')\n" +
                "   AND A.column_name  = '%s'", tableName, columnName);
    }

    @Override
    public String getSqlQueryPrimaryKey() {
        return "select cu.column_name from user_cons_columns cu, user_constraints au where cu.constraint_name = au.constraint_name and au.constraint_type = 'P' and au.table_name = ?";
    }

    @Override
    public String getSqlQueryTablesNameComments() {
        return "select table_name,comments from user_tab_comments";
    }

    @Override
    public String getSqlQueryTableNameComment() {
        return "select table_name,comments as table_comment from user_tab_comments where table_name = ?";
    }

    @Override
    public String getSqlQueryTables(String... tableSchema) {
        return "select table_name from dba_tables where owner='" + tableSchema[0] + "'";
    }

    @Override
    public String getSqlQueryTableSchema(String... args) {
        return "select username from sys.dba_users";
    }


    @Override
    public String getSqlQueryTables() {
        return "select table_name from user_tab_comments";
    }

    @Override
    public String getSqlQueryColumns(String... args) {
        return "select table_name,comments as table_comment from user_tab_comments where table_name = ?";
    }

    @Override
    public String getSqlQueryPages(String sql,Integer start,Integer end){
        StringBuilder sqlbuf = new StringBuilder();
        sqlbuf.append("SELECT * FROM (SELECT a.*, ROWNUM rn FROM ( ");
        sqlbuf.append(sql);
        sqlbuf.append(" ) a WHERE ROWNUM <= ").append(end);
        sqlbuf.append(" ) WHERE rn > ").append(start);
        return sqlbuf.toString();
    }

    @Override
    public String getSqlQueryIsExist(String tableName){
        return String.format("select count(1) from user_all_tables where table_name = '%s'",tableName.toUpperCase());
    }

    @Override
    public String getSqlAddTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName().toUpperCase());
        buffer.append(" add ").append(alterTableDto.getColumnName().toUpperCase());
        buffer.append(" ").append(alterTableDto.getColumnField());
        return buffer.toString();
    }

    @Override
    public String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist) {
        StringBuilder buffer = new StringBuilder();
        int size = alterTableDtolist.size();
        for(int i=0;i<size;i++){
            AlterTableDto alterTableDto = alterTableDtolist.get(i);
            if(i == 0){
                buffer.append("alter table ").append(alterTableDto.getTableName().toUpperCase());
                buffer.append(" add (");
            }
            buffer.append(" ").append(alterTableDto.getColumnName().toUpperCase());
            buffer.append(" ").append(alterTableDto.getColumnField());
            if(i == (size-1)){
                buffer.append(")");
            }else {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }

    @Override
    public String getSqlUpdateTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName().toUpperCase());
        buffer.append(" rename column ").append(alterTableDto.getColumnName().toUpperCase());
        buffer.append(" to ").append(alterTableDto.getNewColumnName().toUpperCase());
        return buffer.toString();
    }

    @Override
    public String getSqlChangeTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName().toUpperCase());
        buffer.append(" modify (").append(alterTableDto.getColumnName().toUpperCase());
        buffer.append(" ").append(alterTableDto.getColumnField()).append(")");
        return buffer.toString();
    }

    @Override
    public String getSqlDelTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName().toUpperCase());
        buffer.append(" drop column ").append(alterTableDto.getColumnName().toUpperCase());
        return buffer.toString();
    }

    @Override
    public String getSqlRenameTableName(String oldtableName, String tableName) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(oldtableName.toUpperCase());
        buffer.append(" rename to ").append(tableName.toUpperCase());
        return buffer.toString();
    }

    @Override
    public String getSqlUpdateTableComment(String tableName, String tableComment) {
        return String.format("comment on table %s is '%s'", tableName.toUpperCase(),tableComment);
    }

    @Override
    public String getSqlUpdateTableColumnComment(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("comment on column ").append(alterTableDto.getTableName().toUpperCase()).append(".").append(alterTableDto.getColumnName().toUpperCase());
        buffer.append(" is '").append(alterTableDto.getColumnComment()).append("'");
        return buffer.toString();
    }

    @Override
    public String getSqlDistanceFinderDataBaseSizeBySchema(String schema) {

        return String.format("select sum(bytes) from dba_segments where owner='%s'",schema);
    }

    @Override
    public String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema, String tableName) {
        return String.format("select sum(bytes) from user_segments where segment_name = upper('%s')",tableName);
    }

    @Override
    public String getSqlTableDesc(String tableName,String titleSchema) {
        return String.format("select a.*,b.COMMENTS from  all_tab_columns a right join all_col_comments b on a.COLUMN_NAME = b.COLUMN_NAME   where a.table_name='%s' and b.table_name='%s'",tableName,tableName);
    }
}

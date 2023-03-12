package com.yanhuanxy.multifunexport.tools.origin.sqlserver;


import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.BaseDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;

import java.util.List;

/**
 * SqlServer数据库 meta信息查询
 *
 * @author yym
 * @date 2020/08/27
 */
public class SqlServerDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private SqlServerDatabaseMeta(){
        super();
    }

    /**
     * 内部类创建
     */
    private static class SqlServerDatabaseMetaHolder{
        private final static SqlServerDatabaseMeta INSTANCE = new SqlServerDatabaseMeta();
    }

    /**
     * 获取 OracleDatabaseMeta
     */
    public static SqlServerDatabaseMeta getInstance(){

        return SqlServerDatabaseMetaHolder.INSTANCE;
    }

    @Override
    public String getSqlQueryTables() {
        return "SELECT Name as table_name FROM SysObjects Where XType='U' ORDER BY Name";
    }

    @Override
    public String getSqlQueryTables(String... tableSchema) {
        return "select schema_name(schema_id)+'.'+object_name(object_id) from sys.objects \n" +
                "where type ='U' \n" +
                "and schema_name(schema_id) ='" + tableSchema[0] + "'";
    }


    @Override
    public String getSqlQueryComment(String schemaName, String tableName, String columnName) {
        return String.format("SELECT " +
        "CONVERT(nvarchar(200),ISNULL(C.value, '')) as column_comment " +
        "FROM sys.tables A " +
        "INNER JOIN sys.columns B ON B.object_id = A.object_id " +
        "LEFT JOIN sys.extended_properties C ON C.major_id = B.object_id AND C.minor_id = B.column_id " +
        "WHERE A.name = '%s' AND B.name = '%s'",tableName,columnName);
    }

    @Override
    public String getSqlQueryTablesNameComments() {
        return "SELECT DISTINCT " +
                "d.name as table_name," +
                "CONVERT(nvarchar(100),ISNULL(f.value, '')) as table_comment " +
                "FROM " +
                "syscolumns a " +
                "LEFT JOIN systypes b ON a.xusertype= b.xusertype " +
                "INNER JOIN sysobjects d ON a.id= d.id " +
                "AND d.xtype= 'U' " +
                "AND d.name<> 'dtproperties' " +
                "LEFT JOIN syscomments e ON a.cdefault= e.id " +
                "LEFT JOIN sys.extended_properties g ON a.id= G.major_id " +
                "AND a.colid= g.minor_id " +
                "LEFT JOIN sys.extended_properties f ON d.id= f.major_id " +
                "AND f.minor_id= 0";
    }

    @Override
    public String getSqlQueryTableSchema(String... args) {
        return "select distinct schema_name(schema_id) from sys.objects where type ='U';";
    }

    @Override
    public String getSqlQueryPages(String sql,Integer start,Integer end){
        StringBuilder sqlbuf = new StringBuilder();
        sqlbuf.append("select * from (select row_number()over(order by tempcolumn)temprownumber,* from (");
        sqlbuf.append("select top ").append(end).append(" tempcolumn=0,* from (");
        sqlbuf.append(sql);
        sqlbuf.append(") inn ) t) tt where temprownumber>").append(start);
        return sqlbuf.toString();
    }

    @Override
    public String getSqlQueryIsExist(String tableName){
        return String.format("SELECT count(1) FROM SysObjects Where XType='U' AND Name = '%s'",tableName);
    }


    @Override
    public String getSqlAddTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName());
        buffer.append(" add ").append(alterTableDto.getColumnName());
        buffer.append(" ").append(alterTableDto.getColumnField());
        return buffer.toString();
    }

    @Override
    public String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist) {
        StringBuilder buffer = new StringBuilder();
        int count = alterTableDtolist.size();
        for(int i = 0;i<count;i++){
            AlterTableDto alterTableDto = alterTableDtolist.get(i);
            if(i==0){
                buffer.append("alter table ").append(alterTableDto.getTableName()).append(" add ");
            }
            buffer.append(alterTableDto.getColumnName());
            buffer.append(" ").append(alterTableDto.getColumnField());
            if((i+1)!=count){
                buffer.append(",");
            }
        }
        return buffer.toString();
    }


    @Override
    public String getSqlChangeTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("EXEC sp_rename '").append(alterTableDto.getTableName()).append(".").append(alterTableDto.getColumnName());
        buffer.append("','").append(alterTableDto.getNewColumnName()).append("','column'");
        return buffer.toString();
    }

    @Override
    public String getSqlUpdateTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName()).append(" alter column ");
        buffer.append(" ").append(alterTableDto.getColumnName()).append(" ").append(alterTableDto.getColumnField());
        return buffer.toString();
    }

    @Override
    public String getSqlDelTableColumn(AlterTableDto alterTableDto) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("alter table ").append(alterTableDto.getTableName());
        buffer.append(" drop column ").append(alterTableDto.getColumnName());
        return buffer.toString();
    }

    @Override
    public String getSqlRenameTableName(String oldtableName, String tableName) {
        return String.format("EXEC sp_rename '%s','%s'",oldtableName,tableName);
    }

    @Override
    public String getSqlUpdateTableComment(String tableName, String tableComment) {
        return null;
    }

    @Override
    public String getSqlUpdateTableColumnComment(AlterTableDto alterTableDto) {
        return null;
    }

    @Override
    public String getSqlDistanceFinderDataBaseSizeBySchema(String schema){
        return String.format("select convert(float,size)*8192.0  from [%s].dbo.sysfiles",schema);
    }

    @Override
    public String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema,String tableName){
        return String.format("exec sp_spaceused '%s'",tableName);
    }


    @Override
    public String getSqlTableDesc(String tableName, String titleSchema) {
        return String.format("SELECT COLUMN_NAME = a.name, PRI = case when exists(SELECT 1 FROM sysobjects where xtype='PK' and parent_obj=a.id and name in( SELECT name FROM sysindexes WHERE indid in( SELECT indid FROM sysindexkeys WHERE id = a.id AND colid=a.colid))) then 'PRI' else '' end, DATA_TYPE = b.name, BYTE_LENGTH = a.length, DATA_LENGTH = COLUMNPROPERTY(a.id,a.name,'PRECISION'), NULLABLE = case when a.isnullable=1 then 'Y'else 'N' end, DEFAULTVALUE = isnull(e.text,''), COMMENTS = isnull(g.[value],'') FROM syscolumns a left join systypes b on a.xusertype=b.xusertype inner join sysobjects d on a.id=d.id and d.xtype='U' and d.name<>'dtproperties' left join syscomments e on a.cdefault=e.id left join sys.extended_properties g on a.id=G.major_id and a.colid=g.minor_id left join sys.extended_properties f on d.id=f.major_id and f.minor_id=0 where d.name='%s' order by a.id,a.colorder",tableName);
    }
}

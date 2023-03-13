package com.yanhuanxy.multifunexport.tools.origin.dm;


import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.BaseDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;

import java.util.List;

public class DmDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private DmDatabaseMeta(){
        super();
    }

    /**
     * 内部类创建
     */
    private static class DmDatabaseMetaHolder{
        private final static DmDatabaseMeta INSTANCE = new DmDatabaseMeta();
    }

    /**
     * 获取 DmDatabaseMeta
     */
    public static DmDatabaseMeta getInstance() {
        return DmDatabaseMetaHolder.INSTANCE;
    }

    @Override
    public String getSqlQueryFields(String tableName) {
        return super.getSqlQueryFields(tableName);
    }

    @Override
    public String getSqlQueryIsExist(String tableName) {
        return null;
    }

    @Override
    public String getSqlAddTableColumn(AlterTableDto alterTableDto) {
        return null;
    }

    @Override
    public String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist) {
        return null;
    }

    @Override
    public String getSqlUpdateTableColumn(AlterTableDto alterTableDto) {
        return null;
    }

    @Override
    public String getSqlChangeTableColumn(AlterTableDto alterTableDto) {
        return null;
    }

    @Override
    public String getSqlDelTableColumn(AlterTableDto alterTableDto) {
        return null;
    }

    @Override
    public String getSqlRenameTableName(String oldtableName, String tableName) {
        return null;
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
    public String getSqlQueryTablesNameComments() {
        return "select table_name as \"table_name\",comments as \"comments\" from user_tab_comments";
    }


    @Override
    public String getSqlQueryTables() {
        return "select table_name as \"table_name\",comments as \"comments\" from user_tab_comments";
    }
}

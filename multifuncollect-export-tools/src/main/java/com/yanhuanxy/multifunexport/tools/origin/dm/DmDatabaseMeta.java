package com.yanhuanxy.multifunexport.tools.origin.dm;


import com.yanhuanxy.multifunexport.tools.origin.base.meta.BaseDatabaseMeta;
import com.yanhuanxy.multifunexport.tools.origin.base.meta.DatabaseInterface;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;

import java.util.List;

public class DmDatabaseMeta extends BaseDatabaseMeta implements DatabaseInterface {

    private static DmDatabaseMeta single;

    private DmDatabaseMeta(){
        super();
    }

    public synchronized static DmDatabaseMeta getInstance() {
        if (single == null) {
            single = new DmDatabaseMeta();
        }
        return single;
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

package com.yanhuanxy.multifunexport.tools.origin.base.meta;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;

import java.util.List;

/**
 * meta信息interface
 *
 * @author yym
 * @date 2020/08/27
 */
public interface DatabaseInterface {

    /**
     * 根据表名查询表字段
     * @param tableName 表名
     * @return String
     */
    String getSqlQueryFields(String tableName);

    /**
     * 获取主键字段
     *
     * @return String
     */
    String getSqlQueryPrimaryKey();

    /**
     * 获取表别名
     * @return String
     */
    String getSqlQueryTableNameComment();

    /**
     * 获取所有表的 表别名
     * @return String
     */
    String getSqlQueryTablesNameComments();

    /**
     * 获取所有表名的sql
     *
     * @return String
     */
    String getSqlQueryTables(String... tableSchema);

    /**
     * 获取所有表名的sql
     *
     * @return String
     */
    String getSqlQueryTables();

    /**
     * 获取 Table schema
     *
     * @return String
     */
    String getSqlQueryTableSchema(String... args);
    /**
     * 获取所有的字段的Sql
     *
     * @return String
     */
    String getSqlQueryColumns(String... args);

    /**
     * 获取表和字段注释的sql语句
     *
     * @return The SQL to launch.
     */
    String getSqlQueryComment(String schemaName, String tableName, String columnName);


    /**
     * 获取当前表maxId
     * @param tableName String
     * @param primaryKey 主键
     * @return String
     */
    String getMaxId(String tableName, String primaryKey);

    /**
     * 获取当前表的 分页查询sql
     * @param sql 语句
     * @return String
     */
    String getSqlQueryPages(String sql, Integer start, Integer end);

    /**
     * 验证表是否存在
     * @param tableName 表名
     * @return String
     */
    String getSqlQueryIsExist(String tableName);

    /**
     * 添加表字段
     * @return String
     */
    String getSqlAddTableColumn(AlterTableDto alterTableDto);

    /**
     * 添加多个表字段
     * @return String
     */
    String getSqlAddTableColumns(List<AlterTableDto> alterTableDtolist);

    /**
     * 修改表字段名 mysql 只能修改属性 oracle 修改字段名称
     * @param alterTableDto 集合
     * @return String
     */
    String getSqlUpdateTableColumn(AlterTableDto alterTableDto);

    /**
     * 修改已有字段名及属性
     * @param alterTableDto 集合
     * @return String
     */
    String getSqlChangeTableColumn(AlterTableDto alterTableDto);

    /**
     * 删除表字段
     * @param alterTableDto 集合
     * @return String
     */
    String getSqlDelTableColumn(AlterTableDto alterTableDto);

    /**
     * 重命名表名
     * @param oldtableName 当前表名
     * @param tableName 修改后表名
     * @return String
     */
    String getSqlRenameTableName(String oldtableName, String tableName);

    /**
     * 删除表数据
     * @param tableName  表名
     * @return String
     */
    String getSqlDelTableData(String tableName);

    /**
     * 删除表数据
     * @param tableName 表名
     * @return String
     */
    String getSqlTruncateTableData(String tableName);

    /**
     * 给表添加注释
     * @return String
     */
    String getSqlUpdateTableComment(String tableName, String tableComment);

    /**
     * 给表字段添加注释
     * @return String
     */
    String getSqlUpdateTableColumnComment(AlterTableDto alterTableDto);

    /**
     * 查看数据库占用空间大小 sql
     * @param schema 数据库
     * @return String
     */
    String getSqlDistanceFinderDataBaseSizeBySchema(String schema);

    /**
     * 查看数据库中某个表占用空间大小 sql
     * @param schema 数据库
     * @return String
     */
    String getSqlDistanceFinderTableSizeBySchemaAndTableName(String schema, String tableName);

    /**
     * 查询数据库中某个表的desc信息
     * @param tableName 表名
     * @return String
     */
    String getSqlTableDesc(String tableName, String titleSchema);

    /**
     * 查询数据库中某个表的desc信息
     * @param tableName 表名
     * @return String
     */
    String getTableTotal(String tableName);

}

package com.yanhuanxy.multifunexport.tools.origin.base.query;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.TableAliasDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.TableInfoDto;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 基础查询接口
 *
 * @author yym
 * @since 2020/8/27
 */
public interface QueryToolInterface {
    /**
     * 构建 tableInfo对象
     *
     * @param tableName 表名
     * @return TableInfoDto 表信息
     */
    TableInfoDto buildTableInfo(String tableName);

    /**
     * 获取指定表信息
     * @param tableName 表名
     * @return list
     */
    List<Map<String, Object>> getTableInfo(String tableName);

    /**
     * 获取当前schema下的所有表
     *
     * @return list
     */
    List<Map<String, Object>> getTables();

    /**
     * 根据表名获取所有字段
     *
     * @param tableName 表名
     * @return list
     */
    List<ColumnInfoDto> getColumns(String tableName);


    /**
     * 根据表名和获取所有字段名称（不包括表名）
     *
     * @param tableName 表名
     * @return list
     */
    List<String> getColumnNames(String tableName, String datasource);


    /**
     * 获取所有可用表名
     *  @param schema 数据库
     * @return list
     */
    List<String> getTableNames(String schema);

    /**
     * 获取所有可用表名
     *
     * @return list
     */
    List<String> getTableNames();

    /**
     * 通过查询sql获取columns
     * @param querySql 语句
     * @return list
     */
    List<String> getColumnsByQuerySql(String querySql) throws SQLException;

    /**
     * 获取当前表maxId
     * @param tableName 表名
     * @param primaryKey 主键
     * @return long
     */
    long getMaxIdVal(String tableName, String primaryKey);

    /**
     * 获取所有 Schema
     * @return list
     */
    List<String> getTableSchema();


    /**
     * 带表头
     * 根据sql 语句查询
     * @param sql 语句
     * @return list
     */
    List<Map<String, Object>> executeQueryListMap(String sql);

    /**
     * 带表头
     * 根据sql 语句查询
     * @param sql 语句
     * @param parameters 参数
     * @param tableAliasDtos 表以及别名字段信息
     * @return list
     */
    List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters, List<TableAliasDto> tableAliasDtos);

    /**
     * 带表头
     * 根据sql 语句分页查询
     * @param sql 语句
     * @param start 开始行数
     * @param end 结束行数
     * @return list
     */
    List<Map<String, Object>> executeQueryListMap(String sql, Integer start, Integer end);

    /**
     * 带表头
     * 根据sql 语句分页查询 带参数 列入 select *from table1 where a = ? and b = ?
     * @param sql  语句
     * @param parameters 参数
     * @param start 开始行数
     * @param end 结束行数
     * @return list
     */
    List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters,Integer start,Integer end);

    /**
     * 带表头
     * 根据sql 语句分页查询 带参数 列入 select *from table1 where a = ? and b = ?
     * @param sql  语句
     * @param parameters 参数
     * @param start 开始行数
     * @param end 结束行数
     * @param tableAliasDtos 表以及别名字段信息
     * @return list
     */
    List<Map<String, Object>> executeQueryListMap(String sql, List<Object> parameters, List<TableAliasDto> tableAliasDtos, Integer start, Integer end);

    /**
     * 根据sql 语句查询数据
     * @param sql 语句
     *
     * @return List
     */
    List<List<Object>> executeQueryList(String sql);

    /**
     * 根据sql 语句查询数据 带参数 例如 select * from table1 where a = ? and b = ?
     * 时间类型字段的值将会被转成 时间字符串
     * @param sql 语句
     * @param parameters 参数
     * @return List
     */
    List<List<Object>> executeQueryList(String sql, List<Object> parameters);

    /**
     * 根据sql语句 分页查询数据 带参数 例如 select * from table1 where a = ? and b = ?
     * 时间类型字段的值将会被转成 时间字符串
     * @param sql 语句
     * @param parameters 参数
     * @param start 开始行数
     * @param end 结束行数
     * @return List
     */
    List<List<Object>> executeQueryList(String sql, List<Object> parameters,Integer start,Integer end);

    /**
     * 检查表是否存在
     * @param tableName 表名
     * @return boolean
     */
    Boolean checkTableIsExist(String tableName);

    /**
     * 获取表名 和注释
     * @return list
     */
    List<Map<String, Object>> getTableNamesComment();

    /**
     * 获取表数据量
     * @return long
     */
    Long getTableTotal(String tableName);

    /**
     * 根据sql 语句查询数据条数 带参数 例如 select count(*) from table1 where a = ? and b = ?
     * @param sql 语句
     * @param parameters 参数
     * @return long
     */
    Long getTableTotal(String sql,List<Object> parameters);
}

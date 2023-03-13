package com.yanhuanxy.multifunexport.tools.origin.base.executor;

import com.yanhuanxy.multifunexport.tools.domain.origin.dto.AlterTableDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.ColumnInfoDto;
import com.yanhuanxy.multifunexport.tools.domain.origin.dto.QueryParamDto;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ddl sql执行器 抽象类
 * @author yym
 * @date 2020/08/27
 */
public interface SqlExecutorInterface {

   /**
    * 获取最大时间
    * @param tableName 表名
    * @param filedName 字段
    * @return date
    */
   Date getMaxDateVal(String tableName, String filedName);

   /**
    * 创建表
    * @param columnMap 字段集合
    * @param tableName 表名
    * @return boolean
    */
   boolean createTable(Map<String, String> columnMap, String tableName);

   /**
    * 删除表
    * @param tableName 表名
    * @return boolean
    */
   boolean dropTable(String tableName);

   /**
    * 修改表名
    * @param oldtableName 当前表名
    * @param tableName 改后表名
    * @return boolean
    */
   boolean renameTable(String oldtableName, String tableName);

   /**
    * 根据sql 语句创建表
    * @param ddl 语句
    * @return boolean
    */
   boolean createTable(String ddl);

   /**
    * 根据sql 删除表
    * @param ddl 语句
    * @return boolean
    */
   boolean dropTableBySql(String ddl);

   /**
    * 根据sql 修改表名
    * @param ddl 语句
    * @return boolean
    */
   boolean renameTable(String ddl);

   /**
    * 添加表字段
    * @return boolean
    */
   boolean addTableColumn(AlterTableDto alterTableDto);

   /**
    * 添加多个表字段
    * @return boolean
    */
   boolean addTableColumns(List<AlterTableDto> alterTableDtolist);

   /**
    * 修改表字段名 mysql 只能修改属性 oracle 修改字段名称
    * @param alterTableDto 属性集合
    * @return boolean
    */
   boolean updateTableColumn(AlterTableDto alterTableDto);

   /**
    * 修改已有字段名及属性
    * @param alterTableDto 属性集合
    * @return boolean
    */
   boolean changeTableColumn(AlterTableDto alterTableDto);

   /**
    * 删除表字段
    * @param alterTableDto 属性集合
    * @return boolean
    */
   boolean delTableColumn(AlterTableDto alterTableDto);

   /**
    * 清空表数据
    * @param tableName 表名
    * @return boolean
    */
   boolean delTableData(String tableName);

   /**
    * 清空表数据
    * @param tableName 表名
    * @return boolean
    */
   boolean truncateTableData(String tableName);

   /**
    * 给表添加注释
    * @return boolean
    */
   boolean updateTableComment(String tableName, String tableComment);

   /**
    * 给表字段添加注释
    * @return boolean
    */
   boolean updateTableColumnComment(AlterTableDto alterTableDto);

   /**
    * 查看数据库占用空间大小 (单位GB)
    * @param schema 数据库
    * @return Double
    */
   Double distanceFinderDataBaseSizeBySchema(String schema);

   /**
    * 查看数据库中某个表占用空间大小 （单位 MB）
    * @param schema 数据库
    * @param tableName 表名
    * @return Double
    */
   Double distanceFinderTableSizeBySchemaAndTableName(String schema, String tableName);

   /**
    * 查询数据库中某个表的desc信息
    * @param tableName 表名
    * @return list
    */
   List<ColumnInfoDto> getSqlTableDesc(String tableName);

   /**
    * 将数据插入表中
    * @param tableName 表名
    * @param data 字段集合
    * @date 2021/09/24 15:07
    * @author yym
    * @return boolean
    */
   boolean insertToTable(String tableName,Map<String, Object> data);

   /**
    * 根据条件 修改表数据
    * @param tableName 表名
    * @param setParameters 修改字段集合
    * @param whereParameters 筛选条件集合
    * @date 2021/09/24 15:07
    * @author yym
    * @return boolean
    */
   boolean updateToTable(String tableName,Map<String,Object> setParameters, List<QueryParamDto> whereParameters);

   /**
    * 根据条件 删除表数据
    * @param tableName 表名
    * @param whereParameters 筛选条件集合
    * @date 2021/09/24 15:07
    * @author yym
    * @return boolean
    */
   boolean deleteToTable(String tableName, List<QueryParamDto> whereParameters);
}

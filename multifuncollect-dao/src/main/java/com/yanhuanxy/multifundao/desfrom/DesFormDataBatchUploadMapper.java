package com.yanhuanxy.multifundao.desfrom;

import com.yanhuanxy.multifundomain.desfrom.dto.DesDataUploadBatchDTO;
import com.yanhuanxy.multifundomain.desfrom.dto.DesTableColumnDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author
 */
@Mapper
public interface DesFormDataBatchUploadMapper {
    /**
     * 批量插入数据
     * @param dataList 字段列默认大写 小写->标识符无效
     * @param tableName
     */
    void insertDataBatch(List<DesDataUploadBatchDTO> dataList, String tableName);

    /**
     * 创建临时表
     * @param newTableName
     * @param oldTableName
     */
    void createTempTable(String newTableName,String oldTableName);

    /**
     * 查询表是否存在
     * @param tableName
     * @return
     */
    Integer findTable(String tableName);

    /**
     * 增量上传
     * @param newTableName
     * @param oldTableName
     * @param primaryKey
     */
    void increaseAmountUpload(String newTableName,String oldTableName,String primaryKey);
    /**
     * 删除临时表中主键不在目标表中的数据
     * @param newTableName
     * @param oldTableName
     * @param primaryKey
     */
    void deleteNewTableNotInOldTable(String newTableName,String oldTableName,String primaryKey);
    /**
     * 删除目标表中主键在临时表中的数据
     * @param newTableName
     * @param oldTableName
     * @param primaryKey
     */
    void deleteOldTableInNewTable(String newTableName,String oldTableName,String primaryKey);
    /**
     * 获得主键
     * @param tableName
     * @return
     */
    String getPrimaryKey(String tableName);

    /**
     * 获取用户主键
     * @param tableName
     * @param user
     * @return
     */
    String getPrimaryUserKey(String tableName,String user);

    /**
     * 获得表字段
     * @param tableName
     * @return
     */
    List<DesTableColumnDTO> getColumns(String tableName);

    /**
     * 获取所有表字段
     * @param tableName
     * @param owner
     * @return
     */
    List<DesTableColumnDTO> getAllColumns(String tableName,String owner);
    /**
     * 将临时表的数据插入到目标表中
     * @param newTableName
     * @param oldTableName
     */
    void updateTable(String newTableName,String oldTableName);
    /**
     * 删除表
     * @param tableName
     */
    void dropTable(String tableName);

    /**
     * 删除表数据
     * @param tableName
     */
    void deleteAll(String tableName);

    /**
     * 根据表名 + 时间字段 查询数据量
     * @param tableName 表名
     * @param uploadDate  时间字段
     */
    Long queryListDataSql(String tableName, String uploadDate);

    /**
     * @Author yym
     * @Date 2022/2/19  18:10
     * @Description  根据字段删除数据
     * @Param [tableName]
     * @Return {@link List < Map>}
     */
    void delListDataSql(@Param("tableName") String tableName, @Param("uploadDateFile") String uploadDateFile, @Param("uploadDate") String uploadDate);
}

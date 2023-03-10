package com.yanhuanxy.multifunservice.putstorage.impl;


import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifunservice.putstorage.PutStorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 增量更新
 * @date 2021/10/18
 */
@Service("increaseAmountUpdate")
public class IncreaseAmountUpdatePutStorageStrategy implements PutStorageStrategy {
    private Logger logger = LoggerFactory.getLogger(IncreaseAmountUpdatePutStorageStrategy.class);
    @Override
    public void putStorageStrategy(String newTableName, String oldTableName, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        //判断是否存在用户信息
        String primaryKey = null;
        String[] split = oldTableName.split("\\.");
        if (split.length>1){
            primaryKey =  desFormDataBatchUploadMapper.getPrimaryUserKey(split[1],split[0]);
        }else {
            primaryKey = desFormDataBatchUploadMapper.getPrimaryKey(oldTableName);
        }
        //删除临时表中主键不在目标表中的数据
        desFormDataBatchUploadMapper.deleteNewTableNotInOldTable(newTableName,oldTableName,primaryKey);
        //删除目标表中主键在临时表中的数据
        desFormDataBatchUploadMapper.deleteOldTableInNewTable(newTableName,oldTableName,primaryKey);
        //将临时表的数据 插入到目标表中
        desFormDataBatchUploadMapper.updateTable(newTableName, oldTableName);
    }
}

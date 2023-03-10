package com.yanhuanxy.multifunservice.putstorage.impl;


import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifunservice.putstorage.PutStorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 增量上传
 * @date 2021/10/18
 */
@Service("increaseAmountUpload")
public class IncreaseAmountUploadPutStorageStrategy implements PutStorageStrategy {
    private Logger logger = LoggerFactory.getLogger(IncreaseAmountUploadPutStorageStrategy.class);

    @Override
    public void putStorageStrategy(String newTableName, String oldTableName, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        //获得主键
        String primaryKey = null;
        String[] split = oldTableName.split("\\.");
        if (split.length>1){
            primaryKey =  desFormDataBatchUploadMapper.getPrimaryUserKey(split[1],split[0]);
        }else {
            primaryKey = desFormDataBatchUploadMapper.getPrimaryKey(oldTableName);
        }
        //执行主体逻辑
        desFormDataBatchUploadMapper.increaseAmountUpload(newTableName,oldTableName,primaryKey);
    }
}

package com.yanhuanxy.multifunservice.putstorage.impl;


import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifunservice.putstorage.PutStorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 全量上传
 * @date 2021/10/18
 */
@Service("fullAmountUpload")
public class FullAmountUploadPutStorageStrategy implements PutStorageStrategy {
    private Logger logger = LoggerFactory.getLogger(FullAmountUploadPutStorageStrategy.class);
    @Override
    public void putStorageStrategy(String newTableName, String oldTableName, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        //将临时表的数据 插入到目标表中
        desFormDataBatchUploadMapper.updateTable(newTableName, oldTableName);
    }
}

package com.yanhuanxy.multifunservice.putstorage.impl;


import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifunservice.putstorage.PutStorageStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 全量覆盖
 * @date 2021/10/18
 */
@Service("fullAmountCover")
public class FullAmountCoverPutStorageStrategy implements PutStorageStrategy {
    private Logger logger = LoggerFactory.getLogger(FullAmountCoverPutStorageStrategy.class);

    @Override
    public void putStorageStrategy(String tempTableName, String oldTableName, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        //删除目标表数据
        desFormDataBatchUploadMapper.deleteAll(oldTableName);
        //将临时表的数据全量插入目标表
        desFormDataBatchUploadMapper.updateTable(tempTableName,oldTableName);

    }
}

package com.yanhuanxy.multifunservice.putstorage;

import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;

/**
 * 数据入库策略
 * @author
 */
public interface PutStorageStrategy {
    /**
     * 数据入库策略
     * @param newTableName
     * @param oldTableName
     * @param desFormDataBatchUploadMapper
     */
    void putStorageStrategy(String newTableName, String oldTableName, DesFormDataBatchUploadMapper desFormDataBatchUploadMapper);
}

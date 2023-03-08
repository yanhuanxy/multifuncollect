package com.yanhuanxy.multifunexport.fileservice.operation.delete;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.DeleteFile;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Deleter {
    protected static final Logger logger = LoggerFactory.getLogger(Deleter.class);

    public abstract void delete(DeleteFile deleteFile);

    protected void deleteCacheFile(DeleteFile deleteFile) {
        if (OssUtils.isImageFile(FilenameUtils.getExtension(deleteFile.getFileUrl()))) {
            File cacheFile = OssUtils.getCacheFile(deleteFile.getFileUrl());
            if (cacheFile.exists()) {
                boolean result = cacheFile.delete();
                if (!result) {
                    logger.error("删除本地缓存文件失败！");
                }
            }
        }
    }
}

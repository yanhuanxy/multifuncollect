package com.yanhuanxy.multifunexport.fileservice.operation.delete.product;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.DeleteFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.DeleteException;
import com.yanhuanxy.multifunexport.fileservice.operation.delete.Deleter;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class LocalStorageDeleter extends Deleter {
    @Override
    public void delete(DeleteFile deleteFile) {
        File localSaveFile = OssUtils.getLocalSaveFile(deleteFile.getFileUrl());
        if (localSaveFile.exists()) {
            boolean result = localSaveFile.delete();
            if (!result) {
                throw new DeleteException("删除本地文件失败");
            }
        }

        deleteCacheFile(deleteFile);
    }
}

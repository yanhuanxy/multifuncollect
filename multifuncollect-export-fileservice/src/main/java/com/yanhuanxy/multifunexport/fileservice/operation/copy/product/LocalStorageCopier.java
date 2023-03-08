package com.yanhuanxy.multifunexport.fileservice.operation.copy.product;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.CopyFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.CopyException;
import com.yanhuanxy.multifunexport.fileservice.operation.copy.Copier;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class LocalStorageCopier extends Copier {

    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = OssUtils.getUploadFileUrl(copyFile.getUserCode(), uuid, copyFile.getExtendName());
        File saveFile = new File(OssUtils.getStaticPath() + fileUrl);
        try {
            FileUtils.copyInputStreamToFile(inputStream, saveFile);
        } catch (IOException e) {
            throw new CopyException("创建文件出现异常", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return fileUrl;
    }
}

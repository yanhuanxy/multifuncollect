package com.yanhuanxy.multifunexport.fileservice.operation.read.product;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.ReadFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.ReadException;
import com.yanhuanxy.multifunexport.fileservice.operation.read.Reader;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import com.yanhuanxy.multifunexport.fileservice.util.ReadFileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.FileInputStream;
import java.io.IOException;

public class LocalStorageReader extends Reader {
    @Override
    public String read(ReadFile readFile) {

        String fileContent;
        try {
            String extendName = FilenameUtils.getExtension(readFile.getFileUrl());
            FileInputStream fileInputStream = new FileInputStream(OssUtils.getStaticPath() + readFile.getFileUrl());
            fileContent = ReadFileUtils.getContentByInputStream(extendName, fileInputStream);
        } catch (IOException e) {
            throw new ReadException("文件读取出现异常", e);
        }
        return fileContent;
    }
}

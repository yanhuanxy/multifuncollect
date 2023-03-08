package com.yanhuanxy.multifunexport.fileservice.operation.copy;

import com.yanhuanxy.multifunexport.fileservice.dto.operation.CopyFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * @author yym
 * @version 1.0
 */
public abstract class Copier {
    protected static final Logger logger = LoggerFactory.getLogger(Copier.class);

    /**
     * 将服务器文件流拷贝到云端，并返回文件url
     * @param inputStream 文件流
     * @param copyFile 拷贝文件相关参数
     * @return 文件url
     */
    public abstract String copy(InputStream inputStream, CopyFile copyFile);
}

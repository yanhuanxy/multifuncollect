package com.yanhuanxy.multifunexport.fileservice.operation.read.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.ReadFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.ReadException;
import com.yanhuanxy.multifunexport.fileservice.operation.read.Reader;
import com.yanhuanxy.multifunexport.fileservice.util.ReadFileUtils;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioReader extends Reader implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioReader() {
        super();
    }

    public MinioReader(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public String read(ReadFile readFile) {
        String fileUrl = readFile.getFileUrl();
        String fileType = FilenameUtils.getExtension(fileUrl);
        try {
            return ReadFileUtils.getContentByInputStream(fileType, getInputStream(readFile.getFileUrl()));
        } catch (IOException e) {
            throw new ReadException("读取文件失败", e);
        }
    }

    protected InputStream getInputStream(String fileUrl) {
        InputStream inputStream = null;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).build());

        } catch (MinioException e) {
            logger.error("Minio Error occurred: " , e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage());
        }
        return inputStream;
    }
}

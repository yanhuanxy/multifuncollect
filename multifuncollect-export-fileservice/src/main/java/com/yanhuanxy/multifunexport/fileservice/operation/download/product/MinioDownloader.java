package com.yanhuanxy.multifunexport.fileservice.operation.download.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.DownloadFile;
import com.yanhuanxy.multifunexport.fileservice.operation.download.Downloader;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioDownloader extends Downloader implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioDownloader(){
        super();
    }

    public MinioDownloader(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public InputStream getInputStream(DownloadFile downloadFile) {
        InputStream inputStream = null;
        try {
            if (downloadFile.getRange() != null) {
                inputStream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(downloadFile.getFileUrl())
                        .offset((long) downloadFile.getRange().getStart())
                        .length((long) downloadFile.getRange().getLength())
                        .build());
            } else {
                inputStream = minioClient.getObject(GetObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(downloadFile.getFileUrl())
                        .build());
            }
        } catch (MinioException e) {
            logger.error("Minio Error occurred: ",e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            logger.error(e.getMessage());
        }
        return inputStream;
    }

}

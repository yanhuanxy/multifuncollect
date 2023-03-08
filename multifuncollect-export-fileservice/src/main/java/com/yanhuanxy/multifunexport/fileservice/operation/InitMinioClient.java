package com.yanhuanxy.multifunexport.fileservice.operation;

import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author yym
 * @version 1.0
 */
public class InitMinioClient {
    private static final Logger logger = LoggerFactory.getLogger(InitMinioClient.class);

    protected MinioConfig minioConfig;

    public InitMinioClient(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    public MinioClient getMinioClient() {
        try {
            logger.info("==================初始化Minio客户端=======================");
            Assert.hasText(minioConfig.getEndpoint(), "Minio url 为空");
            Assert.hasText(minioConfig.getAccessKey(), "Minio accessKey为空");
            Assert.hasText(minioConfig.getSecretKey(), "Minio secretKey为空");

            return MinioClient.builder().endpoint(minioConfig.getEndpoint())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey()).build();
        }catch (Exception e){
            logger.warn("======Minio 配置不存在 客户端初始失败！======");
            return null;
        }
    }
}

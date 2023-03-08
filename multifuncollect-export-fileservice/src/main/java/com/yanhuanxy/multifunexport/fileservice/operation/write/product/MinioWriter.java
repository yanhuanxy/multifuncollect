package com.yanhuanxy.multifunexport.fileservice.operation.write.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.WriteFile;
import com.yanhuanxy.multifunexport.fileservice.operation.write.Writer;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioWriter extends Writer implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioWriter() {
        super();
    }

    public MinioWriter(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public void write(InputStream inputStream, WriteFile writeFile) {

        try {
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if(!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(OssUtils.getObjectNameByFileUrl(writeFile.getFileUrl())).stream(
                                    inputStream, inputStream.available(), -1)
//                            .contentType("video/mp4")
                            .build());
        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            logger.error("Minio 文件写入", e);
        }
    }
}

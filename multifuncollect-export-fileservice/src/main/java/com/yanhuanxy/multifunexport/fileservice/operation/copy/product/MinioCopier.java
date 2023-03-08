package com.yanhuanxy.multifunexport.fileservice.operation.copy.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.CopyFile;
import com.yanhuanxy.multifunexport.fileservice.operation.copy.Copier;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MinioCopier extends Copier implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioCopier() {
        super();
    }

    public MinioCopier(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public String copy(InputStream inputStream, CopyFile copyFile) {
        String uuid = UUID.randomUUID().toString();
        String fileUrl = OssUtils.getUploadFileUrl(copyFile.getUserCode(), uuid, copyFile.getExtendName());

        try {
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if(!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            }

            minioClient.putObject(PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(fileUrl).stream(
                                    inputStream, inputStream.available(), 1024 * 1024 * 5)
//                            .contentType("video/mp4")
                            .build());

        } catch (MinioException | InvalidKeyException | NoSuchAlgorithmException | IOException e) {
            logger.error("Minio 拷贝异常!", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }

        return fileUrl;
    }


}

package com.yanhuanxy.multifunexport.fileservice.operation.delete.product;

import com.yanhuanxy.multifunexport.fileservice.autoconfig.OssProperties;
import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.operation.DeleteFile;
import com.yanhuanxy.multifunexport.fileservice.exception.operation.DeleteException;
import com.yanhuanxy.multifunexport.fileservice.operation.delete.Deleter;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class MinioDeleter extends Deleter implements InitializingBean {

    private MinioConfig minioConfig;

    @Resource
    private MinioClient minioClient;

    @Resource
    private OssProperties ossProperties;

    public MinioDeleter() {
        super();
    }

    public MinioDeleter(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(ossProperties,"配置文件不存在");
        this.minioConfig = ossProperties.getMinio();
    }

    @Override
    public void delete(DeleteFile deleteFile) {

        try {
            // 从mybucket中删除myobject。
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(minioConfig.getBucketName()).object(deleteFile.getFileUrl()).build());
            logger.info("successfully removed mybucket:{} /myobject:{}", minioConfig.getBucketName(), deleteFile.getFileUrl());
        } catch (MinioException e) {
            logger.error("Error: " + e);
            throw new DeleteException("Minio删除文件失败", e);
        } catch (IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new DeleteException("Minio删除文件失败", e);
        }
        deleteCacheFile(deleteFile);
    }
}

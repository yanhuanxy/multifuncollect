package com.yanhuanxy.multifunexport.fileservice.autoconfig;

import com.yanhuanxy.multifunexport.fileservice.config.MinioConfig;
import com.yanhuanxy.multifunexport.fileservice.dto.ThumbImage;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "osskelven")
public class OssProperties {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 策略  StorageTypeEnum
     */
    private String storageType;

    /**
     * 本地存储路径
     */
    private String localStoragePath;

    /**
     * 缩略图 配置对象
     */
    private ThumbImage thumbImage = new ThumbImage();

    /**
     * minio 配置对象
     */
    private MinioConfig minio = new MinioConfig();


    public OssProperties() {
        super();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getLocalStoragePath() {
        return localStoragePath;
    }

    public void setLocalStoragePath(String localStoragePath) {
        this.localStoragePath = localStoragePath;
    }

    public ThumbImage getThumbImage() {
        return thumbImage;
    }

    public void setThumbImage(ThumbImage thumbImage) {
        this.thumbImage = thumbImage;
    }

    public MinioConfig getMinio() {
        return minio;
    }

    public void setMinio(MinioConfig minio) {
        this.minio = minio;
    }
}

package com.yanhuanxy.multifunexport.fileservice.dto.operation;

/**
 * @author yym
 * @version 1.0
 */
public class UploadFileInfo {

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 健
     */
    private String key;

    /**
     * 文件唯一标识
     */
    private String uploadId;


    public UploadFileInfo() {
        super();
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
}
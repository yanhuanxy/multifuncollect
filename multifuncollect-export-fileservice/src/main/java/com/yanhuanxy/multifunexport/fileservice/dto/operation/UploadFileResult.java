package com.yanhuanxy.multifunexport.fileservice.dto.operation;

import com.yanhuanxy.multifunexport.fileservice.dto.enums.StorageTypeEnums;
import com.yanhuanxy.multifunexport.fileservice.dto.enums.UploadFileStatusEnums;

import java.awt.image.BufferedImage;


/**
 * 文件返回信息
 * @author yym
 * @version 1.0
 */
public class UploadFileResult {
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件后缀
     */
    private String extendName;

    /**
     * 文件大小
     */
    private long fileSize;

    /**
     * 文件地址
     */
    private String fileUrl;

    /**
     * 标识符
     */
    private String identifier;

    /**
     * 上传策略
     */
    private StorageTypeEnums storageType;

    /**
     *文件上传状态
     */
    private UploadFileStatusEnums status;

    /**
     * 文件缩略图
     */
    private BufferedImage bufferedImage;

    public UploadFileResult() {
        super();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public StorageTypeEnums getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageTypeEnums storageType) {
        this.storageType = storageType;
    }

    public UploadFileStatusEnums getStatus() {
        return status;
    }

    public void setStatus(UploadFileStatusEnums status) {
        this.status = status;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}

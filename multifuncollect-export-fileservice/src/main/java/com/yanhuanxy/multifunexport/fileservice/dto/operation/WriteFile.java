package com.yanhuanxy.multifunexport.fileservice.dto.operation;

/**
 * @author yym
 * @version 1.0
 */
public class WriteFile {
    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 文件大小
     */
    private long fileSize;

    public WriteFile() {
        super();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}

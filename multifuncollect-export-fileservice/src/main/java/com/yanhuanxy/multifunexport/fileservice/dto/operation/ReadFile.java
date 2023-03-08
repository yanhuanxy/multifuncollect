package com.yanhuanxy.multifunexport.fileservice.dto.operation;

/**
 * @author yym
 * @version 1.0
 */
public class ReadFile {
    /**
     * 文件路径
     */
    private String fileUrl;

    public ReadFile() {
        super();
    }

    public ReadFile(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

package com.yanhuanxy.multifunexport.fileservice.dto.operation;

/**
 * @author yym
 * @version 1.0
 */
public class PreviewFile {
    /**
     * 文件路径
     */
    private String fileUrl;

    public PreviewFile() {
        super();
    }

    public PreviewFile(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}

package com.yanhuanxy.multifunexport.fileservice.dto.operation;

/**
 * @author yym
 * @version 1.0
 */
public class DownloadFile {
    /**
     * 文件路径
     */
    private String fileUrl;
    /**
     * 文件范围
     */
    private RangeFile range;

    public DownloadFile() {
        super();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public RangeFile getRange() {
        return range;
    }

    public void setRange(RangeFile range) {
        this.range = range;
    }
}

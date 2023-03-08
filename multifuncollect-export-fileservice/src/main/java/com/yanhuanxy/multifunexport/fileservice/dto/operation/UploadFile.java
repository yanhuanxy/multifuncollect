package com.yanhuanxy.multifunexport.fileservice.dto.operation;


/**
 * 切片上传相关参数
 * @author yym
 * @version 1.0
 */
public class UploadFile {

    /**
     * 分片编号
     */
    private int chunkNumber;

    /**
     * 分片大小
     */
    private long chunkSize;

    /**
     * 总分片数
     */
    private int totalChunks;

    /**
     * 识别符
     */
    private String identifier;

    /**
     * 文件大小
     */
    private long totalSize;

    /**
     * 当前分片大小
     */
    private long currentChunkSize;

    /**
     * 用户编码
     */
    private String userCode;

    public UploadFile() {
        super();
    }

    public int getChunkNumber() {
        return chunkNumber;
    }

    public void setChunkNumber(int chunkNumber) {
        this.chunkNumber = chunkNumber;
    }

    public long getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(long chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public void setTotalChunks(int totalChunks) {
        this.totalChunks = totalChunks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentChunkSize() {
        return currentChunkSize;
    }

    public void setCurrentChunkSize(long currentChunkSize) {
        this.currentChunkSize = currentChunkSize;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}

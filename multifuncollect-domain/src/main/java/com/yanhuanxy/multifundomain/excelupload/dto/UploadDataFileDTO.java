package com.yanhuanxy.multifundomain.excelupload.dto;

import java.io.File;

public class UploadDataFileDTO {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件对象
     */
    private File file;

    /**
     *  文件存放路径
     */
    private String filePath;

    /**
     * 父级目录路径
     */
    private String parentPath;

    public UploadDataFileDTO() {
        super();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}

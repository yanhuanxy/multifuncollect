package com.yanhuanxy.multifunexport.fileservice.dto.enums;

/**
 * @author yym
 * @version 1.0
 */
public enum FileOperationType {
    COPY("拷贝", "Copier"),DELETE("删除","Deleter"),DOWNLOAD("下载", "Downloader"),
    PREVIEW("预览", "Previewer"),READ("读取", "Reader"),UPLOAD("上传", "Uploader"),WRITE("写入", "Writer");

    private final String name;

    private final String suffix;

    FileOperationType(String name, String suffix) {
        this.name = name;
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }
}

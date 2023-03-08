package com.yanhuanxy.multifunexport.fileservice.dto.enums;

/**
 * @author yym
 * @version 1.0
 */
public enum UploadFileStatusEnums {

    FAIL(0, "上传失败"),
    SUCCESS(1, "上传成功"),
    UNCOMPLATE(2, "未完成");

    private final int code;

    private final String message;

    UploadFileStatusEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}

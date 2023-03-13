package com.yanhuanxy.multifunexport.demo.designpattern.emuns;

/**
 * 数据类型
 */
public enum StoragePackageEnum {

    SORT("Sort", "排序");

    /**
     * 编码
     */
    private final String code;

    /**
     * 信息
     */
    private final String message;

    StoragePackageEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

package com.yanhuanxy.multifunexport.fileservice.dto.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @author yym
 * @version 1.0
 */
public enum StorageTypeEnums {
    // 默认 本地储存
    LOCAL(0, "本地存储", "LocalStorage"),
    FAST_DFS(1, "fastDFS集群存储", "Hdfs"),
    MINIO(2, "minio存储", "Minio");

    private final int code;

    private final String name;

    private final String suffix;

    StorageTypeEnums(int code, String name, String suffix) {
        this.code = code;
        this.name = name;
        this.suffix = suffix;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSuffix() {
        return suffix;
    }

    /**
     * 按code获取message
     * @param code
     * @return
     */
    public static StorageTypeEnums getStorageTypeEnums(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        StorageTypeEnums[] values = StorageTypeEnums.values();
        for (StorageTypeEnums enums : values) {
            if (StringUtils.equals(code, enums.getCode()+ "")) {
                return enums;
            }
        }
        return null;
    }
}

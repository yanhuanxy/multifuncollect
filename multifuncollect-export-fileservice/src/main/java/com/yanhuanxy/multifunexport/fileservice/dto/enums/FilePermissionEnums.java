package com.yanhuanxy.multifunexport.fileservice.dto.enums;

/**
 * @author yym
 * @version 1.0
 */
public enum FilePermissionEnums {
    READ(1, "读取"),
    READ_WRITE(2, "读取/写入"),
    OWNER(3, "所有者");

    private final int type;

    private final String desc;

    FilePermissionEnums(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}

package com.yanhuanxy.multifuncommon.enums;

public enum ReadEventTypeEnum {
    CUR_MAX_ROW("当前配置读取量少于文件行数"), MAX_MAX_ROW("当前配置读取量大于最大一次性读取行数"), CHANGE_CONF("配置变更"),LAST_HANDLE("手动触发");

    private final String message;

    public String getMessage() {
        return message;
    }

    ReadEventTypeEnum(String message) {
        this.message = message;
    }

    public static void main(String[] args) {
        ReadEventTypeEnum.values();
    }
}

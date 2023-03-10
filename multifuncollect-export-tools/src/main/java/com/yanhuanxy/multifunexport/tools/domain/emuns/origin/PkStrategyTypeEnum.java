package com.yanhuanxy.multifunexport.tools.domain.emuns.origin;

/**
 * @author yym
 * @className PkStrategyTypeEnum
 * @description 主键策略枚举类
 * @date 2021/8/30  17:23
 */
public enum PkStrategyTypeEnum {
    /**
     * 主键策略
     */
    OTHER(0, "其他"),
    UUID(1, "通用唯一识别码"),
    MAXID(2, "最大ID值");

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    PkStrategyTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 通过编码转换枚举
     *
     * @param pkStrategy
     * @return
     */
    public static PkStrategyTypeEnum getPkStrategyType(Integer pkStrategy) {
        for (PkStrategyTypeEnum pkStrategyTypeEnum : values()) {
            if (pkStrategyTypeEnum.getCode().equals(pkStrategy)) {
                return pkStrategyTypeEnum;
            }
        }
        return OTHER;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

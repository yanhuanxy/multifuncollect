package com.yanhuanxy.multifunexport.tools.domain.emuns.origin;

/**
 * @author yym
 * @className JoinTypeEnum
 * @description 连接类型枚举类
 * @date 2021/8/30  17:23
 */
public enum JoinTypeEnum {
    /**
     * 连接类型
     */
    OTHER(0, "其他"),
    LEFTJOIN(1, "左连接"),
    RIGHTJOIN(2, "右连接"),
    INNERJOIN(3, "内连接"),
    FULLJOIN(4, "全连接"),
    CROSSJOIN(5, "交叉连接") ;

    /**
     * 编码
     */
    private final Integer code;
    /**
     * 名称
     */
    private final String name;

    JoinTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 通过编码转换枚举
     *
     * @param joinType
     * @return
     */
    public static JoinTypeEnum getJoinType(Integer joinType) {
        for (JoinTypeEnum joinTypeEnum : values()) {
            if (joinTypeEnum.getCode().equals(joinType)) {
                return joinTypeEnum;
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

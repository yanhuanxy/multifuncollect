package com.yanhuanxy.multifunexport.tools.domain.emuns.origin;

import java.util.Arrays;

/**
 * @author yym
 * @className BiOperateTypeEnum
 * @description BI报表 操作符类型枚举类
 * @date 2021/6/28  17:23
 */
public enum BiOperateTypeEnum {
    /**
     * 操作符类型
     */
    OTHER(0,"其他","?"),
    EQ(1,"等于", "="),
    NEQ(2,"不等于", "<>"),
    LT(3,"小于","<"),
    LTE(4,"小于或等于","<="),
    GT(5,"大于",">"),
    GTE(6,"大于或等于",">="),
    LIKE(7,"包含","like"),
    NOT_LIKE(8,"不包含","not like"),
    FIRST_LIKE(9,"开始以","likeX%"),
    END_LIKE(10,"结束以","like%X"),
    IS_NULL(11,"是null","is null"),
    NOT_NULL(12,"不是null","is not null"),
    EQ_NULL(13,"是空的","isEmpty"),
    NOT_EQNULL(14,"不是空的","notEmpty"),
    BETWEEN(15,"介于","between"),
    NOT_BETWEEN(16,"非介于","not between"),
    IN(17,"在列表中","in"),
    NOT_IN(18,"不在列表中","not in");

    /**
     * 操作符类型编码
     */
    private final Integer code;
    /**
     * 操作符类型名称
     */
    private final String name;

    /**
     * 操作符符号
     */
    private final String symbolCode;

    BiOperateTypeEnum(Integer code, String name, String symbolCode) {
        this.code = code;
        this.name = name;
        this.symbolCode = symbolCode;
    }

    /**
     * 通过编码转换枚举
     * @param operateType 操作符类型
     * @return BiOperateTypeEnum
     */
    public static BiOperateTypeEnum getOperateType(Integer operateType){
        for(BiOperateTypeEnum biOperateTypeEnum : values()){
            if(biOperateTypeEnum.getCode().equals(operateType)){
                return biOperateTypeEnum;
            }
        }
        return BiOperateTypeEnum.OTHER;
    }


    /**
     * 通过符号编码转换枚举
     * @param symbolCode 符号
     * @return BiOperateTypeEnum
     */
    public static BiOperateTypeEnum getOperateType(String symbolCode){
        for(BiOperateTypeEnum biOperateTypeEnum : values()){
            if(biOperateTypeEnum.getSymbolCode().equals(symbolCode)){
                return biOperateTypeEnum;
            }
        }
        return BiOperateTypeEnum.OTHER;
    }

    /**
     * 校验枚举是否包含在下列枚举数组中
     * @param operateTypeEnum 操作符类型
     * @return BiOperateTypeEnum
     */
    public static boolean checkBiOperateTypeEnumIsNeedQuo(BiOperateTypeEnum operateTypeEnum){
        BiOperateTypeEnum[] unwantedQuo = new BiOperateTypeEnum[]{LIKE,NOT_LIKE,FIRST_LIKE,END_LIKE,IN,NOT_IN};
        return Arrays.asList(unwantedQuo).contains(operateTypeEnum);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbolCode() {
        return symbolCode;
    }

}

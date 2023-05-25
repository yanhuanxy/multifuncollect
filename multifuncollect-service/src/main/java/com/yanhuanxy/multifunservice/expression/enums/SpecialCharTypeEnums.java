package com.yanhuanxy.multifunservice.expression.enums;

import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;

import java.util.Objects;

/**
 * @author yym
 * @className BiAnalysisDateTypeEnum
 * @description 计算符号枚举类
 * @date 2021/9/6  17:23
 */
public enum SpecialCharTypeEnums {
    /**
     * 计算符
     */
    OTHER(0,"其他","?"),
    PLUS(1,"加","+"),
    SUBTRACT(2,"减","-"),
    MULTIPLY(3, "乘","*"),
    DIVIDE(4, "除","/"),
    LPARENTHESES(5, "左括号","("),
    RPARENTHESES(6, "右括号",")"),
    MOD(7, "求模取余","%"),
    POWER(8, "次幂","^"),
    BITWISEAND(9, "按位与","&"),
    BITWISEOR(10, "按位或","|"),
    AND(11, "逻辑与","&&"),
    OR(12, "逻辑或","||"),
    NOT(13, "逻辑非","!");

    /**
     * 编码
     */
    private final Integer code;
    /**
     *  名称
     */
    private final String name;

    /**
     * 符号
     */
    private final String symbol;

    SpecialCharTypeEnums(Integer code, String name, String symbol) {
        this.code = code;
        this.name = name;
        this.symbol = symbol;
    }

    /**
     * 通过编码转换枚举
     *
     * @param expressionType 类型
     * @return DPUExpressionTypeEnum
     */
    public static SpecialCharTypeEnums getExpressionType(Integer expressionType) {
        for (SpecialCharTypeEnums expressionTypeEnum : values()) {
            if (expressionTypeEnum.getCode().equals(expressionType)) {
                return expressionTypeEnum;
            }
        }
        return OTHER;
    }

    /**
     * 通过符号转换枚举
     *
     * @param expressionTypeSymbol 类型符号
     * @return DPUExpressionTypeEnum
     */
    public static SpecialCharTypeEnums getExpressionType(String expressionTypeSymbol) {
        for (SpecialCharTypeEnums expressionTypeEnum : values()) {
            if (expressionTypeEnum.getSymbol().equals(expressionTypeSymbol)) {
                return expressionTypeEnum;
            }
        }
        return OTHER;
    }

    /**
     * 判断字符是否为 四则运算符
     * @param charVal 字符
     * @return boolean
     */
    public static boolean checkCharIsSymbol(String charVal){

        return Objects.equals(charVal, PLUS.getSymbol()) || Objects.equals(charVal, SUBTRACT.getSymbol())  || Objects.equals(charVal, MULTIPLY.getSymbol()) ||
                Objects.equals(charVal, DIVIDE.getSymbol());
    }

    /**
     * 判断字符是否为 四则运算符 + 括号
     * @param charVal 字符
     * @return boolean
     */
    public static boolean checkCharIsSymbolAndHeses(String charVal){

        return Objects.equals(charVal, PLUS.getSymbol()) || Objects.equals(charVal, SUBTRACT.getSymbol())  || Objects.equals(charVal, MULTIPLY.getSymbol()) ||
                Objects.equals(charVal, DIVIDE.getSymbol()) || Objects.equals(charVal, LPARENTHESES.getSymbol()) || Objects.equals(charVal, RPARENTHESES.getSymbol());
    }

    /**
     * 获取四则运算优先级
     * @param charVal 字符
     * @return int
     */
    public static int getPriority(String charVal) {
        if(charVal==null) return 0;
        switch(getExpressionType(charVal)) {
            case LPARENTHESES:return 1;
            case PLUS:
            case SUBTRACT:return 2;
            case MULTIPLY:
            case DIVIDE:return 3;
            default:break;
        }
        throw new BaseRuntimeException("无效的运算优先级！");
    }

    /**
     * 当前字符为 - 号前个字符为
     * @param charVal 字符
     * @return boolean
     */
    public static boolean checkSubtractIsNegative(String charVal){
        SpecialCharTypeEnums expressionType = getExpressionType(charVal);
        return OTHER.equals(expressionType) || RPARENTHESES.equals(expressionType);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}

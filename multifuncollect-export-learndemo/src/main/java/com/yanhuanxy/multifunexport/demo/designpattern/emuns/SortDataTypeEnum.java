package com.yanhuanxy.multifunexport.demo.designpattern.emuns;

import java.util.Arrays;
import java.util.Objects;

/**
 * 数据类型
 */
public enum SortDataTypeEnum {

    SHORT("Short", "java.lang.Short"),INTEGER("Integer", "java.lang.Integer"),
    LONG("Long", "java.lang.Long"), FLOAT("Float", "java.lang.Float"),
    DOUBLE("Double", "java.lang.Double"),BIGDECIMAL("Bigdecimal", "java.math.BigDecimal"),
    COMPARABLE("Comparable", "com.yanhuanxy.multifunexport.demo.designpattern.strategy.sort.SortComparable");

    /**
     * 编码
     */
    private final String code;

    /**
     * 编码
     */
    private final String className;

    SortDataTypeEnum(String code, String className) {
        this.code = code;
        this.className = className;
    }

    public String getCode() {
        return code;
    }

    public String getClassName() {
        return className;
    }

    public static SortDataTypeEnum convertByClassName(Class<?> className){
        return Arrays.stream(SortDataTypeEnum.values()).filter(sortDataTypeEnum ->
                Objects.equals(className.getName(), sortDataTypeEnum.getClassName())).findAny().orElse(null);
    }
}

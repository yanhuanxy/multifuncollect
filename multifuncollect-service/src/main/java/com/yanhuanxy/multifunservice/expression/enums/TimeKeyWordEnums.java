package com.yanhuanxy.multifunservice.expression.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 条件类型类型
 * @author yym
 */
public enum TimeKeyWordEnums {

    DEF_SYSDATE("sysdate", "sysdate", "YYYY-MM-dd", "yyyy-MM-dd", "系统日期"),
    DEF_SYSTIME("systime", "sysdate", "YYYY-MM-dd HH:mm:ss", "yyyy-MM-dd HH24:mi:ss", "系统时间"),
    OTHER("","","","","其他");

    private final String keyWord;
    private final String oracleKeyWord;
    private final String dataFormatter;
    private final String oracleDataFormatter;
    private final String message;

    TimeKeyWordEnums(String keyWord, String oracleKeyWord, String dataFormatter, String oracleDataFormatter, String message) {
        this.keyWord = keyWord;
        this.oracleKeyWord = oracleKeyWord;
        this.dataFormatter = dataFormatter;
        this.oracleDataFormatter = oracleDataFormatter;
        this.message = message;
    }


    public String getKeyWord() {
        return keyWord;
    }

    public String getOracleKeyWord() {
        return oracleKeyWord;
    }

    public String getDataFormatter() {
        return dataFormatter;
    }

    public String getOracleDataFormatter() {
        return oracleDataFormatter;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 校验dataType是否存在
     * @param dataType
     * @return
     */
    public static boolean exist(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        TimeKeyWordEnums[] values = TimeKeyWordEnums.values();
        for (TimeKeyWordEnums enums : values) {
            if (StringUtils.equalsIgnoreCase(enums.getKeyWord(), dataType)) {
                return true;
            }
        }
        return false;
    }

    public static TimeKeyWordEnums getTimeKeyWordEnums(String dataType){
        for (TimeKeyWordEnums enums: values()) {
            if(StringUtils.equals(enums.getKeyWord(), dataType)){
                return enums;
            }
        }
        return OTHER;
    }

    /**
     * 获取转换 sql 语句片段
     * @return
     */
    public String convertToOracleKeyWordFun(){
        return String.format("to_date(to_char(%s,'%s'),'%s')", oracleKeyWord, oracleDataFormatter, oracleDataFormatter);
    }
}

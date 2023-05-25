package com.yanhuanxy.multifunservice.expression.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据类型
 */
public enum ParamDataTypeEnums {
    CHAR("CHAR", "java.lang.String", "变长的字符型数据，长度以字节为单位"),
    VARCHAR("VARCHAR", "java.lang.String", "变长的字符型数据，长度以字节为单位"),
    VARCHAR2("VARCHAR2", "java.lang.String", "变长的字符型数据，长度以字节为单位（用null替代了varchar的空字符）"),
    NVARCHAR2("NVARCHAR2", "java.lang.String", "变长的字符型数据，长度以字符为单位（英文也占2字节）"),
    CLOB("CLOB", "java.lang.String", "大字符对象"),
    LONG("LONG", "java.lang.String", "长文本类型"),

    NUMBER("NUMBER", null, "存储整型或浮点型数值"),
    FLOAT("FLOAT", "java.lang.Float", "存储单精度浮点型"),
    BINARY_FLOAT("BINARY_FLOAT", "java.lang.Float", "存储单精度浮点型"),
    BINARY_DOUBLE("BINARY_DOUBLE", "java.lang.Double", "存储双精度浮点型"),

    DATE("DATE", "java.util.Date", "存储日期时间数据"),

    RAW("RAW", null, "byte[]"),
    BLOB("BLOB", null, "byte[]"),
    BLOB_RAW("BLOB RAW", null, "byte[]"),
    LONG_RAW("LONG RAW", null, "byte[]"),
    BFILE("BFILE", null, "byte[]");

    private String dataType;
    private String javaType;
    private String message;

    ParamDataTypeEnums(String dataType, String javaType, String message) {
        this.dataType = dataType;
        this.javaType = javaType;
        this.message = message;
    }

    public String getDataType() {
        return dataType;
    }

    private void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getJavaType() {
        return javaType;
    }

    private void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
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
        ParamDataTypeEnums[] values = ParamDataTypeEnums.values();
        for (ParamDataTypeEnums enums : values) {
            if (StringUtils.containsIgnoreCase(dataType, enums.getDataType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取函数结果的数据类型
     * @param formulaName 函数字段 DPUFormulaTypeEnum
     * @return DPUFormulaTypeEnum
     */
    public static ParamDataTypeEnums getParamDataTypeEnum(String formulaName){
        String tmpFormulaName = formulaName.toUpperCase();
        ParamDataTypeEnums[] formulaTypeEnums = ParamDataTypeEnums.values();
        for (ParamDataTypeEnums paramDataTypeEnum : formulaTypeEnums) {
            if(tmpFormulaName.startsWith(paramDataTypeEnum.toString().concat("("))){
                return paramDataTypeEnum;
            }
        }
        return null;
    }


    /**
     * 按dataType获取javaType
     * @param dataType
     * @return
     */
    public static String getJavaType(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return null;
        }
        ParamDataTypeEnums[] values = ParamDataTypeEnums.values();
        for (ParamDataTypeEnums enums : values) {
            if (StringUtils.containsIgnoreCase(dataType, enums.getDataType())) {
                return enums.javaType;
            }
        }
        return null;
    }

    /**
     * 按dataType获取message
     * @param dataType
     * @return
     */
    public static String getMessage(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return null;
        }
        ParamDataTypeEnums[] values = ParamDataTypeEnums.values();
        for (ParamDataTypeEnums enums : values) {
            if (StringUtils.equalsIgnoreCase(enums.getDataType(), dataType)) {
                return enums.message;
            }
        }
        return null;
    }

    /**
     * 是否为数字
     * @param dataType
     * @return
     */
    public static boolean isNumber(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        return StringUtils.containsAny(dataType.toUpperCase(), NUMBER.getDataType(), FLOAT.getDataType(), BINARY_FLOAT.getDataType(), BINARY_DOUBLE.getDataType());
    }

    /**
     * 是否为整数
     * @param dataType
     * @return
     */
    public static boolean isInteger(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        if (StringUtils.contains(dataType.toUpperCase(), NUMBER.getDataType())) {
            return !StringUtils.contains(dataType, ",") && StringUtils.contains(dataType, "(");
        }
        return false;
    }

    /**
     * 是否为小数
     * @param dataType
     * @return
     */
    public static boolean isDecimal(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        if (StringUtils.equals(dataType.toUpperCase(), NUMBER.getDataType())) {
            return true;
        } else if (StringUtils.contains(dataType.toUpperCase(), NUMBER.getDataType()) && StringUtils.contains(dataType.toUpperCase(), ",")) {
            return true;
        }else if(StringUtils.containsAny(dataType.toUpperCase(), FLOAT.getDataType(), BINARY_FLOAT.getDataType(), BINARY_DOUBLE.getDataType())){
            return true;
        }
        return false;
    }

    /**
     * 是否为日期/时间
     * @param dataType
     * @return
     */
    public static boolean isDate(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        return StringUtils.contains(dataType.toUpperCase(), DATE.getDataType());
    }

    /**
     *是否为字符
     * @param dataType
     * @return
     */
    public static boolean isCharacter(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        return StringUtils.containsAny(dataType.toUpperCase(), CHAR.getDataType(), VARCHAR.getDataType(), VARCHAR2.getDataType(), NVARCHAR2.getDataType(), CLOB.getDataType(), LONG.getDataType());
    }

    /**
     * 特殊的类型
     * @param dataType
     * @return
     */
    public static boolean isSpecial(String dataType) {
        if (StringUtils.isBlank(dataType)) {
            return false;
        }
        return StringUtils.containsAny(dataType.toUpperCase(), RAW.getDataType(), BLOB.getDataType(), BLOB_RAW.getDataType(), BFILE.getDataType());
    }

    /**
     * 获取参数类型
     * @param dataType
     * @return
     */
    public static String getParamType(String dataType) {
        if (isCharacter(dataType)) {
            return "string";
        } else if (isDate(dataType)) {
            return "datetime";
        } else if (isDecimal(dataType)) {
            return "decimal";
        } else if (isInteger(dataType)) {
            return "integer";
        } else {
            return "other";
        }
    }
}

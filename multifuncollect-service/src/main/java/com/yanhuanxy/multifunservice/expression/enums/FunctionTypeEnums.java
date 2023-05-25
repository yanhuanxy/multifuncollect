package com.yanhuanxy.multifunservice.expression.enums;

import java.util.Objects;

/**
 * @author yym
 * @className BiOperateTypeEnum
 * @description 数据加工 公式类型枚举类
 * @date 2022/9/1  17:23
 */
public enum FunctionTypeEnums {
    /**
     * 公式类型
     */
    OTHER(0, "其他", "", null),
    ROUND(1,"保留指定精度", "ROUND(%s)", ParamDataTypeEnums.NUMBER),
    ABS(2,"绝对值", "ABS(%s)", ParamDataTypeEnums.NUMBER),
    TO_NUMBER(3,"转数值", "TO_NUMBER(%s)", ParamDataTypeEnums.NUMBER),
    ADD_MONTHS(4,"增或减月份", "ADD_MONTHS(%s)", ParamDataTypeEnums.DATE),
    TO_DATE(5,"转时间", "TO_DATE(%s)", ParamDataTypeEnums.DATE),
    TRUNC(6,"按指定精度截取", "TRUNC(%s)", ParamDataTypeEnums.DATE),
    TRUNC_N(7,"按指定精度截取", "TRUNC(%s)", ParamDataTypeEnums.NUMBER);

    /**
     * 公式类型编码
     */
    private final Integer code;

    /**
     * 公式类型名称
     */
    private final String name;

    /**
     * 公式类型
     */
    private final String formula;

    /**
     * 公式结果数据类型
     */
    private final ParamDataTypeEnums paramDataTypeEnum;

    FunctionTypeEnums(Integer code, String name, String formula, ParamDataTypeEnums paramDataTypeEnum) {
        this.code = code;
        this.name = name;
        this.formula = formula;
        this.paramDataTypeEnum = paramDataTypeEnum;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public ParamDataTypeEnums getDpuDataTypeEnum() {
        return paramDataTypeEnum;
    }

    /**
     * 通过编码转换枚举
     * @param formulaType 表达式类型
     * @return DPUFormulaTypeEnum
     */
    public static FunctionTypeEnums getFormulaType(Integer formulaType){
        for(FunctionTypeEnums formulaTypeEnum : values()){
            if(formulaTypeEnum.getCode().equals(formulaType)){
                return formulaTypeEnum;
            }
        }
        return OTHER;
    }

    /**
     * 通过编码转换枚举
     * @param formulaName 表达式类型
     * @return DPUFormulaTypeEnum
     */
    public static FunctionTypeEnums getFormulaType(String formulaName){
        for(FunctionTypeEnums formulaTypeEnum : values()){
            if(formulaTypeEnum.toString().equals(formulaName.toUpperCase())){
                return formulaTypeEnum;
            }
        }
        return OTHER;
    }

    /**
     * 获取函数结果的数据类型
     * @param formulaName 函数字段 DPUFormulaTypeEnum
     * @return DPUFormulaTypeEnum
     */
    public static FunctionTypeEnums getFormulaDataType(String formulaName){
        String tmpFormulaName = formulaName.toUpperCase();
        FunctionTypeEnums[] formulaTypeEnums = FunctionTypeEnums.values();
        for (FunctionTypeEnums formulaTypeEnum : formulaTypeEnums) {
            if(tmpFormulaName.startsWith(formulaTypeEnum.toString().concat("("))){
                return formulaTypeEnum;
            }
        }
        return OTHER;
    }

    /**
     * 检查函数是否存在
     * @param formulaName 函数字段
     * @return DPUFormulaTypeEnum
     */
    public static Boolean checkFormulaIsExit(String formulaName){
        String tmpFormulaName = formulaName.toUpperCase();
        FunctionTypeEnums[] formulaTypeEnums = FunctionTypeEnums.values();
        for (FunctionTypeEnums formulaTypeEnum : formulaTypeEnums) {
            if(tmpFormulaName.equals(formulaTypeEnum.toString())){
                return true;
            }
        }
        return false;
    }


    /**
     * 检查是否为函数
     * @param formulaName 函数字段
     * @return FunctionTypeEnum
     */
    public static Boolean checkIsFormula(String formulaName){
        String tmpFormulaName = formulaName.toUpperCase();
        if(formulaName.startsWith(OTHER.toString().concat("("))){
            return false;
        }
        FunctionTypeEnums[] formulaTypeEnums = FunctionTypeEnums.values();
        for (FunctionTypeEnums formulaTypeEnum : formulaTypeEnums) {
            if(tmpFormulaName.startsWith(formulaTypeEnum.toString().concat("("))){
                return true;
            }
        }
        return false;
    }
}

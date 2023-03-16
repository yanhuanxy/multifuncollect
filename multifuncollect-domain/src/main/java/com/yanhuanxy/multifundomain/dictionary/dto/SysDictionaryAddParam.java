package com.yanhuanxy.multifundomain.dictionary.dto;

/**
 * 添加字典
 * @author yym
 * @date 2023/3/14
 */

public class SysDictionaryAddParam {

    /*字典编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典编码不能为空！*/
    private String dictCode;

    /*字典名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典名称不能为空！*/
    private String dictName;

    /*字典描述*/
    private String description;


    public SysDictionaryAddParam() {
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
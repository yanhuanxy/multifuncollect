package com.yanhuanxy.multifundomain.dictionary.dto;

/**
 * 查询字典
 * @author yym
 * @date 2023/3/14
 */
public class SysDictionaryQueryParam {

    /*字典编码*/
    private String dictCode;

    /*字典名称*/
    private String dictName;

    public SysDictionaryQueryParam() {
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
}
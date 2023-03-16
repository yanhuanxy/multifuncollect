package com.yanhuanxy.multifundomain.dictionary.dto;

/**
 * 修改字典
 * @author yym
 * @date 2023/3/14
 */

public class SysDictionaryEditParam {

    /*字典ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典ID不能为空！*/
    private Long id;

    /*字典编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典编码不能为空！*/
    private String dictCode;

    /*字典名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字典名称不能为空！*/
    private String dictName;

    /*字典描述*/
    private String description;

    public SysDictionaryEditParam() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
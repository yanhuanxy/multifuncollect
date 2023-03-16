package com.yanhuanxy.multifundomain.dictionary.dto;

/**
 * 添加字典项
 * @author yym
 * @date 2023/3/14
 */

public class SysDictionaryItemAddParam {

    /*字典ID")
    @NotNull(message = "字典ID不能为空！*/
    private Long dictId;

    /*字典项名称")
    @NotNull(message = "字典项名称不能为空！*/
    private String itemText;

    /*字典项值*/
    private String itemValue;

    /*字典项描述*/
    private String description;

    /*排序*/
    private Integer sortOrder;

    /*是否启用：0禁用，1启用*/
    private Integer status;

    public SysDictionaryItemAddParam() {
    }

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
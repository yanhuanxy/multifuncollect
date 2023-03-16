package com.yanhuanxy.multifundomain.dictionary.dto;

/**
 * 查询字典项
 * @author yym
 * @date 2023/3/14
 */
public class SysDictionaryItemQueryParam {

    /*字典项名称*/
    private String itemText;



    public SysDictionaryItemQueryParam() {
    }

    public String getItemText() {
        return itemText;
    }

    public void setItemText(String itemText) {
        this.itemText = itemText;
    }
}
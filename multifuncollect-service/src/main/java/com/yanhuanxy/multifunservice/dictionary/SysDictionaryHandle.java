package com.yanhuanxy.multifunservice.dictionary;

/**
 * 处理字典项
 */
public interface SysDictionaryHandle {

    /**
     * 获取字典项名称
     * @param dictCode 字典编码
     * @param dictItemValue 字典项值
     */
    String findDictItemName(String dictCode, String dictItemValue);
}

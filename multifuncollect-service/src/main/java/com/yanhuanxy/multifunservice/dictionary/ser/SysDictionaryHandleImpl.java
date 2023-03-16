package com.yanhuanxy.multifunservice.dictionary.ser;

import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryHandle;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 处理字典项
 */
@Service
public class SysDictionaryHandleImpl implements SysDictionaryHandle {

    private final SysDictionaryService sysDictionaryService;

    public SysDictionaryHandleImpl(SysDictionaryService sysDictionaryService) {
        this.sysDictionaryService = sysDictionaryService;
    }

    public String findDictItemName(String dictCode, String dictItemValue){
        List<SysDictionaryItem> sysDictionaryItemList = sysDictionaryService.findSysDictionaryItemList(dictCode);

        return sysDictionaryItemList.stream().filter(item-> Objects.equals(dictItemValue, item.getItemValue())).map(SysDictionaryItem::getItemText).findAny().orElse(null);
    }
}

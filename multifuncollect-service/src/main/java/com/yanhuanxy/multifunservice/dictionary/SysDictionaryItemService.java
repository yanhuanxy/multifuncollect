package com.yanhuanxy.multifunservice.dictionary;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemAddParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemEditParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemQueryParam;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import com.yanhuanxy.multifunexport.tools.base.PageDTO;

import java.util.List;

/**
 * 字典项
 * @author yym
 * @date 2023/3/14
 */
public interface SysDictionaryItemService extends IService<SysDictionaryItem> {

    /**
     * 添加字典项
     * @param param 参数信息
     */
    Long add(SysDictionaryItemAddParam param);

    /**
     * 修改字典项
     * @param param 参数信息
     */
    void edit(SysDictionaryItemEditParam param);

    /**
     * 校验字典项名称 唯一
     * @param id id
     * @param itemText 字典项名称
     */
    Boolean checkDictItemText(Long id, String itemText);

    /**
     * 校验字典项编码 唯一
     * @param id id
     * @param itemValue 字典项编码
     */
    Boolean checkDictItemValue(Long id, String itemValue);


    /**
     * 删除字典项
     * @param id 参数信息
     */
    void delete(Long id);

    /**
     * 通过字典id 删除字典项
     * @param dictId 参数信息
     */
    void deleteByDictId(Long dictId);

    /**
     * 条件查询
     * @param param 参数信息
     */
    List<SysDictionaryItem> list(SysDictionaryItemQueryParam param);

    /**
     * 分页查询
     * @param param 参数信息
     */
    IPage<SysDictionaryItem> page(PageDTO pageParam, SysDictionaryItemQueryParam param);

}

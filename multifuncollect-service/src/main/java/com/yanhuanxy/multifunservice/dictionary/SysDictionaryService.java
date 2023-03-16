package com.yanhuanxy.multifunservice.dictionary;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryAddParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryEditParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryQueryParam;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionary;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import com.yanhuanxy.multifunexport.tools.base.PageDTO;

import java.util.List;

/**
 * 字典
 * @author yym
 * @date 2023/3/14
 */
public interface SysDictionaryService extends IService<SysDictionary> {

    /**
     * 创建字典
     * @param param 参数信息
     */
    Long add(SysDictionaryAddParam param);

    /**
     * 修改字典
     * @param param 参数信息
     */
    void edit(SysDictionaryEditParam param);

    /**
     * 校验字典名称 唯一
     * @param id id
     * @param dictName 字典名称
     */
    Boolean checkDictName(Long id, String dictName);

    /**
     * 校验字典编码 唯一
     * @param id id
     * @param dictValue 字典编码
     */
    Boolean checkDictCode(Long id, String dictValue);

    /**
     * 删除字典
     * @param id 参数信息
     */
    void delete(Long id);

    /**
     * 条件查询
     * @param param 参数信息
     */
    List<SysDictionary> list(SysDictionaryQueryParam param);

    /**
     * 分页查询
     * @param param 参数信息
     */
    IPage<SysDictionary> page(PageDTO pageParam, SysDictionaryQueryParam param);


    /**
     * 根据字典编码获取字典项信息
     * @param dictCode 字典编码
     */
    List<SysDictionaryItem> findSysDictionaryItemList(String dictCode);
}

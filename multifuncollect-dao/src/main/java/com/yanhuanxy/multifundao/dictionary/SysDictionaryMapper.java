package com.yanhuanxy.multifundao.dictionary;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionary;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 字典
 * @author yym
 * @date 2023/3/14
 */

@Mapper
public interface SysDictionaryMapper extends BaseMapper<SysDictionary> {

    /**
     * 根据字典编码获取字典项信息
     * @param dictCode 字典编码
     */
    List<SysDictionaryItem> getDictionaryItemListByDictCode(String dictCode);
}
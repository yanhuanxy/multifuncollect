package com.yanhuanxy.multifunservice.dictionary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifundao.dictionary.SysDictionaryItemMapper;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemAddParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemEditParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryItemQueryParam;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import com.yanhuanxy.multifunexport.tools.base.PageDTO;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典项
 * @author yym
 * @date 2023/3/14
 */

@Service
public class SysDictionaryItemServiceImpl extends ServiceImpl<SysDictionaryItemMapper, SysDictionaryItem> implements SysDictionaryItemService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long add(SysDictionaryItemAddParam param) {

        // 校验字典项 名称 和 值
        checkDictItemTextAndValue(null, param.getItemText(), param.getItemValue());

        SysDictionaryItem sysDictionaryItem = new SysDictionaryItem();
        sysDictionaryItem.setDictId(param.getDictId());
        sysDictionaryItem.setItemText(param.getItemText());
        sysDictionaryItem.setItemValue(param.getItemValue());
        sysDictionaryItem.setSortOrder(param.getSortOrder());
        sysDictionaryItem.setStatus(param.getStatus());
        sysDictionaryItem.setDescription(param.getDescription());
        super.save(sysDictionaryItem);

        return sysDictionaryItem.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysDictionaryItemEditParam param) {

        SysDictionaryItem sysDictionaryItem = super.getById(param.getId());
        if(sysDictionaryItem == null){
            throw new BaseRuntimeException("字典不存在！校验后再尝试");
        }

        // 校验 字典项名称 和 编码
        checkDictItemTextAndValue(param.getId(), param.getItemText(), param.getItemValue());

        sysDictionaryItem.setItemText(param.getItemText());
        sysDictionaryItem.setItemValue(param.getItemValue());
        sysDictionaryItem.setSortOrder(param.getSortOrder());
        sysDictionaryItem.setStatus(param.getStatus());
        sysDictionaryItem.setDescription(param.getDescription());

        super.updateById(sysDictionaryItem);
    }

    /**
     * 校验 字典项名称 和 编码
     */
    public void checkDictItemTextAndValue(Long id, String itemText, String itemValue){

        List<SysDictionaryItem> dictionaryItems = queryList(itemText, itemValue);
        if(checkDictItemText(id, itemText, dictionaryItems)){
            throw new BaseRuntimeException("字典项名称重复！校验后再尝试");
        }
        if(checkDictItemValue(id, itemValue, dictionaryItems)){
            throw new BaseRuntimeException("字典项编码重复！校验后再尝试");
        }
    }

    /**
     * 根据 字典项名称 或者 编码 查询字典项信息
     */
    private List<SysDictionaryItem> queryList(String itemText, String itemValue){
        QueryWrapper<SysDictionaryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(itemText),"item_text", itemText)
                .or(wrapper-> wrapper.eq(StringUtils.isNotBlank(itemValue),"item_value", itemValue));
        List<SysDictionaryItem> dictionaryItems = super.list(queryWrapper);

        return dictionaryItems;
    }

    /**
     * 校验字典名称
     */
    public Boolean checkDictItemText(Long id, String itemText){
        List<SysDictionaryItem> dictionaryItems = queryList(itemText, null);
        return checkDictItemText(id, itemText, dictionaryItems);
    }

    /**
     * 校验字典编码
     */
    public Boolean checkDictItemValue(Long id, String itemValue){
        List<SysDictionaryItem> dictionaryItems = queryList(null, itemValue);
        return checkDictItemValue(id, itemValue, dictionaryItems);
    }

    /**
     * 校验字典项名称
     */
    private Boolean checkDictItemText(Long id, String itemText, List<SysDictionaryItem> dictionaryItems){
        List<SysDictionaryItem> tmpDictionaryItems = dictionaryItems.stream().filter(item->
                Objects.equals(item.getItemText(), itemText)).collect(Collectors.toList());
        if(tmpDictionaryItems.isEmpty()){
            return false;
        }
        if(Objects.isNull(id)){
            return true;
        }
        return tmpDictionaryItems.stream().noneMatch(item-> Objects.equals(item.getId(), id));
    }

    /**
     * 校验字典项编码
     */
    private Boolean checkDictItemValue(Long id, String itemValue, List<SysDictionaryItem> dictionaryItems){
        List<SysDictionaryItem> tmpDictionaries = dictionaryItems.stream().filter(item->
                Objects.equals(item.getItemValue(), itemValue)).collect(Collectors.toList());
        if(tmpDictionaries.isEmpty()){
            return false;
        }
        if(Objects.isNull(id)){
            return true;
        }
        return dictionaryItems.stream().noneMatch(item-> Objects.equals(item.getId(), id));
    }

    @Override
    public void delete(Long id) {

        super.removeById(id);
    }

    @Override
    public void deleteByDictId(Long dictId) {

        QueryWrapper<SysDictionaryItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dict_id", dictId);
        super.remove(queryWrapper);
    }


    @Override
    public List<SysDictionaryItem> list(SysDictionaryItemQueryParam param) {

        // 参数
        QueryWrapper<SysDictionaryItem> queryWrapper = queryWrapper(param);
        // 查询
        return super.list(queryWrapper);
    }

    @Override
    public IPage<SysDictionaryItem> page(PageDTO pageParam, SysDictionaryItemQueryParam param) {

        // 分页参数
        Page<SysDictionaryItem> mpPage = new Page<>(Objects.isNull(pageParam.getPageNo())? 1 : pageParam.getPageNo(),
                Objects.isNull(pageParam.getPageSize())? 10 : pageParam.getPageSize());// PageUtil.toMpPage(pageParam);
        // 参数
        QueryWrapper<SysDictionaryItem> queryWrapper = queryWrapper(param);
        queryWrapper.orderByDesc("create_time");
        // 查询
        IPage<SysDictionaryItem> iPage = super.page(mpPage, queryWrapper);
        return iPage;
    }


    /**
     * 组装参数
     * @param param 参数信息
     */
    private QueryWrapper<SysDictionaryItem> queryWrapper(SysDictionaryItemQueryParam param){

        QueryWrapper<SysDictionaryItem> queryWrapper = new QueryWrapper<>();
        if (param != null) {
            String itemText = param.getItemText();
            queryWrapper.like(StringUtils.isNotBlank(itemText), "item_text", itemText);
        }
        return queryWrapper;
    }

}

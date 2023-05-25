package com.yanhuanxy.multifunservice.dictionary.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifundao.dictionary.SysDictionaryMapper;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryAddParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryEditParam;
import com.yanhuanxy.multifundomain.dictionary.dto.SysDictionaryQueryParam;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionary;
import com.yanhuanxy.multifundomain.dictionary.entity.SysDictionaryItem;
import com.yanhuanxy.multifunexport.tools.base.PageDTO;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryItemService;
import com.yanhuanxy.multifunservice.dictionary.SysDictionaryService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 字典
 * @author yym
 * @date 2023/3/14
 */

@Service
public class SysDictionaryServiceImpl extends ServiceImpl<SysDictionaryMapper, SysDictionary> implements SysDictionaryService {

    private final SysDictionaryMapper sysDictionaryMapper;

    private final SysDictionaryItemService sysDictionaryItemService;

    public SysDictionaryServiceImpl(SysDictionaryMapper sysDictionaryMapper,
                                    SysDictionaryItemService sysDictionaryItemService) {
        this.sysDictionaryMapper = sysDictionaryMapper;
        this.sysDictionaryItemService = sysDictionaryItemService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long add(SysDictionaryAddParam param) {

        // 校验字典名称 字典编码 是否重复
        checkDictNameAndCode(null, param.getDictName(), param.getDictCode());

        SysDictionary dictionary = new SysDictionary();
        dictionary.setDictName(param.getDictName());
        dictionary.setDictCode(param.getDictCode());
        dictionary.setDescription(param.getDescription());
        super.save(dictionary);

        return dictionary.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysDictionaryEditParam param) {

        SysDictionary dictionary = super.getById(param.getId());
        if(dictionary == null){
            throw new BaseRuntimeException("字典信息不存在！刷新页面重新尝试");
        }
        // 校验字典名称 字典编码 是否重复
        checkDictNameAndCode(param.getId(), param.getDictName(), param.getDictCode());

        dictionary.setDictName(param.getDictName());
        dictionary.setDictCode(param.getDictCode());
        dictionary.setDescription(param.getDescription());

        super.updateById(dictionary);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(Long id) {

        super.removeById(id);

        // 删除字典项信息
        sysDictionaryItemService.deleteByDictId(id);
    }

    /**
     * 校验 用户名 和 用户编码
     */
    public void checkDictNameAndCode(Long id, String dictName, String dictCode){

        List<SysDictionary> dictionaries = queryList(dictName, dictCode);
        if(checkDictName(id, dictName, dictionaries)){
            throw new BaseRuntimeException("字典名称重复！校验后再尝试");
        }
        if(checkDictCode(id, dictCode, dictionaries)){
            throw new BaseRuntimeException("字典编码重复！校验后再尝试");
        }
    }

    /**
     * 根据 字典名称 或者 编码 查询字典
     */
    private List<SysDictionary> queryList(String dictName, String dictCode){
        QueryWrapper<SysDictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(dictName),"dict_name", dictName)
                .or(wrapper-> wrapper.eq(StringUtils.isNotBlank(dictCode),"dict_code", dictCode));
        List<SysDictionary> dictionaries = super.list(queryWrapper);

        return dictionaries;
    }

    /**
     * 校验字典名称
     */
    public Boolean checkDictName(Long id, String dictName){
        List<SysDictionary> dictionaries = queryList(dictName, null);
        return checkDictName(id, dictName,dictionaries);
    }

    /**
     * 校验字典编码
     */
    public Boolean checkDictCode(Long id, String dictCode){
        List<SysDictionary> dictionaries = queryList(null, dictCode);
        return checkDictCode(id, dictCode,dictionaries);
    }

    /**
     * 校验字典名称
     */
    private Boolean checkDictName(Long id, String dictName, List<SysDictionary> dictionaries){
        List<SysDictionary> tmpDictionaries = dictionaries.stream().filter(item->
                Objects.equals(item.getDictName(), dictName)).collect(Collectors.toList());
        if(tmpDictionaries.isEmpty()){
            return false;
        }
        if(Objects.isNull(id)){
            return true;
        }
        return dictionaries.stream().noneMatch(item-> Objects.equals(item.getId(), id));
    }

    /**
     * 校验字典编码
     */
    private Boolean checkDictCode(Long id, String dictCode, List<SysDictionary> dictionaries){
        List<SysDictionary> tmpDictionaries = dictionaries.stream().filter(item->
                Objects.equals(item.getDictCode(), dictCode)).collect(Collectors.toList());
        if(tmpDictionaries.isEmpty()){
            return false;
        }
        if(Objects.isNull(id)){
            return true;
        }
        return dictionaries.stream().noneMatch(item-> Objects.equals(item.getId(), id));
    }

    @Override
    public List<SysDictionary> list(SysDictionaryQueryParam param) {

        // 参数
        QueryWrapper<SysDictionary> queryWrapper = queryWrapper(param);
        // 查询
        return super.list(queryWrapper);
    }

    @Override
    public IPage<SysDictionary> page(PageDTO pageParam, SysDictionaryQueryParam param) {

        // 分页参数
        Page<SysDictionary> mpPage = new Page<>(Objects.isNull(pageParam.getPageNo())? 1 : pageParam.getPageNo(),
                Objects.isNull(pageParam.getPageSize())? 10 : pageParam.getPageSize());// PageUtil.toMpPage(pageParam);
        // 参数
        QueryWrapper<SysDictionary> queryWrapper = queryWrapper(param);
        queryWrapper.orderByDesc("create_time");
        // 查询
        IPage<SysDictionary> iPage = super.page(mpPage, queryWrapper);
        return iPage;
    }

    @Override
    public List<SysDictionaryItem> findSysDictionaryItemList(String dictCode) {
        if(ObjectUtils.isEmpty(dictCode)){
            throw new BaseRuntimeException("字典编码为空！");
        }

        return sysDictionaryMapper.getDictionaryItemListByDictCode(dictCode);
    }

    /**
     * 组装参数
     * @param param 参数信息
     */
    private QueryWrapper<SysDictionary> queryWrapper(SysDictionaryQueryParam param){

        QueryWrapper<SysDictionary> queryWrapper = new QueryWrapper<>();
        if (param != null) {
            String dictName = param.getDictName();
            queryWrapper.like(StringUtils.isNotBlank(dictName), "dict_name", dictName);
            String dictCode = param.getDictCode();
            queryWrapper.eq(ObjectUtils.isNotEmpty(dictCode), "dict_code", dictCode);
        }
        return queryWrapper;
    }
}

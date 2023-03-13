package com.yanhuanxy.multifunservice.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuanxy.multifundao.message.ProSmsMapper;
import com.yanhuanxy.multifundomain.message.dto.ProSmsDTO;
import com.yanhuanxy.multifundomain.message.entity.ProSms;
import com.yanhuanxy.multifunexport.fileservice.util.Result;
import com.yanhuanxy.multifunservice.message.ProSmsService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author
 * @date 2021/7/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProSmsServiceImpl extends ServiceImpl<ProSmsMapper, ProSms> implements ProSmsService {
    private final ProSmsMapper proSmsMapper;
    @Autowired
    public ProSmsServiceImpl(ProSmsMapper proSmsMapper) {
        this.proSmsMapper = proSmsMapper;
    }

    @Override
    public Result<?> page(ProSmsDTO dto) {
        QueryWrapper<ProSms> wrapper = new QueryWrapper<>();
        Page<ProSms> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        wrapper.eq(ObjectUtils.isNotEmpty(dto.getEsContent()),"ES_CONTENT",dto.getEsContent());
        wrapper.like(ObjectUtils.isNotEmpty(dto.getEsTitle()),"ES_TITLE",dto.getEsTitle());
        wrapper.like(ObjectUtils.isNotEmpty(dto.getEsReceiver()),"ES_RECEIVER",dto.getEsReceiver());
        wrapper.eq(ObjectUtils.isNotEmpty(dto.getEsReadStatus()),"ES_READ_STATUS",dto.getEsReadStatus());
        wrapper.orderByDesc("ES_SEND_TIME");
        //只能查看与当前用户相关的消息
        if(ObjectUtils.isNotEmpty(dto.getIsNotRead())){
            wrapper.eq("ES_RECEIVER", "admin");
        }else{
            wrapper.and(wp->{wp.eq("ES_RECEIVER", "admin").or().eq("CREATOR","admin");});
        }
        return Result.ok(proSmsMapper.selectPage(page,wrapper));
    }
    @Override
    public Result<?> detail(Integer id) {
        ProSms proSms = proSmsMapper.selectById(id);
        //将未读状态改为已读
        if(proSms.getEsReadStatus().equals(0)&&proSms.getEsReceiver().equals("admin")){
            proSms.setEsReadStatus(1);
            proSmsMapper.updateById(proSms);
        }
        return Result.ok(proSms);
    }

    @Override
    public Result<?> readAll() {
        ProSms proSms = new ProSms();
        //将未读状态改为已读
        proSms.setEsReadStatus(1);
        proSmsMapper.update(proSms,new QueryWrapper<ProSms>().eq("ES_RECEIVER","admin").eq("ES_READ_STATUS","0"));
        return Result.ok(proSms);
    }
}

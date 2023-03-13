package com.yanhuanxy.multifunservice.message.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanhuanxy.multifundao.message.ProSmsTemplateMapper;
import com.yanhuanxy.multifundomain.message.dto.ProSmsSendDTO;
import com.yanhuanxy.multifundomain.message.dto.ProSmsTemplateDTO;
import com.yanhuanxy.multifundomain.message.entity.ProSms;
import com.yanhuanxy.multifundomain.message.entity.ProSmsTemplate;
import com.yanhuanxy.multifunexport.fileservice.util.Result;
import com.yanhuanxy.multifunservice.message.ProSmsTemplateService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @date 2021/7/28
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProSmsTemplateServiceImpl extends ServiceImpl<ProSmsTemplateMapper, ProSmsTemplate> implements ProSmsTemplateService {
    private final ProSmsTemplateMapper proSmsTemplateMapper;

    @Autowired
    public ProSmsTemplateServiceImpl(ProSmsTemplateMapper proSmsTemplateMapper) {
        this.proSmsTemplateMapper = proSmsTemplateMapper;
    }


    @Override
    public Result<?> page(ProSmsTemplateDTO dto) {
        QueryWrapper<ProSmsTemplate> wrapper = new QueryWrapper<>();
        Page<ProSmsTemplate> page = new Page<>(dto.getPageNo(), dto.getPageSize());
        wrapper.eq(ObjectUtils.isNotEmpty(dto.getTemplateType()),"TEMPLATE_TYPE",dto.getTemplateType());
        wrapper.like(ObjectUtils.isNotEmpty(dto.getTemplateName()),"TEMPLATE_NAME",dto.getTemplateName());
        wrapper.like(ObjectUtils.isNotEmpty(dto.getTemplateContent()),"TEMPLATE_CONTENT",dto.getTemplateContent());

        return Result.ok(proSmsTemplateMapper.selectPage(page,wrapper));
    }

    @Override
    public Result<?> sendTestSms(ProSmsSendDTO dto, List<File> files) {
        ProSms proSms = new ProSms();
        proSms.setEsContent(dto.getEsContent());
        proSms.setEsParam(dto.getEsParam());
        proSms.setEsReceiver(dto.getEsReceiver());
        proSms.setEsType(dto.getEsType());
        proSms.setEsTitle(dto.getEsTitle());
        proSms.setEsSendTime(new Date());
        proSms.setEsSendStatus(0);
        proSms.setEsReadStatus(0);
//        ProSmsVO proSmsVO = new ProSmsVO();
//        BeanUtils.copyProperties(proSms, proSmsVO);
//        Result<?> rpcresult = this.smsExportService.sendSms(proSmsVO, SmsType.SYSTEM.getObj(dto.getEsType()), ObjectUtils.isNotEmpty(files) ? files.toArray(new File[]{}) : null);
        Result<Object> result = new Result<>();
//        BeanUtils.copyProperties(rpcresult, result);
        return result;
    }
}

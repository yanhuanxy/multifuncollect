package com.yanhuanxy.multifunservice.message.impl;

import com.yanhuanxy.multifundomain.message.entity.ProSms;
import com.yanhuanxy.multifundomain.message.vo.ProSmsVO;
import com.yanhuanxy.multifundomain.system.SysUser;
import com.yanhuanxy.multifunexport.fileservice.util.Result;
import com.yanhuanxy.multifunservice.message.ProSmsService;
import com.yanhuanxy.multifunservice.message.StrategySms;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;

/**
 * @date 2021/8/2
 */
@Component("system")
public class StrategySmsSystemImpl implements StrategySms {
    private static final Logger LOG = LoggerFactory.getLogger(StrategySmsSystemImpl.class);
    private final ProSmsService smsService;

    @Autowired
    public StrategySmsSystemImpl(ProSmsService smsService) {
        this.smsService = smsService;
    }
    @Override
    public Result sendSms(ProSmsVO vo, SysUser userVO, Long deptId){
        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(), vo.getEsReceiver(), vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
        try {
            proSms.setEsReadStatus(0);
            proSms.setEsSendStatus(1);
            proSms.setEsSendTime(new Date());
            if(ObjectUtils.isEmpty(proSms.getCreator())){
                proSms.setCreator(DEFAULT_ADMIN);
                proSms.setModifyer(DEFAULT_ADMIN);
            }
            proSms.setCreateTime(new Date());
            proSms.setModifyTime(new Date());
        }catch (Exception e){
            proSms.setEsSendStatus(2);
            LOG.error("发送消息异常 {}",e.getMessage());
            return Result.error("发送消息异常，请联系管理员");
        }finally {
            smsService.save(proSms);
        }
        return Result.ok();
    }

    @Override
    public Result sendSms(ProSmsVO vo, SysUser userVO, Long deptId, File... files) {
        return Result.error("系统消息不支持 附件发送");
    }

    @Override
    public Result sendSms(ProSmsVO vo, String[] esReceivers, Long deptId, File... files) {
        return Result.error("系统消息不支持 附件发送");
    }
}

package com.yanhuanxy.multifunservice.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuanxy.multifundomain.message.dto.ProSmsSendDTO;
import com.yanhuanxy.multifundomain.message.dto.ProSmsTemplateDTO;
import com.yanhuanxy.multifundomain.message.entity.ProSmsTemplate;
import com.yanhuanxy.multifunexport.fileservice.util.Result;

import java.io.File;
import java.util.List;

/**
 * @author
 * @date 2021/7/28
 */

public interface ProSmsTemplateService extends IService<ProSmsTemplate> {
    /**
     * 查询消息模版分页
     * @param dto
     * @return
     */
    Result<?> page(ProSmsTemplateDTO dto);
    /**
     * 发送测试消息
     * @param dto
     * @param files
     * @return
     */
    Result<?> sendTestSms(ProSmsSendDTO dto, List<File> files);
}

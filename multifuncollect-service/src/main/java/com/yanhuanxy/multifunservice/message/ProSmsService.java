package com.yanhuanxy.multifunservice.message;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhuanxy.multifundomain.message.dto.ProSmsDTO;
import com.yanhuanxy.multifundomain.message.entity.ProSms;
import com.yanhuanxy.multifunexport.fileservice.util.Result;

/**
 * @author
 * @date 2021/7/28
 */

public interface ProSmsService extends IService<ProSms> {

    /**
     * 查询消息模版分页
     * @param dto
     * @return
     */
    Result<?> page(ProSmsDTO dto);
    /**
     * 获得详情
     * @param id
     * @return
     */
    Result<?> detail(Integer id);

    /**
     * 全部已读
     * @return
     */
    Result<?> readAll();
}

package com.yanhuanxy.multifunservice.message;

import com.yanhuanxy.multifundomain.message.vo.ProSmsVO;
import com.yanhuanxy.multifundomain.system.SysUser;
import com.yanhuanxy.multifunexport.fileservice.util.Result;

import java.io.File;

/**
 * @author
 * 消息策略
 */
public interface StrategySms {
    static final String DEFAULT_ADMIN = "newadmin";
    /**
     * 发送消息
     * @param vo
     * @param userVO
     * @param deptId
     * @return
     */
    Result sendSms(ProSmsVO vo, SysUser userVO, Long deptId);
    /**
     * 发送消息(可携带附件)
     * @param vo
     * @param userVO
     * @param deptId
     * @param files
     * @return
     */
    Result sendSms(ProSmsVO vo, SysUser userVO, Long deptId, File... files);

    /**
     * 发送消息(可携带附件)
     * @param vo
     * @param esReceivers
     * @param deptId
     * @param files
     * @return
     */
    Result sendSms(ProSmsVO vo, String[] esReceivers, Long deptId, File... files);
}

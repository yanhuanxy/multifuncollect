package com.yanhuanxy.multifundomain.message.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhuanxy.multifundomain.base.PageDTO1;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @date 2021/7/28
 */

public class ProSmsDTO extends PageDTO1 {
    /**
     * 是否只查询收到的消息
     */
    private Integer isNotRead;
    private Integer id;
    /**
     * 消息标题
     */
    private String esTitle;
    /**
     * 发送方式（0:系统，1:邮件，2:短信）
     */
    private String esType;
    /**
     * 接收人
     */
    private String esReceiver;
    /**
     * 发送所需参数 JSON格式
     */
    private String esParam;
    /**
     * 推送内容
     */
    private String esContent;
    /**
     * 推送时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date esSendTime;
    /**
     * 推送状态
     */
    private String esSendStatus;
    /**
     * 备注
     */
    private String remark;

    private Integer esReadStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEsTitle() {
        return esTitle;
    }

    public void setEsTitle(String esTitle) {
        this.esTitle = esTitle;
    }

    public String getEsType() {
        return esType;
    }

    public void setEsType(String esType) {
        this.esType = esType;
    }

    public String getEsReceiver() {
        return esReceiver;
    }

    public void setEsReceiver(String esReceiver) {
        this.esReceiver = esReceiver;
    }

    public String getEsParam() {
        return esParam;
    }

    public void setEsParam(String esParam) {
        this.esParam = esParam;
    }

    public String getEsContent() {
        return esContent;
    }

    public void setEsContent(String esContent) {
        this.esContent = esContent;
    }

    public Date getEsSendTime() {
        return esSendTime;
    }

    public void setEsSendTime(Date esSendTime) {
        this.esSendTime = esSendTime;
    }

    public String getEsSendStatus() {
        return esSendStatus;
    }

    public void setEsSendStatus(String esSendStatus) {
        this.esSendStatus = esSendStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getEsReadStatus() {
        return esReadStatus;
    }

    public void setEsReadStatus(Integer esReadStatus) {
        this.esReadStatus = esReadStatus;
    }

    public Integer getIsNotRead() {
        return isNotRead;
    }

    public void setIsNotRead(Integer isNotRead) {
        this.isNotRead = isNotRead;
    }

    public ProSmsDTO() {
    }

    public ProSmsDTO(Integer id, String esTitle, String esType, String esReceiver, String esParam, String esContent, Date esSendTime, String esSendStatus, String remark,Integer esReadStatus) {
        this.id = id;
        this.esTitle = esTitle;
        this.esType = esType;
        this.esReceiver = esReceiver;
        this.esParam = esParam;
        this.esContent = esContent;
        this.esSendTime = esSendTime;
        this.esSendStatus = esSendStatus;
        this.remark = remark;
        this.esReadStatus = esReadStatus;
    }

    @Override
    public String toString() {
        return "ProSmsDTO{" +
                "id=" + id +
                ", esTitle='" + esTitle + '\'' +
                ", esType='" + esType + '\'' +
                ", esReceiver='" + esReceiver + '\'' +
                ", esParam='" + esParam + '\'' +
                ", esContent='" + esContent + '\'' +
                ", esSendTime='" + esSendTime + '\'' +
                ", esSendStatus='" + esSendStatus + '\'' +
                ", esReadStatus='" + esReadStatus + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}

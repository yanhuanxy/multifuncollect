package com.yanhuanxy.multifundomain.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yanhuanxy.multifundomain.base.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 表单信息表
 * @date 2021/7/12
 */
@TableName("PRO_SMS")
@KeySequence(value = "SEQ_PRO_SMS")
public class ProSms extends BaseEntity {

    @TableId(value = "ID", type = IdType.INPUT)
    private Integer id;
    /**
     * 消息标题
     */
    @TableField(value = "ES_TITLE")
    private String esTitle;
    /**
     * 发送方式（0:系统，1:邮件，2:短信）
     */
    @TableField(value = "ES_TYPE")
    private String esType;
    /**
     * 接收人
     */
    @TableField(value = "ES_RECEIVER")
    private String esReceiver;
    /**
     * 发送所需参数 JSON格式
     */
    @TableField(value = "ES_PARAM")
    private String esParam;
    /**
     * 推送内容
     */
    @TableField(value = "ES_CONTENT")
    private String esContent;
    /**
     * 推送时间
     */
    @TableField(value = "ES_SEND_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date esSendTime;
    /**
     * 推送状态
     */
    @TableField(value = "ES_SEND_STATUS")
    private Integer esSendStatus;
    /**
     * 是否查看
     */
    @TableField(value = "ES_READ_STATUS")
    private Integer esReadStatus;
    /**
     * 备注
     */
    @TableField(value = "REMARK")
    private String remark;

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

    public Integer getEsSendStatus() {
        return esSendStatus;
    }

    public void setEsSendStatus(Integer esSendStatus) {
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

    public ProSms() {
    }

    public ProSms(Integer id, String esTitle, String esType, String esReceiver, String esParam, String esContent, Date esSendTime, Integer esSendStatus, Integer esReadStatus, String remark) {
        this.id = id;
        this.esTitle = esTitle;
        this.esType = esType;
        this.esReceiver = esReceiver;
        this.esParam = esParam;
        this.esContent = esContent;
        this.esSendTime = esSendTime;
        this.esSendStatus = esSendStatus;
        this.esReadStatus = esReadStatus;
        this.remark = remark;
    }

}

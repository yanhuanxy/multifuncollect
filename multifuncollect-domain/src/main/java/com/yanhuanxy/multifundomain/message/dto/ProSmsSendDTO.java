package com.yanhuanxy.multifundomain.message.dto;


/**
 * @date 2021/7/28
 */

public class ProSmsSendDTO{
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

    public ProSmsSendDTO() {
    }

    @Override
    public String toString() {
        return "ProSmsSendDTO{" +
                "esTitle='" + esTitle + '\'' +
                ", esType='" + esType + '\'' +
                ", esReceiver='" + esReceiver + '\'' +
                ", esParam='" + esParam + '\'' +
                ", esContent='" + esContent + '\'' +
                '}';
    }

    public ProSmsSendDTO(String esTitle, String esType, String esReceiver, String esParam, String esContent) {
        this.esTitle = esTitle;
        this.esType = esType;
        this.esReceiver = esReceiver;
        this.esParam = esParam;
        this.esContent = esContent;
    }
}

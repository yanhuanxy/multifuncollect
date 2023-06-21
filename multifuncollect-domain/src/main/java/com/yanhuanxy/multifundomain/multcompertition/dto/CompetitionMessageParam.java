package com.yanhuanxy.multifundomain.multcompertition.dto;

import java.util.List;

/**
 * socket 多人竞赛信息
 * @author yym
 */
public class CompetitionMessageParam {

    /*@Parameter(description = "消息类型 1、建立连接 2、等待用户分配房间 3、答题 ")*/
    private Integer messageType;

    /*@Parameter(description = "消息内容")*/
    private String content;

    /*@Parameter(description = "发送者")*/
    private String sender;

    /*@Parameter(description = "接受者")*/
    private List<String> receiver;

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceiver() {
        return receiver;
    }

    public void setReceiver(List<String> receiver) {
        this.receiver = receiver;
    }
}

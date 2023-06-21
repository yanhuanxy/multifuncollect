package com.yanhuanxy.multifundomain.multcompertition.vo;

import java.util.List;

/**
 * socket 多人竞赛信息
 * @author yym
 */
public class CompetitionMessageVO {

    private Integer messageType;

    private String content;

    private String sender;

    private List<String> receiver;

    private boolean isIdle;

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

    public boolean isIdle() {
        return isIdle;
    }

    public void setIdle(boolean idle) {
        isIdle = idle;
    }
}

package com.yanhuanxy.multifundomain.message.vo;


import com.yanhuanxy.multifundomain.base.PageDTO1;

import java.util.Arrays;
import java.util.Date;

/**
 * @author
 * @date 2021/7/28
 */

public class ProSmsVO extends PageDTO1 {
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
     * 接收人
     */
    private String[] esReceivers;
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
    private Date esSendTime;
    /**
     * 推送状态
     */
    private Integer esSendStatus;
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

    public String[] getEsReceivers() {
        return esReceivers;
    }

    public void setEsReceivers(String[] esReceivers) {
        this.esReceivers = esReceivers;
    }


    public ProSmsVO() {
    }
    public ProSmsVO(Integer id, String esTitle, String esType, String[] esReceivers, String esParam, String esContent, Date esSendTime, Integer esSendStatus, String remark, Integer esReadStatus) {
        this.id = id;
        this.esTitle = esTitle;
        this.esType = esType;
        this.esReceivers = esReceivers;
        this.esParam = esParam;
        this.esContent = esContent;
        this.esSendTime = esSendTime;
        this.esSendStatus = esSendStatus;
        this.remark = remark;
        this.esReadStatus = esReadStatus;
    }

    public ProSmsVO(Integer id, String esTitle, String esType, String esReceiver, String esParam, String esContent, Date esSendTime, Integer esSendStatus, String remark, Integer esReadStatus) {
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
        return "ProSmsVO{" +
                "id=" + id +
                ", esTitle='" + esTitle + '\'' +
                ", esType='" + esType + '\'' +
                ", esReceiver='" + esReceiver + '\'' +
                ", esReceivers=" + Arrays.toString(esReceivers) +
                ", esParam='" + esParam + '\'' +
                ", esContent='" + esContent + '\'' +
                ", esSendTime=" + esSendTime +
                ", esSendStatus=" + esSendStatus +
                ", remark='" + remark + '\'' +
                ", esReadStatus=" + esReadStatus +
                '}';
    }


    public static final class Builder {
        private Integer id;
        private String esTitle;
        private String esType;
        private String esReceiver;
        private String[] esReceivers;
        private String esParam;
        private String esContent;
        private Date esSendTime;
        private Integer esSendStatus;
        private String remark;
        private Integer esReadStatus;
        private Integer pageNo;
        private Integer pageSize;
        private Integer total;
        private String sort;

        private Builder() {
        }

        public static Builder aProSmsVO() {
            return new Builder();
        }

        public Builder withId(Integer id) {
            this.id = id;
            return this;
        }

        public Builder withEsTitle(String esTitle) {
            this.esTitle = esTitle;
            return this;
        }

        public Builder withEsType(String esType) {
            this.esType = esType;
            return this;
        }

        public Builder withEsReceiver(String esReceiver) {
            this.esReceiver = esReceiver;
            return this;
        }

        public Builder withEsReceivers(String[] esReceivers) {
            this.esReceivers = esReceivers;
            return this;
        }

        public Builder withEsParam(String esParam) {
            this.esParam = esParam;
            return this;
        }

        public Builder withEsContent(String esContent) {
            this.esContent = esContent;
            return this;
        }

        public Builder withEsSendTime(Date esSendTime) {
            this.esSendTime = esSendTime;
            return this;
        }

        public Builder withEsSendStatus(Integer esSendStatus) {
            this.esSendStatus = esSendStatus;
            return this;
        }

        public Builder withRemark(String remark) {
            this.remark = remark;
            return this;
        }

        public Builder withEsReadStatus(Integer esReadStatus) {
            this.esReadStatus = esReadStatus;
            return this;
        }

        public Builder withPageNo(Integer pageNo) {
            this.pageNo = pageNo;
            return this;
        }

        public Builder withPageSize(Integer pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public Builder withTotal(Integer total) {
            this.total = total;
            return this;
        }

        public Builder withSort(String sort) {
            this.sort = sort;
            return this;
        }

        public ProSmsVO build() {
            ProSmsVO proSmsVO = new ProSmsVO();
            proSmsVO.setId(id);
            proSmsVO.setEsTitle(esTitle);
            proSmsVO.setEsType(esType);
            proSmsVO.setEsReceiver(esReceiver);
            proSmsVO.setEsReceivers(esReceivers);
            proSmsVO.setEsParam(esParam);
            proSmsVO.setEsContent(esContent);
            proSmsVO.setEsSendTime(esSendTime);
            proSmsVO.setEsSendStatus(esSendStatus);
            proSmsVO.setRemark(remark);
            proSmsVO.setEsReadStatus(esReadStatus);
            proSmsVO.setPageNo(pageNo);
            proSmsVO.setPageSize(pageSize);
            return proSmsVO;
        }
    }
}

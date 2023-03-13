package com.yanhuanxy.multifundomain.excelupload.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 文件上传配置表
 * 多表多数据上传主配置表
 * @date 2021/1/19
 */
public class UploadMoreDataMain implements Serializable {

    private static final long serialVersionUID = -1549904062393717920L;
    /**
     * 编号
     */
    private Long mainId;

    /**
     * 配置模板名称
     */
    private String mainTemplateName;

    /**
     * 配置模板路径
     */
    private String mainTemplateUrl;

    /**
     * 上传类型，1.单文件、2.多文件同格式 3.多文件不同格式
     */
    private String mainUploadType;

    /**
     * 文件类型：excel、rar ，zip
     */
    private String mainFileType;

    /**
     * 创建人
     */
    private String mainCreatePeople;

    /**
     * 创建时间
     */
    private Date mainCreateTime;

    /**
     * 1、配置 2、外部json模板
     */
    private Integer dataConfType;

    /**
     * 手动添加上传数据时间 0 默认当前日期 1 手动指定上传数据日期
     */
    private Integer dateConfType;

    private String curUploadDate;

    public UploadMoreDataMain() {
        super();
    }

    public UploadMoreDataMain(Long mainId, String mainTemplateName, String mainTemplateUrl,
                              String mainUploadType, String mainFileType, String mainCreatePeople,
                              Date mainCreateTime) {
        this.mainId = mainId;
        this.mainTemplateName = mainTemplateName;
        this.mainTemplateUrl = mainTemplateUrl;
        this.mainUploadType = mainUploadType;
        this.mainFileType = mainFileType;
        this.mainCreatePeople = mainCreatePeople;
        this.mainCreateTime = mainCreateTime;
    }

    public static class Builder{
        /**
         * 编号
         */
        private Long mainId;

        /**
         * 配置模板名称
         */
        private String mainTemplateName;

        /**
         * 配置模板路径
         */
        private String mainTemplateUrl;

        /**
         * 上传类型，1.单文件、2.多文件同格式 3.多文件不同格式
         */
        private String mainUploadType;

        /**
         * 文件类型：excel、rar ，zip
         */
        private String mainFileType;

        /**
         * 创建人
         */
        private String mainCreatePeople;

        /**
         * 创建时间
         */
        private Date mainCreateTime;

        /**
         * 1、配置 2、外部json模板
         */
        private Integer dataConfType;

        public Builder() {
            super();
        }

        public Builder mainId(Long mainId) {
            this.mainId = mainId;
            return this;
        }

        public Builder mainTemplateName(String mainTemplateName) {
            this.mainTemplateName = mainTemplateName;
            return this;
        }

        public Builder mainTemplateUrl(String mainTemplateUrl) {
            this.mainTemplateUrl = mainTemplateUrl;
            return this;
        }

        public Builder setMainUploadType(String mainUploadType) {
            this.mainUploadType = mainUploadType;
            return this;
        }

        public Builder mainFileType(String mainFileType) {
            this.mainFileType = mainFileType;
            return this;
        }

        public Builder mainCreatePeople(String mainCreatePeople) {
            this.mainCreatePeople = mainCreatePeople;
            return this;
        }

        public Builder mainCreateTime(Date mainCreateTime) {
            this.mainCreateTime = mainCreateTime;
            return this;
        }

        public Builder dataConfType(Integer dataConfType) {
            this.dataConfType = dataConfType;
            return this;
        }

        public UploadMoreDataMain build() {
            return new UploadMoreDataMain(this);
        }
    }

    public UploadMoreDataMain(Builder builder) {
        this.mainId = builder.mainId;
        this.mainTemplateName = builder.mainTemplateName;
        this.mainTemplateUrl = builder.mainTemplateUrl;
        this.mainUploadType = builder.mainUploadType;
        this.mainFileType = builder.mainFileType;
        this.mainCreatePeople = builder.mainCreatePeople;
        this.mainCreateTime = builder.mainCreateTime;
        this.dataConfType = builder.dataConfType;
    }

    public Long getMainId() {
        return mainId;
    }

    public void setMainId(Long mainId) {
        this.mainId = mainId;
    }

    public String getMainTemplateName() {
        return mainTemplateName;
    }

    public void setMainTemplateName(String mainTemplateName) {
        this.mainTemplateName = mainTemplateName;
    }

    public String getMainTemplateUrl() {
        return mainTemplateUrl;
    }

    public void setMainTemplateUrl(String mainTemplateUrl) {
        this.mainTemplateUrl = mainTemplateUrl;
    }

    public String getMainUploadType() {
        return mainUploadType;
    }

    public void setMainUploadType(String mainUploadType) {
        this.mainUploadType = mainUploadType;
    }

    public String getMainFileType() {
        return mainFileType;
    }

    public void setMainFileType(String mainFileType) {
        this.mainFileType = mainFileType;
    }

    public String getMainCreatePeople() {
        return mainCreatePeople;
    }

    public void setMainCreatePeople(String mainCreatePeople) {
        this.mainCreatePeople = mainCreatePeople;
    }

    public Date getMainCreateTime() {
        return mainCreateTime;
    }

    public void setMainCreateTime(Date mainCreateTime) {
        this.mainCreateTime = mainCreateTime;
    }

    public Integer getDataConfType() {
        return dataConfType;
    }

    public void setDataConfType(Integer dataConfType) {
        this.dataConfType = dataConfType;
    }

    public Integer getDateConfType() {
        return dateConfType;
    }

    public void setDateConfType(Integer dateConfType) {
        this.dateConfType = dateConfType;
    }

    public String getCurUploadDate() {
        return curUploadDate;
    }

    public void setCurUploadDate(String curUploadDate) {
        this.curUploadDate = curUploadDate;
    }
}

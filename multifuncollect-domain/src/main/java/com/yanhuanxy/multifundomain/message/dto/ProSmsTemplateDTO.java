package com.yanhuanxy.multifundomain.message.dto;

import com.yanhuanxy.multifundomain.base.PageDTO1;

/**
 * @date 2021/7/28
 */

public class ProSmsTemplateDTO extends PageDTO1 {
    private Integer id;
    /**
     * 消息标题
     */
    private String templateName;
    /**
     * 模板类型（system:系统，email:邮件，message:短信,voice:语音 ）
     */
    private String templateType;
    /**
     * 消息标题
     */
    private String templateContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public ProSmsTemplateDTO() {
    }

    public ProSmsTemplateDTO(Integer id, String templateName, String templateType, String templateContent) {
        this.id = id;
        this.templateName = templateName;
        this.templateType = templateType;
        this.templateContent = templateContent;
    }

    @Override
    public String toString() {
        return "ProSmsTemplateDTO{" +
                "id=" + id +
                ", templateName='" + templateName + '\'' +
                ", templateType=" + templateType +
                ", templateContent='" + templateContent + '\'' +
                '}';
    }
}

package com.yanhuanxy.multifundomain.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.yanhuanxy.multifundomain.base.BaseEntity;

/**
 * 表单信息表
 * @date 2021/7/12
 */
@TableName("PRO_SMS_TEMPLATE")
@KeySequence(value = "SEQ_PRO_SMS_TEMPLATE")
public class ProSmsTemplate extends BaseEntity {

    @TableId(value = "ID", type = IdType.INPUT)
    private Integer id;
    /**
     * 消息标题
     */
    @TableField(value = "TEMPLATE_NAME")
    private String templateName;
    /**
     * 模板类型（system:系统，email:邮件，message:短信,voice:语音 ）
     */
    @TableField(value = "TEMPLATE_TYPE")
    private String templateType;
    /**
     * 模版内容
     */
    @TableField(value = "TEMPLATE_CONTENT")
    private String templateContent;
    /**
     * 模版测试消息 JSON格式
     */
    @TableField(value = "TEMPLATE_TEXT_JSON")
    private String templateTextJson;

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

    public String getTemplateTextJson() {
        return templateTextJson;
    }

    public void setTemplateTextJson(String templateTextJson) {
        this.templateTextJson = templateTextJson;
    }
}

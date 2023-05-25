package com.yanhuanxy.multifunservice.expression.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据矩阵 自定义公式配置表
 * @author yym
 * @date 2022/07/11
 */
public class ExpressionConfigVO implements Serializable {

    private static final long serialVersionUID = -4764231219297905435L;
    private Long id;

    /**
     * 步骤ID
     */
    private Long stepId;

    /**
     * 列名
     */
    private String columnName;

    /**
     * 列数据类型（数据库类型）
     */
    private String columnDataType;

    /**
     * 表达式（aaa+bbb-ccc+aaa-ddd）
     */
    private String expression;


    /**
     * 表达式html（aaa+bbb-ccc+aaa-ddd）
     */
    private String expressionHtml;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 创建人
     */
    private String createBy;


    public ExpressionConfigVO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnDataType() {
        return columnDataType;
    }

    public void setColumnDataType(String columnDataType) {
        this.columnDataType = columnDataType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getExpressionHtml() {
        return expressionHtml;
    }

    public void setExpressionHtml(String expressionHtml) {
        this.expressionHtml = expressionHtml;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}

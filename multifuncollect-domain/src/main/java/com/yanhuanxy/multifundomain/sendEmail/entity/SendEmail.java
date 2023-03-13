package com.yanhuanxy.multifundomain.sendEmail.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author
 * @className SendEmail
 * @description 自动发送邮件实体类
 * @date 2021/12/23
 */
@TableName("QY_AUTO_SEND_EMAIL" )
@KeySequence(value = "SEQ_QY_AUTO_SEND_EMAIL")
public class SendEmail  {

  /**
   * 主键
   */
  @TableId("ID" )
  private Long id;

  /**
   * 值
   */
  @TableField("REPORT_ID" )
  private String  reportId;

  /**
   * 状态
   */
  @TableField("STATUS")
  private Integer status;

  @TableField("BUREAUS")
  private String bureaus;

  @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
  private Date createTime;

  public SendEmail() {
  }

  public SendEmail(Long id, String reportId, Integer status, String bureaus,
      Date createTime) {
    this.id = id;
    this.reportId = reportId;
    this.status = status;
    this.bureaus = bureaus;
    this.createTime = createTime;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getReportId() {
    return reportId;
  }

  public void setReportId(String reportId) {
    this.reportId = reportId;
  }

  public String getBureaus() {
    return bureaus;
  }

  public void setBureaus(String bureaus) {
    this.bureaus = bureaus;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "SendEmail{" +
        "id=" + id +
        ", reportId='" + reportId + '\'' +
        ", status=" + status +
        ", bureaus='" + bureaus + '\'' +
        ", createTime=" + createTime +
        '}';
  }
}

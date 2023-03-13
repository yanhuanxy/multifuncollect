package com.yanhuanxy.multifundomain.sendEmail.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Description: a
 * @Author:
 * @Date: 2021/12/28 09:59
 */
@TableName("QY_OFFLINE_REPORT_EMAIL" )
@KeySequence(value = "SEQ_QY_OFFLINE_REPORT_EMAIL")
public class OfflineReportEmail {

  /**
   * 主键
   */
  @TableId("ID" )
  private Long id;

  /**
   * 离线报表ID
   */
  @TableField("OFFLINE_REPORT_ID")
  private Integer offlineReportId;

  /**
   * 邮箱
   */
  @TableField("EMAIL")
  private String email;

  @TableField("BUREAUS")
  private String bureaus;


  public OfflineReportEmail() {
  }

  public OfflineReportEmail(Long id, Integer offlineReportId, String email, String bureaus) {
    this.id = id;
    this.offlineReportId = offlineReportId;
    this.email = email;
    this.bureaus = bureaus;
  }

  public OfflineReportEmail(Long id, Integer offlineReportId, String email) {
    this.id = id;
    this.offlineReportId = offlineReportId;
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getOfflineReportId() {
    return offlineReportId;
  }

  public void setOfflineReportId(Integer offlineReportId) {
    this.offlineReportId = offlineReportId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getBureaus() {
    return bureaus;
  }

  public void setBureaus(String bureaus) {
    this.bureaus = bureaus;
  }

  @Override
  public String toString() {
    return "OfflineReportEmail{" +
        "id=" + id +
        ", offlineReportId=" + offlineReportId +
        ", email='" + email + '\'' +
        ", bureaus='" + bureaus + '\'' +
        '}';
  }
}

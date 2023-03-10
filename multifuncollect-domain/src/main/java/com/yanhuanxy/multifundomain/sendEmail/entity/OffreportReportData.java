package com.yanhuanxy.multifundomain.sendEmail.entity;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @Description: a
 * @Author:
 * @Date: 2021/12/27 17:53
 */
@TableName("QY_OFFREPORT_REPORTDATA" )
@KeySequence(value = "SEQ_QY_OFFREPORT_REPORTDATA")
public class OffreportReportData {


  /**
   * 主键
   */
  @TableId("ID" )
  private Long id;

  @TableField("REPORT_ID")
  private Integer reportId;

  @TableField("REPORT_DATA_ID")
  private Integer reportDataId;

  @TableField("STATUS")
  private Integer status;

  public OffreportReportData() {
  }

  public OffreportReportData(Long id, Integer reportId, Integer reportDataId,
      Integer status) {
    this.id = id;
    this.reportId = reportId;
    this.reportDataId = reportDataId;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getReportId() {
    return reportId;
  }

  public void setReportId(Integer reportId) {
    this.reportId = reportId;
  }

  public Integer getReportDataId() {
    return reportDataId;
  }

  public void setReportDataId(Integer reportDataId) {
    this.reportDataId = reportDataId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "OffreportReportData{" +
        "id=" + id +
        ", reportId=" + reportId +
        ", reportDataId=" + reportDataId +
        ", status=" + status +
        '}';
  }
}

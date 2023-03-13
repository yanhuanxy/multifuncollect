package com.yanhuanxy.multifunexport.fileservice.dto;

import java.io.Serializable;
import java.util.Date;

public class OssFileDTO  implements Serializable {

    private static final long serialVersionUID = 14455446L;

    private Long id;

    private String bucketNameReal;

    private String fileName;

    private String fileType;

    private Double fileSize;

    private String url;

    private Date createTime;

    private String creator;

    private Date modifyTime;

    private String modifyer;

    private Integer valid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBucketNameReal() {
        return bucketNameReal;
    }

    public void setBucketNameReal(String bucketNameReal) {
        this.bucketNameReal = bucketNameReal;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyer() {
        return modifyer;
    }

    public void setModifyer(String modifyer) {
        this.modifyer = modifyer;
    }

    public Integer getValid() {
        return valid;
    }

    public void setValid(Integer valid) {
        this.valid = valid;
    }
}

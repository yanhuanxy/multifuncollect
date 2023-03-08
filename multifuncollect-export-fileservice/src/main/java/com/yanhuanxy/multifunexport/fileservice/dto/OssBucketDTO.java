package com.yanhuanxy.multifunexport.fileservice.dto;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

public class OssBucketDTO  implements Serializable {

    @Serial
    private static final long serialVersionUID = 14455447L;

    private Long id;

    private String bucketName;

    private String bucketNameReal;

    private String secretKey;

    private String bucketSize;

    private String bucketPoliy;

    private Date uploadTime;

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

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketNameReal() {
        return bucketNameReal;
    }

    public void setBucketNameReal(String bucketNameReal) {
        this.bucketNameReal = bucketNameReal;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucketSize() {
        return bucketSize;
    }

    public void setBucketSize(String bucketSize) {
        this.bucketSize = bucketSize;
    }

    public String getBucketPoliy() {
        return bucketPoliy;
    }

    public void setBucketPoliy(String bucketPoliy) {
        this.bucketPoliy = bucketPoliy;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
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

package com.yanhuanxy.multifunexport.fileservice.dto.operation;


/**
 * @author yym
 * @version 1.0
 */
public class CopyFile {

    /**
     * 文件拓展名
     */
    private String extendName;

    /**
     * 用户编码
     */
    private String userCode;

    public CopyFile() {
        super();
    }

    public String getExtendName() {
        return extendName;
    }

    public void setExtendName(String extendName) {
        this.extendName = extendName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}

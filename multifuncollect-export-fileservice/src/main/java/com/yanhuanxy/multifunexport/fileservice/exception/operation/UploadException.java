package com.yanhuanxy.multifunexport.fileservice.exception.operation;

import com.yanhuanxy.multifunexport.fileservice.exception.OssException;

/**
 * @author yym
 * @version 1.0
 */
public class UploadException extends OssException {

    public UploadException(Throwable cause) {
        super("上传出现了异常", cause);
    }

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

}

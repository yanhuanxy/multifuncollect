package com.yanhuanxy.multifunexport.fileservice.exception;

/**
 * @author yym
 * @version 1.0
 */
public class OssException extends RuntimeException {

    public OssException(Throwable cause) {
        super("统一文件操作组件出现异常", cause);
    }

    public OssException(String message) {
        super(message);
    }

    public OssException(String message, Throwable cause) {
        super(message, cause);
    }

}

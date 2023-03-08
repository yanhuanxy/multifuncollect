package com.yanhuanxy.multifunexport.fileservice.exception.operation;

import com.yanhuanxy.multifunexport.fileservice.exception.OssException;

/**
 * @author yym
 * @version 1.0
 */
public class CopyException extends OssException {

    public CopyException(Throwable cause) {
        super("创建出现了异常", cause);
    }

    public CopyException(String message) {
        super(message);
    }

    public CopyException(String message, Throwable cause) {
        super(message, cause);
    }

}

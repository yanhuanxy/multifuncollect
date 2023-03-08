package com.yanhuanxy.multifunexport.fileservice.exception.operation;

import com.yanhuanxy.multifunexport.fileservice.exception.OssException;

/**
 * @author yym
 * @version 1.0
 */
public class ReadException extends OssException {

    public ReadException(Throwable cause) {
        super("文件读取出现了异常", cause);
    }

    public ReadException(String message) {
        super(message);
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);
    }
}

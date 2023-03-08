package com.yanhuanxy.multifunexport.fileservice.exception.operation;

import com.yanhuanxy.multifunexport.fileservice.exception.OssException;

/**
 * @author yym
 * @version 1.0
 */
public class PreviewException extends OssException {

    public PreviewException(Throwable cause) {
        super("预览出现了异常", cause);
    }

    public PreviewException(String message) {
        super(message);
    }

    public PreviewException(String message, Throwable cause) {
        super(message, cause);
    }

}

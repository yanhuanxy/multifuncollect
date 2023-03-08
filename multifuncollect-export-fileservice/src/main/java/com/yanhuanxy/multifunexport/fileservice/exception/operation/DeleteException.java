package com.yanhuanxy.multifunexport.fileservice.exception.operation;

import com.yanhuanxy.multifunexport.fileservice.exception.OssException;

/**
 * @author yym
 * @version 1.0
 */
public class DeleteException extends OssException {

    public DeleteException(Throwable cause) {
        super("删除出现了异常", cause);
    }

    public DeleteException(String message) {
        super(message);
    }

    public DeleteException(String message, Throwable cause) {
        super(message, cause);
    }

}

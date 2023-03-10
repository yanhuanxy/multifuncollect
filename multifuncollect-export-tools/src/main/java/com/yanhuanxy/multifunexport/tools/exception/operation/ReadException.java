package com.yanhuanxy.multifunexport.tools.exception.operation;

import com.yanhuanxy.multifunexport.tools.exception.ToolsException;

/**
 * @author yym
 * @version 1.0
 */
public class ReadException extends ToolsException {

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

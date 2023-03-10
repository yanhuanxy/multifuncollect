package com.yanhuanxy.multifunexport.tools.exception;

/**
 * @author yym
 * @version 1.0
 */
public class ToolsException extends RuntimeException {

    public ToolsException(Throwable cause) {
        super("统一文件操作组件出现异常", cause);
    }

    public ToolsException(String message) {
        super(message);
    }

    public ToolsException(String message, Throwable cause) {
        super(message, cause);
    }

}

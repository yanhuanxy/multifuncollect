package com.yanhuanxy.multifunexport.tools.exception.origin;

/**
 * @author yym
 * @version 1.0
 */
public class BaseException extends RuntimeException {

    public BaseException(Throwable cause) {
        super("数据源操作组件出现异常", cause);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

}

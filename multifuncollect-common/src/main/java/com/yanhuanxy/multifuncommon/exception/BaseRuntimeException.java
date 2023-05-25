package com.yanhuanxy.multifuncommon.exception;


import com.yanhuanxy.multifuncommon.enums.BaseExceptionEnums;

/**
 * 自定义运行时异常类
 */
public class BaseRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private BaseExceptionEnums baseExceptionEnums;

    public BaseRuntimeException() {
        super();
    }

    public BaseRuntimeException(String message) {
        super(message);
    }

    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseRuntimeException(BaseExceptionEnums baseExceptionEnums) {
        this.baseExceptionEnums = baseExceptionEnums;
    }

    /**
     * get/set异常枚举
     *
     * @return
     */
    public BaseExceptionEnums getBaseExceptionEnums() {
        return baseExceptionEnums;
    }

    public void setBaseExceptionEnums(BaseExceptionEnums baseExceptionEnums) {
        this.baseExceptionEnums = baseExceptionEnums;
    }
}

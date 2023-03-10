package com.yanhuanxy.multifunexport.tools.exception.origin;

import com.yanhuanxy.multifunexport.tools.constant.origin.ErrorCode;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

/**
 * 运行时异常类
 * @author yym
 * @date 2020/08/27
 */
public class DbException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    private ErrorCode errorCode;

    public DbException(ErrorCode errorCode, String errorMessage) {
        super(errorCode.toString() + " - " + errorMessage);
        this.errorCode = errorCode;
    }

    private DbException(ErrorCode errorCode, String errorMessage, Throwable cause) {
        super(errorCode.toString() + " - " + getMessage(errorMessage) + " - " + getMessage(cause), cause);

        this.errorCode = errorCode;
    }

    public static DbException asDbException(ErrorCode errorCode, String message) {
        return new DbException(errorCode, message);
    }

    public static DbException asDbException(ErrorCode errorCode, String message, Throwable cause) {
        if (cause instanceof DbException) {
            return (DbException) cause;
        }
        return new DbException(errorCode, message, cause);
    }

    public static DbException asDbException(ErrorCode errorCode, Throwable cause) {
        if (cause instanceof DbException) {
            return (DbException) cause;
        }
        return new DbException(errorCode, getMessage(cause), cause);
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    private static String getMessage(Object obj) {
        if (obj == null) {
            return "";
        }

        if (obj instanceof Throwable) {
            StringWriter str = new StringWriter();
            PrintWriter pw = new PrintWriter(str);
            ((Throwable) obj).printStackTrace(pw);
            return str.toString();
        } else {
            return obj.toString();
        }
    }
}

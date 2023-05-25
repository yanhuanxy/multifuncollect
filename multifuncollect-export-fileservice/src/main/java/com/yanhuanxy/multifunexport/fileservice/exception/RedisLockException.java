package com.yanhuanxy.multifunexport.fileservice.exception;

/**
 * @author yym
 * @version 1.0
 */
public class RedisLockException extends RuntimeException {

    public RedisLockException(Throwable cause) {
        super("Redis锁出现异常", cause);
    }

    public RedisLockException(String message) {
        super(message);
    }

    public RedisLockException(String message, Throwable cause) {
        super(message, cause);
    }

}

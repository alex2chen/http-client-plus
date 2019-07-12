package com.github.middleware.httpadapter.core.error;

/**
 * @Author: alex
 * @Description: 执行请求失败
 * @Date: created in 2018/1/4.
 */
public class FailRequestException extends RuntimeException{
    public FailRequestException(String message) {
        super(message);
    }

    public FailRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailRequestException(Throwable cause) {
        super(cause);
    }
}

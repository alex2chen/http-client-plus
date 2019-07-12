package com.github.middleware.httpadapter.core.error;

/**
 * @Author: alex
 * @Description: 请求被拦截
 * @Date: created in 2018/1/3.
 */
public class RefuseRequestException extends RuntimeException {
    public RefuseRequestException(String message) {
        super(message);
    }
}

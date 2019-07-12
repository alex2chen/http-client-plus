package com.github.middleware.httpadapter.core.client;

/**
 * @param <T> input type
 * @param <R> return type
 * @Author: alex
 * @Description: 响应处理器
 * @Date: created in 2018/1/30.
 */
public interface AfterResponseHandler<T, R> {
    R afterResponse(T input);
}

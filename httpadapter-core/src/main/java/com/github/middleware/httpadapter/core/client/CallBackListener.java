package com.github.middleware.httpadapter.core.client;


/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/3.
 */
public interface CallBackListener<P, V> {
    void onSuccess(P param, V result);

    void onFailure(P param, Throwable t);
}

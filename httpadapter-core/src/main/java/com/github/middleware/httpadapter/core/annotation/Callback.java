package com.github.middleware.httpadapter.core.annotation;

import com.github.middleware.httpadapter.core.client.CallBackListener;

import java.lang.annotation.*;


/**
 * @Author: alex
 * @Description: 异步接口回调方法，仅在返回为void的时候生效，用此注解标识的void方法，默认为异步方法
 * @Date: created in 2018/1/12.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Callback {
    /**
     * 回调
     *
     * @return
     */
    Class<? extends CallBackListener> value();

    /**
     * 回调时参数索引（request arg）
     *
     * @return
     */
    int callBackArgIndex() default 0;
}

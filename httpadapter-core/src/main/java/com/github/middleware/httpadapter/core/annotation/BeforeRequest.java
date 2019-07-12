package com.github.middleware.httpadapter.core.annotation;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.client.support.DoNothingRequestHandle;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description: 请求前置方法
 * @Date: created in 2018/1/22.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeforeRequest {
    Class<? extends BeforeRequestHandler>[] beforeMethods() default DoNothingRequestHandle.class;
}

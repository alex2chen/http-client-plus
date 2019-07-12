package com.github.middleware.httpadapter.core.annotation;

import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.client.support.DoNothingResponseHandle;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description: 响应后置方法
 * @Date: created in 2018/1/22.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterResponse {
    /**
     * 类上的和方法上的会合并
     */
    Class<? extends ResponseHandleChain>[] afterMethods() default DoNothingResponseHandle.class;

    /**
     * 用于方法覆盖类的属性，当方法上有该属性时，无视类上和方法上的afterMethods
     *
     * @return
     */
    Class<? extends ResponseHandleChain>[] overrideMethods() default DoNothingResponseHandle.class;
}

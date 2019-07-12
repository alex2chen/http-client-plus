package com.github.middleware.httpadapter.core.annotation;

import com.github.middleware.httpadapter.core.HttpMethod;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Request {
    /**
     * ESB编号，或者HTTP URL
     *
     * @return
     */
    String url();

    /**
     * 执行器的BeanName
     *
     * @return
     */
    String requestExecutor() default "esbClient";

    /**
     * HTTP请求的方式
     *
     * @return
     */
    HttpMethod httpMethod() default HttpMethod.GET;

    /**
     * 是否打印日志
     *
     * @return
     */
    boolean isLogTrace() default true;

    /**
     * 日志tag
     *
     * @return
     */
    String logTag() default "";

    /**
     * HTTP请求参数
     *
     * @return
     */
    String charset() default "UTF-8";

    /**
     * HTTP请求参数：超时时间
     *
     * @return
     */
    int timeout() default -1;
}

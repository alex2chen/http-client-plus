package com.github.middleware.httpadapter.core.annotation;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface KxRequestParam {
    String value();//请求参数名
}

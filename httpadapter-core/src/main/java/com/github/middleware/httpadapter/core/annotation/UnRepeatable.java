package com.github.middleware.httpadapter.core.annotation;

import java.lang.annotation.*;


/**
 * @Author: alex
 * @Description: 阻止重复提交
 * @Date: created in 2018/1/25.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UnRepeatable {
    /**
     * 定义判断重复提交参数索引
     *
     * @return
     */
    int[] value() default {};
}

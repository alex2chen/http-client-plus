package com.github.middleware.httpadapter.core.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/4.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface KxRequestAgent {

    String name() default "CommonAgent";

}

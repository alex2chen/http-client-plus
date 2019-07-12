package com.github.middleware.httpadapter.spring;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/8.
 */
@Deprecated
public class SpringContextHoder implements ApplicationContextAware, InitializingBean {
    private ApplicationContext appContext;

    public <T> T getBean(Class<T> requireType) {
        return appContext.getBean(requireType);
    }

    @Override
    public void afterPropertiesSet() {
        //todo
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        appContext = applicationContext;
    }
}

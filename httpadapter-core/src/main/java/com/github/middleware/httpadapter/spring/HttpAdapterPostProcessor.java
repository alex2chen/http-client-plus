package com.github.middleware.httpadapter.spring;

import com.github.middleware.httpadapter.core.server.StartConfig;
import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/9.
 */
public class HttpAdapterPostProcessor implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;
    private Class<? extends Annotation> annotationClz;
    private String[] basePackages;
    private List<Class> driverDelegates;

    private StartConfig startConfig;

    public void setStartConfig(StartConfig startConfig) {
        this.startConfig = startConfig;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) {
        // 执行器Map
        Map<String, Object> executorDelegates = Maps.newConcurrentMap();
        for (Class clz : driverDelegates) {
            executorDelegates.putAll(applicationContext.getBeansOfType(clz));
        }
        HttpAdapterScanner scanner = new HttpAdapterScanner(beanDefinitionRegistry, this.annotationClz, executorDelegates, this.startConfig);
        scanner.registerFilters();
        scanner.doScan(this.basePackages);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setAnnotationClz(Class<? extends Annotation> annotationClz) {
        this.annotationClz = annotationClz;
    }

    public void setDriverDelegates(List<Class> driverDelegates) {
        this.driverDelegates = driverDelegates;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(startConfig, "startConfig is required.");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) {
        //todo
    }
}

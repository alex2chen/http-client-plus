package com.github.middleware.httpadapter.spring;

import com.github.middleware.httpadapter.core.error.ReadMetaException;
import com.github.middleware.httpadapter.core.server.AgentGenerator;
import com.github.middleware.httpadapter.core.server.StartConfig;
import com.github.middleware.httpadapter.core.server.support.AgentGeneratorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public class HttpAdapterScanner extends ClassPathBeanDefinitionScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpAdapterScanner.class);
    private Class<? extends Annotation> annotationClz;
    private Map<String, Object> executorClients;
    private StartConfig startConfig;

    public HttpAdapterScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotationClz, Map<String, Object> executorClients, StartConfig startConfig) {
        super(registry, false);
        this.annotationClz = annotationClz;
        this.executorClients = executorClients;
        this.startConfig = startConfig;
    }

    public void registerFilters() {
        boolean isAcceptAllInterface = true;
        if (this.annotationClz != null) {
            addIncludeFilter(new AnnotationTypeFilter(this.annotationClz));
            isAcceptAllInterface = false;
        }
        if (isAcceptAllInterface) {
            addIncludeFilter(new TypeFilter() {
                @Override
                public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
                    return true;
                }
            });
        }
    }

    @Override
    protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitionHolders = super.doScan(basePackages);
        if (beanDefinitionHolders == null || beanDefinitionHolders.isEmpty()) {
            String logPKG = Arrays.toString(basePackages);
            LOGGER.warn("http agent is not found in '{}'.", logPKG);
        } else {
            AgentGeneratorBuilder builder = AgentGeneratorBuilder.newBuilder(this.startConfig);
            try {
                builder.setExecutorClients(executorClients);
                for (BeanDefinitionHolder beanDefinition : beanDefinitionHolders) {
                    GenericBeanDefinition agentBean = (GenericBeanDefinition) beanDefinition.getBeanDefinition();
                    Class<?> agentClz = Class.forName(agentBean.getBeanClassName());
                    builder.parse(agentClz);
                    agentBean.setBeanClass(HttpAdapterFactoryBean.class);
                    agentBean.getConstructorArgumentValues().addIndexedArgumentValue(0, agentClz);
                }
                AgentGenerator agentGenerator = builder.build();
                //避免重复创建浪费资源
                for (BeanDefinitionHolder beanDefinition : beanDefinitionHolders) {
                    GenericBeanDefinition agentBean = (GenericBeanDefinition) beanDefinition.getBeanDefinition();
                    agentBean.getConstructorArgumentValues().addIndexedArgumentValue(1, agentGenerator);
                }
            } catch (ClassNotFoundException ex) {
                throw new ReadMetaException(ex);
            }
        }
        return beanDefinitionHolders;
    }

    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }
}

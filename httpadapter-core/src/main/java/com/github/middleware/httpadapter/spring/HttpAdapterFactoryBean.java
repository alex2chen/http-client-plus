package com.github.middleware.httpadapter.spring;

import com.github.middleware.httpadapter.core.server.AgentGenerator;
import org.springframework.beans.factory.FactoryBean;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public class HttpAdapterFactoryBean implements FactoryBean<Object> {

    private Class<?> agentClz;
    private AgentGenerator agentGenerator;

    public HttpAdapterFactoryBean(Class<?> agentClz, AgentGenerator agentGenerator) {
        this.agentClz = agentClz;
        this.agentGenerator = agentGenerator;
    }

    @Override
    public Object getObject() throws Exception {
        return agentGenerator.newAgent(agentClz);
    }

    @Override
    public Class<?> getObjectType() {
        return agentClz;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

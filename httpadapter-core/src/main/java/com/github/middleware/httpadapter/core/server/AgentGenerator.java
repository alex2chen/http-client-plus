package com.github.middleware.httpadapter.core.server;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public interface AgentGenerator {
    <T> T newAgent(Class<T> agentClz);
}

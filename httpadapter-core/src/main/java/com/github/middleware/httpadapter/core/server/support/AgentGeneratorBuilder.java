package com.github.middleware.httpadapter.core.server.support;

import com.github.middleware.httpadapter.core.HttpContentType;
import com.github.middleware.httpadapter.core.annotation.AfterResponse;
import com.github.middleware.httpadapter.core.annotation.BeforeRequest;
import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.client.support.DoNothingRequestHandle;
import com.github.middleware.httpadapter.core.client.support.DoNothingResponseHandle;
import com.github.middleware.httpadapter.core.control.ExecuteClientSelectors;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxy;
import com.github.middleware.httpadapter.core.server.AgentGenerator;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import com.github.middleware.httpadapter.core.server.StartConfig;
import com.github.middleware.httpadapter.core.util.AgentNameGenTools;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public class AgentGeneratorBuilder {
    /**
     * 执行请求的执行器
     */
    private Map<String, Object> executorClients;
    /**
     * 代理对象
     */
    private Map<String, RequestTargetProxy> requestProxys = Maps.newConcurrentMap();
    private StartConfig startConfig;

    public AgentGeneratorBuilder(StartConfig startConfig) {
        this.startConfig = startConfig;
    }

    public static AgentGeneratorBuilder newBuilder(StartConfig startConfig) {
        return new AgentGeneratorBuilder(startConfig);
    }

    public void setExecutorClients(Map<String, Object> executorClients) {
        this.executorClients = executorClients;
    }

    public AgentGeneratorBuilder parse(Class<?> agentClz) {
        Preconditions.checkNotNull(agentClz, "agentClz is required.");
        Preconditions.checkState(agentClz.isInterface(), "agentClz must be Interface type.");
        Method[] methods = agentClz.getDeclaredMethods();
        if (methods == null || methods.length == 0) {
            return this;
        }
        String methodKey;
        RequestHeader requestHeader;
        for (Method method : methods) {
            methodKey = AgentNameGenTools.generateMethodKey(method);
            requestHeader = RequestHeader.newBuilder(method)
                    .setContentType(HttpContentType.JSON)
                    .setAlias(methodKey)
                    .build();
            String requestExecutorName = requestHeader.getRequestExecutor();
            Object executorClient = executorClients.get(requestExecutorName);
            String key = ClassUtils.getShortName(executorClient.getClass());

            List<Class<? extends BeforeRequestHandler>> beforeOnClass = this.collectBeforeMethods(agentClz.getDeclaredAnnotation(BeforeRequest.class));
            List<Class<? extends BeforeRequestHandler>> beforeOnMethod = this.collectBeforeMethods(method.getDeclaredAnnotation(BeforeRequest.class));

            List<Class<? extends ResponseHandleChain>> afterOnClass = this.collectAfterMethods(agentClz.getDeclaredAnnotation(AfterResponse.class));
            List<Class<? extends ResponseHandleChain>> afterOnMethod = this.collectAfterMethods(method.getDeclaredAnnotation(AfterResponse.class));
            List<Class<? extends ResponseHandleChain>> overrideAfterOnMethod = this.collectOverrideAfterMethods(method.getDeclaredAnnotation(AfterResponse.class));

            RequestTargetProxy requestTargetProxy = ExecuteClientSelectors.select(key)
                    .clear()
                    .setRequestHeader(requestHeader)
                    .setExecutorClient(executorClient)
                    .addBeforeMethods(beforeOnClass)
                    .addBeforeMethods(beforeOnMethod)
                    .addResponseHandleMethods(afterOnClass, false)
                    .addResponseHandleMethods(afterOnMethod, false)
                    .addResponseHandleMethods(overrideAfterOnMethod, true)
                    .setStartConfig(startConfig)
                    .build();
            requestProxys.put(methodKey, requestTargetProxy);
        }
        return this;
    }

    private List<Class<? extends BeforeRequestHandler>> collectBeforeMethods(BeforeRequest beforeRequest) {
        List<Class<? extends BeforeRequestHandler>> beforeMethods = new ArrayList<>();
        Optional.ofNullable(beforeRequest).filter(x -> x != null).filter(x -> x.beforeMethods() != null).ifPresent(y -> {
            for (Class<? extends BeforeRequestHandler> item : y.beforeMethods()) {
                if (item == DoNothingRequestHandle.class || beforeMethods.contains(item)) {
                    continue;
                }
                beforeMethods.add(item);
            }
        });
        return beforeMethods;
    }

    private List<Class<? extends ResponseHandleChain>> collectAfterMethods(AfterResponse afterResponse) {
        return collectAfterMethods(afterResponse == null ? null : afterResponse.afterMethods());
    }

    private List<Class<? extends ResponseHandleChain>> collectOverrideAfterMethods(AfterResponse afterResponse) {
        return collectAfterMethods(afterResponse == null ? null : afterResponse.overrideMethods());
    }

    private List<Class<? extends ResponseHandleChain>> collectAfterMethods(Class<? extends ResponseHandleChain>[] afterMethodsOnClass) {
        List<Class<? extends ResponseHandleChain>> result = new ArrayList<>();
        if (afterMethodsOnClass == null || afterMethodsOnClass.length == 0) {
            return result;
        }
        for (Class<? extends ResponseHandleChain> item : afterMethodsOnClass) {
            if (item == DoNothingResponseHandle.class || result.contains(item)) {
                continue;
            }
            result.add(item);
        }
        return result;
    }

    public AgentGenerator build() {
        return new AgentGeneratorImpl(requestProxys);
    }
}

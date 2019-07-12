package com.github.middleware.httpadapter.core.proxy;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.proxy.support.StandardRequestTargetProxy;
import com.github.middleware.httpadapter.core.server.AfterResponseHandlerFactory;
import com.github.middleware.httpadapter.core.server.RequestExecutor;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import com.github.middleware.httpadapter.core.server.StartConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: alex
 * @Description: 单例
 * @Date: created in 2018/5/8.
 */
public abstract class RequestTargetProxyBuilder {
    /**
     * 请求头部信息
     */
    private RequestHeader requestHeader;
    /**
     * 请求执行客户端
     */
    private Object executorClient;
    /**
     * 请求前置处理
     */
    private List<Class<? extends BeforeRequestHandler>> beforeMethods;
    private List<Class<? extends ResponseHandleChain>> responseHandleMethods;
    /**
     * 以下是共享区
     */
    private RequestExecutor requestExecutor;
    private Map<String, BeforeRequestHandler> beforeHandleInstacneCache = Maps.newConcurrentMap();
    protected StartConfig startConfig;

    public RequestTargetProxyBuilder setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
        return this;
    }

    public RequestTargetProxyBuilder setExecutorClient(Object executorClient) {
        this.executorClient = executorClient;
        return this;
    }

    public RequestTargetProxyBuilder addBeforeMethods(List<Class<? extends BeforeRequestHandler>> beforeMethods) {
        if (this.beforeMethods == null) {
            this.beforeMethods = beforeMethods;
            return this;
        }
        beforeMethods.forEach(x -> {
            if (!this.beforeMethods.contains(x)) {
                this.beforeMethods.add(x);
            }
        });
        return this;
    }

    public RequestTargetProxyBuilder addResponseHandleMethods(List<Class<? extends ResponseHandleChain>> responseHandleMethods, boolean isOverride) {
        boolean needOverrite = isOverride && !responseHandleMethods.isEmpty();
        if (this.responseHandleMethods == null || needOverrite) {
            this.responseHandleMethods = responseHandleMethods;
            return this;
        }
        responseHandleMethods.forEach(x -> {
            if (!this.responseHandleMethods.contains(x)) {
                this.responseHandleMethods.add(x);
            }
        });
        return this;
    }

    public void setRequestExecutor(RequestExecutor requestExecutor) {
        this.requestExecutor = requestExecutor;
    }

    public RequestTargetProxyBuilder setStartConfig(StartConfig startConfig) {
        this.startConfig = startConfig;
        return this;
    }

    public abstract void profileSet();

    public RequestTargetProxy build() {
        List<BeforeRequestHandler> beforeHandles = Lists.newArrayList();
        String key = null;
        for (Class<? extends BeforeRequestHandler> item : beforeMethods) {
            key = item.getName();
            BeforeRequestHandler handler = beforeHandleInstacneCache.get(key);
            if (handler == null) {
                handler = BeanUtils.instantiate(item);
                beforeHandleInstacneCache.putIfAbsent(key, handler);
            }
            beforeHandles.add(handler);
        }
        ResponseHandleChain responseHandleChain = AfterResponseHandlerFactory.generateHandleChain(responseHandleMethods);
        profileSet();
        return new StandardRequestTargetProxy(executorClient, requestExecutor, requestHeader, beforeHandles, responseHandleChain);
    }

    public RequestTargetProxyBuilder clear() {
        this.requestHeader = null;
        this.executorClient = null;
        this.beforeMethods = null;
        this.responseHandleMethods = null;
        return this;
    }
}
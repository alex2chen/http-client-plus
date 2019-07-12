package com.github.middleware.httpadapter.core.proxy.support;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.RequestExecutor;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import com.github.middleware.httpadapter.core.server.ResponseEntity;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description: 多例实现
 * @Date: created in 2018/4/7.
 */
public class StandardRequestTargetProxy extends AbstractRequestTargetProxy{
    /**
     * HTTP请求执行客户端
     */
    private Object httpClient;
    /**
     * HTTP请求执行器
     */
    private RequestExecutor httpRequestExecutor;

    public StandardRequestTargetProxy(Object httpClient, RequestExecutor httpRequestExecutor, RequestHeader requestHeader, List<BeforeRequestHandler> beforeMethods, ResponseHandleChain responseHandleChain) {
        super(requestHeader, beforeMethods, responseHandleChain);
        this.httpClient = httpClient;
        this.httpRequestExecutor = httpRequestExecutor;
    }
    /**
     * 同步请求
     *
     * @param requestBody
     * @return
     */
    @Override
    public Object execute(RequestHeader requestHeader, RequestBody requestBody) {
        ResponseEntity response = httpRequestExecutor.execute(httpClient, requestHeader, requestBody);
        return afterResponse(response);
    }

    /**
     * 异步请求
     *
     * @param requestBody
     * @return
     */
    @Override
    public Future<ResponseEntity> asyncExecute(RequestHeader requestHeader, RequestBody requestBody) {
        return httpRequestExecutor.asyncExecute(httpClient, requestHeader, requestBody);
    }
}

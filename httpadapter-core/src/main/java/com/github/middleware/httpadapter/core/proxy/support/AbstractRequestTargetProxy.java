package com.github.middleware.httpadapter.core.proxy.support;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.error.RefuseRequestException;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxy;
import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import com.github.middleware.httpadapter.core.server.ResponseEntity;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.JdkFutureAdapters;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/4/3.
 */
public abstract class AbstractRequestTargetProxy implements RequestTargetProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequestTargetProxy.class);
    private static final String FUTURE_GET = "get";
    /**
     * 请求头部信息
     */
    private RequestHeader requestHeader;
    /**
     * 请求前置处理
     */
    private List<BeforeRequestHandler> beforeMethods;
    /**
     * 请求后置处理
     */
    private ResponseHandleChain responseHandleChain;

    public AbstractRequestTargetProxy(RequestHeader requestHeader, List<BeforeRequestHandler> beforeMethods, ResponseHandleChain responseHandleChain) {
        this.requestHeader = requestHeader;
        this.beforeMethods = beforeMethods;
        this.responseHandleChain = responseHandleChain;
    }

    @Override
    public Object doExecute(RequestBody requestBody) {
        // 请求前置处理
        if (!this.beforeRequest(requestBody)) {
            throw new RefuseRequestException("执行操作被前置拦截!");
        }
        Object result;
        if (!requestHeader.isAsync()) {
            // 同步请求
            result = this.execute(requestHeader, requestBody);
        } else {
            // 异步请求
            Future<ResponseEntity> future = this.asyncExecute(requestHeader, requestBody);
            // 添加Future对象的代理，在执行future.get()方法时，插入后置处理
            Future resultFuture = Reflection.newProxy(Future.class, new AbstractInvocationHandler() {
                @Override
                protected Object handleInvocation(Object target, Method method, Object[] arges) throws Throwable {
                    if (!method.getName().equals(FUTURE_GET)) {
                        return method.invoke(future, arges);
                    }
                    ResponseEntity response = (ResponseEntity) method.invoke(future, arges);
                    return afterResponse(response);
                }
            });
            if (requestHeader.isFuture() && requestHeader.getCallbackMethod() == null) {
                return resultFuture;
            }
            // 添加Future对象的回调方法
            return responseListenableFuture(resultFuture, requestBody);
        }

        return result;
    }

    /**
     * 响应后置处理
     *
     * @param response
     */
    public Object afterResponse(ResponseEntity response) {
        if (responseHandleChain != null) {
            return this.responseHandleChain.afterResponse(response);
        }
        return response;
    }

    public abstract Object execute(RequestHeader requestHeader, RequestBody requestBody);

    public abstract Future<ResponseEntity> asyncExecute(RequestHeader requestHeader, RequestBody requestBody);

    /**
     * 请求前置处理
     *
     * @param requestBody
     * @return
     */
    /**
     * @param requestBody
     * @return
     */
    private boolean beforeRequest(RequestBody requestBody) {
        if (beforeMethods != null) {
            for (BeforeRequestHandler beforeMethod : beforeMethods) {
                if (!beforeMethod.beforeRequest(requestHeader, requestBody)) {
                    LOGGER.warn("{} 前置处理异常，中止执行", beforeMethod.getClass().getName());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 封装Future对象的回调方法
     *
     * @param resultFuture
     * @param requestBody
     * @return
     */
    private ListenableFuture responseListenableFuture(Future resultFuture, RequestBody requestBody) {
        ListenableFuture listenableFuture = JdkFutureAdapters.listenInPoolThread(resultFuture);
        if (requestHeader.getCallbackMethod() != null) {
            Futures.addCallback(listenableFuture, new FutureCallback<Object>() {
                @Override
                public void onSuccess(Object result) {
                    /**
                     * 可以直接取请求参数值，而没有采用Future-request建立映射
                     * 当前AbstractKxRequestProxy本身是多例的
                     */
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("requestHeader:{},requestBody:{}", requestHeader, requestBody);
                    }
                    requestHeader.getCallbackMethod().onSuccess(requestBody.getRequestArg(requestHeader.getCallBackArgIndex()), result);
                }

                @Override
                public void onFailure(Throwable t) {
                    requestHeader.getCallbackMethod().onFailure(requestBody.getRequestArg(requestHeader.getCallBackArgIndex()), t);
                }
            });
        }
        return listenableFuture;
    }
}

package com.github.middleware.httpadapter.core.server;

import com.github.middleware.httpadapter.core.HttpContentType;
import com.github.middleware.httpadapter.core.HttpMethod;
import com.github.middleware.httpadapter.core.annotation.Callback;
import com.github.middleware.httpadapter.core.annotation.Request;
import com.github.middleware.httpadapter.core.client.CallBackListener;
import com.google.common.base.Preconditions;
import com.google.common.reflect.TypeToken;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public class RequestHeader {
    private HttpContentType httpContentType;
    private RequestMapping requestMapping;
    private Type returnType;
    private String alias;
    private boolean isAsync;
    private boolean isFuture;
    private CallBackListener callbackMethod;
    private Integer callBackArgIndex;
    private boolean isOptional;

    public HttpMethod getHttpMethod() {
        return requestMapping.getHttpMethod();
    }

    public HttpContentType getHttpContentType() {
        return httpContentType;
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public String getRequestExecutor() {
        return requestMapping.getRequestExecutor();
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean isAsync() {
        return isAsync;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isFuture() {
        return isFuture;
    }

    public void setFuture(boolean future) {
        isFuture = future;
    }

    public CallBackListener getCallbackMethod() {
        return callbackMethod;
    }

    public Integer getCallBackArgIndex() {
        return callBackArgIndex;
    }

    public void setCallBackArgIndex(Integer callBackArgIndex) {
        this.callBackArgIndex = callBackArgIndex;
    }

    public void setCallbackMethod(CallBackListener callbackMethod) {
        this.callbackMethod = callbackMethod;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public void setAsync(boolean async) {
        isAsync = async;
    }

    public RequestHeader(HttpContentType httpContentType, RequestMapping requestMapping, Type returnType, String alias, CallBackListener callbackMethod, Integer callBackArgIndex) {
        this.httpContentType = httpContentType;
        this.requestMapping = requestMapping;
        this.returnType = returnType;
        this.alias = alias;
        this.callbackMethod = callbackMethod;
        this.callBackArgIndex = callBackArgIndex;
    }

    @Override
    public String toString() {
        return "RequestHeader{" +
                "httpContentType=" + httpContentType +
                ", requestMapping=" + requestMapping +
                ", returnType=" + returnType +
                ", alias='" + alias + '\'' +
                ", isAsync=" + isAsync +
                ", isFuture=" + isFuture +
                ", callbackMethod=" + callbackMethod +
                ", callBackArgIndex=" + callBackArgIndex +
                '}';
    }

    public static RequestHeaderBuilder newBuilder(Method method) {
        return new RequestHeaderBuilder(method);
    }

    public static class RequestMapping {

        private String url;
        private String requestExecutor;
        private HttpMethod httpMethod;
        private String charset;
        private int timeout;
        private boolean isLogTrace;
        private String logTag;

        public RequestMapping(Request request) {
            Preconditions.checkNotNull(request, "request is required.");
            this.url = request.url();
            this.requestExecutor = request.requestExecutor();
            this.httpMethod = request.httpMethod();
            this.charset = request.charset();
            this.timeout = request.timeout();
            this.isLogTrace = request.isLogTrace();
            this.logTag = request.logTag();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getRequestExecutor() {
            return requestExecutor;
        }

        public void setRequestExecutor(String requestExecutor) {
            this.requestExecutor = requestExecutor;
        }

        public HttpMethod getHttpMethod() {
            return httpMethod;
        }

        public void setHttpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
        }

        public String getCharset() {
            return charset;
        }

        public void setCharset(String charset) {
            this.charset = charset;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public boolean isLogTrace() {
            return isLogTrace;
        }

        public void setIsLogTrace(boolean isLogTrace) {
            this.isLogTrace = isLogTrace;
        }

        public String getLogTag() {
            return logTag;
        }

        public void setLogTag(String logTag) {
            this.logTag = logTag;
        }
    }

    public static class RequestHeaderBuilder {

        private HttpContentType contentType;
        private RequestMapping requestMapping;
        private Type returnType;
        private String alias;
        private boolean isAsync;
        private boolean isFuture;
        private boolean isOptional;
        private AtomicReference<CallBackListener> callbackMethod = new AtomicReference<>(null);
        private AtomicReference<Integer> callBackArgIndex = new AtomicReference<>();

        public RequestHeaderBuilder(Method method) {
            Preconditions.checkNotNull(method, "method is required.");
            this.requestMapping = new RequestMapping(method.getDeclaredAnnotation(Request.class));
            this.returnType = method.getGenericReturnType();
            resolverRealReturnType(method);
        }

        private void resolverRealReturnType(Method method) {
            if (void.class == returnType) {
                // 没有返回的，有@Callback注解的，为异步方法
                Callback callback = method.getDeclaredAnnotation(Callback.class);
                if (callback != null) {
                    isAsync = true;
                    callBackArgIndex.set(callback.callBackArgIndex());
                    Optional.ofNullable(callback.value()).ifPresent(y -> callbackMethod.set(BeanUtils.instantiate(y)));
                }
                return;
            }
            Class<?> orgReturnType = method.getReturnType();

            boolean needRealType = false;
            if (Future.class.isAssignableFrom(orgReturnType)) {
                isAsync = true;
                isFuture = true;
                needRealType = true;
            }
            if (ListenableFuture.class.isAssignableFrom(orgReturnType)) {
                isAsync = true;
                //严格意义上的Future
                isFuture = false;
                needRealType = true;
            }
            if (Optional.class.isAssignableFrom(orgReturnType)) {
                isOptional = true;
                needRealType = true;
            }
            findRealType(needRealType, method.getGenericReturnType());
        }

        private void findRealType(boolean needRealType, Type orgReturnType) {
            if (!needRealType) {
                return;
            }
            TypeToken typeToken = TypeToken.of(orgReturnType);
            int maxTims = 5;
            while (typeToken.getRawType().getTypeParameters().length > 0) {
                if (maxTims <= 0) {
                    break;
                }
                //向下找一层，如：Optional<Order>找到Order
                typeToken = typeToken.resolveType(typeToken.getRawType().getTypeParameters()[0]);
                returnType = typeToken.getRawType();
                maxTims--;
                if (Optional.class.isAssignableFrom(typeToken.getRawType())) {
                    isOptional = true;
                } else {
                    break;
                }
            }
        }

        public RequestHeaderBuilder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public RequestHeaderBuilder setContentType(HttpContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestHeader build() {
            RequestHeader requestHeader = new RequestHeader(contentType, requestMapping, returnType, alias, callbackMethod.get(), callBackArgIndex.get());
            requestHeader.setAsync(isAsync);
            requestHeader.setFuture(isFuture);
            requestHeader.setOptional(isOptional);
            return requestHeader;
        }
    }
}

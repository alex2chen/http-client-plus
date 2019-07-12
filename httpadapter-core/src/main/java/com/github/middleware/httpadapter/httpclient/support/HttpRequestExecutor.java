package com.github.middleware.httpadapter.httpclient.support;

import com.alibaba.fastjson.JSON;
import com.github.middleware.httpadapter.core.HttpMethod;
import com.github.middleware.httpadapter.core.RequestType;
import com.github.middleware.httpadapter.core.error.FailRequestException;
import com.github.middleware.httpadapter.core.server.*;
import com.github.middleware.httpadapter.httpclient.HttpClient;
import com.github.middleware.httpadapter.httpclient.HttpStartConfig;
import com.google.common.base.Strings;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/17.
 */
public class HttpRequestExecutor implements RequestExecutor {
    private HttpStartConfig startConfig;

    @Override
    public void initWithConfig(StartConfig startConfig) {
        this.startConfig = (HttpStartConfig) startConfig;
    }

    public HttpStartConfig getStartConfig() {
        return startConfig;
    }

    /**
     * 同步请求
     *
     * @param requestExecutor
     * @param requestHeader
     * @param requestBody
     * @return
     */
    @Override
    public ResponseEntity execute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody) {
        ResponseEntity result;
        try {
            result = this.asyncExecute(requestExecutor, requestHeader, requestBody).get();
        } catch (Exception e) {
            throw new FailRequestException(e);
        }
        return result;
    }

    /**
     * 异步请求
     *
     * @param requestExecutor
     * @param requestHeader
     * @param requestBody
     * @return
     */
    @Override
    public Future<ResponseEntity> asyncExecute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody) {
        HttpClient kxHttpClient = (HttpClient) requestExecutor;
        String prefix = Strings.isNullOrEmpty(startConfig.getHttpUrlPrefix()) ? "" : startConfig.getHttpUrlPrefix();
        String url = "http://" + prefix + requestHeader.getRequestMapping().getUrl();
        Future<HttpResponse> future;
        Object params = requestBody.getPayload();
        if (requestHeader.getHttpMethod() == HttpMethod.GET) {
            // GET请求
            String paramUri = this.generateParamUri(params);
            if (paramUri != null && paramUri.length() > 0) {
                url = url.concat("?").concat(paramUri);
            }
            HttpGet httpGet = new HttpGet(url);
            future = kxHttpClient.execute(httpGet, null);
        } else {
            // POST请求
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                try {
                    StringEntity stringEntity = new StringEntity(JSON.toJSONString(params), Charset.forName("UTF-8"));
                    stringEntity.setContentEncoding("UTF-8");
                    stringEntity.setContentType("application/json");
                    httpPost.setEntity(stringEntity);
                } catch (Exception e) {
                    throw new FailRequestException(e);
                }
            }
            future = kxHttpClient.execute(httpPost, null);
        }
        return Reflection.newProxy(Future.class, new AbstractInvocationHandler() {
            @Override
            protected Object handleInvocation(Object o, Method method, Object[] objects) throws Throwable {
                if (!method.getName().equals("get")) {
                    return method.invoke(future, objects);
                }
                HttpResponse httpResponse = (HttpResponse) method.invoke(future, objects);
                return toResponse(requestHeader, httpResponse);
            }
        });
    }

    private ResponseEntity toResponse(RequestHeader requestHeader, HttpResponse httpResponse) throws IOException {
        ResponseEntity responseEntity = new ResponseEntity(requestHeader, RequestType.REST, true);
        responseEntity.setBody(httpResponse.getEntity() == null ? null : EntityUtils.toString(httpResponse.getEntity()));
        if (httpResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            responseEntity.setSuccess(false);
            responseEntity.setFormatErrorMsg(httpResponse.getStatusLine().getStatusCode(), httpResponse.getStatusLine().getReasonPhrase());
        }
        return responseEntity;
    }

    /**
     * 根据参数Map拼接get请求的URI
     *
     * @param params
     * @return
     */
    private String generateParamUri(Object params) {
        StringBuilder paramUri = new StringBuilder();
        if (params == null) {
            return paramUri.toString();
        }
        if (params instanceof Map) {
            Map<String, Object> parameters = (Map<String, Object>) params;
            int size = parameters.size();
            int temp = 0;
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                temp++;
                paramUri = paramUri.append(entry.getKey()).append("=");
                if (entry.getValue() instanceof String) {
                    paramUri = paramUri.append((String) entry.getValue());
                } else {
                    paramUri = paramUri.append(JSON.toJSONString(entry.getValue()));
                }
                if (temp < size) {
                    paramUri = paramUri.append("&");
                }
            }
        }
        return paramUri.toString();
    }
}

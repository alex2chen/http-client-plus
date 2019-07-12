package com.github.middleware.httpadapter.esb;

import com.alibaba.fastjson.JSON;
import com.gillion.esb.api.client.ESBClient;
import com.gillion.esb.api.response.Response;
import com.gillion.esb.api.response.ResponseHeader;
import com.github.middleware.httpadapter.core.RequestType;
import com.github.middleware.httpadapter.core.server.*;
import com.google.common.base.Preconditions;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description: 单例
 * @Date: created in 2018/1/3.
 */
public class EsbRequestExecutor implements RequestExecutor {
    private EsbStartConfig startConfig;

    @Override
    public void initWithConfig(StartConfig startConfig) {
        this.startConfig = (EsbStartConfig) startConfig;
    }

    public EsbStartConfig getStartConfig() {
        return startConfig;
    }

    @Override
    public ResponseEntity execute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody) {
        Preconditions.checkNotNull(requestHeader);
        ESBClient esbClient = (ESBClient) requestExecutor;
        Response response = esbClient.request(requestHeader.getRequestMapping().getUrl(), requestBody.getPayload());
        return toResponse(requestHeader, response);
    }

    private ResponseEntity toResponse(RequestHeader requestHeader, Response response) {
        ResponseEntity responseEntity = new ResponseEntity(requestHeader, RequestType.ESB, true);
        responseEntity.setBody(response.getBody() == null ? null : JSON.toJSONString(response.getBody()));
        if (response.getHeader().getReturnCode() != ResponseHeader.DEFAULT_SUCCESS_CODE) {
            responseEntity.setSuccess(false);
            responseEntity.setFormatErrorMsg(response.getHeader().getReturnCode(), response.getHeader().getReturnMessage());
        }
        return responseEntity;
    }

    @Override
    public Future<ResponseEntity> asyncExecute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody) {
        Preconditions.checkNotNull(requestHeader);
        // 发起ESB异步请求，
        return this.startConfig.getPoolTaskExecutor().submit(new Callable<ResponseEntity>() {
            @Override
            public ResponseEntity call() throws Exception {
                Response response = ((ESBClient) requestExecutor).request(requestHeader.getRequestMapping().getUrl(), requestBody.getPayload());
                return toResponse(requestHeader, response);
            }
        });
    }
}

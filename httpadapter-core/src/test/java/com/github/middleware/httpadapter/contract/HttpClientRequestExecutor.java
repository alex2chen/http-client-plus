package com.github.middleware.httpadapter.contract;

import com.github.middleware.httpadapter.core.HttpMethod;
import com.github.middleware.httpadapter.core.annotation.AfterResponse;
import com.github.middleware.httpadapter.core.annotation.KxRequestAgent;
import com.github.middleware.httpadapter.core.annotation.KxRequestParam;
import com.github.middleware.httpadapter.core.annotation.Request;
import com.github.middleware.httpadapter.core.client.support.DefaultGenericTypeResponseHandler;
import com.github.middleware.httpadapter.core.client.support.DefaultStatusCheckResponseHandle;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/8.
 */
@KxRequestAgent
@AfterResponse(afterMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class})
public interface HttpClientRequestExecutor {
    @Request(url = "xxx.com:8848/omsConfig/setConfig", requestExecutor = "defaultHttpClient")
    Object httpGetTest(@KxRequestParam(value = "key") String key, @KxRequestParam(value = "value") Integer value);

    @Request(url = "xxx.com:8848/basTransRoute/enable", requestExecutor = "defaultHttpClient", httpMethod = HttpMethod.POST)
    String httpPostTest(@KxRequestParam(value = "transRouteIds") List<String> transRouteIds);
}

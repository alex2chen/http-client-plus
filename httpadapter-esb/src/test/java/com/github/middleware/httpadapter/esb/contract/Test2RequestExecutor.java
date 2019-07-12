package com.github.middleware.httpadapter.esb.contract;

import com.github.middleware.httpadapter.core.HttpMethod;
import com.github.middleware.httpadapter.esb.dto.CompanyInfo;
import com.github.middleware.httpadapter.esb.handle.TestAfterResponse;
import com.github.middleware.httpadapter.esb.handle.TestBeforeRequest;
import com.github.middleware.httpadapter.esb.handle.TestCompanyInfoAfterResponse;
import com.github.middleware.httpadapter.core.annotation.*;
import com.github.middleware.httpadapter.core.client.support.DefaultGenericTypeResponseHandler;
import com.github.middleware.httpadapter.core.client.support.DefaultStatusCheckResponseHandle;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/1.
 */
@KxRequestAgent
@BeforeRequest(beforeMethods = TestBeforeRequest.class)
@AfterResponse(afterMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class})
public interface Test2RequestExecutor {

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(afterMethods = TestAfterResponse.class)
    CompanyInfo getCommpany(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(afterMethods = TestCompanyInfoAfterResponse.class)
    CompanyInfo getCommpany2(@KxRequestParam("orgId") String orgId);

    @Request(url = "xxx.com:8848/basTransRoute/enable", requestExecutor = "defaultHttpClient", httpMethod = HttpMethod.POST)
    String httpPostTest(@KxRequestParam(value = "transRouteIds") List<String> transRouteIds);
}

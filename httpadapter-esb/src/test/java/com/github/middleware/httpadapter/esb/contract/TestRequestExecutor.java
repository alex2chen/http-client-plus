package com.github.middleware.httpadapter.esb.contract;

import com.github.middleware.httpadapter.esb.dto.CompanyInfo;
import com.github.middleware.httpadapter.esb.dto.ServiceRegionInfo;
import com.github.middleware.httpadapter.esb.handle.TestAfterResponse;
import com.github.middleware.httpadapter.esb.handle.TestBeforeRequest;
import com.github.middleware.httpadapter.esb.handle.TestCallBack;
import com.github.middleware.httpadapter.esb.handle.TestCompanyInfoAfterResponse;
import com.github.middleware.httpadapter.core.annotation.*;
import com.github.middleware.httpadapter.core.client.support.DefaultGenericTypeResponseHandler;
import com.github.middleware.httpadapter.core.client.support.DefaultStatusCheckResponseHandle;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/1.
 */
@KxRequestAgent
@BeforeRequest(beforeMethods = TestBeforeRequest.class)
@AfterResponse(afterMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class})
public interface TestRequestExecutor {

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(afterMethods = TestCompanyInfoAfterResponse.class)
    CompanyInfo requestParamTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    @AfterResponse(overrideMethods = {DefaultStatusCheckResponseHandle.class, DefaultGenericTypeResponseHandler.class, TestAfterResponse.class})
    CompanyInfo requestBodyTest(Map<String, String> param);

    @Request(url = "MSHIP_S_00157")
    CompanyInfo mapTest(Map<String, String> param);

    @Request(url = "MSHIP_S_00157")
    String stringTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    void voidTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    List<ServiceRegionInfo> listTest(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    Future<CompanyInfo> asyncFutureTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    ListenableFuture<List<ServiceRegionInfo>> asyncFutureListTest(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    @UnRepeatable(0)
    CompanyInfo lockTest(@KxRequestParam("orgId") String orgId);

    @Request(url = "MSHIP_S_00157")
    Optional<CompanyInfo> requestParamTest2(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    Optional<List<ServiceRegionInfo>> listTest2(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    Future<Optional<CompanyInfo>> asyncFutureTest2(@KxRequestParam("orgId") String orgId);

    @Request(url = "OMS_S_00087")
    ListenableFuture<Optional<List<ServiceRegionInfo>>> asyncFutureListTest2(@KxRequestParam("serverResgionIds") List<String> regionIds);

    @Request(url = "MSHIP_S_00157")
    @Callback(TestCallBack.class)
    void callBackTest(@KxRequestParam("orgId") String orgId);
}

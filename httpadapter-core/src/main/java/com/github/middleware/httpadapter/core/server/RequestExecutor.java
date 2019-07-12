package com.github.middleware.httpadapter.core.server;

import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public interface RequestExecutor {
    /**
     * 初始化配置
     * todo：StartConfig应该在executorClient组件中，but由于业务原因，没有统一接口契约
     *
     * @param startConfig
     */
    void initWithConfig(StartConfig startConfig);

    /**
     * 同步请求
     *
     * @param requestExecutor
     * @param requestHeader
     * @param requestBody
     * @return
     */
    ResponseEntity execute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody);

    /**
     * 异步请求
     *
     * @param requestExecutor
     * @param requestHeader
     * @param requestBody
     * @return
     */
    Future<ResponseEntity> asyncExecute(Object requestExecutor, RequestHeader requestHeader, RequestBody requestBody);

}

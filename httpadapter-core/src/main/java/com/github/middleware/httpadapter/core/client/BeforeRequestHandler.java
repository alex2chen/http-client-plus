package com.github.middleware.httpadapter.core.client;

import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.RequestHeader;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/22.
 */
public interface BeforeRequestHandler {

    boolean beforeRequest(RequestHeader requestHeader, RequestBody requestBody);

}

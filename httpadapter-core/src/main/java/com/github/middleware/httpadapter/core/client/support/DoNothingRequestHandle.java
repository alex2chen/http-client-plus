package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.RequestHeader;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/29.
 */
public class DoNothingRequestHandle implements BeforeRequestHandler {

    @Override
    public boolean beforeRequest(RequestHeader requestHeader, RequestBody requestBody) {
        return true;
    }
}

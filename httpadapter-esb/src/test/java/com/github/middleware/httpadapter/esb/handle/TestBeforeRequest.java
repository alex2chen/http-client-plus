package com.github.middleware.httpadapter.esb.handle;

import com.github.middleware.httpadapter.core.client.BeforeRequestHandler;
import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class TestBeforeRequest implements BeforeRequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestBeforeRequest.class);

    @Override
    public boolean beforeRequest(RequestHeader requestHeader, RequestBody requestBody) {
        String url = requestHeader.getRequestMapping().getUrl();
        LOGGER.info("Before Request [{}] Method executing......",url);
        return true;
    }
}

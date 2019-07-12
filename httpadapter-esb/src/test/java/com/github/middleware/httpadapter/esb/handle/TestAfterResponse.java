package com.github.middleware.httpadapter.esb.handle;

import com.github.middleware.httpadapter.core.client.support.CustomLevelAbstractResponseHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class TestAfterResponse extends CustomLevelAbstractResponseHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestAfterResponse.class);

    @Override
    public Object doAfterResponse(Object httpResponse) {
        LOGGER.info("Executing After Response For Object：{}",httpResponse);
        return httpResponse;
    }
}

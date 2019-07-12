package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.client.HighLevelResponseHandle;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.server.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/24.
 */
public abstract class HighLevelAbstractResponseHandle implements HighLevelResponseHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighLevelAbstractResponseHandle.class);
    private ResponseHandleChain nextHandle;

    @Override
    public boolean check(ResponseEntity input) {
        ResponseLevel level = this.getLevel();
        return level == ResponseLevel.HIGH;
    }

    @Override
    public void setNextHandle(ResponseHandleChain nextHandle) {
        this.nextHandle = nextHandle;
    }

    @Override
    public Object afterResponse(ResponseEntity input) {
        if (!this.check(input)) {
            LOGGER.error("错误的链路，中止结果");
        }
        this.doAfterResponse(input);
        if (nextHandle != null) {
            return nextHandle.afterResponse(input);
        }
        return input;
    }

    public abstract void doAfterResponse(ResponseEntity responseEntity);
}

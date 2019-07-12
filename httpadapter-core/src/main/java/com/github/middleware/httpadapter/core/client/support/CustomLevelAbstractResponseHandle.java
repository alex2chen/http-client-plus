package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.client.CustomLevelResponseHandle;
import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/24.
 */
public abstract class CustomLevelAbstractResponseHandle<T> implements CustomLevelResponseHandle<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLevelAbstractResponseHandle.class);
    private ResponseHandleChain<T, T> nextHandle;

    @Override
    public boolean check(T input) {
        if (this.getLevel() != ResponseLevel.CUSTOM) {
            return false;
        }
        return checkNext(nextHandle);
    }

    @Override
    public void setNextHandle(ResponseHandleChain nextHandle) {
        this.nextHandle = nextHandle;
    }

    @Override
    public T afterResponse(T input) {
        if (!this.check(input)) {
            LOGGER.error("错误的链路，中止结果");
        }
        T afterResponse = doAfterResponse(input);
        if (nextHandle != null) {
            return nextHandle.afterResponse(afterResponse);
        }
        return afterResponse;
    }

    public abstract T doAfterResponse(T httpResponse);
}

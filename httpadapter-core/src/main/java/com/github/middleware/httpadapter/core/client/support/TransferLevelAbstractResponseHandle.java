package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.client.TransferLevelResponseHandler;
import com.github.middleware.httpadapter.core.server.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/30.
 */
public abstract class TransferLevelAbstractResponseHandle implements TransferLevelResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferLevelAbstractResponseHandle.class);
    private ResponseHandleChain nextHandle;

    /**
     * 验证执行链路的正确性
     *
     * @param input
     * @return
     */
    @Override
    public boolean check(ResponseEntity input) {
        if (this.getLevel() != ResponseLevel.TRANSFER) {
            return false;
        }
        return checkNext(nextHandle);
    }

    /**
     * 设置下一个链路节点
     *
     * @param nextHandle
     */
    @Override
    public void setNextHandle(ResponseHandleChain nextHandle) {
        this.nextHandle = nextHandle;
    }

    @Override
    public Object afterResponse(ResponseEntity input) {
        if (!this.check(input)) {
            LOGGER.error("错误的链路，中止结果");
        }
        Object response = this.doAfterResponse(input);
        if (this.nextHandle != null) {
            return nextHandle.afterResponse(response);
        }
        return response;
    }

    public abstract Object doAfterResponse(ResponseEntity responseEntity);
}

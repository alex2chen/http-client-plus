package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.error.FailRequestException;
import com.github.middleware.httpadapter.core.server.ResponseEntity;
import org.springframework.util.Assert;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/29.
 */
public class DefaultStatusCheckResponseHandle extends HighLevelAbstractResponseHandle {

    @Override
    public void doAfterResponse(ResponseEntity responseEntity) {
        Assert.notNull(responseEntity, "responseEntity is required");
        if (!responseEntity.isSuccess()) {
            throw new FailRequestException(responseEntity.getErrorMsg());
        }
    }
}

package com.github.middleware.httpadapter.core.client.support;

import com.alibaba.fastjson.JSON;
import com.github.middleware.httpadapter.core.server.RequestHeader;
import com.github.middleware.httpadapter.core.server.ResponseEntity;
import org.springframework.util.Assert;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/24.
 */
public class DefaultGenericTypeResponseHandler extends TransferLevelAbstractResponseHandle {

    @Override
    public Object doAfterResponse(ResponseEntity responseEntity) {
        Assert.notNull(responseEntity, "responseEntity is required.");
        Assert.notNull(responseEntity.getRequestHeader(), "responseEntity.RequestHeader is required.");
        RequestHeader requestHeader = responseEntity.getRequestHeader();

        if (void.class == requestHeader.getReturnType()) {
            return null;
        }
        return this.transfer(responseEntity.getBody(), requestHeader.getReturnType(), requestHeader.isOptional());
    }


    /**
     * 对象转换
     *
     * @param body
     * @param returnType
     * @param isOptional
     * @return
     */
    public <R> R transfer(String body, Type returnType, boolean isOptional) {
        if (String.class.isAssignableFrom(returnType.getClass())) {
            return isOptional ? (R) Optional.ofNullable(body) : ((R) body);
        }
        Object result = JSON.parseObject(body, returnType);
        return isOptional ? (R) Optional.ofNullable(result) : ((R) result);
    }
}

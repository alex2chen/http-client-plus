package com.github.middleware.httpadapter.core.client.support;

import com.github.middleware.httpadapter.core.client.ResponseHandleChain;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/24.
 */
public class DoNothingResponseHandle implements ResponseHandleChain<Object, Object> {

    @Override
    public ResponseLevel getLevel() {
        return null;
    }

    @Override
    public boolean check(Object input) {
        return false;
    }

    @Override
    public void setNextHandle(ResponseHandleChain nextHandle) {
        //todo
    }

    @Override
    public Object afterResponse(Object input) {
        return input;
    }
}

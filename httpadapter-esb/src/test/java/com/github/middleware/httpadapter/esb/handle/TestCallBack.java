package com.github.middleware.httpadapter.esb.handle;

import com.github.middleware.httpadapter.core.client.CallBackListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class TestCallBack implements CallBackListener<String, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestCallBack.class);

    @Override
    public void onSuccess(String param, Object result) {
        LOGGER.info("callBack.onSuccess():{}" ,param);
    }

    @Override
    public void onFailure(String param, Throwable t) {
        LOGGER.info("callBack.onFailure():{}" , t);
    }
}

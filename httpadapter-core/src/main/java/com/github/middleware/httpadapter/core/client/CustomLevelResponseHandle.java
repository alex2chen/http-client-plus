package com.github.middleware.httpadapter.core.client;

/**
 * @Author: alex
 * @Description: 客户端响应处理(反序列后)
 * @Date: created in 2018/1/24.
 */
public interface CustomLevelResponseHandle<T> extends ResponseHandleChain<T, T> {

    @Override
    default ResponseLevel getLevel() {
        return ResponseLevel.CUSTOM;
    }

}

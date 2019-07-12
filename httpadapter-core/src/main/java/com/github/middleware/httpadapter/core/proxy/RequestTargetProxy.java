package com.github.middleware.httpadapter.core.proxy;

import com.github.middleware.httpadapter.core.server.RequestBody;

/**
 * @Author: alex
 * @Description: 代理对象,抽象真实的实现
 * @Date: created in 2018/1/22.
 */
public interface RequestTargetProxy {
    Object doExecute(RequestBody requestBody);
}

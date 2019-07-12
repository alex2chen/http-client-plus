package com.github.middleware.httpadapter.core.control;

import com.github.middleware.httpadapter.core.proxy.RequestTargetProxyBuilder;

/**
 * @Author: alex
 * @Description: 执行模型选择器
 * @Date: created in 2018/5/7.
 */
public interface ExecuteClientSelector {
    RequestTargetProxyBuilder select(String key);
}

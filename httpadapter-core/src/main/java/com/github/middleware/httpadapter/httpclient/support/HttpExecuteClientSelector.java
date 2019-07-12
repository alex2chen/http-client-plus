package com.github.middleware.httpadapter.httpclient.support;

import com.github.middleware.httpadapter.core.control.ExecuteClientSelector;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxyBuilder;
import com.google.common.collect.Sets;
import org.springframework.util.ClassUtils;

import java.util.Set;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/8.
 */
public class HttpExecuteClientSelector implements ExecuteClientSelector {
    private Set<String> keys;
    private RequestTargetProxyBuilder value;

    public HttpExecuteClientSelector() {
        value = new HttpRequestTargetProxyBuilder();
        keys = Sets.newHashSet();
        keys.add(ClassUtils.getShortName(DefaultHttpClient.class));
    }

    @Override
    public RequestTargetProxyBuilder select(String key) {
        if (keys.contains(key)) {
            return value;
        }
        return null;
    }
}

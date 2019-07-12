package com.github.middleware.httpadapter.esb;

import com.gillion.esb.api.client.ESBPoolClient;
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
public class EsbExecuteClientSelector implements ExecuteClientSelector {
    private Set<String> keys;
    private RequestTargetProxyBuilder value;

    public EsbExecuteClientSelector() {
        value = new EsbRequestTargetProxyBuilder();
        keys = Sets.newHashSet();
        keys.add(ClassUtils.getShortName(ESBPoolClient.class));
    }

    @Override
    public RequestTargetProxyBuilder select(String key) {
        if (keys.contains(key)) {
            return value;
        }
        return null;
    }
}

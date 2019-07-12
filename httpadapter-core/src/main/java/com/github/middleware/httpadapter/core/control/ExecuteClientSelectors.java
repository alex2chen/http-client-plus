package com.github.middleware.httpadapter.core.control;

import com.github.middleware.httpadapter.core.ExtensionLoaders;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxyBuilder;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/8.
 */
public class ExecuteClientSelectors {
    private static List<ExecuteClientSelector> selectors;

    private ExecuteClientSelectors() {
    }

    public static RequestTargetProxyBuilder select(String key) {
        if (selectors == null) {
            init();
        }
        Optional<RequestTargetProxyBuilder> result = selectors.stream().filter(x -> x.select(key) != null).findFirst().map(x -> x.select(key));
        Preconditions.checkArgument(result.isPresent(), "ExecuteClientSelector is not such.");
        return result.get();
    }

    private static void init() {
        ExtensionLoaders.getExtensionLoader(ExecuteClientSelector.class).ifPresent(x -> selectors = x.getExtensions());
        Preconditions.checkNotNull(selectors, "ExecuteClientSelector is required.");
    }
}

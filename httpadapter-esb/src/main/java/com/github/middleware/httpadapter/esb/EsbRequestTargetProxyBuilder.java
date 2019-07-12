package com.github.middleware.httpadapter.esb;

import com.github.middleware.httpadapter.core.ExtensionLoaders;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxyBuilder;
import com.github.middleware.httpadapter.core.server.RequestExecutor;
import com.google.common.base.Preconditions;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/2/8.
 */
public class EsbRequestTargetProxyBuilder extends RequestTargetProxyBuilder {
    private AtomicReference<RequestExecutor> executor = new AtomicReference<>();

    @Override
    public void profileSet() {
        if (executor.get() == null) {
            ExtensionLoaders.getExtensionLoader(RequestExecutor.class).ifPresent(x -> x.getExtension(EsbRequestExecutor.class).ifPresent(y -> executor.set(y)));
            Preconditions.checkNotNull(executor.get(), "RequestExecutor is not such.");
            executor.get().initWithConfig(startConfig);
        }
        this.setRequestExecutor(executor.get());
    }
}

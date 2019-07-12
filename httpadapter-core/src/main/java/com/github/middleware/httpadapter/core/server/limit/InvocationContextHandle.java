package com.github.middleware.httpadapter.core.server.limit;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/25.
 */
public interface InvocationContextHandle {
    boolean set(InvocationContext invocationContext);

    void remove(InvocationContext invocationContext);
}

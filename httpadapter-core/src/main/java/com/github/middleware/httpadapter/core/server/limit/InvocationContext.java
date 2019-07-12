package com.github.middleware.httpadapter.core.server.limit;

import com.google.common.base.Objects;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/25.
 */
public class InvocationContext {
    private Method method;
    private List<Object> args;

    public InvocationContext(Method method, List<Object> args) {
        this.method = method;
        this.args = args;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method, args);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        InvocationContext other = (InvocationContext) obj;
        return Objects.equal(method, other.method) && Objects.equal(args, other.args);
    }
}

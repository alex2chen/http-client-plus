package com.github.middleware.httpadapter.core.server.support;

import com.github.middleware.httpadapter.core.annotation.UnRepeatable;
import com.github.middleware.httpadapter.core.error.RefuseRequestException;
import com.github.middleware.httpadapter.core.proxy.RequestTargetProxy;
import com.github.middleware.httpadapter.core.server.AgentGenerator;
import com.github.middleware.httpadapter.core.server.RequestBody;
import com.github.middleware.httpadapter.core.server.limit.InvocationContext;
import com.github.middleware.httpadapter.core.server.limit.InvocationContextHandle;
import com.github.middleware.httpadapter.core.util.AgentNameGenTools;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.AbstractInvocationHandler;
import com.google.common.reflect.Reflection;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/4.
 */
public class AgentGeneratorImpl implements AgentGenerator {

    private Map<String, RequestTargetProxy> requestDelegates;

    @Override
    public <T> T newAgent(Class<T> agentClz) {
        return Reflection.newProxy(agentClz, new MethodProxy(new InvocationContextHandle() {
            //保存类级别下所有方法提交参数
            private final ConcurrentMap<InvocationContext, Boolean> context = Maps.newConcurrentMap();

            @Override
            public boolean set(InvocationContext invocationContext) {
                return context.putIfAbsent(invocationContext, Boolean.TRUE) == null;
            }

            @Override
            public void remove(InvocationContext invocationContext) {
                context.remove(invocationContext);
            }
        }));
    }

    public AgentGeneratorImpl(Map<String, RequestTargetProxy> requestDelegates) {
        this.requestDelegates = requestDelegates;
    }

    /**
     * 代理
     */
    public class MethodProxy extends AbstractInvocationHandler {
        private InvocationContextHandle invocationContextHandle;

        public MethodProxy(InvocationContextHandle invocationContextHandle) {
            this.invocationContextHandle = invocationContextHandle;
        }

        @Override
        protected Object handleInvocation(Object o, Method method, Object[] args) throws Throwable {
            UnRepeatable unRepeatable = method.getAnnotation(UnRepeatable.class);
            if (unRepeatable == null) {
                return doHandleInvocation(method, args);
            }
            return refuseRepeateSubmit(unRepeatable, method, args);
        }

        /**
         * 阻止重复提交
         *
         * @param unRepeatable
         * @param method
         * @param args
         * @return
         */
        private Object refuseRepeateSubmit(UnRepeatable unRepeatable, Method method, Object[] args) {
            //TODO:异步的情况下感觉此次控制没啥意义，后续完善
            int[] indexs = unRepeatable.value();
            InvocationContext invocationContext = null;
            if (indexs == null || indexs.length == 0) {
                invocationContext = new InvocationContext(method, null);
            } else {
                List<Object> params = Lists.newArrayListWithCapacity(indexs.length);
                for (int index : indexs) {
                    params.add(args[index]);
                }
                invocationContext = new InvocationContext(method, params);
            }
            if (!invocationContextHandle.set(invocationContext)) {
                throw new RefuseRequestException("Method[" + method.toGenericString() + "] can not execute repeatble!!!");
            }
            try {
                return doHandleInvocation(method, args);
            } finally {
                invocationContextHandle.remove(invocationContext);
            }
        }

        private Object doHandleInvocation(Method method, Object[] args) {
            String methodKey = AgentNameGenTools.generateMethodKey(method);
            RequestTargetProxy requestDelegate = requestDelegates.get(methodKey);
            RequestBody requestBody = new RequestBody().resolveRequestParameter(method, args);
            return requestDelegate.doExecute(requestBody);
        }

    }

}

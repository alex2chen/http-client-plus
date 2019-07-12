package com.github.middleware.httpadapter.core.server;

import com.github.middleware.httpadapter.core.annotation.KxRequestParam;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/5.
 */
public class RequestBody {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestBody.class);
    private Object[] args;
    /**
     * obj或map类型
     */
    private Object payload;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getRequestArg(Integer index) {
        if (index == null || args == null) {
            return null;
        }
        return args[index];
    }


    /**
     * 生成请求参数
     *
     * @param method
     * @param args
     * @return
     */
    public RequestBody resolveRequestParameter(Method method, Object[] args) {
        RequestBody requestBody = new RequestBody();
        if (args == null || args.length == 0) {
            return requestBody;
        }
        requestBody.args = args;
        boolean hasParamProp = true;
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Map<String, Object> parameters = Maps.newHashMapWithExpectedSize(args.length);
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
                hasParamProp = false;
                if (annotation instanceof KxRequestParam) {
                    parameters.put(((KxRequestParam) annotation).value(), args[i]);
                }
            }
        }
        if (hasParamProp) {
            if (args.length != 1) {
                LOGGER.warn("参数传递格式有误，请检查是否缺失@KxRequestParam.");
            }
            requestBody.setPayload(args[0]);
        } else {
            requestBody.setPayload(parameters);
        }
        return requestBody;
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "args=" + Arrays.toString(args) +
                ", payload=" + payload +
                '}';
    }
}

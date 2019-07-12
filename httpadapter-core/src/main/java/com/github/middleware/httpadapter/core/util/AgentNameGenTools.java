package com.github.middleware.httpadapter.core.util;

import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/4.
 */
public class AgentNameGenTools {
    private static boolean needOptimizationMD5 = true;
    private static final Logger LOGGER = LoggerFactory.getLogger(AgentNameGenTools.class);

    private AgentNameGenTools() {
        if (LOGGER.isDebugEnabled()) {
            needOptimizationMD5 = false;
        }
    }
    /**
     * 获取后置处理链的名称
     *
     * @param classes
     * @return
     */
    public static String generateClassesKey(List<Class> classes) {
        StringBuilder name = new StringBuilder();
        for (Class item : classes) {
            name.append(item.getName()).append("-");
        }
        String input = name.toString();
        return needOptimizationMD5 ? Hashing.md5().hashBytes(input.getBytes()).toString() : input;
    }

    public static String generateMethodKey(Method method) {
        String input = toString(method);
        return needOptimizationMD5 ? Hashing.md5().hashBytes(input.getBytes()).toString() : input;
    }

    private static String toString(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getDeclaringClass().getTypeName()).append(".").append(method.getName());
        sb.append('(');
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int j = 0; j < parameterTypes.length; j++) {
            sb.append(parameterTypes[j].getTypeName());
            if (j < (parameterTypes.length - 1)) {
                sb.append(",");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}

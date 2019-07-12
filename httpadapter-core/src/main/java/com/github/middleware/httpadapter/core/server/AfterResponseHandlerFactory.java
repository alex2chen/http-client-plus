package com.github.middleware.httpadapter.core.server;

import com.github.middleware.httpadapter.core.client.ResponseHandleChain;
import com.github.middleware.httpadapter.core.client.support.DefaultGenericTypeResponseHandler;
import com.github.middleware.httpadapter.core.client.support.DefaultStatusCheckResponseHandle;
import com.github.middleware.httpadapter.core.error.ReadMetaException;
import com.github.middleware.httpadapter.core.util.AgentNameGenTools;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/24.
 */
public class AfterResponseHandlerFactory {
    private static final Map<String, ResponseHandleChain> handleChainMap = Maps.newConcurrentMap();
    /**
     * 默认先判断状态
     * 然后做类型的转换
     */
    private static final ResponseHandleChain defaultChain;

    private AfterResponseHandlerFactory() {
    }

    static {
        try {
            DefaultStatusCheckResponseHandle statuHandle = new DefaultStatusCheckResponseHandle();
            DefaultGenericTypeResponseHandler typeResponseHanle = new DefaultGenericTypeResponseHandler();
            defaultChain = generateHandleChain(null, Lists.newArrayList(statuHandle, typeResponseHanle));
        } catch (Exception ex) {
            throw new ReadMetaException(ex);
        }
    }

    /**
     * 生成后置处理链
     *
     * @param responseHandleChains
     * @return
     */
    public static ResponseHandleChain generateHandleChain(List<Class<? extends ResponseHandleChain>> responseHandleChains) {
        if (responseHandleChains == null || responseHandleChains.isEmpty()) {
            return defaultChain;
        }
        List<Class> classes = responseHandleChains.stream().collect(Collectors.toList());
        String chainName = AgentNameGenTools.generateClassesKey(classes);
        if (handleChainMap.containsKey(chainName)) {
            return handleChainMap.get(chainName);
        }
        List<ResponseHandleChain> responseHandles = Lists.newArrayList();
        for (Class<? extends ResponseHandleChain> item : responseHandleChains) {
            //暂时不用缓存
            responseHandles.add(BeanUtils.instantiate(item));
        }
        return generateHandleChain(chainName, responseHandles);
    }

    private static ResponseHandleChain generateHandleChain(String chainName, List<ResponseHandleChain> responseHandleChains) {
        if (Strings.isNullOrEmpty(chainName)) {
            List<Class> classes = responseHandleChains.stream().map(x -> x.getClass()).collect(Collectors.toList());
            chainName = AgentNameGenTools.generateClassesKey(classes);
        }
        int size = responseHandleChains.size();
        for (int i = size - 1; i >= 0; i--) {
            if (i == size - 1) {
                responseHandleChains.get(i).setNextHandle(null);
            } else {
                responseHandleChains.get(i).setNextHandle(responseHandleChains.get(i + 1));
            }
        }
        // 将结果存储在Map中，可以减少对象的创建
        handleChainMap.put(chainName, responseHandleChains.get(0));
        return responseHandleChains.get(0);
    }
}

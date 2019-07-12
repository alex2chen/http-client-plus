package com.github.middleware.httpadapter.core.client;

/**
 * @Author: alex
 * @Description: 响应处理连接点
 * @Date: created in 2018/1/24.
 */
public interface ResponseHandleChain<T, R> extends AfterResponseHandler<T, R> {

    /**
     * 获取级别
     *
     * @return
     */
    ResponseLevel getLevel();

    /**
     * 验证执行链路的正确性
     *
     * @param input
     * @return
     */
    boolean check(T input);

    /**
     * 设置下一个链路节点
     *
     * @param nextHandle
     */
    void setNextHandle(ResponseHandleChain nextHandle);

    /**
     * 链路节点类型枚举
     */
    enum ResponseLevel {
        /**
         * 基本的节点类型，为高优先级的链路处理节点类型，传递的参数类型为通用的ResponssEntity
         */
        HIGH,
        /**
         * 做中间转换的节点类型
         */
        TRANSFER,
        /**
         * 客户端自定义的节点类型，为用户自定义的链路处理节点类型，传递的参数为<泛型>，即用户自定义的类型
         */
        CUSTOM
    }
    default boolean checkNext(ResponseHandleChain nextHandle) {
        if (nextHandle == null) {
            return true;
        }
        if (nextHandle.getLevel() != ResponseLevel.CUSTOM) {
            return false;
        }
        return true;
    }
}

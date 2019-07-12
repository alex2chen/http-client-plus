package com.github.middleware.httpadapter.core.error;

/**
 * @Author: alex
 * @Description: 初始化时读取meta信息失败
 * @Date: created in 2018/2/9.
 */
public class ReadMetaException extends RuntimeException{
    public ReadMetaException(Throwable cause) {
        super(cause);
    }
}

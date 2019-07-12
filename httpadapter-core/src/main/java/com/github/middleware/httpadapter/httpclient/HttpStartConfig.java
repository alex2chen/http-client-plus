package com.github.middleware.httpadapter.httpclient;

import com.github.middleware.httpadapter.core.server.StartConfig;
import org.springframework.beans.factory.InitializingBean;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/5/8.
 */
public class HttpStartConfig  implements StartConfig,InitializingBean {
    /**
     * HTTP请求URL前缀（test-,prd-之类的多环境前缀）
     */
    private String httpUrlPrefix;

    public String getHttpUrlPrefix() {
        return httpUrlPrefix;
    }

    public void setHttpUrlPrefix(String httpUrlPrefix) {
        this.httpUrlPrefix = httpUrlPrefix;
    }

    @Override
    public void afterPropertiesSet(){
        //todo
    }
}

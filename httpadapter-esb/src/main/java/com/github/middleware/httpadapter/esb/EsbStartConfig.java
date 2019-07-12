package com.github.middleware.httpadapter.esb;

import com.github.middleware.httpadapter.httpclient.HttpStartConfig;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @Author: alex
 * @Description: 注意：继承不是最佳选择,组合复用大于继承
 * @Date: created in 2018/5/8.
 */
public class EsbStartConfig extends HttpStartConfig {

    private ThreadPoolTaskExecutor poolTaskExecutor;

    public EsbStartConfig() {
        poolTaskExecutor = new ThreadPoolTaskExecutor();
        poolTaskExecutor.setCorePoolSize(6);
        poolTaskExecutor.setKeepAliveSeconds(200);
        poolTaskExecutor.setMaxPoolSize(10);
        poolTaskExecutor.setQueueCapacity(200);
        poolTaskExecutor.setThreadNamePrefix("EsbAsyncTaskExecutor-");
        poolTaskExecutor.initialize();
    }

    public ThreadPoolTaskExecutor getPoolTaskExecutor() {
        return poolTaskExecutor;
    }
}

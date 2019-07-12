package com.github.middleware.httpadapter.httpclient.support;

import com.github.middleware.httpadapter.httpclient.HttpClient;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description: 单例，CloseableHttpAsyncClient的代理
 * @Date: created in 2018/1/19.
 */
public class DefaultHttpClient implements HttpClient, InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultHttpClient.class);
    private CloseableHttpAsyncClient httpAsyncClient;

    @Override
    public void start() {
        httpAsyncClient.start();
    }

    @Override
    public void destroy() {
        try {
            httpAsyncClient.close();
        } catch (IOException e) {
            LOGGER.error("httpAsyncClient destroy error,", e);
        }
    }

    @Override
    public boolean isRunning() {
        return httpAsyncClient.isRunning();
    }

    @Override
    public <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback) {
        return null;
    }

    @Override
    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        return httpAsyncClient.execute(target, request, context, callback);
    }

    @Override
    public Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback) {
        return httpAsyncClient.execute(target, request, callback);
    }

    @Override
    public Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
        return httpAsyncClient.execute(request, callback);
    }

    @Override
    public Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback) {
        return httpAsyncClient.execute(request, context, callback);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        httpAsyncClient = HttpAsyncClients.custom()
                .setConnectionManager(ConnectionManagerFactory.connectionManager)
                .setDefaultRequestConfig(HttpClientConfigFactory.defaultConfig)
                .build();
    }

    private static class ConnectionManagerFactory {
        private static int ioThreadCount = 20;
        /**
         * 连接池最大数量
         */
        private static int maxConnection = 100;
        /**
         * 单个路由最大连接数量
         */
        private static int defaultMaxRoute = 50;
        private static PoolingNHttpClientConnectionManager connectionManager = getConnectionManager();

        private static PoolingNHttpClientConnectionManager getConnectionManager() {
            PoolingNHttpClientConnectionManager connectionManager = null;
            try {
                IOReactorConfig ioReactorConfig = IOReactorConfig.custom().setIoThreadCount(ioThreadCount).build();
                ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);
                connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
                connectionManager.setMaxTotal(maxConnection);
                connectionManager.setDefaultMaxPerRoute(defaultMaxRoute);
            } catch (Exception e) {
                LOGGER.error("创建HTTP请求池出错：" + e.getMessage(), e);
            }
            return connectionManager;
        }
    }

    private static class HttpClientConfigFactory {
        private static int connectTimeOut = 60000;
        private static int connectionRequestTimeOut = 60000;
        private static int socketTimeOut = 60000;
        private static RequestConfig defaultConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeOut)
                .setConnectionRequestTimeout(connectionRequestTimeOut)
                .setSocketTimeout(socketTimeOut)
                .setCookieSpec(CookieSpecs.STANDARD_STRICT)
                .build();
    }
}

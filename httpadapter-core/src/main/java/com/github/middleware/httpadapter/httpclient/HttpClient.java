package com.github.middleware.httpadapter.httpclient;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.Future;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/19.
 */
public interface HttpClient {

    void start();

    void destroy();

    boolean isRunning();

    <T> Future<T> execute(HttpAsyncRequestProducer requestProducer, HttpAsyncResponseConsumer<T> responseConsumer, FutureCallback<T> callback);

    Future<HttpResponse> execute(HttpHost target, HttpRequest request, HttpContext context, FutureCallback<HttpResponse> callback);

    Future<HttpResponse> execute(HttpHost target, HttpRequest request, FutureCallback<HttpResponse> callback);

    Future<HttpResponse> execute(HttpUriRequest request, FutureCallback<HttpResponse> callback);

    Future<HttpResponse> execute(HttpUriRequest request, HttpContext context, FutureCallback<HttpResponse> callback);

}

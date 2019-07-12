package com.github.middleware.httpadapter.core;


/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/30.
 */
public enum RequestType {
    ESB("DefaultHttpClient"),
    REST("ESBPoolClient");
    private String type;

    RequestType(String type) {
        this.type = type;
    }
}

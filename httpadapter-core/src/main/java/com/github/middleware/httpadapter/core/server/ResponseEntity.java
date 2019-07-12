package com.github.middleware.httpadapter.core.server;

import com.github.middleware.httpadapter.core.RequestType;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/3.
 */
public class ResponseEntity {
    private RequestHeader requestHeader;
    private RequestType requestType;
    private boolean success;
    private String errorMsg;
    private String body;

    public ResponseEntity(RequestHeader requestHeader, RequestType requestType, boolean success) {
        this.requestHeader = requestHeader;
        this.requestType = requestType;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public void setFormatErrorMsg(int errorCode, String errorMsg) {
        this.errorMsg = String.format("发起%s请求失败,errorCode=%s, errorMsg=%s", this.getRequestType().toString(), errorCode, errorMsg);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }
}

package com.github.middleware.httpadapter.esb.dto;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class ServiceRegionInfo {
    private String serviceRegionId;
    private String regionName;

    public String getServiceRegionId() {
        return serviceRegionId;
    }

    public void setServiceRegionId(String serviceRegionId) {
        this.serviceRegionId = serviceRegionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }
}

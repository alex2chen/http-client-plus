package com.github.middleware.httpadapter.esb.dto;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2018/1/31.
 */
public class CompanyInfo {
    private String name;
    private String usersNo;
    private String mobile;
    private String createTime;
    private Byte identityType;
    private String vipUsersId;

    public Byte getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Byte identityType) {
        this.identityType = identityType;
    }

    public String getVipUsersId() {
        return vipUsersId;
    }

    public void setVipUsersId(String vipUsersId) {
        this.vipUsersId = vipUsersId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsersNo() {
        return usersNo;
    }

    public void setUsersNo(String usersNo) {
        this.usersNo = usersNo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}

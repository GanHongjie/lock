package com.yiyun.lockcontroller.bean.common;

/**
 * 用于注册的用户信息bean
 * Created by Layo on 2018-1-3.
 */

public class RegisterMsgBean {
    private String phoneNo;
    private String password;
    private String userName;
    private String userIdNo;

    public RegisterMsgBean(String phoneNo, String password, String userName, String userIdNo) {
        this.phoneNo = phoneNo;
        this.password = password;
        this.userName = userName;
        this.userIdNo = userIdNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdNo() {
        return userIdNo;
    }

    public void setUserIdNo(String userIdNo) {
        this.userIdNo = userIdNo;
    }
}

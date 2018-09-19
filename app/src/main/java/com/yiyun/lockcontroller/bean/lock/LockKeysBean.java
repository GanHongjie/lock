package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;

/**
 * 存放我的钥匙列表的
 * Created by Layo on 2017-8-15.
 */

public class LockKeysBean implements Serializable {
    private String address;   //寝室号
    private String mac;     //mac地址
    private String lockNo;     //锁编号
    private int userType;   //用户类型
    private int autoType;   //授权类型
    private long startTime; //开始时间
    private long endTime;   //结束时间
    private int count;  //剩余开锁次数

    public static final int USER_TYPE_COMMON = 1;    //普通用户
    public static final int USER_TYPE_TEPORARY = 2;   //临时用户

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public int getAutoType() {
        return autoType;
    }

    public void setAutoType(int autoType) {
        this.autoType = autoType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLockNo() {
        return lockNo;
    }

    public void setLockNo(String lockNo) {
        this.lockNo = lockNo;
    }
}

package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;

/**
 * 开锁人员的历史纪录
 * Created by Layo on 2017-7-14.
 */
public class LockOpenBean implements Serializable {
    private long date;      //开锁日期
    private int openType;   //开锁类型
    private int userType;   //开锁用户类型

    public static final int OPEN_COMMON = 1;
    public static final int OPEN_FAIL = -1;
    public static final int OPEN_TIME = 11;
    public static final int OPEN_COUNT = 12;
    public static final int OPEN_TIME_COUNT = 13;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getOpenType() {
        return openType;
    }

    public void setOpenType(int openType) {
        this.openType = openType;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "LockOpenBean{" +
                ", date=" + date +
                ", openType=" + openType +
                ", userType=" + userType +
                '}';
    }
}

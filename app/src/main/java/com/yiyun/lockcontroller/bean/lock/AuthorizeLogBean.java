package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;

/**
 * Created by Layo on 2017-7-22.
 */
//
public class AuthorizeLogBean implements Serializable {
    private int type;   //授权类型
    private long start; //授权时间起
    private long end;   //授权时间止
    private int count;  //剩余开锁次数
    private boolean isUse; //是否有效
    private int notUseReason;   //失效原因
    private String autoLockNo; //授权锁具号
    private String autoStuNo;  //授权用户学号
    private String autoUsername;//授权的账号
    private int tid;

    public static final int AUTO_STATE_TIME = 1;    //时间段授权
    public static final int AUTO_STATE_COUNT = 2;   //次数授权
    public static final int AUTO_STATE_TIME_COUNT = 3;  //时间段与次数授权

    public static final int AUTO_REASON_CANCLE = -1;    //授权被取消
    public static final int AUTO_REASON_TIME = 1;   //不在时间段
    public static final int AUTO_REASON_COUNT = 2;  //开锁次数不足

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean getIsUse() {
        return isUse;
    }

    public void setIsUse(boolean isUse) {
        this.isUse = isUse;
    }

    public int getNotUseReason() {
        return notUseReason;
    }

    public void setNotUseReason(int notUseReason) {
        this.notUseReason = notUseReason;
    }

    public String getAutoLockNo() {
        return autoLockNo;
    }

    public void setAutoLockNo(String autoLockNo) {
        this.autoLockNo = autoLockNo;
    }

    public String getAutoStuNo() {
        return autoStuNo;
    }

    public void setAutoStuNo(String autoStuNo) {
        this.autoStuNo = autoStuNo;
    }



    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getAutoUsername() {
        return autoUsername;
    }

    public void setAutoUsername(String autoUsername) {
        this.autoUsername = autoUsername;
    }

    @Override
    public String toString() {
        return "AuthorizeLogBean{" +
                "type=" + type +
                ", start=" + start +
                ", end=" + end +
                ", count=" + count +
                ", isUse=" + isUse +
                ", notUseReason=" + notUseReason +
                ", autoLockNo='" + autoLockNo + '\'' +
                ", autoStuNo='" + autoStuNo + '\'' +
                ", autoUsername='" + autoUsername + '\'' +
                ", tid=" + tid +
                '}';
    }
}

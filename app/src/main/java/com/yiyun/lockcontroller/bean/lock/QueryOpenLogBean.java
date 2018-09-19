package com.yiyun.lockcontroller.bean.lock;

/**
 * 查询开锁信息发送的bean
 * Created by Layo on 2017-7-27.
 */
public class QueryOpenLogBean {
    public static final int addRecord = 15;     //要查询增加的数目

    private int startRecord = 0;    //查询的起始条数
    private int endRecord = startRecord + addRecord;    //查询的结束条数
    private String stuNoOrName;      //申请人开锁学号
    private int openType = 0;       //开锁类型 可空
    private int userType = 0;       //用户类型
    private long start = 0;   //查询开始时间
    private long end = 0;     //查询结束时间
    private String lockNo;

    public static final int OPEN_TYPE_FAILE = -1;   //开锁失败
    public static final int OPEN_TYPE_OWNER = 1;    //自身开锁
    public static final int OPEN_TYPE_TIME = 11;    //时间段授权开锁
    public static final int OPEN_TYPE_COUNT = 12;   //次数授权开锁
    public static final int OPEN_TYPE_TIME_COUNT = 13;  //时间段与次数双层授权开锁

    public static final int USER_TYPE_COMMON = 1;   //普通用户
    public static final int USER_TYPE_ADMINI = 2;   //管理员

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
        this.endRecord = startRecord + addRecord;
    }

    public String getLockNo() {
        return lockNo;
    }

    public void setLockNo(String lockNo) {
        this.lockNo = lockNo;
    }

    public int getEndRecord() {
        return endRecord;
    }

    public void setEndRecord(int endRecord) {
        this.endRecord = endRecord;
    }

    public String getStudNoOrName() {
        return stuNoOrName;
    }

    public void setStudNoOrName(String studNo) {
        this.stuNoOrName = studNo;
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
}

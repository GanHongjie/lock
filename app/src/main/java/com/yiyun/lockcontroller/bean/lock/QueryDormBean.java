package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;

/**
 * 查询寝室锁申请情况时的bean
 * 已序列化为了传送数据
 * Created by Layo on 2017-7-12.
 */
public class QueryDormBean implements Serializable {
    private String stuNo;  //学号
    private String stuName;    //姓名
    private long date;    //操作时间
    private String reason;  //申请原因
    private String changeDep;    //申请更换的目标寝室
    private String dep;    //原寝室号
    private int state;   //申请状态
    private int tid;

    public static final int REPLACE_STATE_CANCLE = -1;  //请求已取消
    public static final int REPLACE_STATE_LOADING = 1;  //请求未审核
    public static final int REPLACE_STATE_SUCCESS = 2;  //请求审核通过
    public static final int REPLACE_STATE_FAILE = 3;    //请求审核未通过
    public static final int REPLACE_OUT_TIME = 7;    //超时时间

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String name) {
        this.stuName = stuName;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDep() {
        return dep;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public String getChangeDep() {
        return changeDep;
    }

    public void setChangeDep(String changeDep) {
        this.changeDep = changeDep;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}

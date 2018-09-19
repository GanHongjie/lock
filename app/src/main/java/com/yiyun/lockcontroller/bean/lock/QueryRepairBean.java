package com.yiyun.lockcontroller.bean.lock;

import java.io.Serializable;

/**
 * 查询报销时的状态
 * Created by Layo on 2017-7-12.
 */
public class QueryRepairBean implements Serializable {
    private int state;   //锁具维修状态
    private String reason;  //报修原因
    private String dep;    //寝室号
    private long date;    //最新操作时间
    private String stuName;    //姓名
    private String stuNo;  //学号

    public static final int REPAIR_STATE_CANCLE = -1;  //请求已取消
    public static final int REPAIR_STATE_LOADING = 1;  //请求未审核
    public static final int REPAIR_STATE_SUCCESS = 2;  //请求审核通过
    public static final int REPAIR_STATE_FAILE = 3;    //请求审核未通过
    public static final int REPAIR_OUT_TIME = 7;    //超时时间

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDep() {
        return dep;
    }

    public void setDepNo(String depNo) {
        this.dep = dep;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuNo() {
        return stuNo;
    }

    public void setStuNo(String stuNo) {
        this.stuNo = stuNo;
    }

    @Override
    public String toString() {
        return "QueryRepairBean{" +
                "state=" + state +
                ", reason='" + reason + '\'' +
                ", depNo='" + dep + '\'' +
                ", date=" + date +
                ", stuName='" + stuName + '\'' +
                ", stuNo='" + stuNo + '\'' +
                '}';
    }
}

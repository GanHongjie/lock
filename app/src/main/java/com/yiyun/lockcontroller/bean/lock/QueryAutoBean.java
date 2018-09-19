package com.yiyun.lockcontroller.bean.lock;

/**
 * 查询被授权人信息接口的bean
 * Created by Layo on 2017-8-2.
 */

public class QueryAutoBean {
    public static final int addRecord = 15;     //要查询增加的数目

    private int pattern; //请求模式
    private int startRecord = 0; //查询起始条数
    private int endRecord = startRecord + addRecord;   //查询结束条数
    private String autoStuNo;   //被授权人学号

    public int getPattern() {
        return pattern;
    }

    public void setPattern(int pattern) {
        this.pattern = pattern;
    }

    public int getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(int startRecord) {
        this.startRecord = startRecord;
        this.endRecord = startRecord + addRecord;
    }

    public int getEndRecord() {
        return endRecord;
    }

    public void setEndRecord(int endRecord) {
        this.endRecord = endRecord;
    }

    public String getAutoStuNo() {
        return autoStuNo;
    }

    public void setAutoStuNo(String autoStuNo) {
        this.autoStuNo = autoStuNo;
    }
}

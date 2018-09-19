package com.yiyun.lockcontroller.bean.lock;

/**
 * 查询钥匙
 * Created by Layo on 2017-8-15.
 */

public class QueryMyKeysBean {
    public static final int addRecord = 15;     //要查询增加的数目

    private int startRecord = 0;    //查询的起始条数
    private int endRecord = startRecord + addRecord;    //查询的结束条数

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
}

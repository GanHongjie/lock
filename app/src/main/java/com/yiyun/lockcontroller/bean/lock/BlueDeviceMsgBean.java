package com.yiyun.lockcontroller.bean.lock;

/**
 * 设备信息内容的bean
 * Created by Layo on 2018-1-15.
 */

public class BlueDeviceMsgBean {
    private String lockNo;
    private String version;
    private String elect;
    private String r = "00";

    public BlueDeviceMsgBean(String parameter) {
        //设置参数
        initParam(parameter);
    }

    private void initParam(String str) {
        this.lockNo = str.substring(0, 24);
        this.version = str.substring(24, 28);
        this.elect = str.substring(28, 32);
        this.r = str.substring(32, str.length());
    }

    public String getLockNo() {
        return lockNo;
    }

    public String getVersion() {
        return version;
    }

    public String getElect() {
        return elect;
    }

    public String getR() {
        return r;
    }

    public String getElect10() {
        int ten = Integer.parseInt(elect.substring(0, 2), 16);
        String abit = String.valueOf(Integer.parseInt(elect.substring(2, 4), 16));
        if (abit.length()==1)
            abit='0'+abit;

        return ten + "." + abit;
    }
}

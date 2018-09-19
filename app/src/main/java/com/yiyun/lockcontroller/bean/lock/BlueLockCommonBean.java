package com.yiyun.lockcontroller.bean.lock;

import java.util.List;

/**
 * Created by Layo on 2017-8-13.
 */

public abstract class BlueLockCommonBean {
    private String header = "*";  //指令头
    private String type;    //指令类型
    private String command; //指令代码
    private String plMSB = "00";      //指令参数长度高位
    private String plLSB = "00";      //指令参数长度低位
    private String pl;
    private String checkSum;    //校验位
    private String end = "~";     //指令尾

    public BlueLockCommonBean() {

    }

    public BlueLockCommonBean(List<String> list) {
        initData(list);
    }

    private void initData(List<String> list) {
        this.type = list.get(0);
        this.command = list.get(1);
        this.plMSB = list.get(2);
        this.plLSB = list.get(3);
        this.checkSum = list.get(list.size() - 1);
    }

    public String getHeader() {
        return header;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPlMSB() {
        return plMSB;
    }

    public void setPlMSB(String plMSB) {
        this.plMSB = plMSB;
    }

    public String getPlLSB() {
        return plLSB;
    }

    public void setPlLSB(String plLSB) {
        this.plLSB = plLSB;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(int length) {
        String str = Integer.toHexString(length);
        int strLength = str.length();
        for (int i = 0; i < 4 - strLength; i++) {
            str = "0" + str;
        }
        this.plMSB = str.substring(0, 2);
        this.plLSB = str.substring(2, 4);
    }

    public String getEnd() {
        return end;
    }

    /**
     * 自动生成校验码
     *
     * @return
     */
    public abstract String getCheckSumNumber();

    public abstract String toString();


}

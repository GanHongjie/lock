package com.yiyun.lockcontroller.bean.lock;

import android.util.Log;

import java.util.List;

/**
 * Created by Layo on 2017-8-13.
 */

public class BlueLockResponseBean extends BlueLockCommonBean {
    private String parameter;   //指令参数

    public BlueLockResponseBean() {

    }

    public BlueLockResponseBean(List<String> list) {
        super(list);
        initData(list);
    }

    @Override
    public String toString() {
        return getType() + getCommand() + getPlMSB() + getPlLSB() + getParameter() + getCheckSumNumber();
    }

    //设置初始
    private void initData(List<String> list) {
        String strPar = "";
        for (int i = 4; i < list.size() - 1; i++) {
            strPar += list.get(i);
        }
        this.parameter = strPar;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getCheckSumNumber() {
        int sum = 0;
        sum += Integer.parseInt(getType(), 16);
        sum += Integer.parseInt(getCommand(), 16);
        sum += Integer.parseInt(getPlMSB(), 16);
        sum += Integer.parseInt(getPlLSB(), 16);
        try {
            for (int i = 0; i < parameter.length(); i += 2) {
                String rByte = parameter.substring(i, i + 2);
                sum += Integer.parseInt(rByte, 16);
            }
        } catch (StringIndexOutOfBoundsException e) {
            Log.getStackTraceString(e);
        }

        String strSum = String.valueOf(Integer.toHexString(sum));
        if (strSum.length() < 2) {
            int strLength = strSum.length();
            for (int i = 0; i < 2 - strLength; i++) {
                strSum = "0" + strSum;
            }
            return strSum;
        } else
            return strSum.substring(strSum.length() - 2, strSum.length());
    }


}

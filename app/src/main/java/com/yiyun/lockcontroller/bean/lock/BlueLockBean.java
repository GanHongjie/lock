package com.yiyun.lockcontroller.bean.lock;

import android.util.Log;

import java.util.List;

/**
 * Created by Layo on 2017-7-25.
 */
public class BlueLockBean extends BlueLockCommonBean {
    private ParBean parameter;   //指令参数

    public BlueLockBean() {
        parameter = new ParBean();
    }

    public BlueLockBean(List<String> list) {
        super(list);
        initData(list);
    }

    //设置初始
    private void initData(List<String> list) {
        String strPar = "";
        for (int i = 4; i < list.size() - 1; i++) {
            strPar += list.get(i);
        }
        this.parameter = new ParBean(strPar);
    }

    @Override
    public String toString() {
        return getType() + getCommand() + getPlMSB() + getPlLSB() + parameter.getR() + parameter.getOther() + getCheckSumNumber();
    }

    public ParBean getParameter() {
        return parameter;
    }

    public void setParameter(ParBean parameter) {
        this.parameter = parameter;
    }

    public class ParBean {
        private String lockNo;
        private String version;
        private String elect;
        private String r = "00";
        private String other = "";

        public ParBean(String str) {
            initParam(str);
        }

        public ParBean() {

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

        public void setLockNo(String lockNo) {
            this.lockNo = lockNo;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getElect() {
            return elect;
        }

        public void setElect(String elect) {
            this.elect = elect;
        }

        public String getR() {
            return r;
        }

        public void setR(String r) {
            this.r = r;
        }

        public String getOther() {
            return other;
        }

        public void setOther(String other) {
            this.other = other;
        }
    }

    /**
     * 自动生成校验码
     *
     * @return
     */
    public String getCheckSumNumber() {
        int sum = 0;
        sum += Integer.parseInt(getType(), 16);
        sum += Integer.parseInt(getCommand(), 16);
        sum += Integer.parseInt(getPlMSB(), 16);
        sum += Integer.parseInt(getPlLSB(), 16);
        try {
            for (int i = 0; i < parameter.getR().length(); i += 2) {
                String rByte = parameter.getR().substring(i, i + 2);
                sum += Integer.parseInt(rByte, 16);
            }
            for (int i = 0; i < parameter.getOther().length(); i += 2) {
                String rByte = parameter.getOther().substring(i, i + 2);
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

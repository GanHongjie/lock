package com.yiyun.lockcontroller.utils.bluetoothlock;

/**
 * 蓝牙发送体
 * 辅助自动设置长度和计算校验位
 * Created by Layo on 2018-1-12.
 */

public class BlueSendMsgBody extends BlueMsgBody {

    public BlueSendMsgBody(String parameter) {
        this.type = BT_SEND_TYPE;
        this.parameter = parameter;
        this.pl = calPL(parameter);
    }

    /**
     * 计算长度
     */
    private String calPL(String parameter) {
        StringBuilder pStr = new StringBuilder(Integer.toHexString(parameter.length() / 2));
        //补齐
        int pStrLength = pStr.length();
        for (int i = 0; i < 4 - pStrLength; i++)
            pStr.insert(0, "0");

        return String.valueOf(pStr);
    }

    /**
     * 计算校验和
     */
    private String calCheckSum(String str) {
        int sum = 0;
        for (int i = 0; i < str.length(); i += 2) {
            String rByte = str.substring(i, i + 2);
            sum += Integer.parseInt(rByte, 16);
        }
        StringBuilder strSum = new StringBuilder(Integer.toHexString(sum));
        //补齐
        int strSumLength = strSum.length();
        for (int i = 0; i < 2 - strSumLength; i++)
            strSum.insert(0, "0");
        if (strSumLength > 2)
            strSum = new StringBuilder(strSum.substring(strSumLength - 2, strSumLength));

        return String.valueOf(strSum);
    }

    /**
     * 转化为规格的发送体
     */
    public String getSendMsg(String command) {
        String send = type + command + pl + parameter;
        String checkSum = calCheckSum(send);

        return send + checkSum;
    }

}

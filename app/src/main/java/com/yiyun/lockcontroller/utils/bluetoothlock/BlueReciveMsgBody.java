package com.yiyun.lockcontroller.utils.bluetoothlock;

/**
 * 蓝牙接收体
 * Created by Layo on 2018-1-12.
 */

public class BlueReciveMsgBody extends BlueMsgBody{
    private int pl10;  //参数长度，十进制

    public void initData(String msg){
        int length = msg.length();
        type = msg.substring(0, 2);
        command = msg.substring(2, 4);
        pl = msg.substring(4, 8);
        pl10 = Integer.parseInt(pl, 16);
        parameter = msg.substring(8, length - 2);
        checkSum = msg.substring(length - 2, length);
    }

    public int getPl10() {
        return pl10;
    }

    public void setPl10(int pl10) {
        this.pl10 = pl10;
    }

    /**
     * 判断校验和
     */
    public boolean judgeCheckSum() {
        String str = type + command + pl + parameter;
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
//        Log.i(BlueReciveMsgBody.class.getSimpleName(), "checkSum=" + checkSum
//                + ", calc result=" + String.valueOf(strSum));
//        System.out.println("checkSum=" + checkSum + ", calc result=" + String.valueOf(strSum));
        return String.valueOf(strSum).equals(checkSum);
    }

}

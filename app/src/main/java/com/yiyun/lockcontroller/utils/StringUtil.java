package com.yiyun.lockcontroller.utils;

/**
 * Created by Layo on 2017-8-6.
 */

public class StringUtil {

    /**
     * 将string转化为变成assic的string
     *
     * @param str
     * @return
     */
    public static String str2ASSStr(String str) {
        String transStr = "";
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            transStr += (int) chars[i];
        }

        return transStr;
    }

    /**
     * 将string转化为变成16进制的assic的string
     *
     * @param str
     * @return
     */
    public static String str2ASSStr16(String str) {
        String transStr = "";
        char[] chars = str.toCharArray();
        for (int i = 0; i < str.length(); i++) {
            String strHex = Integer.toHexString((int) chars[i]);
            transStr += strHex;
        }

        return transStr;
    }

}

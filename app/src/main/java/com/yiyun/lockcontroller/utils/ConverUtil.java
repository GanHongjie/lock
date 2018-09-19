package com.yiyun.lockcontroller.utils;


import android.util.Base64;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.Locale;


/**
 * Convert help class.
 * <hr>
 * <b>&copy; Copyright 2011 Guidebee, Inc. All Rights Reserved.</b>
 *
 * @author Guidebee Pty Ltd.
 * @version 1.00, 13/09/11
 */

public class ConverUtil {

    // Hex help
    private static final byte[] HEX_CHAR_TABLE_LOWER_CASE = {(byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
            (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f'
    };

    private static final byte[] HEX_CHAR_TABLE_UPPER_CASE = {(byte) '0', (byte) '1',
            (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
            (byte) '7', (byte) '8', (byte) '9', (byte) 'A', (byte) 'B',
            (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F'
    };

    public static String bytes2HexString(byte[] raw, int len) {

        byte[] hex = new byte[2 * len];
        int index = 0;
        int pos = 0;

        for (byte b : raw) {

            if (pos >= len) {

                break;

            }

            pos++;
            int v = b & 0xFF;
            hex[index++] = HEX_CHAR_TABLE_UPPER_CASE[v >>> 4];
            hex[index++] = HEX_CHAR_TABLE_UPPER_CASE[v & 0xF];

        }

        return new String(hex);

    }


    public static String bytes2HexString(byte[] raw) {

        return bytes2HexString(raw, raw.length);

    }


    public static byte[] hexString2Bytes(String str) {

        String digital = "0123456789ABCDEF";
        str = str.toUpperCase(Locale.US);

        char[] hex2char = str.toCharArray();
        byte[] bytes = new byte[str.length() / 2];

        int temp = 0;
        for (int i = 0; i < bytes.length; i++) {

            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);

        }

        return bytes;

    }


    public static String encodeBase64(byte[] data) {

        if (data == null) {

            return null;

        }

        return Base64.encodeToString(data, Base64.NO_WRAP);

    }

    /***
     * check a given string is a hex string or not, two characters as one byte
     *
     * @param str given string for check
     * @return true, if the str is hex string; otherwise false
     */
    public static boolean isHexString(String str) {

        if (null == str || str.length() == 0 || str.length() % 2 != 0)
            return false;

        String matchStr = "^[0-9a-fA-F]*";
        return str.matches(matchStr);

    }


    public static byte[] swap(byte[] input) {
        byte temp = 0;
        for (int i = 0; i <= input.length / 2; i++) {
            temp = input[i];
            input[i] = input[input.length - 1 - i];
            input[input.length - 1 - i] = temp;
        }
        return input;
    }

    /**
     * convert int to byte []
     *
     * @param number
     * @return
     */
    public static byte[] intToByte(int number) {

        int temp = number;

        byte[] b = new byte[4];

        for (int i = 0; i < b.length; i++) {

            b[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;

        }

        return b;
    }

    public static byte[] shortToByte(short number) {

        short temp = number;

        byte[] b = new byte[2];

        for (int i = 0; i < b.length; i++) {

            b[i] = new Integer(temp & 0xff).byteValue();
            temp = (short) (temp >> 8);

        }

        return b;
    }


    public static byte[] longToByte(long number) {

        long temp = number;

        byte[] b = new byte[8];

        for (int i = 0; i < b.length; i++) {

            b[i] = new Long(temp & 0xff).byteValue(); //

            temp = temp >> 8; // ������8λ
        }

        return b;

    }

    // byte����ת��long
    public static long byteToLong(byte[] b) {

        long s = 0;

        long s0 = b[0] & 0xff;// ���λ

        long s1 = b[1] & 0xff;

        long s2 = b[2] & 0xff;

        long s3 = b[3] & 0xff;

        long s4 = b[4] & 0xff;// ���λ

        long s5 = b[5] & 0xff;

        long s6 = b[6] & 0xff;

        long s7 = b[7] & 0xff;

        // s0����

        s1 <<= 8;

        s2 <<= 16;

        s3 <<= 24;

        s4 <<= 8 * 4;

        s5 <<= 8 * 5;

        s6 <<= 8 * 6;

        s7 <<= 8 * 7;

        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;

        return s;

    }

    /**
     * ע�ͣ��ֽ����鵽int��ת����
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte[] b) {

        int s = 0;

        if (b == null)
            throw new IllegalArgumentException("parameter b is null");

        for (int i = 0; i < b.length; i++) {

            int sT = (b[i] & 0xff) << (i * 8);
            s |= sT;

        }

        return s;
    }

    public static String decodeBase64(String enData) {

        if (enData == null) {

            return null;

        }

        try {

            byte[] decBytes = Base64.decode(enData, Base64.DEFAULT);
            if (decBytes != null) {

                return bytes2HexString(decBytes);

            }

        } catch (IllegalArgumentException err) {

            return null;
        }

        return null;

    }

    public static int bytesToInt(byte[] intByte) {
        int fromByte = 0;
        for (int i = 1; i >= 0; i--) {
            int n = (intByte[i] < 0 ? (int) intByte[i] + 256 : (int) intByte[i]) << (8 * (1 - i));
            fromByte += n;
        }
        return fromByte;
    }

    public static short bytesToShort(byte[] b) {
        return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
    }

    public static ArrayList<Byte> byteToArrayList(byte[] src) {
        if (src == null)
            return null;
        ArrayList<Byte> list = new ArrayList<Byte>();
        for (int i = 0; i < src.length; i++) {
            // Byte b=new Byte(src[i]);
            // list.add(b);
            list.add(src[i]);
        }
        return list;
    }

    public static byte[] arrayListToByte(ArrayList<Byte> list) {

        int iSize = list.size();
        byte[] arrData = new byte[iSize];

        for (int i = 0; i < iSize; i++) {

            Byte b = (Byte) list.get(i);
            if (null != b) {

                arrData[i] = b;

            }

        }

        return arrData;

    }

    public static String arrayListToString(ArrayList<Byte> list) {

        byte[] bytes = arrayListToByte(list);
        return bytes2HexString(bytes);

    }

    public static ArrayList<Byte> swap(byte[] src, int count) {
        ArrayList<Byte> list = new ArrayList<Byte>();

        byte[] arrData = new byte[count];
        for (int i = 0; i < count; i++)
            arrData[count - 1 - i] = src[i];

        for (int i = 0; i < count; i++) {
            Byte b = new Byte(arrData[i]);
            list.add(b);
        }
        return list;
    }

    public static ArrayList<Byte> swap(int intValue, int ulLcLen) {
        ArrayList<Byte> b = new ArrayList<Byte>();
        for (int i = 0; i < ulLcLen; i++) {
            b.add((byte) (intValue >> 8 * (ulLcLen - i - 1) & 0xFF));
        }
        return b;
    }

    public static boolean compareBytes(byte[] date1, byte[] data2, int len) {

        int index = date1.length < data2.length ? date1.length : data2.length;
        index = len < index ? len : index;
        for (int i = 0; i < index; i++)
            if (date1[i] != data2[i])
                return false;
        return true;
    }

    public static PublicKey getPublicKey(String modulus, String publicExponent) throws Exception {

        BigInteger m = new BigInteger(modulus, 16);
        BigInteger e = new BigInteger(publicExponent, 16);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        return publicKey;

    }

    public static int getByteCount(byte[][] byteArray) {

        if (null == byteArray)
            return 0;
        int count = 0;
        for (int i = 0; i < byteArray.length; i++) {
            for (int j = 0; j < byteArray[i].length; j++) {
                if (byteArray[i][j] != (byte) 0x00) {
                    count++;
                    break;
                }
            }
        }

        return count;
    }


    public static boolean isNumeric(String str) {

        boolean tag = true;
        for (int i = 0; i < str.length(); i++) {

            if (!Character.isDigit(str.charAt(i))) {

                tag = false;
                break;

            }

        }

        return tag;

    }

    public static boolean isMobileNo(String strNo) {

        if (null == strNo || strNo.length() <= 0) {

            return false;

        }

        String regex = "[1]\\d{10}";
        return strNo.matches(regex);

    }

}

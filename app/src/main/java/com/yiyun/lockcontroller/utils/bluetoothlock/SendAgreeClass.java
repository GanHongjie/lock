package com.yiyun.lockcontroller.utils.bluetoothlock;


import com.yiyun.lockcontroller.utils.bluetooth.BlueToothUtils;
import com.yiyun.lockcontroller.utils.rc4.RC4;

/**
 * 发送的协议内容
 * 负责加头尾、去头尾、加密
 * Created by Layo on 2017-7-26.
 */
public class SendAgreeClass {
    public static final String PUBLIC_KEY = "12345678";
    public static final String KEY_VERIFY_DEFAULT = "IKY45DJG";

    public static final String BT_HEAD = "2a"; //发送的头
    public static final String BT_END = "7e";  //发送的结尾
    //    private String BT_HEAD = "*"; //发送的头
//    private String BT_END = "~";  //发送的结尾
    private String body = "";     //发送的中间加密部分

    public SendAgreeClass() {

    }

    public SendAgreeClass(String body) {
        int length = body.length();
        if (body.substring(0, BT_HEAD.length()).equals(BT_HEAD) && body.substring(length - BT_END.length(), length).equals(BT_END)) {
            //发现存在头尾
            this.body = body.substring(BT_HEAD.length(), length - BT_END.length());
        } else
            this.body = body;
    }

    @Override
    public String toString() {
        return BT_HEAD + body + BT_END;
    }

    public String toByteRC4Byte() {
        byte[] hexBody = BlueToothUtils.hexStringToBytes(body);
        byte[] encryptByte = RC4.RC4Base(hexBody, PUBLIC_KEY);
        String encrypt = BlueToothUtils.bytesToHexString(encryptByte);

        return BT_HEAD + encrypt + BT_END;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

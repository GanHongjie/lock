package com.yiyun.lockcontroller.net;

/**
 * Created by Layo on 2017-12-28.
 */

public final class EidNetConstant {
    //public static final String PROTOCOL = "http://dev.m-eid.cn";
    public static final String PROTOCOL = "http://kingeid.iego.cn";

    public static final String ROOT_DEV = PROTOCOL + "/simeidsignas/";
//    public static final String ROOT_TEST = PROTOCOL + "/simeidas_test/";
    public static final String ROOT_TEST = PROTOCOL + "/SIMeIDServerExample/";
    public static final String ROOT_IDC = PROTOCOL + "/simeidas/";
    public static final String ROOT_ONLINE = PROTOCOL + "/simeidas_online/";

    /**
     * S模式的请求路径
     */
    public final class URLContextSMS {
        //身份识别
        public static final String REAL_NAME = "sms/realname";
        //匿名认证
//        public static final String DIRECT_LOGIN = "sms/directlogin";
        public static final String DIRECT_LOGIN = "sms/direct";
    }

    /**
     * O模式相关请求地址
     */
    public class URLContextOMA {
        //请求签名指令
        public static final String GET_APDU = "oma/signapdu";
        //身份识别
        public static final String REAL_NAME = "oma/realname";
        //匿名认证
//        public static final String DIRECT_LOGIN = "oma/directlogin";
        public static final String DIRECT_LOGIN = "oma/direct";
    }

}

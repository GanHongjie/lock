package com.yiyun.lockcontroller.net.lock;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.yiyun.lockcontroller.utils.SPUtil;

/**
 * 传输给接口的的公共参数类
 * Created by Layo on 2017-7-10.
 */
public class PublicPutParameter {
    public static final String USER_NAME = "username";
    public static final String APP_USER_EID = "appeidcode";
    public static final String APP_VERSION = "version";
    public static final String APP_R = "r";

    private String version = "2.0";
    private String stuEid = "";
    private String r = "";

    public PublicPutParameter() {
        this.stuEid = initStuEid();
        this.r = initR();
    }

    public String getVersion(Context context) {
        return String.valueOf(getVersionName(context));
    }

    public String getStuEid() {
        return stuEid;
    }

    public String getR() {
        return r;
    }

    private String initStuEid() {
        return SPUtil.getInstance().getString(APP_USER_EID);
    }

    private String initR() {
        return (int) (Math.random() * 70) + 20 + "";
    }

    private int getVersionName(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo != null ? packInfo.versionCode : 0;
    }
}

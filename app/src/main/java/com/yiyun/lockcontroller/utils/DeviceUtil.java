package com.yiyun.lockcontroller.utils;

import android.content.Context;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.yiyun.lockcontroller.App;

import static android.content.ContentValues.TAG;

/**
 * Created by Layo on 2017-8-22.
 */

public class DeviceUtil {
    /**
     * 判断有没有打开gps
     *
     * @return
     */
    public static boolean isOpenGPS() {
        LocationManager locationManager = (LocationManager) App.getAppContext().getSystemService(Context
                .LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 判断是否包含SIM卡
     *
     * @return 状态
     */
    public static boolean hasSimCard() {
        TelephonyManager telMgr = (TelephonyManager)
                App.getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        Log.d(TAG, result ? "有SIM卡" : "无SIM卡");
        return result;
    }
}

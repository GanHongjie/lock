package com.yiyun.lockcontroller.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.yiyun.lockcontroller.utils.ToastUtil;

/**
 * Created by joker on 2017/7/12.
 */

public class NetworkStateReceiver extends BroadcastReceiver {
    public static final String NETWORK_STATE_CHECK = "NETWORK_STATE_CHECK";
    private static boolean isNetConnect = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        isNetConnect = activeInfo != null && activeInfo.isConnected();
        Log.i("tag",isNetConnect+"");
        if (!isNetConnect) {
            ToastUtil.showToast("网络连接已断开");
        }
    }

    public static boolean isNetConnect() {
        return isNetConnect;
    }
}

package com.yiyun.lockcontroller;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.tencent.bugly.crashreport.CrashReport;
import com.yiyun.lockcontroller.net.NetworkStateReceiver;

import java.util.Stack;

/**
 * Created by Layo on 2017-12-28.
 */

public class App extends Application {

    public static final boolean DEBUG = false;

    // TODO: 2018-2-1 安全性增加NDK防二次打包，敏感内容存在NDK中
    // TODO: 2018-2-1 记录用户操作日志
    // TODO: 2018-2-2 全局异常处理
    // TODO: 2018-2-3 权限管理
    private static App instance;
    private Object attached = null;
    private Stack<AppCompatActivity> activityStack = new Stack<>();

    // 全局 Context 获取
    public static Context getAppContext() {
        return instance.getApplicationContext();
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        // TODO: 2018-1-30  安全性内容，SP保存的时候需要密文读取和保存
        //Bugly异常上报
        CrashReport.initCrashReport(getApplicationContext(), "58a03db40c", false); // "19b44aadba"
        //Log工具
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .tag("myTag")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        // Android N 以下使用广播，Android N 以上使用 NetworkCallback
        // 初始化网络状态
        Intent intent = new Intent(NetworkStateReceiver.NETWORK_STATE_CHECK);
        sendBroadcast(intent);
    }

    public void setReaderAttached(Object attached) {
        this.attached = attached;
    }

    public Object getReaderAttached() {
        return attached;
    }

    public void addActivity(final AppCompatActivity curAT) {
        if (null == activityStack) {
            activityStack = new Stack<>();
        }
        activityStack.push(curAT);
    }

    public void removeActivity(final AppCompatActivity curAT) {
        if (null == activityStack) {
            activityStack = new Stack<>();
        }
        activityStack.remove(curAT);
    }

    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
     }
}

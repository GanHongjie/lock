package com.yiyun.lockcontroller.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 线程计时器
 * 传入一个时间，线程调用监听回调
 * Created by Layo on 2017-8-8.
 */

public class ThreadTimeUtil extends TimerTask {
    private ThreadTimeListener listener;
    private int time;
    private Timer timer;

    /**
     * 创建线程计时
     *
     * @param listener
     * @param time
     * @return
     */
    public ThreadTimeUtil create(ThreadTimeListener listener, int time) {
        this.listener = listener;
        this.time = time;

        return this;
    }

    /**
     * 启动线程计时
     */
    public void start() {
        timer = new Timer();
        timer.schedule(this, time);
    }

    /**
     * 延迟多长时间开始循环time时间的任务
     *
     * @param delay 延迟的时间
     */
    public void start(int delay) {
        timer = new Timer();
        timer.schedule(this, delay, time);
    }

    /**
     * 取消线程计时
     */
    public void cancle() {
        if (timer != null)
            timer.cancel();
    }

    @Override
    public void run() {
        if (timer != null) {
            listener.timeOut();
        }
    }


    public interface ThreadTimeListener {
        //接收满数据处理后发给服务端
        void timeOut();
    }

}


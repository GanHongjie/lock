package com.yiyun.lockcontroller.utils.bluetoothlock;

import com.yiyun.lockcontroller.bean.lock.BlueDeviceMsgBean;

/**
 * 包是否接收完全的接口
 * Created by Layo on 2017-7-26.
 */
public interface BlueGetMsgListener {
    //最大重传次数
    int MAX_RESEND_COUNT = 5;

    //接收到获取设备信息数据
    void sendDeviceDate(BlueDeviceMsgBean deviceMsgBean);

    //接收到正确返回值
    void sendSuccess(String strMsg);

    //发送验证
    void sendVerify(String strR);

    //发送重传信息
    void sendReSend(String commandMsg);

    //接收到错误的信息(校验错误，丢包错误)
    void sendError(String strMsg);

    //发送断开连接信息
    void sendDisConnected();
}

package com.yiyun.lockcontroller.utils.bluetoothlock;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yiyun.lockcontroller.bean.lock.BlueDeviceMsgBean;

import java.util.Stack;

import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueGetMsgListener.MAX_RESEND_COUNT;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_COMMAND_ERROR;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_COMMAND_GET;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_COMMAND_INIT;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_COMMAND_OPEN;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_COMMAND_RESPONSE;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_ERR_CHECK;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_ERR_LENGTH;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_ERR_LENGTH1;
import static com.yiyun.lockcontroller.utils.bluetoothlock.SendAgreeClass.BT_END;
import static com.yiyun.lockcontroller.utils.bluetoothlock.SendAgreeClass.BT_HEAD;
//
/**
 * 得到蓝牙的零散数据
 * Created by Layo on 2017-7-26.
 */
public class BlueGetMsg<T extends AppCompatActivity> {

    private static final String TAG = BlueGetMsg.class.getSimpleName();

    private BlueGetMsgListener listener;
    private Context context;
    private String strMsg = "";
    private Stack<String> commandMsg = null;
    private int reSendCount = 0;
    private String lastData = null;

    public BlueGetMsg(Context context) {
        this.context = context;
    }

    public void setListener(BlueGetMsgListener listener) {
        this.listener = listener;
    }

    public void pushCommand(String s) {
        if (commandMsg == null)
            commandMsg = new Stack<>();
        commandMsg.push(s);
    }

    //添加数据判断是否把包接收完全
    public void addMsg(String str) {
        if (str.equalsIgnoreCase(lastData)) { // 避免接收重复数据
            return;
        }
        lastData = str;
        //分包接收的内容
        if (str.substring(0, BT_HEAD.length()).equals(BT_HEAD)) {
            strMsg = "";
        }
        strMsg += str;
        if (str.substring(str.length() - BT_END.length(), str.length()).equals(BT_END) && listener != null) {
            //去头去尾的方法，暂时没有加入解密过程
            SendAgreeClass sendAgreeClass = new SendAgreeClass(strMsg);
            final BlueReciveMsgBody reciveMsgBody = new BlueReciveMsgBody();
            reciveMsgBody.initData(sendAgreeClass.getBody());
            ((T) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //此时已在主线程中，可以更新UI了
                    if (reciveMsgBody.judgeCheckSum()) {
                        switch (reciveMsgBody.getCommand()) {
                            case BT_COMMAND_GET:
                                BlueDeviceMsgBean deviceMsgBean = new BlueDeviceMsgBean(reciveMsgBody.getParameter());
                                listener.sendDeviceDate(deviceMsgBean);
                                break;
                            case BT_COMMAND_OPEN:
                                listener.sendSuccess(reciveMsgBody.getParameter());
                                Log.i(TAG, "open" + reciveMsgBody.getParameter());
                                break;
                            case BT_COMMAND_INIT:
                                listener.sendSuccess("初始化成功");
                                break;
                            case BT_COMMAND_RESPONSE:
                                Log.i(TAG, "BT_COMMAND_RESPONSE=0f 验证");
                                listener.sendVerify(reciveMsgBody.getParameter());
                                break;
                            case BT_COMMAND_ERROR:
                                Log.i(TAG, "BT_COMMAND_ERROR=11 数据错误");
                                listener.sendError("数据错误");
                                //超过重传次数断开连接
                                String errMsg = reciveMsgBody.getParameter();
                                if (errMsg.equals(BT_ERR_LENGTH) || errMsg.equals(BT_ERR_LENGTH1) || errMsg.equals(BT_ERR_CHECK)) {
                                    if (reSendCount++ < MAX_RESEND_COUNT)
                                        listener.sendReSend(commandMsg.pop());
                                    else {
                                        reSendCount = 0;
                                        listener.sendDisConnected();
                                    }
                                }
                                break;
                            default:
                                listener.sendError("未知错误");
                                break;
                        }
                    } else {
                        listener.sendError("校验码错误");
                    }
                }
            });

        }

    }

}

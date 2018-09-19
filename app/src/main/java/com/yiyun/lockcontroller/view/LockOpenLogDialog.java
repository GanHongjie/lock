package com.yiyun.lockcontroller.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.LockOpenBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 单个信息的开锁记录dialog
 * Created by Layo on 2017-8-2.
 */

public class LockOpenLogDialog extends Dialog {

    private Context context;
    private LockOpenBean bean;
    private View.OnClickListener cancleButtonClickListener;

    private TextView tvLogDate;
    private TextView tvLogUserType;
    private TextView tvLogOpenType;
    private Button btnDialogCancel;

    private String[] strUserType = {"普通用户", "普通用户", "管理员", "临时用户"};
    private String[] strOpenType = {"开锁失败", "自身开锁", "时间段授权开锁", "次数授权开锁", "时间段与次数双层授权开锁"};

    /**
     * @param context 传入上下文
     * @param bean    传入接收到的开锁记录bean
     */
    public LockOpenLogDialog(@NonNull Context context, LockOpenBean bean) {
        super(context);
        this.context = context;
        this.bean = bean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_open_log);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initFindId();
        initConfig();
        initData(); //初始化数据
    }

    private void initConfig() {
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancleButtonClickListener != null) {
                    cancleButtonClickListener.onClick(v);
                }
            }
        });
    }

    private void initFindId() {
        tvLogDate = (TextView) findViewById(R.id.tv_log_date);
        tvLogUserType = (TextView) findViewById(R.id.tv_log_userType);
        tvLogOpenType = (TextView) findViewById(R.id.tv_log_openType);
        btnDialogCancel = (Button) findViewById(R.id.btn_dialog_cancel);
    }

    private void initData() {
        Log.i("test","bean = " + bean.toString());
        long time = bean.getDate();
        if (time != 0)
            tvLogDate.setText(dateFormat(time));
        else
            tvLogDate.setText("error");
        int openType = bean.getOpenType();
        if (openType == 11 || openType == 12 || openType == 13) {
            //临时用户
            tvLogUserType.setText(strUserType[3]);
        } else {
            //非临时用户
            tvLogUserType.setText(strUserType[bean.getUserType()]);
        }
        switch (openType) {
            case -1:
                tvLogOpenType.setText(strOpenType[0]);
                break;
            case 1:
                tvLogOpenType.setText(strOpenType[1]);
                break;
            case 11:
                tvLogOpenType.setText(strOpenType[2]);
                break;
            case 12:
                tvLogOpenType.setText(strOpenType[3]);
                break;
            case 13:
                tvLogOpenType.setText(strOpenType[4]);
                break;
            default:
                tvLogOpenType.setText("error");
        }
    }

    public LockOpenLogDialog setCancleButtonOnClickListener(View.OnClickListener listener) {
        this.cancleButtonClickListener = listener;
        return this;
    }


    private String dateFormat(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(time);

        return sdf.format(date);
    }

}

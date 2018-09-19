package com.yiyun.lockcontroller.presenter.lock.contract;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * 用在获得设备回传信息，并与网络端通讯
 * Created by Layo on 2017-7-26.
 */
public interface LockBlueOpenContract {
    // V 层做的事
    interface View extends BaseView {
        //开锁成功展示
        void showData(String cipher);

        //api的错误
        void showApiError(int errorEnum);
    }

    // P 层做的事
    interface Presenter extends BasePresenter {
        //开锁
        void openLock(Context context, LockBean bean);

    }
}

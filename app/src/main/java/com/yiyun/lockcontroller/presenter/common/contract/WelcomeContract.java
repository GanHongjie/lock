package com.yiyun.lockcontroller.presenter.common.contract;

import android.content.Context;

import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * Created by Layo on 2018-1-3.
 */

public interface WelcomeContract {

    interface View extends BaseView {
        //eid检测完成
        void eidIsComplete();

        //版本成功回调
        void versionSuccess();

        //版本失败回调
        void versionFail();
    }

    interface Presenter extends BasePresenter {
        //判断有无存在eid卡
        void haveEidCard();

        //检查版本
        void updateVersion();

        //请求RSA
        void requestPki(Context context);
    }

}

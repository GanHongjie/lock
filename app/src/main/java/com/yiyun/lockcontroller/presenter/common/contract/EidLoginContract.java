package com.yiyun.lockcontroller.presenter.common.contract;


import android.content.Context;

import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * Created by Layo on 2017-12-28.
 */

public interface EidLoginContract {
    interface View extends BaseView {

        //登录成功回调
        void loginSuccess();

        //登录失败回调
        void loginFail();

    }

    interface Presenter extends BasePresenter {
        //账号密码的方式登录
//        void commonLogin();

        //登录接口
        void login(Context context, String moblieNo, String password, boolean isOMAFound);
    }
}

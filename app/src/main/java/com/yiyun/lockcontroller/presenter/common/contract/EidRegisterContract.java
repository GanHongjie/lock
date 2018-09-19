package com.yiyun.lockcontroller.presenter.common.contract;

import android.content.Context;

import com.yiyun.lockcontroller.bean.common.RegisterMsgBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * Created by Layo on 2018-1-3.
 */

public interface EidRegisterContract {
    interface View extends BaseView {
        //注册成功回调
        void registerSuccess();
        //注册失败回调
        void registerFail();

    }

    interface Presenter extends BasePresenter {

        //注册接口
        void register(Context context, RegisterMsgBean registerMsgBean, boolean isOMAFound);
    }
}

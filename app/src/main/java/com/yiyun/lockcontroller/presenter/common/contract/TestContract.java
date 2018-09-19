package com.yiyun.lockcontroller.presenter.common.contract;


import android.content.Context;

import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * Created by Layo on 2017-12-28.
 */

public interface TestContract {
    interface View extends BaseView {

        //登录成功回调
        void success();

        //登录失败回调
        void fail();

        void showApiError(int errCode);
    }

    interface Presenter extends BasePresenter {
        //判断有无存在eid卡
        void test1(Context context);

        //判断有无存在eid卡
        void test2(Context context);

        //判断有无存在eid卡
        void test3(Context context);
    }
}

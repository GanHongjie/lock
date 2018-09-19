package com.yiyun.lockcontroller.presenter.lock.contract;


import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Layo on 2017-12-28.
 */

public interface LockMainContract {
    interface View extends BaseView {
        //请求成功
        void openRequestSuccess(String r);

        //请求失败
        void openRequestFail(String r);

        void searchMyKeysSuccess(List<LockKeysBean> logBeans);

        void gotoLoginActivity();
    }

    interface Presenter extends BasePresenter {
        //请求开锁
        void openLock(Context context, LockBean bean);

        public void searchMyKeys(Context context);
    }
}

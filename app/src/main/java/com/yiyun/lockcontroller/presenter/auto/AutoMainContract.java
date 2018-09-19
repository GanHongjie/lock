package com.yiyun.lockcontroller.presenter.auto;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2018-3-7.
 */

public class AutoMainContract {

    public interface View extends BaseView {
        //查询成功
        void searchSuccess(List<LockKeysBean> myLockKeyBeans);

    }

    public interface Presenter extends BasePresenter {
        //查询我的所有钥匙
        void searchMyLockKey(Context context);
    }
}

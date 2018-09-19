package com.yiyun.lockcontroller.presenter.lock.contract;


import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.bean.lock.QueryMyKeysBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Layo on 2017-8-15.
 */

public interface LockMyKeysContract {

    // V 层做的事
    interface View extends BaseView {
        //查询成功后回传数据
        void showMyKeys(List<LockKeysBean> lockOpenBeen);

        //API的错误提示
        void showApiError(int errorEnum);

    }

    // P 层做的事
    interface Presenter extends BasePresenter {
        void searchMyKeys(Context context, QueryMyKeysBean bean);
    }
}

package com.yiyun.lockcontroller.presenter.lock.contract;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Layo on 2017-7-23.
 */
public interface LockAutoInValidContract {
    // V 层做的事
    interface View extends BaseView {
        //显示所有的被授权人
        void showAutoLog(List<AuthorizeLogBean> logBeans);

        //API的错误提示
        void showApiError(int errorEnum);
    }

    // P 层做的事
    interface Presenter extends BasePresenter{

        void autoQuery(Context context, QueryAutoBean bean);
    }
}

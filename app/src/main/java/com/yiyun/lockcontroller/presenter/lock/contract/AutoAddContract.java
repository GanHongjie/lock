package com.yiyun.lockcontroller.presenter.lock.contract;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.AuthorizeAddBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Layo on 2017-7-21.
 */
public interface AutoAddContract {
    // V 层做的事
    interface View extends BaseView {
        //绑定学号后之后的操作
        void showAuto();

        //API的错误提示
        void showApiError(int errorEnum);

        void searchMyKeysSuccess(List<LockKeysBean> logBeans);
    }

    // P 层做的事
    interface Presenter extends BasePresenter {
        //添加授权人
        void autoAdd(Context context, AuthorizeAddBean authroize);

        void searchMyKeys(Context context);
    }
}

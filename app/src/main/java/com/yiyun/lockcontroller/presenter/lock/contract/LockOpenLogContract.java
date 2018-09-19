package com.yiyun.lockcontroller.presenter.lock.contract;


import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockOpenBean;
import com.yiyun.lockcontroller.bean.lock.QueryOpenLogBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * Created by Layo on 2017-7-27.
 */
public interface LockOpenLogContract {
    // V 层做的事
    interface View extends BaseView {
        //查询成功后回传数据
        void showOpenLog(List<LockOpenBean> lockOpenBeen);

        //API的错误提示
        void showApiError(int errorEnum);

    }

    // P 层做的事
    interface Presenter extends BasePresenter {
        void searchOpenLog(Context context, QueryOpenLogBean bean);
    }

}

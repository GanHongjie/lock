package com.yiyun.lockcontroller.presenter.lock.contract;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

import java.util.List;

/**
 * 包含删除和查询被授权人信息
 * Created by Layo on 2017-7-22.
 */
public interface AutoTrueContract {
    interface View extends BaseView {
        //显示所有的被授权人
        void showAutoLog(List<AuthorizeLogBean> logBeans);

        //显示删除成功之后的操作
        void showDel();

    }

    interface Presenter extends BasePresenter {

        void autoDel(Context context, String autoStuNo, String lockNumber);

        void autoQuery(Context context, QueryAutoBean bean);
    }

}

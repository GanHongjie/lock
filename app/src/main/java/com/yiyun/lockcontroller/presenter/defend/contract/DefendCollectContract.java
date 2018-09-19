package com.yiyun.lockcontroller.presenter.defend.contract;

import com.yiyun.lockcontroller.ui.base.BasePresenter;
import com.yiyun.lockcontroller.ui.base.BaseView;

/**
 * Created by Layo on 2018-1-3.
 */

public interface DefendCollectContract {
    interface View extends BaseView {
        //请求成功回调
        void showData();
    }

    interface Presenter extends BasePresenter {
        //网络端口请求收集数据
        void collectMsg();
    }
}

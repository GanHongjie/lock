package com.yiyun.lockcontroller.presenter.auto;

import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;

/**
 * Created by Administrator on 2018-4-16.
 */

public class MyQrCodePresenter extends BaseMvpPresenter<MyQrCodeContract.View> implements MyQrCodeContract.Presenter {

    public MyQrCodePresenter(MyQrCodeContract.View mView) {
        super(mView);
    }


}

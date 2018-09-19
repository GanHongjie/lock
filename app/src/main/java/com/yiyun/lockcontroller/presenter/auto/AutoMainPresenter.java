package com.yiyun.lockcontroller.presenter.auto;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.bean.lock.LockKeysBean.USER_TYPE_COMMON;

/**
 * Created by Administrator on 2018-3-7.
 */

public class AutoMainPresenter extends BaseMvpPresenter<AutoMainContract.View> implements AutoMainContract.Presenter {

    public AutoMainPresenter(AutoMainContract.View mView) {
        super(mView);
    }

    @Override
    public void searchMyLockKey(Context context) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getUnData().addProperty("startRecord", 0);
        publicJsonObject.getUnData().addProperty("endRecord", 999);

        Disposable subscribe = NetHelper.getInstance().searchMyKeys(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        JsonArray list = publicGetJsonObject.getList();
                        List<LockKeysBean> myLockBeans = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            LockKeysBean authorizeLogBean = new Gson().fromJson(list.get(i), LockKeysBean.class);
                            if (authorizeLogBean.getUserType() == USER_TYPE_COMMON) {
                                myLockBeans.add(authorizeLogBean);
                            }
                        }
                        getView().searchSuccess(myLockBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showError("api错误" + errCode);
                        } else {
                            getView().showError("错误" + throwable.getMessage());
                        }
                    }
                });
        addSubscription(subscribe);
    }
}

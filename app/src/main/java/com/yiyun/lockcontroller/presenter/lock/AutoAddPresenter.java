package com.yiyun.lockcontroller.presenter.lock;


import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.bean.lock.AuthorizeAddBean;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.AutoAddContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.yiyun.lockcontroller.utils.SPUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.bean.lock.LockKeysBean.USER_TYPE_COMMON;


/**
 * Created by Layo on 2017-7-21.
 */
public class AutoAddPresenter extends BaseMvpPresenter<AutoAddContract.View> implements AutoAddContract.Presenter {

    public AutoAddPresenter(AutoAddContract.View mView) {
        super(mView);
    }

    @Override
    public void autoAdd(Context context, AuthorizeAddBean authroize) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //授权人
        int state = authroize.getState();
        if ((state == AuthorizeAddBean.AUTO_STATE_TIME) || (state == AuthorizeAddBean.AUTO_STATE_TIME_COUNT)) {
            publicJsonObject.getEnData().addProperty("startTime", authroize.getStartTime());
            publicJsonObject.getEnData().addProperty("endTime", authroize.getEndTime());
        }
        if ((state == AuthorizeAddBean.AUTO_STATE_COUNT) || (state == AuthorizeAddBean.AUTO_STATE_TIME_COUNT))
            publicJsonObject.getEnData().addProperty("autoCount", authroize.getAutoCount());

        publicJsonObject.getEnData().addProperty("autoUsername", authroize.getAutoUsername());
        publicJsonObject.getEnData().addProperty("state", authroize.getState());
        publicJsonObject.getEnData().addProperty("lockNo", authroize.getLockNo());

        Disposable subscribe = NetHelper.getInstance().autoUser(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        getView().showAuto();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showApiError(errCode);
                        } else {
                            getView().showError(throwable.getMessage());
                        }
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void searchMyKeys(Context context) {
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
                        List<LockKeysBean> logBeans = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            LockKeysBean authorizeLogBean = new Gson().fromJson(list.get(i), LockKeysBean.class);
                            if (App.DEBUG) {
                                Log.i("lock", "authorizeLogBean ="  + new Gson().toJson(authorizeLogBean));
                            }
                            if (authorizeLogBean.getUserType() == USER_TYPE_COMMON) {
                                logBeans.add(authorizeLogBean);
                            }
                        }
                        getView().searchMyKeysSuccess(logBeans);
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

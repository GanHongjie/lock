package com.yiyun.lockcontroller.presenter.lock;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.bean.lock.QueryMyKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.LockMyKeysContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Layo on 2017-8-15.
 */

public class LockMyKeysPresenter extends BaseMvpPresenter<LockMyKeysContract.View> implements LockMyKeysContract.Presenter {

    public LockMyKeysPresenter(LockMyKeysContract.View mView) {
        super(mView);
    }

    @Override
    public void searchMyKeys(Context context, QueryMyKeysBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getUnData().addProperty("startRecord", bean.getStartRecord());
        publicJsonObject.getUnData().addProperty("endRecord", bean.getEndRecord());

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
                            logBeans.add(authorizeLogBean);
                        }
                        getView().showMyKeys(logBeans);
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
}

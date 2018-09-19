package com.yiyun.lockcontroller.presenter.lock;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.LockAutoInValidContract;
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
 * 无效的授权人信息
 * Created by Layo on 2017-7-23.
 */
public class LockAutoInValidPresenter extends BaseMvpPresenter<LockAutoInValidContract.View> implements LockAutoInValidContract.Presenter {

    public LockAutoInValidPresenter(LockAutoInValidContract.View mView) {
        super(mView);
    }

    @Override
    public void autoQuery(Context context, QueryAutoBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //查询所有授权人
        publicJsonObject.getUnData().addProperty("pattern", bean.getPattern());
        publicJsonObject.getUnData().addProperty("startRecord", bean.getStartRecord());
        publicJsonObject.getUnData().addProperty("endRecord", bean.getEndRecord());
        if (bean.getAutoStuNo()!=null)
            publicJsonObject.getEnData().addProperty("autoStuNo", bean.getAutoStuNo());//可空
        Disposable subscribe = NetHelper.getInstance().searchAuto(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        JsonArray list = publicGetJsonObject.getList();
                        List<AuthorizeLogBean> logBeans = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            AuthorizeLogBean authorizeLogBean = new Gson().fromJson(list.get(i), AuthorizeLogBean.class);
                            logBeans.add(authorizeLogBean);
                        }
                        if (getView() != null) {
                            getView().showAutoLog(logBeans);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            if (getView() != null) {
                                getView().showApiError(errCode);
                            }
                        } else {
                            if (getView() != null) {
                                getView().showError(throwable.getMessage());
                            }
                        }
                    }
                });
        addSubscription(subscribe);
    }
}

package com.yiyun.lockcontroller.presenter.common;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.AuthorizeAddBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.bean.lock.QueryMyKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.common.contract.TestContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.bean.lock.AuthorizeAddBean.AUTO_STATE_COUNT;

/**
 * Created by Layo on 2018-1-19.
 */

public class TestPresenter extends BaseMvpPresenter<TestContract.View> implements TestContract.Presenter {

    public TestPresenter(TestContract.View mView) {
        super(mView);

    }

    @Override
    public void test1(Context context) {
        Logger.i("授权开锁接口");
        String userNo = "1234567";
        AuthorizeAddBean authroize = new AuthorizeAddBean("", "", "5", userNo,"363443464439304533344232", AUTO_STATE_COUNT);
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //授权人
        int state = authroize.getState();
        if ((state == AuthorizeAddBean.AUTO_STATE_TIME) || (state == AuthorizeAddBean.AUTO_STATE_TIME_COUNT)) {
            publicJsonObject.getEnData().addProperty("startTime", authroize.getStartTime());
            publicJsonObject.getEnData().addProperty("endTime", authroize.getEndTime());
        }
        if ((state == AUTO_STATE_COUNT) || (state == AuthorizeAddBean.AUTO_STATE_TIME_COUNT))
            publicJsonObject.getEnData().addProperty("autoCount", authroize.getAutoCount());

        publicJsonObject.getEnData().addProperty("autoUsername", authroize.getAutoUsername());
        publicJsonObject.getEnData().addProperty("state", authroize.getState());
        publicJsonObject.getEnData().addProperty("lockNo", authroize.getLockNo());

        Logger.json(publicJsonObject.toString());
        Logger.i(publicJsonObject.toStringAES());

        Disposable subscribe = NetHelper.getInstance().autoUser(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        //到这已经成功
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        Logger.i(bean.getEncrypt());
                        Logger.json(publicGetJsonObject.getEnData().toString());
                        getView().success();
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
    public void test2(Context context) {
        Logger.i("查询所有授权人接口");
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //查询所有授权人
        Logger.i(publicJsonObject.toStringAES());
        Disposable subscribe = NetHelper.getInstance().searchAuto(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        try {
                            getView().success();
                        } catch (NullPointerException e) {
                            e.getMessage();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showApiError(errCode);
                        } else {
                            try {
                                getView().showError(throwable.getMessage());
                            } catch (NullPointerException e) {
                                e.getMessage();
                            }
                        }
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void test3(Context context) {
        Logger.i("查询所有钥匙接口");
        QueryMyKeysBean bean = new QueryMyKeysBean();
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
                        getView().success();
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

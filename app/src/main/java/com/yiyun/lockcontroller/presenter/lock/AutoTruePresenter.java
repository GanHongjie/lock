package com.yiyun.lockcontroller.presenter.lock;

import android.content.Context;
import android.util.Log;

import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.AutoTrueContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 有效的授权人信息
 * Created by Layo on 2017-7-22.
 */
public class AutoTruePresenter extends BaseMvpPresenter<AutoTrueContract.View> implements AutoTrueContract.Presenter {

    public AutoTruePresenter(AutoTrueContract.View mView) {
        super(mView);
    }

    @Override
    public void autoDel(Context context, String autoUsername, String lockNumber) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //放入要删除人的信息
        publicJsonObject.getEnData().addProperty("autoUsername", autoUsername);
//        String address = SPUtil.getInstance().getString(USER_LOCK_ADDRESS);
//        String lockNumber = SPUtil.getInstance().getString(address + USER_LOCK_NO);
        publicJsonObject.getEnData().addProperty("lockNo", lockNumber);
        // autoUsername123456, lockNo324336423744373439364236
        Log.i("lock", "autoUsername" + autoUsername + ", lockNo" + lockNumber);
        Disposable subscribe = NetHelper.getInstance().autoDelete(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        getView().showDel();
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

    @Override
    public void autoQuery(Context context, QueryAutoBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //查询所有授权人
        publicJsonObject.getUnData().addProperty("pattern", bean.getPattern());
        publicJsonObject.getUnData().addProperty("startRecord", bean.getStartRecord());
        publicJsonObject.getUnData().addProperty("endRecord", bean.getEndRecord());
        if (bean.getAutoStuNo() != null)
            publicJsonObject.getEnData().addProperty("autoStuNo", bean.getAutoStuNo());//可空
        Disposable subscribe = NetHelper.getInstance().searchAuto(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        List<AuthorizeLogBean> logBeans = new ArrayList<>();
                        if (publicGetJsonObject.getList() != null) {
                            JsonArray list = publicGetJsonObject.getList();
                            for (int i = 0; i < list.size(); i++) {
                                AuthorizeLogBean authorizeLogBean = new Gson().fromJson(list.get(i), AuthorizeLogBean.class);
                                if (authorizeLogBean.getIsUse()) { // 筛选出有效记录
                                    logBeans.add(authorizeLogBean);
                                }
                            }
                        }
                        try {
                            getView().showAutoLog(logBeans);
                        } catch (NullPointerException e) {
                            e.getMessage();
                        }
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

package com.yiyun.lockcontroller.presenter.lock;

import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockOpenBean;
import com.yiyun.lockcontroller.bean.lock.QueryOpenLogBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.LockOpenLogContract;
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
 * Created by Layo on 2017-7-27.
 */
public class LockOpenLogPresent extends BaseMvpPresenter<LockOpenLogContract.View> implements LockOpenLogContract.Presenter {

    public LockOpenLogPresent(LockOpenLogContract.View mView) {
        super(mView);
    }

    @Override
    public void searchOpenLog(Context context, QueryOpenLogBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //放入锁的信息
        //将锁信息放入json准备同步放入
        publicJsonObject.getEnData().addProperty("lockNo", bean.getLockNo());
        publicJsonObject.getUnData().addProperty("startRecord", bean.getStartRecord());
        publicJsonObject.getUnData().addProperty("endRecord", bean.getEndRecord());
        if (bean.getStudNoOrName() != null)
            publicJsonObject.getEnData().addProperty("studNoOrName", bean.getStudNoOrName());
        if (bean.getOpenType() != 0)
            publicJsonObject.getEnData().addProperty("openType", bean.getOpenType());
        if (bean.getUserType() != 0)
            publicJsonObject.getEnData().addProperty("userType", bean.getUserType());
        if (bean.getStart() != 0 && bean.getEnd() != 0) {
            publicJsonObject.getEnData().addProperty("start", bean.getStart());
            publicJsonObject.getEnData().addProperty("end", bean.getEnd());
        }
        Disposable subscribe = NetHelper.getInstance().searchOpenLog(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        JsonArray list = publicGetJsonObject.getList();
                        List<LockOpenBean> logBeans = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++) {
                            LockOpenBean authorizeLogBean = new Gson().fromJson(list.get(i), LockOpenBean.class);
                            logBeans.add(authorizeLogBean);
                        }
                        getView().showOpenLog(logBeans);
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

package com.yiyun.lockcontroller.presenter.lock;


import android.content.Context;

import com.yiyun.lockcontroller.bean.lock.LockBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.LockBlueOpenContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Layo on 2017-7-26.
 */
public class LockBlueOpenPresenter extends BaseMvpPresenter<LockBlueOpenContract.View> implements LockBlueOpenContract.Presenter {

    public LockBlueOpenPresenter(LockBlueOpenContract.View mView) {
        super(mView);
    }

    @Override
    public void openLock(Context context, LockBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //放入锁的信息
        //将锁信息放入json准备同步放入
        publicJsonObject.getEnData().addProperty("lockNo", bean.getLockNo());
        publicJsonObject.getEnData().addProperty("cipher", bean.getCipher());
        Disposable subscribe = NetHelper.getInstance().openLock(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        String cipher = publicGetJsonObject.getEnData().get("cipher").getAsString();
                        getView().showData(cipher);
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

package com.yiyun.lockcontroller.presenter.lock;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.LockBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.lock.contract.LockMainContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.bean.lock.LockKeysBean.USER_TYPE_COMMON;
import static com.yiyun.lockcontroller.bean.lock.LockKeysBean.USER_TYPE_TEPORARY;
//
/**
 * Created by Layo on 2018-1-19.
 */

public class LockMainPresenter extends BaseMvpPresenter<LockMainContract.View> implements LockMainContract.Presenter {

    public LockMainPresenter(LockMainContract.View mView) {
        super(mView);
    }


    @Override
    public void openLock(Context context, LockBean bean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //放入锁的信息
        //将锁信息放入json准备同步放入
        publicJsonObject.getEnData().addProperty("lockNo", bean.getLockNo());
        publicJsonObject.getEnData().addProperty("cipher", bean.getCipher());
        Log.i("lock", publicJsonObject.toStringAES());
        Disposable subscribe = NetHelper.getInstance().openLock(publicJsonObject.toStringAES())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        String cipher = publicGetJsonObject.getEnData().get("cipher").getAsString();
                        getView().openRequestSuccess(cipher);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            if (errCode == 10004) { // 10004：AES密钥过期，需要重新登录
                                getView().gotoLoginActivity();
                            } else {
                                getView().openRequestFail("api错误" + errCode);
                            }
                        } else {
                            getView().openRequestFail("错误" + throwable.getMessage());
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
                            // 自己管理的锁
                            if (authorizeLogBean.getUserType() == USER_TYPE_COMMON) {
                                logBeans.add(authorizeLogBean);
                                // 授权给自己的锁
                            } else if (authorizeLogBean.getUserType() == USER_TYPE_TEPORARY) {
                                // 时间段授权
                                if (authorizeLogBean.getAutoType() == AuthorizeLogBean.AUTO_STATE_TIME
                                        || authorizeLogBean.getAutoType() == AuthorizeLogBean.AUTO_STATE_TIME_COUNT) {
                                    // 筛选当前时间段子在授权时间段之内的钥匙
                                    long currentTime = System.currentTimeMillis();
                                    if (authorizeLogBean.getStartTime() < currentTime && currentTime < authorizeLogBean.getEndTime()) {
                                        logBeans.add(authorizeLogBean);
                                    }
                                    // 次数授权
                                } else if (authorizeLogBean.getAutoType() == AuthorizeLogBean.AUTO_STATE_COUNT) {
                                    logBeans.add(authorizeLogBean);
                                }
                            }
                        }
                        LockKeysBean lockKeysBean = new LockKeysBean();
                        lockKeysBean.setAddress("南昌航空大学测试");
                        lockKeysBean.setMac("2C:6B:7D:74:99:07");
                        lockKeysBean.setUserType(LockKeysBean.USER_TYPE_COMMON);
                        logBeans.add(lockKeysBean);
                        getView().searchMyKeysSuccess(logBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            if (errCode == 10004) {
                                getView().gotoLoginActivity();
                            } else {
                                getView().showError("api错误" + errCode);
                            }
                        } else {
                            getView().showError("错误" + throwable.getMessage());
                        }
                    }
                });
        addSubscription(subscribe);
    }

}

package com.yiyun.lockcontroller.presenter.common;

import android.content.Context;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.common.contract.WelcomeContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.orhanobut.logger.Logger;

import org.simeid.sdk.api.SIMeIDRouter;
import org.simeid.sdk.defines.ChannelList;
import org.simeid.sdk.defines.ResultCode;
import org.simeid.sdk.defines.SIMeIDChannel;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.utils.ConstantsUtil.IS_OMA;
import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_PKI;

/**
 * Created by Layo on 2018-1-3.
 */

public class WelcomePresenter extends BaseMvpPresenter<WelcomeContract.View> implements WelcomeContract.Presenter {
    private boolean isFoundComplete = false;
    private boolean isVersionComplete = true;
    private boolean isPkiComplete = false;
    private SIMeIDRouter router;

    public WelcomePresenter(WelcomeContract.View mView) {
        super(mView);
        router = new SIMeIDRouter(App.getInstance());
        App.getInstance().setReaderAttached(router);
    }

    @Override
    public void haveEidCard() {
        //利用rxjava在线程里完成费时操作
        //读取eid卡信息
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                ChannelList channelList = new ChannelList();
                long ret = router.getChannel(channelList);
                Boolean isOMAFound = false;

                if (ret != ResultCode.RC_00.getIndex()) {
                    Logger.v(ResultCode.getEnum(ret).getMeaning() + "(" + ret + ")");
                    e.onNext(false);
                } else {
                    for (SIMeIDChannel channel : channelList.channels) {
                        if (channel == SIMeIDChannel.CH_OMA) {
                            isOMAFound = true;
                            break;
                        }
                    }
                    e.onNext(isOMAFound);
                }
                e.onComplete();
            }
        })
                .compose(RxOperator.<Boolean>rxSchedulerTransformer())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) throws Exception {
                        //接收到判断有没有EID卡
                        SPUtil.getInstance().putBoolean(IS_OMA, b);
                        isFoundComplete = true;
                        //版本更新检测成功后可跳转
                        taskComplete();
                    }

                });

    }

    @Override
    public void updateVersion() {
        // TODO: 2018-1-3 版本更新的内容检测
    }

    @Override
    public void requestPki(Context context) {
        // TODO: 2018-1-19 制作缓存功能，在有效天数内或可无需请求
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        //放入锁的信息
        Disposable subscribe = NetHelper.getInstance().getPki(publicJsonObject.toString())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        String pki = bean.getUnEncrypt().getData().get("pkiKey").getAsString();
                        SPUtil.getInstance().putString(USER_PKI, pki);
                        isPkiComplete = true;
                        taskComplete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        throwable.printStackTrace();
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

    /**
     * 检测有没有同时完成所有准备任务，都完成后调用跳转
     */
    private void taskComplete() {
       // if (isFoundComplete & isVersionComplete & isPkiComplete)
            getView().eidIsComplete();
    }
}

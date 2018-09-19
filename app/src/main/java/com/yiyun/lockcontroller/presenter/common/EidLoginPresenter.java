package com.yiyun.lockcontroller.presenter.common;


import android.content.Context;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutParameter;
import com.yiyun.lockcontroller.presenter.common.contract.EidLoginContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.yiyun.lockcontroller.utils.ConverUtil;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.yiyun.lockcontroller.utils.rc4.AES;
import com.orhanobut.logger.Logger;

import org.simeid.sdk.api.SIMeIDRouter;
import org.simeid.sdk.defines.ResultCode;
import org.simeid.sdk.defines.SIMeIDInfo;
import org.simeid.sdk.defines.StringResult;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.utils.ConstantsUtil.IS_OMA;
import static com.yiyun.lockcontroller.utils.ConstantsUtil.USER_AES;

/**
 * 登录逻辑p层
 * Created by Layo on 2017-12-28.
 */

public class EidLoginPresenter extends BaseMvpPresenter<EidLoginContract.View> implements EidLoginContract.Presenter {
    private boolean isOMAFound = false;
    private SIMeIDRouter router = null;
    //OMA1,2步参数
    private SIMeIDInfo info;
    //OMA3，4步参数
    private String bizId;
    private String signPacket;

    public EidLoginPresenter(EidLoginContract.View mView) {
        super(mView);
        isOMAFound = SPUtil.getInstance().getBoolean(IS_OMA);
        router = (SIMeIDRouter) App.getInstance().getReaderAttached();
    }

    @Override
    public void login(Context context, String mobileNo, String password, boolean isOMAFound) {
        if (!isOMAFound) {
            //S模式匿名认证
            // TODO: 2018-1-2 实现存储中获取手机号码
            Logger.i("SMS登录");
            loginWithSMS(context, mobileNo, password);
        } else {
            //O模式匿名认证
            Logger.i("OMA登录");
            //第1阶段：（读取SIMeID卡内的）载体标识
            if (!readIdCarrier())
                return;
            //第2阶段：（向相应服务后台）请求签名指令数据
            reqSignCMD(context, password);

        }
    }

    private void loginWithSMS(Context context, String mobileNO, String password) {
        //生成一个AES密钥
        final String aesKey = AES.CreateAES();
//        SPUtil.getInstance().putString(USER_NAME, mobileNO);
        SPUtil.getInstance().putString(USER_AES, aesKey);

        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("mobile_no", mobileNO);
        publicJsonObject.getEnData().addProperty("data_to_sign", password);
        publicJsonObject.getEnData().addProperty("aesKey", aesKey);

        Disposable subscribe = NetHelper.getInstance().loginWithSMS(publicJsonObject.toStringRSA())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        Logger.i("验证成功");
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        String appeidcode = publicGetJsonObject.getEnData().get("appeidcode").getAsString();
                        String userName = publicGetJsonObject.getEnData().get("username").getAsString();
                        SPUtil.getInstance().putString(PublicPutParameter.APP_USER_EID, appeidcode);
                        SPUtil.getInstance().putString(PublicPutParameter.USER_NAME, userName);
                        getView().loginSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d("" + throwable.getMessage());
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showError("api错误" + errCode);
                        } else {
                            getView().showError("错误" + throwable.getMessage());
                        }
                        getView().loginFail();
                    }
                });
        addSubscription(subscribe);
    }

    private boolean readIdCarrier() {
        info = new SIMeIDInfo();
        long ret = router.getSIMeIDInfo(info);
        if (ResultCode.RC_00.getIndex() != ret) {
            Logger.d(ResultCode.getEnum(ret).getMeaning() + "(" + ret + ")");
            getView().loginFail();
            return false;
        }
        return true;
    }

    private void reqSignCMD(final Context context, String password) {
        String idcarrier = info.idcarrier;
        //网络请求数据
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("idcarrier", idcarrier);
        publicJsonObject.getEnData().addProperty("data_to_sign", password);

        Disposable subscribe = NetHelper.getInstance().getAPDUWithOMA(publicJsonObject.toStringRSA())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        Logger.json(publicGetJsonObject.getUnData().toString());
                        bizId = publicGetJsonObject.getUnData().get("biz_id").getAsString();
                        String apdu = publicGetJsonObject.getUnData().get("apdu").getAsString();
                        //第3阶段：（在SIMeID卡内）签名
                        if (transmitSignCMD(apdu)) {
                            //第4阶段：（提交到相应服务后台去）验签
                            loginWithOMA(context);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showError("api错误" + errCode);
                        } else {
                            getView().showError("错误" + throwable.getMessage());
                        }
                        getView().loginFail();
                    }
                });
        addSubscription(subscribe);
    }

    private boolean transmitSignCMD(String apdu) {
        byte[] cmd = ConverUtil.hexString2Bytes(apdu);
        StringResult recv = new StringResult();
        long ret = router.sendApduAndGetResponse(cmd, recv);
        signPacket = recv.data;

        if (ResultCode.RC_00.getIndex() != ret || signPacket == null) {
            Logger.d(ResultCode.getEnum(ret).getMeaning() + "(" + router.getLastError() + ")");
            getView().loginFail();
            return false;
        }
        return true;
    }

    private void loginWithOMA(Context context) {
        //生成一个AES密钥
        final String aesKey = AES.CreateAES();
        SPUtil.getInstance().putString(USER_AES, aesKey);

        //网络请求数据
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("biz_id", bizId);
        publicJsonObject.getEnData().addProperty("sign_packet", signPacket);
        publicJsonObject.getEnData().addProperty("aesKey", aesKey);

        Disposable subscribe = NetHelper.getInstance().loginWithOMA(publicJsonObject.toStringRSA())
                .map(new HTTPResultFunc<LockDownBean>())
                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
                .subscribe(new Consumer<LockDownBean>() {
                    @Override
                    public void accept(@NonNull LockDownBean bean) throws Exception {
                        Logger.i("验证成功");
                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
                        String appeidcode = publicGetJsonObject.getEnData().get("appeidcode").getAsString();
                        String userName = publicGetJsonObject.getEnData().get("username").getAsString();
                        SPUtil.getInstance().putString(PublicPutParameter.APP_USER_EID, appeidcode);
                        SPUtil.getInstance().putString(PublicPutParameter.USER_NAME, userName);
                        getView().loginSuccess();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Logger.d(throwable.getMessage());
                        if (throwable instanceof ApiException) {
                            int errCode = ((ApiException) throwable).getErrCode();
                            getView().showError("api错误" + errCode);
                        } else {
                            getView().showError("错误" + throwable.getMessage());
                        }
                        getView().loginFail();
                    }
                });
        addSubscription(subscribe);
    }
}

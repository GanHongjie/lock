package com.yiyun.lockcontroller.presenter.common;

import android.content.Context;
import android.text.TextUtils;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.bean.common.RegisterMsgBean;
import com.yiyun.lockcontroller.bean.lock.LockDownBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.net.HTTPResultFunc;
import com.yiyun.lockcontroller.net.NetHelper;
import com.yiyun.lockcontroller.net.lock.PublicGetJsonObject;
import com.yiyun.lockcontroller.net.lock.PublicPutJsonObject;
import com.yiyun.lockcontroller.presenter.common.contract.EidRegisterContract;
import com.yiyun.lockcontroller.rxjava.RxOperator;
import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;
import com.yiyun.lockcontroller.utils.ConverUtil;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;

import org.simeid.sdk.api.SIMeIDRouter;
import org.simeid.sdk.defines.ResultCode;
import org.simeid.sdk.defines.SIMeIDInfo;
import org.simeid.sdk.defines.StringResult;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

import static com.yiyun.lockcontroller.utils.ConstantsUtil.IS_OMA;

/**
 * Created by Layo on 2018-1-3.
 */

public class EidRegisterPresenter extends BaseMvpPresenter<EidRegisterContract.View> implements EidRegisterContract.Presenter {

    private SIMeIDRouter router = null;
    //OMA1,2步参数
    private SIMeIDInfo info;
    //OMA3，4步参数
    private String bizId;
    private String signPacket;
    private boolean isOMAFound;

    public EidRegisterPresenter(EidRegisterContract.View mView) {
        super(mView);
        router = (SIMeIDRouter) App.getInstance().getReaderAttached();
        isOMAFound = SPUtil.getInstance().getBoolean(IS_OMA);
    }

    @Override
    public void register(Context context, RegisterMsgBean registerMsgBean, boolean isOMAFound) {
        //登录自动判断用S或者O
        if (!isOMAFound) {
            //S模式身份识别
            realNameWithSMS(context, registerMsgBean);
        } else {
            //O模式身份识别
            //第1阶段：（读取SIMeID卡内的）载体标识
            if (!readIdCarrier())
                return;
            //第2阶段：（向相应服务后台）请求签名指令数据
            reqSignCMD(context, registerMsgBean);
        }
    }

    private boolean readIdCarrier() {
        info = new SIMeIDInfo();
        long ret = router.getSIMeIDInfo(info);
        if (ResultCode.RC_00.getIndex() != ret) {
            Logger.d(ResultCode.getEnum(ret).getMeaning() + "(" + ret + ")");
            getView().registerFail();
            return false;
        }
        return true;
    }

    private void reqSignCMD(final Context context, final RegisterMsgBean registerMsgBean) {
        String idcarrier = info.idcarrier;
        //网络请求数据
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("idcarrier", idcarrier);
        publicJsonObject.getEnData().addProperty("data_to_sign", registerMsgBean.getPassword());
//        publicJsonObject.getEnData().addProperty("aesKey", aesKey);

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
                            realNameWithOMA(context, registerMsgBean);
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
                        getView().registerFail();
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
            getView().registerFail();
            return false;
        }
        return true;
    }

    private void realNameWithSMS(Context context, RegisterMsgBean registerMsgBean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("mobile_no", registerMsgBean.getPhoneNo());
        publicJsonObject.getEnData().addProperty("data_to_sign", registerMsgBean.getPassword());
        if (!TextUtils.isEmpty(registerMsgBean.getUserName()))
            publicJsonObject.getEnData().addProperty("name", registerMsgBean.getUserName());
        if (!TextUtils.isEmpty(registerMsgBean.getUserIdNo()))
            publicJsonObject.getEnData().addProperty("idnum", registerMsgBean.getUserIdNo());
        //发送网络请求
        Disposable subscribe = NetHelper.getInstance().realNameWithSMS(publicJsonObject.toStringRSA())
                .compose(RxOperator.<JsonObject>rxSchedulerTransformer())
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(@NonNull JsonObject json) throws Exception {
                        Logger.json(json.toString());
                        if (!json.get("state").getAsString().equals("0")) {
                            getView().registerFail();
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
                    }
                });
        addSubscription(subscribe);
    }

    private void realNameWithOMA(Context context, RegisterMsgBean registerMsgBean) {
        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
        publicJsonObject.getEnData().addProperty("biz_id", bizId);
        publicJsonObject.getEnData().addProperty("sign_packet", signPacket);
        if (!TextUtils.isEmpty(registerMsgBean.getUserName()))
            publicJsonObject.getEnData().addProperty("name", registerMsgBean.getUserName());
        if (!TextUtils.isEmpty(registerMsgBean.getUserIdNo()))
            publicJsonObject.getEnData().addProperty("idnum", registerMsgBean.getUserIdNo());

        //发送网络请求
        Disposable subscribe = NetHelper.getInstance().realNameWithOMA(publicJsonObject.toStringRSA())
                .compose(RxOperator.<JsonObject>rxSchedulerTransformer())
                .subscribe(new Consumer<JsonObject>() {
                    @Override
                    public void accept(@NonNull JsonObject json) throws Exception {
                        Logger.json(json.toString());
                        JsonObject jsonData = json.get("data").getAsJsonObject();
                        if (!json.get("state").getAsString().equals("0") || bizId == null) {
                            //请求失败返回错误
                            Logger.d(json.get("errCode").getAsString());
                            getView().registerFail();
                        } else {
                            Logger.i("验证成功");
                            getView().registerSuccess();
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
                    }
                });
        addSubscription(subscribe);
    }
}

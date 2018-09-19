package com.yiyun.lockcontroller.presenter;

import com.yiyun.lockcontroller.ui.base.BaseMvpPresenter;

/**
 * 主activity的逻辑层
 * 负责统领所有的内容 如查询用户拥有钥匙
 * Created by Layo on 2018-1-19.
 */

public class MainPresenter extends BaseMvpPresenter<MainContract.View> implements MainContract.Presenter {

    public MainPresenter(MainContract.View mView) {
        super(mView);
    }

//    @Override
//    public void searchMyKeys(Context context) {
//        PublicPutJsonObject publicJsonObject = new PublicPutJsonObject(context);
//        publicJsonObject.getUnData().addProperty("startRecord", 0);
//        publicJsonObject.getUnData().addProperty("endRecord", 999);
//
//        Disposable subscribe = NetHelper.getInstance().searchMyKeys(publicJsonObject.toStringAES())
//                .map(new HTTPResultFunc<LockDownBean>())
//                .compose(RxOperator.<LockDownBean>rxSchedulerTransformer())
//                .subscribe(new Consumer<LockDownBean>() {
//                    @Override
//                    public void accept(@NonNull LockDownBean bean) throws Exception {
//                        PublicGetJsonObject publicGetJsonObject = new PublicGetJsonObject(bean);
//                        JsonArray list = publicGetJsonObject.getList();
//                        List<LockKeysBean> logBeans = new ArrayList<>();
//                        for (int i = 0; i < list.size(); i++) {
//                            LockKeysBean authorizeLogBean = new Gson().fromJson(list.get(i), LockKeysBean.class);
//                            logBeans.add(authorizeLogBean);
//                            if (authorizeLogBean.getUserType() == USER_TYPE_COMMON) {
//                                SPUtil.getInstance().putString(USER_LOCK_ADDRESS, authorizeLogBean.getAddress());
//                                SPUtil.getInstance().putString(authorizeLogBean.getAddress() + USER_LOCK_MAC, authorizeLogBean.getMac());
//                                SPUtil.getInstance().putString(authorizeLogBean.getAddress() + USER_LOCK_NO, authorizeLogBean.getLockNo());
//                            }
//                        }
//                        getView().searchSuccess(logBeans);
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(@NonNull Throwable throwable) throws Exception {
//                        if (throwable instanceof ApiException) {
//                            int errCode = ((ApiException) throwable).getErrCode();
//                            getView().showError("api错误" + errCode);
//                        } else {
//                            getView().showError("错误" + throwable.getMessage());
//                        }
//                    }
//                });
//        addSubscription(subscribe);
//    }
}

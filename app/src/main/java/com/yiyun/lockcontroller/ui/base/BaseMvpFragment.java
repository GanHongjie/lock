package com.yiyun.lockcontroller.ui.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyun.lockcontroller.utils.DialogUtil;
import com.yiyun.lockcontroller.utils.ToastUtil;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

public abstract class BaseMvpFragment<T extends BasePresenter> extends
        Fragment implements BaseView {
    protected T mPresenter;
    protected Activity mActivity;
//    protected ProgressDialog progressDialog = null;
    protected Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        return createView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
        mPresenter = initPresenter();
    }

    protected abstract T initPresenter();

    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState);

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void showSnackBar(String msg, Prompt prompt) {
        final ViewGroup viewGroup = (ViewGroup) mActivity.findViewById(android.R.id.content).getRootView();
        TSnackbar snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_SHORT, APP_DOWn);
        snackBar.setPromptThemBackground(prompt);
        snackBar.show();
    }

    @Override
    public void showLoading(String msg) {
        if (dialog == null) {
            dialog = DialogUtil.showWaiting(mActivity, msg);
        } else {
            stopLoading();
            dialog = DialogUtil.showWaiting(mActivity, msg);
        }
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(mActivity);
////            progressDialog.setTitle(getResources().getString(R.string.loading_title));
//            progressDialog.setMessage(msg);
//            progressDialog.setCancelable(false);
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
//        } else {
//            progressDialog.setMessage(msg);
//        }
    }

    @Override
    public void stopLoading() {
//        if (null != progressDialog && progressDialog.isShowing()) {
//            progressDialog.hide();
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
        if (null != dialog && dialog.isShowing()) {
            dialog.hide();
            dialog.dismiss();
            dialog = null;
        }
    }
}

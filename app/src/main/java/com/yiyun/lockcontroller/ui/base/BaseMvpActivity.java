package com.yiyun.lockcontroller.ui.base;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.utils.DialogUtil;
import com.yiyun.lockcontroller.utils.ScreenUtil;
import com.yiyun.lockcontroller.utils.ToastUtil;
import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

/**
 * Created by layo on 2017/7/17.
 */

public abstract class BaseMvpActivity<T extends BasePresenter> extends
        AppCompatActivity implements BaseView {
    protected T mPresenter;
    //    protected ProgressDialog progressDialog = null;
    protected Dialog dialog;


    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = initPresenter();
        App.getInstance().addActivity(this);
        create(savedInstanceState);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && getCurrentFocus() instanceof EditText) {
            ScreenUtil.hideInputForce(this);
        }
        return super.dispatchTouchEvent(ev);
    }

    // 实例化presenter
    protected abstract T initPresenter();

    // 设置布局文件
    protected abstract void create(Bundle savedInstanceState);

    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(msg);
    }

    @Override
    public void showSnackBar(String msg, Prompt prompt) {
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content).getRootView();
        TSnackbar snackBar = TSnackbar.make(viewGroup, msg, TSnackbar.LENGTH_SHORT, APP_DOWn);
        snackBar.setPromptThemBackground(prompt);
        snackBar.show();
    }

    @Override
    public void showLoading(String msg) {
        if (dialog == null) {
            dialog = DialogUtil.showWaiting(this, msg);
        } else {
            stopLoading();
            dialog = DialogUtil.showWaiting(this, msg);
        }
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
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
        if (null != dialog && dialog.isShowing()) {
            dialog.hide();
            dialog.dismiss();
            dialog = null;
        }
//        if (null != progressDialog && progressDialog.isShowing()) {
//            progressDialog.cancel();
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
    }
}

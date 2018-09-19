package com.yiyun.lockcontroller.ui.base;

import com.trycatch.mysnackbar.Prompt;
import com.trycatch.mysnackbar.TSnackbar;

/**
 * Created by layo on 2017/7/17.
 */

public interface BaseView {
    //snackbar的内容
    int APP_DOWn = TSnackbar.APPEAR_FROM_TOP_TO_DOWN;

    void showError(CharSequence msg);
    void showToast(String msg);
    void showSnackBar(String msg,Prompt prompt);
    void showLoading(String msg);
    void stopLoading();
}

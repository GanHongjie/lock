package com.yiyun.lockcontroller.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.common.RegisterMsgBean;
import com.yiyun.lockcontroller.presenter.common.EidRegisterPresenter;
import com.yiyun.lockcontroller.presenter.common.contract.EidRegisterContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.orhanobut.logger.Logger;
import com.trycatch.mysnackbar.Prompt;

import java.util.regex.Pattern;

/**
 * Created by Layo on 2018-1-3.
 */

public class RegisterActivity extends BaseMvpActivity<EidRegisterContract.Presenter> implements EidRegisterContract.View {

    private static final boolean DEBUG = false && App.DEBUG;

    private TextView tvReturn;
    private EditText etUsername;
    private EditText etPhone;
    private EditText etIdCard;
    private EditText etPassward;
    private EditText etCheckPassward;
    private Button btOk;

    @Override
    protected EidRegisterContract.Presenter initPresenter() {
        return new EidRegisterPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.eid_register_activity);
        initFindId();

    }

    private void old() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("SMS身份识别");
                RegisterMsgBean registerMsgBean = new RegisterMsgBean("18770099843", "123", "黄聪", "362527199505280514");
                mPresenter.register(getApplicationContext(), registerMsgBean, false);
            }
        });
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i("OMA身份识别");
                RegisterMsgBean registerMsgBean = new RegisterMsgBean("18770099843", "123", "黄聪", "362527199505280514");
                mPresenter.register(getApplicationContext(), registerMsgBean, true);
            }
        });
    }

    private void initFindId() {
        tvReturn = (TextView) findViewById(R.id.tv_return);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etIdCard = (EditText) findViewById(R.id.et_idCard);
        etPassward = (EditText) findViewById(R.id.et_passward);
        etCheckPassward = (EditText) findViewById(R.id.et_check_passward);
        btOk = (Button) findViewById(R.id.bt_ok);
        if (DEBUG) {
            etUsername.setText("陈剑辉");
            etPhone.setText("15570146381");
            etIdCard.setText("360732199605283659");
        }
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!judgeFormat())
                    return;

//                boolean isOMAFound = SPUtil.getInstance().getBoolean(IS_OMA);
                boolean isOMAFound = false;
                String phoneNo = "";
                if (!isOMAFound)
                    phoneNo = etPhone.getText().toString();
                String username = etUsername.getText().toString();
                String idCard = etIdCard.getText().toString();
                RegisterMsgBean msgBean = new RegisterMsgBean(phoneNo, "123", username, idCard);

                mPresenter.register(getApplicationContext(), msgBean, isOMAFound);
            }
        });
    }

    private boolean judgeFormat() {
        boolean isErr = false;
        if (TextUtils.isEmpty(etUsername.getText())) {
            isErr = true;
            etUsername.setError(getString(R.string.register_err2));
        }
        if (!Pattern.matches("^1(3|4|5|7|8)\\d{9}$", etPhone.getText())) {
            isErr = true;
            etPhone.setError(getString(R.string.login_err1));
        }
        if (!Pattern.matches("(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)", etIdCard.getText())) {
            isErr = true;
            etIdCard.setError(getString(R.string.register_err3));
        }
        if (!etCheckPassward.getText().toString().equals(etPassward.getText().toString())) {
            isErr = true;
            etCheckPassward.setError(getString(R.string.register_err4));
        }

        if (isErr) {
            showSnackBar(getString(R.string.login_warning), Prompt.WARNING);
            return false;
        } else
            return true;
    }

    @Override
    public void showError(CharSequence msg) {

    }

    @Override
    public void registerSuccess() {
        showSnackBar(getString(R.string.register_success), Prompt.SUCCESS);
    }

    @Override
    public void registerFail() {
        showSnackBar(getString(R.string.register_err0), Prompt.ERROR);
    }

}

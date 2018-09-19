package com.yiyun.lockcontroller.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.presenter.common.EidLoginPresenter;
import com.yiyun.lockcontroller.presenter.common.contract.EidLoginContract;
import com.yiyun.lockcontroller.ui.MainActivity;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.yiyun.lockcontroller.utils.ScreenUtil;
import com.trycatch.mysnackbar.Prompt;

import java.util.regex.Pattern;

import static com.yiyun.lockcontroller.utils.ConstantsUtil.IS_OMA;

public class LoginActivity extends BaseMvpActivity<EidLoginPresenter> implements EidLoginContract.View {

    private static final String KEY_USERNAME = "key_username";

    private EditText etUsername;
    //    private EditText etPassword;
    private Button btOk;
    private Button btRegister;

    private boolean isOMAFound;


    @Override
    protected EidLoginPresenter initPresenter() {
        return new EidLoginPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.eid_login_activity);

        isOMAFound = SPUtil.getInstance().getBoolean(IS_OMA);
        initFindId();
        initConfig();
    }

    private void initConfig() {
//        isOMAFound = SPUtil.getInstance().getBoolean(IS_OMA);
        findViewById(R.id.tv_tint).setVisibility(isOMAFound ? View.VISIBLE : View.GONE);
        findViewById(R.id.ll_edit).setVisibility(isOMAFound ? View.GONE : View.VISIBLE);
    }

    private void initFindId() {
        etUsername = (EditText) findViewById(R.id.et_username);
        if (App.DEBUG) {
            etUsername.setText("15570146381");
        }
        String username = SPUtil.getInstance().getString(KEY_USERNAME, null);
        if (!TextUtils.isEmpty(username)) {
            etUsername.setText(username);
        }
//        etPassword = findViewById(R.id.et_password);
        btOk = (Button) findViewById(R.id.bt_ok);
        btRegister = (Button) findViewById(R.id.bt_register);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ScreenUtil.isValidClick())
                    return;
                if (!judgeFormat())
                    return;
                String mobileNo = "";
                if (!isOMAFound) {
                    mobileNo = etUsername.getText().toString();
                    SPUtil.getInstance().putString(KEY_USERNAME, mobileNo);
                }
//               String passward = etPassword.getText().toString();
                showLoading("正在登录…");
                mPresenter.login(getApplicationContext(), mobileNo, "123", isOMAFound);
            }
        });

    }

    //判断输入信息格式
    private boolean judgeFormat() {
        boolean isErr = false;
        if (!Pattern.matches("^1(3|4|5|7|8)\\d{9}$", etUsername.getText())) {
            isErr = true;
            etUsername.setError(getString(R.string.login_err1));
        }

        if (isErr) {
            showSnackBar(getString(R.string.login_warning), Prompt.WARNING);
            return false;
        } else
            return true;
    }

    @Override
    public void showError(CharSequence msg) {
        showToast(msg.toString());
    }

    @Override
    public void loginSuccess() {
        stopLoading();
        showSnackBar(getString(R.string.login_success), Prompt.SUCCESS);
        startActivity(new Intent().setClass(this, MainActivity.class));
        // finish();
    }

    @Override
    public void loginFail() {
        stopLoading();
        startActivity(new Intent().setClass(this, MainActivity.class));
        //showSnackBar(getString(R.string.login_err0), Prompt.ERROR);  2018.9.19
    }

}

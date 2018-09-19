package com.yiyun.lockcontroller.ui;

import android.os.Bundle;
import android.view.View;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.presenter.common.TestPresenter;
import com.yiyun.lockcontroller.presenter.common.contract.TestContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.utils.ToastUtil;
import com.yiyun.lockcontroller.view.ripple_button.RippleDiffuse;

/**
 * Created by Layo on 2018-1-15.
 */

public class TestActivity extends BaseMvpActivity<TestContract.Presenter> implements TestContract.View {
    private RippleDiffuse mRdButton;


    @Override
    protected TestContract.Presenter initPresenter() {
        return new TestPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.test_activity);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.test1(getApplicationContext());
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.test2(getApplicationContext());
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.test3(getApplicationContext());
            }
        });

        mRdButton = (RippleDiffuse) findViewById(R.id.rd_button);

        mRdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRdButton.setAnimationEach(1000);
                mRdButton.showWaveAnimation();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRdButton.showWaveAnimation();

    }

    @Override
    public void showError(CharSequence msg) {

    }

    @Override
    public void success() {

    }

    @Override
    public void fail() {

    }

    @Override
    public void showApiError(int errCode) {
        ToastUtil.showToast("" + errCode);

    }
}

package com.yiyun.lockcontroller.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.LinearLayout;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.presenter.MainContract;
import com.yiyun.lockcontroller.presenter.MainPresenter;
import com.yiyun.lockcontroller.ui.auto.AutoMainFragment;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.ui.defend.DefendMainFragment;
import com.yiyun.lockcontroller.ui.lock.LockMainFragment;
import com.yiyun.lockcontroller.utils.ScreenUtil;
import com.yiyun.lockcontroller.utils.ToastUtil;


/**
 * 主页的activity
 * Created by Layo on 2018-1-30.
 */

public class MainActivity extends BaseMvpActivity<MainContract.Presenter>
        implements MainContract.View, View.OnClickListener {

    public static final int AUTO_FRAGMENT = 1;
    public static final int LOCK_FRAGMENT = 2;
    public static final int DEFEND_FRAGMENT = 3;

    private LinearLayout mLlButton1;
    private LinearLayout mLlButton2;
    private LinearLayout mLlButton3;
    private int nowFragment = 0;
//    public List<LockKeysBean> logBeans = new ArrayList<>();
    private Fragment fragmentAutoMain;
    private Fragment fragmentLockMain;
    private Fragment fragmentDefendMain;

    @Override
    protected MainContract.Presenter initPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.main_activity);
        initFindId();
        ScreenUtil.fullScreenTop(this);
        fragmentAutoMain = new AutoMainFragment();
        fragmentLockMain = new LockMainFragment();
        fragmentDefendMain = new DefendMainFragment();
        //初始化fm
        nowFragment = LOCK_FRAGMENT;
        transFragment(new LockMainFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mPresenter.searchMyKeys(getApplicationContext());
    }

    private void transFragment(Fragment fragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(fragmentAutoMain).remove(fragmentLockMain)
                .remove(fragmentDefendMain).commit();
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    private void initFindId() {
        mLlButton1 = (LinearLayout) findViewById(R.id.ll_button1);
        mLlButton2 = (LinearLayout) findViewById(R.id.ll_button2);
        mLlButton3 = (LinearLayout) findViewById(R.id.ll_button3);

        mLlButton1.setOnClickListener(this);
        mLlButton2.setOnClickListener(this);
        mLlButton3.setOnClickListener(this);
    }

    @Override
    public void showError(CharSequence msg) {

    }

//    @Override
//    public void searchSuccess(List<LockKeysBean> logBeans) {
//        Log.i("lock", "my keys =" + new Gson().toJson(logBeans));
//        this.logBeans = logBeans;
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_button1:
                if (nowFragment != AUTO_FRAGMENT) {
                    nowFragment = AUTO_FRAGMENT;
                    transFragment(new AutoMainFragment());
                }
                break;
            case R.id.ll_button2:
                if (nowFragment != LOCK_FRAGMENT) {
                    nowFragment = LOCK_FRAGMENT;
                    transFragment(new LockMainFragment());
                }
                break;
            case R.id.ll_button3:
                if (nowFragment != DEFEND_FRAGMENT) {
                    nowFragment = DEFEND_FRAGMENT;
                    transFragment(new DefendMainFragment());
                }
                break;
        }
    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast("再按一次确认退出");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}

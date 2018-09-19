package com.yiyun.lockcontroller.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.presenter.common.WelcomePresenter;
import com.yiyun.lockcontroller.presenter.common.contract.WelcomeContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.ui.login.LoginActivity;

/**
 * Created by Layo on 2018-1-19.
 */

public class WelcomeActivity extends BaseMvpActivity<WelcomeContract.Presenter>
        implements WelcomeContract.View {
    private static final int REQUEST_CODE_PERMISSIONS_BLUETOOTH = 1;
    @Override
    protected WelcomeContract.Presenter initPresenter() {
        return new WelcomePresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.welcome_activity);
        String[] permissions;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            permissions = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_PRIVILEGED, // api 19
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            permissions = new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_LOGS,
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.VIBRATE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.READ_EXTERNAL_STORAGE};
        }
        boolean isGranted = true;
        for (int i=0; i< permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(WelcomeActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
                break;
            }
        }
        if (isGranted) {
            mPresenter.haveEidCard();
            mPresenter.requestPki(getApplicationContext());
        } else {
            ActivityCompat.requestPermissions(WelcomeActivity.this, permissions, REQUEST_CODE_PERMISSIONS_BLUETOOTH);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("lock", "onRequestPermissionsResult ");
        if (requestCode == REQUEST_CODE_PERMISSIONS_BLUETOOTH) {
            Log.i("lock", "requestCode == REQUEST_CODE_PERMISSIONS_BLUETOOTH");
            if (grantResults != null && grantResults.length > 0) {
                Log.i("lock", "检查授权");
                boolean isGranted = true;
                for (int i=0; i< grantResults.length; i++) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        isGranted = false;
                        break;
                    }
                }
                if (isGranted) {
                    Log.i("lock", "授权成功");
                    mPresenter.haveEidCard();
                    mPresenter.requestPki(getApplicationContext());
                }
            } else {
                Log.i("lock", "拒绝授权");
                Toast.makeText(getApplicationContext(), "拒绝授权", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void showError(CharSequence msg) {

    }

    @Override
    public void eidIsComplete() {
        startActivity(new Intent().setClass(this, LoginActivity.class));
        finish();
    }

    @Override
    public void versionSuccess() {

    }

    @Override
    public void versionFail() {

    }

    @Override
    public void onBackPressed() {

    }
}

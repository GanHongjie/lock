package com.yiyun.lockcontroller.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.net.lock.PublicPutParameter;
import com.yiyun.lockcontroller.ui.auto.MyQrCodeActivity;
import com.yiyun.lockcontroller.ui.lock.MyKeysActivity;
import com.yiyun.lockcontroller.ui.lock.OpenLogActivity;
import com.yiyun.lockcontroller.utils.SPUtil;

public class UserCenterActivity extends AppCompatActivity implements View.OnClickListener {

    private View vLayoutMyKeys;
    private View vLayoutOpenLockHistory;
    private View vImageBack;
    private TextView vTextUserName;
    private View vLayoutMyQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        vLayoutOpenLockHistory = findViewById(R.id.vLayoutOpenLockHistory);
        vLayoutMyKeys = findViewById(R.id.vLayoutMyKeys);
        vImageBack = findViewById(R.id.vImageBack);
        vTextUserName = findViewById(R.id.vTextUserName);
        vLayoutMyQrCode = findViewById(R.id.vLayoutMyQrCode);

        vLayoutOpenLockHistory.setOnClickListener(this);
        vLayoutMyKeys.setOnClickListener(this);
        vImageBack.setOnClickListener(this);
        vLayoutMyQrCode.setOnClickListener(this);

        final String userName = SPUtil.getInstance().getString(PublicPutParameter.USER_NAME);
        vTextUserName.setText(userName);
    }

    @Override
    public void onClick(View view) {
        if (view == vLayoutOpenLockHistory){
            startActivity(new Intent(UserCenterActivity.this, OpenLogActivity.class));
        } else if (view == vLayoutMyKeys) {
            startActivity(new Intent(UserCenterActivity.this, MyKeysActivity.class));
        } else if (view == vImageBack) {
            finish();
        } else if (view == vLayoutMyQrCode) {
            Intent intent = new Intent(this, MyQrCodeActivity.class);
            startActivity(intent);
        }
    }
}

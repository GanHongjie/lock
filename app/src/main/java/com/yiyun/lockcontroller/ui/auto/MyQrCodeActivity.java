package com.yiyun.lockcontroller.ui.auto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.net.lock.PublicPutParameter;
import com.yiyun.lockcontroller.presenter.auto.MyQrCodeContract;
import com.yiyun.lockcontroller.presenter.auto.MyQrCodePresenter;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.utils.PixelUtil;
import com.yiyun.lockcontroller.utils.SPUtil;
import com.yzq.zxinglibrary.encode.CodeCreator;

import static com.yiyun.lockcontroller.net.lock.PublicPutParameter.APP_USER_EID;

/**
 * Created by Administrator on 2018-4-16.
 */

public class MyQrCodeActivity  extends BaseMvpActivity<MyQrCodePresenter> implements MyQrCodeContract.View,
        View.OnClickListener {

    private ImageView vImageBack;
    private ImageView vImageMyQrCode;

    @Override
    protected MyQrCodePresenter initPresenter() {
        return new MyQrCodePresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_qr_code);
        vImageMyQrCode = findViewById(R.id.vImageMyQrCode);
        vImageBack = findViewById(R.id.vImageBack);
        vImageBack.setOnClickListener(this);

        final String userName = SPUtil.getInstance().getString(PublicPutParameter.USER_NAME);
        Bitmap bitmap = null;
        try {
            Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_lock);
            final int width = PixelUtil.dip2px(getApplicationContext(), 250);
            bitmap = CodeCreator.createQRCode(userName, width, width, logo);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        if (bitmap != null) {
            vImageMyQrCode.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == vImageBack) {
            finish();
        }
    }

    @Override
    public void showError(CharSequence msg) {

    }
}

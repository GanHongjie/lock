package com.yiyun.lockcontroller.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yiyun.lockcontroller.R;
import com.sdsmdg.harjot.vectormaster.VectorMasterView;
import com.sdsmdg.harjot.vectormaster.models.GroupModel;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 等待时显示的dialog，有沙漏旋转的效果
 * 利用.show来显示  .
 * .dismiss来隐藏
 * Created by Layo on 2017-7-15.
 */
public class DialogLoading extends Dialog {
    private AppCompatActivity appCompatActivity;
    private VectorMasterView hourglassView;
    private Timer timer;
    private float translationY = 0;
    private float rotation = 0;
    private int state = 0;

    /**
     * @param appCompatActivity 当前使用的activity
     */
    public DialogLoading(AppCompatActivity appCompatActivity) {
        super(appCompatActivity, R.style.LoadDialogTheme);
        this.appCompatActivity = appCompatActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_load_dialog);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        hourglassView = (VectorMasterView) findViewById(R.id.hg_vector);
        animateHourglass();
    }

    private void animateHourglass() {
        final GroupModel frame = hourglassView.getGroupModelByName("hourglass_frame");
        final GroupModel fillOutlines = hourglassView.getGroupModelByName("fill_outlines");
        final GroupModel fillOutlinesPivot = hourglassView.getGroupModelByName("fill_outlines_pivot");
        final GroupModel group_fill_path = hourglassView.getGroupModelByName("group_fill_path");

        state = 0;
        translationY = -24;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (state == 0) {    // Slide the clip-path down by changing translationY of parent group
                    translationY += 0.3f;
                    fillOutlinesPivot.setTranslateY(translationY);
                    group_fill_path.setTranslateY(-1 * translationY);
                    if (translationY >= -12) {
                        state = 1;
                    }
                } else if (state == 1) {    // Rotate the groups by 180 degress
                    rotation += 3f;
                    frame.setRotation(rotation);
                    fillOutlines.setRotation(rotation);
                    if (rotation == 180) {
                        state = 2;
                    }
                } else if (state == 2) {    // Slide the clip-path up by changing translationY of parent
                    // group
                    translationY -= 0.3f;
                    fillOutlinesPivot.setTranslateY(translationY);
                    group_fill_path.setTranslateY(-1 * translationY);
                    if (translationY <= -24) {
                        state = 3;
                    }
                } else if (state == 3) {    // Rotate the groups by 180 degress
                    rotation += 3f;
                    frame.setRotation(rotation);
                    fillOutlines.setRotation(rotation);
                    if (rotation == 360) {
                        rotation = 0;
                        state = 0;
                    }
                }
                appCompatActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hourglassView.update();        // Update the view from the UI thread
                    }
                });
            }
        }, 500, 1000 / 60);
    }

    @Override
    protected void onStop() {
        timer.cancel();
        super.onStop();
    }

}



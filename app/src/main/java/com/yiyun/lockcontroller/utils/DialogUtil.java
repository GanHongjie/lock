package com.yiyun.lockcontroller.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;

import java.util.List;

/**
 * Created by dengnengcai on 2018-3-23.
 */

public class DialogUtil {

        /**
         * 得到自定义的progressDialog
         *
         * @param context
         * @param msg
         * @return
         */
        public static Dialog showWaiting(Context context, String msg) {
            // 首先得到整个View
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_waiting, null);
            // 获取整个布局
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.vLayoutDialog);
            // 页面中的Img
            ImageView img = (ImageView) view.findViewById(R.id.vImageDialog);
            // 页面中显示文本
            TextView tipText = (TextView) view.findViewById(R.id.vTextDialog);
            // 加载动画，动画用户使img图片不停的旋转
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_waiting);
            // 显示动画
            img.startAnimation(animation);
            // 显示文本
            tipText.setText(msg);
            // 创建自定义样式的Dialog
            Dialog dialog = new Dialog(context, R.style.sDialogWaiting);
            // 设置返回键返回
            dialog.setCancelable(false);
            //点击空白取消
            dialog.setCanceledOnTouchOutside(false);
            layout.setOnClickListener(new OnDoubleClilckToDissmisListener(dialog));
            dialog.setContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            dialog.show();
            return dialog;
        }

        private static class OnDoubleClilckToDissmisListener implements View.OnClickListener {

            private Dialog mDialog;
            private long lastOnCliskTimestamp;

            public OnDoubleClilckToDissmisListener(Dialog dialog) {
                mDialog = dialog;
            }

            @Override
            public void onClick(View view) {
                long currentTimastamp = System.currentTimeMillis();
                if (currentTimastamp - lastOnCliskTimestamp < 1000) {
                    mDialog.dismiss();
                }
                lastOnCliskTimestamp = currentTimastamp;
            }
        }

    /**
     * 得到自定义的progressDialog
     *
     * @param context
     * @param title
     * @param dataList
     * @return
     */
    public static Dialog showList(Context context, String title, List<String> dataList,
                                  AdapterView.OnItemClickListener onItemClickListener) {
        // 首先得到整个View
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_list_select, null);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.vLayoutDialog);
        // 获取整个布局
        TextView vTextTitle = view.findViewById(R.id.vTextTitle);
        ListView vListDialog = view.findViewById(R.id.vListDialog);
        vTextTitle.setText(title);
        vListDialog.setAdapter(new ArrayAdapter<String>(context, R.layout.item_dialog_select,
                R.id.vTextDialogItem, dataList));
        vListDialog.setOnItemClickListener(onItemClickListener);
        Dialog dialog = new Dialog(context, R.style.sDialogWaiting);
        // 设置返回键返回
        dialog.setCancelable(true);
        //点击空白取消
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.show();
        return dialog;
    }
}

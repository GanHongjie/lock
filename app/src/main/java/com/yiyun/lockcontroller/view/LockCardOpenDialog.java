package com.yiyun.lockcontroller.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.MyKeysRecyclerAdapter;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;

import java.util.List;

/**
 * Created by Layo on 2018-2-3.
 */

public class LockCardOpenDialog extends Dialog {

    private Context context;
    private RecyclerView rvCard;
    private List<LockKeysBean> list;
    private OnCardSelectListener mListener;


    public interface OnCardSelectListener {
        void cardFinished(int position);
    }

    public LockCardOpenDialog(@NonNull Context context, OnCardSelectListener mListener, List<LockKeysBean> list) {
        super(context, R.style.MyDialog);
        this.setCanceledOnTouchOutside(true);
        this.context = context;
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dialog_lock_card);
        rvCard = (RecyclerView) findViewById(R.id.rv_card);
        initConfig();
    }

    private void initConfig() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvCard.setLayoutManager(linearLayoutManager);
        MyKeysRecyclerAdapter adapter = new MyKeysRecyclerAdapter(context, list);
        rvCard.setAdapter(adapter);
        //设置增加动画
        rvCard.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new MyKeysRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mListener.cardFinished(position);
                dismiss();
            }
        });
    }
}

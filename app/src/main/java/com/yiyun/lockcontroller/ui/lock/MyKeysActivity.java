package com.yiyun.lockcontroller.ui.lock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.MyKeysRecyclerAdapter;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.bean.lock.QueryMyKeysBean;
import com.yiyun.lockcontroller.presenter.lock.LockMyKeysPresenter;
import com.yiyun.lockcontroller.presenter.lock.contract.LockMyKeysContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的开锁钥匙界面
 * Created by Layo on 2017-8-16.
 */

public class MyKeysActivity extends BaseMvpActivity<LockMyKeysPresenter>
        implements LockMyKeysContract.View {

    private RecyclerView mRvMyKeys;
    private ImageView vImageBack;
    private List<LockKeysBean> lists = new ArrayList<>();
    private MyKeysRecyclerAdapter mAdapter;
    private Intent intent = new Intent();
    private Bundle bundle = new Bundle();

    public static final String MAC_ADDRESS = "mac";
    public static final String DEP_NO= "dep";

    @Override
    public void showError(CharSequence msg) {

    }

    @Override
    public void showMyKeys(List<LockKeysBean> lockOpenBeen) {
        lists.clear();
        lists.addAll(lockOpenBeen);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showApiError(int errorEnum) {

    }

    @Override
    protected LockMyKeysPresenter initPresenter() {
        return new LockMyKeysPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_keys);
        initConfig();
        initRecycleView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        QueryMyKeysBean bean = new QueryMyKeysBean();
        mPresenter.searchMyKeys(getApplicationContext(), bean);
    }

    private void initConfig() {
        mRvMyKeys = (RecyclerView) findViewById(R.id.rv_myKeys);
        vImageBack = findViewById(R.id.vImageBack);
        vImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initRecycleView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvMyKeys.setLayoutManager(linearLayoutManager);
        mAdapter = new MyKeysRecyclerAdapter(this, lists);
        mRvMyKeys.setAdapter(mAdapter);
        //设置增加动画
        mRvMyKeys.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new MyKeysRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                bundle.putString(MAC_ADDRESS, lists.get(position).getMac());
                bundle.putString(DEP_NO, lists.get(position).getAddress());
                intent.putExtras(bundle);
                // TODO: 2018-1-4 点击后返回的信息
                //setResult(KEYS_RESULT_CODE, intent);
//                finish();
            }
        });

    }
}

package com.yiyun.lockcontroller.ui.auto;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.AutoValidFalseAdapter;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.presenter.lock.LockAutoInValidPresenter;
import com.yiyun.lockcontroller.presenter.lock.contract.LockAutoInValidContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 授权管理历史记录界面
 * Created by Layo on 2017-7-23.
 */
@SuppressLint("ValidFragment")
public class AutoFalseFragment extends BaseMvpFragment<LockAutoInValidPresenter>
        implements LockAutoInValidContract.View {
    private RecyclerView mRvAutoLog;
    private Context mContext;
    private AutoValidFalseAdapter mAdapter;
    private List<AuthorizeLogBean> lists = new ArrayList<>();
    private boolean isLoading;
    private String mLockNumber;

    public AutoFalseFragment(Context context, String mLockNumber) {
        this.mContext = context;
        this.mLockNumber = mLockNumber;
    }

    @Override
    protected LockAutoInValidPresenter initPresenter() {
        return new LockAutoInValidPresenter(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.auto_valid_false_fragment, container, false);
        mRvAutoLog = (RecyclerView) v.findViewById(R.id.rv_auto_log);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecycleView();
    }

    private void initRecycleView() {
        //设置布局管理器
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvAutoLog.setLayoutManager(linearLayoutManager);
        mAdapter = new AutoValidFalseAdapter(mContext, lists);
        mRvAutoLog.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mRvAutoLog.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new AutoValidFalseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i("test", "点击到了AutoValidFalseAdapter" + position);
            }
        });
        //设置上拉加载更多
//        mRvAutoLog.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
//                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {
//                    if (!isLoading) {
//                        isLoading = true;
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                QueryAutoBean bean = new QueryAutoBean();
//                                bean.setPattern(AutoMainFragment.AUTO_PERSON_INEFF);
//                                bean.setStartRecord(mAdapter.getItemCount() - 1);
//                                mPresenter.autoQuery(getActivity().getApplicationContext(), bean);
//                                isLoading = false;
//                            }
//                        }, 1000);
//                    }
//                }
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        QueryAutoBean queryAutoBean = new QueryAutoBean();
        queryAutoBean.setPattern(AutoMainFragment.AUTO_PERSON_INEFF);
        mPresenter.autoQuery(getActivity().getApplicationContext(), queryAutoBean);
    }

    @Override
    public void showAutoLog(List<AuthorizeLogBean> logBeans) {
        //设置adapter
        lists.clear();
        lists.addAll(logBeans);
        selectByLockNumber(mLockNumber);
    }

    public void selectByLockNumber(String lockNumber) {
        ArrayList<AuthorizeLogBean> authorizeLogBeanlist = new ArrayList<AuthorizeLogBean>();
        for (int i=0; i < lists.size(); i++) {
            AuthorizeLogBean authorizeLogBean = lists.get(i);
            if (lockNumber.equals(authorizeLogBean.getAutoLockNo())) {
                authorizeLogBeanlist.add(authorizeLogBean);
            }
        }
        mAdapter.refresh(authorizeLogBeanlist);
    }

    @Override
    public void showApiError(int errorEnum) {
    }

    @Override
    public void showError(CharSequence msg) {
    }
}

package com.yiyun.lockcontroller.ui.auto;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.AutoValidTrueAdapter;
import com.yiyun.lockcontroller.bean.lock.AuthorizeLogBean;
import com.yiyun.lockcontroller.bean.lock.QueryAutoBean;
import com.yiyun.lockcontroller.presenter.lock.AutoTruePresenter;
import com.yiyun.lockcontroller.presenter.lock.contract.AutoTrueContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpFragment;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
//
/**
 * 授权管理有效记录界面
 * Created by Layo on 2017-7-23.
 */

@SuppressLint("ValidFragment")
public class AutoTrueFragment extends BaseMvpFragment<AutoTruePresenter>
        implements AutoTrueContract.View {
    private RecyclerView mRvAutoLog;
    private Context mContext;
    private AutoValidTrueAdapter mAdapter;
//    private FloatingActionButton mFabAddAuto;

    private List<AuthorizeLogBean> lists = new ArrayList<>();
    private int selectPosition;
    private boolean isLoading;
    private boolean isNewState = true;  //判读当前的状态是要直接添加还是要下拉的刷新
    private String mLockNumber;


    public AutoTrueFragment(Context mContext, String mLockNumber) {
        this.mContext = mContext;
        this.mLockNumber = mLockNumber;
    }

    @Override
    protected AutoTruePresenter initPresenter() {
        return new AutoTruePresenter(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Logger.i("createView");
        return inflater.inflate(R.layout.auto_valid_true_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvAutoLog = (RecyclerView) view.findViewById(R.id.rv_autoLog);
        initRecycleView();
    }

    private void initRecycleView() {
        //设置布局管理器
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        mRvAutoLog.setLayoutManager(linearLayoutManager);
        mAdapter = new AutoValidTrueAdapter(mContext, lists);
        mRvAutoLog.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mRvAutoLog.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new AutoValidTrueAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selectPosition = position;
                mPresenter.autoDel(getActivity().getApplicationContext(),
                        lists.get(position).getAutoUsername(), lists.get(position).getAutoLockNo());
            }
        });
        mRvAutoLog.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == mAdapter.getItemCount()) {

                    if (!isLoading) {
                        isLoading = true;
                        new android.os.Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                QueryAutoBean bean = new QueryAutoBean();
                                bean.setPattern(AutoMainFragment.AUTO_PERSON_EFF);
                                bean.setStartRecord(mAdapter.getItemCount() - 1);
                                isNewState = false;
                                //mPresenter.autoQuery(bean);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

    }

//    public void showAllDeleteButton() {
//        for (int i=0; i<mRvAutoLog.getChildCount(); i++) {
//            RecyclerView.ViewHolder holder = mRvAutoLog.getChildViewHolder(mRvAutoLog.getChildAt(i));
//            if (holder instanceof AutoValidTrueAdapter.ItemViewHolder) {
//                ((AutoValidTrueAdapter.ItemViewHolder)holder).swipeLayout.open(false);
//            }
//        }
//    }

    @Override
    public void onStart() {
        super.onStart();

        //开始查找
        QueryAutoBean queryAutoBean = new QueryAutoBean();
        //设置查询模式
        queryAutoBean.setPattern(AutoMainFragment.AUTO_PERSON_EFF);
        isNewState = true;
        mPresenter.autoQuery(getActivity().getApplicationContext(), queryAutoBean);
    }

    @Override
    public void showAutoLog(final List<AuthorizeLogBean> logBeans) {
        //设置adapter
        for (AuthorizeLogBean bean : logBeans) {
            Logger.i(bean.toString());
        }
        if (isNewState) {
            lists.clear();
        }
        lists.addAll(logBeans);
        selectByLockNumber(mLockNumber);
    }

    public void selectByLockNumber(String lockNumber) {
        mLockNumber = lockNumber;
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
    public void showDel() {
        lists.remove(selectPosition);
        selectByLockNumber(mLockNumber);
    }

    @Override
    public void showError(CharSequence msg) {
    }
}

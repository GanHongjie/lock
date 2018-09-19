package com.yiyun.lockcontroller.ui.auto;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.TabViewPagerAdapter;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.presenter.auto.AutoMainContract;
import com.yiyun.lockcontroller.presenter.auto.AutoMainPresenter;
import com.yiyun.lockcontroller.ui.base.BaseMvpFragment;
import com.yiyun.lockcontroller.utils.DialogUtil;
import com.yiyun.lockcontroller.view.segment_tab_layout.OnTabSelectListener;
import com.yiyun.lockcontroller.view.segment_tab_layout.SegmentTabLayout;

import java.util.ArrayList;
import java.util.List;
//
/**
 * Created by Layo on 2018-1-30.
 * 授权管理页面
 */

// TODO: 2018-1-31 toolbar中齿轮管理图标的大小
public class AutoMainFragment extends BaseMvpFragment<AutoMainContract.Presenter>
        implements AutoMainContract.View {

    public static final int AUTO_PERSON_EFF = 0;
    public static final int AUTO_PERSON_INEFF = 1;

    private final String[] vpTitles = {"有效记录", "历史记录"};

    private SegmentTabLayout stlTab;
    private ViewPager vpAutoLog;
    private AutoTrueFragment autoTrueFragment; // 授权管理有效记录界面
    private AutoFalseFragment autoFalseFragment; // 授权管理历史记录界面
    private TextView vTextAddress; // 锁的地址的View
    private ImageView vImageSetting;
    private List<LockKeysBean> mLockKeyBeans;
    private int mIndexCurrentKey; // 选中的锁的索引
    private Dialog mDialog;

    @Override
    protected AutoMainContract.Presenter initPresenter() {
        return new AutoMainPresenter(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_auto_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stlTab = (SegmentTabLayout) view.findViewById(R.id.stl_tab);
        vpAutoLog = (ViewPager) view.findViewById(R.id.vp_autoLog);
        vTextAddress = (TextView) view.findViewById(R.id.vTextAddress);
        vImageSetting = (ImageView) view.findViewById(R.id.vImageSetting);
        view.findViewById(R.id.fab_addAuto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(getContext(), AutoAddActivity.class));
            }
        });
        vTextAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> dataList = new ArrayList<String>();
                for (int i=0; i < mLockKeyBeans.size(); i++) {
                    dataList.add(mLockKeyBeans.get(i).getAddress());
                }
                mDialog = DialogUtil.showList(getActivity(), "选择钥匙", dataList,
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                mIndexCurrentKey = position;
                                vTextAddress.setText(mLockKeyBeans.get(mIndexCurrentKey).getAddress());
                                autoTrueFragment.selectByLockNumber(mLockKeyBeans.get(mIndexCurrentKey).getLockNo());
                                autoFalseFragment.selectByLockNumber(mLockKeyBeans.get(mIndexCurrentKey).getLockNo());
                                mDialog.dismiss();
                            }
                        });
            }
        });
        mPresenter.searchMyLockKey(getActivity());
    }

    private void initTab() {
        List<Fragment> fragmentList = new ArrayList<>();
        autoTrueFragment = new AutoTrueFragment(getContext(), mLockKeyBeans.get(mIndexCurrentKey).getLockNo());
        autoFalseFragment = new AutoFalseFragment(getContext(), mLockKeyBeans.get(mIndexCurrentKey).getLockNo());
        fragmentList.add(autoTrueFragment);
        fragmentList.add(autoFalseFragment);
        vpAutoLog.setAdapter(new TabViewPagerAdapter(getChildFragmentManager(), fragmentList, vpTitles));
        stlTab.setTabData(vpTitles);
        stlTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vpAutoLog.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        vpAutoLog.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                stlTab.setCurrentTab(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        vpAutoLog.setCurrentItem(0);
    }

    @Override
    public void showError(CharSequence msg) {
        Log.i("lock", msg.toString());
    }

    @Override
    public void searchSuccess(List<LockKeysBean> mLockKeyBeans) {
        this.mLockKeyBeans = mLockKeyBeans;
        mIndexCurrentKey = 0;
        vTextAddress.setText(mLockKeyBeans.get(mIndexCurrentKey).getAddress());
        initTab();
    }
}

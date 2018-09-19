package com.yiyun.lockcontroller.ui.lock;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.adapter.lock.OpenLogRecyclerAdapter;
import com.yiyun.lockcontroller.bean.lock.LockOpenBean;
import com.yiyun.lockcontroller.bean.lock.QueryOpenLogBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.presenter.lock.LockOpenLogPresent;
import com.yiyun.lockcontroller.presenter.lock.contract.LockOpenLogContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.view.LockOpenLogDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 查询历史开锁记录的activity
 * Created by Layo on 2017-7-27.
 */
public class OpenLogActivity extends BaseMvpActivity<LockOpenLogPresent>
        implements LockOpenLogContract.View, View.OnClickListener {
    private RecyclerView mRvOpenLog;
    private ImageView vImageBack;
    private List<LockOpenBean> lists = new ArrayList<>();
    private OpenLogRecyclerAdapter mAdapter;
    private boolean isNewState = true;  //判读当前的状态是要直接添加还是要下拉的刷新
    private String nowStuNoOrName;
    //toolbar
//    private Toolbar mToolbar;
//    private LinearLayout mLlToolBarTv;  //标题父容器
//    private TextView mTvToolBarBig;     //标题的大标题
//    private TextView mTvToolBarSmall;   //标题的小标题
//    private MaterialSearchView mSearchView; //查询按钮
//    private ImageView mIvDatePicker;        //日历时间选择
//    private CompactCalendarView compactCalendarView;    //日历的视图
    private boolean selectSearchTag = false;
    private int selectTag = 0;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
    private boolean calendarShouldShow = true;
    private long startTime = 0;
    private long endTime = 0;
    private boolean isLoading;


    @Override
    protected LockOpenLogPresent initPresenter() {
        return new LockOpenLogPresent(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_open_log);
        initFindId();
        initConfig();
        initRecycleView();
    }


    private void initFindId() {
        mRvOpenLog = (RecyclerView) findViewById(R.id.rv_openLog);
        vImageBack = findViewById(R.id.vImageBack);
        vImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initConfig() {
//        mToolbar.setTitle("");
//        setSupportActionBar(mToolbar);
//        mIvDatePicker.setOnClickListener(exposeCalendarListener);
//        String[] str = {"一", "二", "三", "四", "五", "六", "日"};
//        compactCalendarView.setDayColumnNames(str);
//        compactCalendarView.invalidate();
//        compactCalendarView.hideCalendar();
//        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
//            @Override
//            public void onOpened() {
//
//            }
//
//            @Override
//            public void onClosed() {
//                compactCalendarView.setVisibility(View.GONE);
//                if (selectSearchTag) {
//                    selectSearchTag = false;
//                    mTvToolBarBig.setVisibility(View.GONE);
//                    calendarShouldShow = !calendarShouldShow;
//                    //mLlToolBarTv.setVisibility(View.GONE);
//                    QueryOpenLogBean bean = new QueryOpenLogBean();
//                    bean.setStart(startTime);
//                    bean.setEnd(endTime);
//                    searchOpenLog(bean);
//                }
//
//            }
//        });
//
//        //set title on calendar scroll
//        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
//            @Override
//            public void onDayClick(Date dateClicked) {
//                if (selectTag == 0) {
//                    mTvToolBarSmall.setText(df.format(dateClicked));
//                    selectTag = 1;
//                    startTime = dateClicked.getTime();
//                    mTvToolBarBig.setText("请选择结束时间");
//
//                } else if (selectTag == 1) {
//
//                    selectTag = 0;
//                    //执行查询内容
//                    selectSearchTag = true;
//                    long tampTime = dateClicked.getTime();
//                    if (tampTime < startTime) {
//                        endTime = startTime;
//                        startTime = tampTime;
//                        String strTvSmall = mTvToolBarSmall.getText().toString();
//                        mTvToolBarSmall.setText(df.format(dateClicked) + "->" + strTvSmall);
//                    } else {
//                        endTime = tampTime;
//                        mTvToolBarSmall.append("->" + df.format(dateClicked));
//                    }
//                    compactCalendarView.hideCalendar();
//
//                }
//
//            }
//
//            @Override
//            public void onMonthScroll(Date firstDayOfNewMonth) {
//
//            }
//
//        });
//        mSearchView.setVoiceSearch(false);
//        mSearchView.setCursorDrawable(R.drawable.custom_cursor);
//        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                selectTag = 0;
//                calendarShouldShow = true;
//                compactCalendarView.hideCalendar();
//                mLlToolBarTv.setVisibility(View.VISIBLE);
//                mTvToolBarSmall.setVisibility(View.GONE);
//                mTvToolBarBig.setVisibility(View.VISIBLE);
//                mTvToolBarBig.setText(query);
//                //开始查询姓名或学号的匹配
//                nowStuNoOrName = query;
//                QueryOpenLogBean bean = new QueryOpenLogBean();
//                searchOpenLog(bean);
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                //Do some magic
//                return false;
//            }
//        });
//
//        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//                //Do some magic
//
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//                //Do some magic
//
//            }
//        });

    }

    private void initRecycleView() {
        //设置布局管理器
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRvOpenLog.setLayoutManager(linearLayoutManager);
        mAdapter = new OpenLogRecyclerAdapter(this, lists);
        mRvOpenLog.setAdapter(mAdapter);
        //设置增加动画
        mRvOpenLog.setItemAnimator(new DefaultItemAnimator());
        mAdapter.setOnItemClickListener(new OpenLogRecyclerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                final LockOpenLogDialog lockOpenLogDialog = new LockOpenLogDialog(OpenLogActivity.this, lists.get(position));
                lockOpenLogDialog.setCancleButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lockOpenLogDialog.dismiss();
                    }
                }).show();
            }
        });
        mRvOpenLog.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                QueryOpenLogBean bean = new QueryOpenLogBean();
                                Log.i("test", "mAdapter.getItemCount()" + (mAdapter.getItemCount() - 1));
                                bean.setStartRecord(mAdapter.getItemCount() - 1);

                                searchNextOpenLog(bean);
                                isLoading = false;
                            }
                        }, 1000);
                    }
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        QueryOpenLogBean bean = new QueryOpenLogBean();
        searchOpenLog(bean);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_white, menu);
        MenuItem item = menu.findItem(R.id.action_search);
//        mSearchView.setMenuItem(item);

        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (mSearchView.isSearchOpen()) {
//            mSearchView.closeSearch();
//        } else {
//            super.onBackPressed();
//        }
//    }

//    @NonNull
//    View.OnClickListener exposeCalendarListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            compactCalendarView.setVisibility(View.VISIBLE);
//            mTvToolBarSmall.setVisibility(View.VISIBLE);
//            selectTag = 0;
//            if (!compactCalendarView.isAnimating()) {
//                compactCalendarView.clearAnimation(); //显示或隐藏
//                if (calendarShouldShow) {
//                    mLlToolBarTv.setVisibility(View.VISIBLE);
//                    mTvToolBarBig.setVisibility(View.VISIBLE);
//                    mTvToolBarSmall.setText(df.format(new Date()));
//                    mTvToolBarBig.setText("请选择起始日期");
//                    compactCalendarView.showCalendar();
//                } else {
//                    mLlToolBarTv.setVisibility(View.GONE);
//                    compactCalendarView.hideCalendar();
//                }
//                calendarShouldShow = !calendarShouldShow;
//            }
//        }
//    };

    public void searchOpenLog(QueryOpenLogBean bean) {
        isNewState = true;
        bean.setStudNoOrName(nowStuNoOrName);
        mPresenter.searchOpenLog(getApplicationContext(), bean);
    }

    public void searchNextOpenLog(QueryOpenLogBean bean) {
        isNewState = false;
        bean.setStudNoOrName(nowStuNoOrName);
        mPresenter.searchOpenLog(getApplicationContext(), bean);
    }

    @Override
    public void showOpenLog(List<LockOpenBean> lockOpenBeen) {
        if (isNewState) {
            lists.clear();
        }
        lists.addAll(lockOpenBeen);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showApiError(int errorEnum) {
        switch (errorEnum) {
            case ApiException.LOCK_OPEN_LOG_EMPTY_EXCEPTION:
                if (mAdapter.getItemCount() != 0) {
                    //已无历史记录可查询
                    lists.clear();
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void showError(CharSequence msg) {
    }
}

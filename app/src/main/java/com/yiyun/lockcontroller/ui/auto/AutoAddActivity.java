package com.yiyun.lockcontroller.ui.auto;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.AuthorizeAddBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.net.ApiException;
import com.yiyun.lockcontroller.presenter.lock.AutoAddPresenter;
import com.yiyun.lockcontroller.presenter.lock.contract.AutoAddContract;
import com.yiyun.lockcontroller.ui.base.BaseMvpActivity;
import com.yiyun.lockcontroller.utils.DialogUtil;
import com.yiyun.lockcontroller.utils.ToastUtil;
import com.yiyun.lockcontroller.view.SublimePickerFragment;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 添加临时授权人的界面
 * 数据为空的判断
 * Created by Layo on 2017-7-17.
 */
public class AutoAddActivity extends BaseMvpActivity<AutoAddPresenter>
        implements AutoAddContract.View, View.OnClickListener {

    private static final int REQUEST_CODE_SCAN = 111;

    private TextView mEtUsername;
    private TextView mTvKey;
    private LinearLayout mLlAllday;
    private TextView mTvAllday;
    private EditText mEtAllowtimes;
    private TextView mTvStarttime;
    private TextView mTvEndtime;
    private View vImageBack;
    private Button mBtnSend;
    private View vImageScanQrCode;
    private View vImageMyQrCode;
    private RelativeLayout mRlEndtime;
    private RelativeLayout mRlStarttime;
    private SublimePickerFragment pickerFragStart = new SublimePickerFragment();
    private SublimePickerFragment pickerFragEnd = new SublimePickerFragment();
    private long startTime;
    private long endTime;
    private int autoState;
    private List<LockKeysBean> mLogBeans;
    private int lockIndex;
    private Dialog mDialog;

    @Override
    protected AutoAddPresenter initPresenter() {
        return new AutoAddPresenter(this);
    }

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.auto_add_activity);
        initFindId();
        initConfig();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initFindId() {
        mEtUsername = (TextView) findViewById(R.id.et_username);
        mRlStarttime = (RelativeLayout) findViewById(R.id.rl_startTime);
        mRlEndtime = (RelativeLayout) findViewById(R.id.rl_endTime);
        mLlAllday = (LinearLayout) findViewById(R.id.ll_allday);
        mTvAllday = (TextView) findViewById(R.id.tv_allday);
        mEtAllowtimes = (EditText) findViewById(R.id.et_allowTimes);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mTvStarttime = (TextView) findViewById(R.id.tv_startTime);
        mTvEndtime = (TextView) findViewById(R.id.tv_endTime);
        mTvKey = (TextView) findViewById(R.id.tv_key);
        vImageBack = findViewById(R.id.vImageBack);
        vImageMyQrCode = findViewById(R.id.vImageMyQrCode);
        vImageScanQrCode = findViewById(R.id.vImageScanQrCode);

        vImageBack.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mRlStarttime.setOnClickListener(this);
        mRlEndtime.setOnClickListener(this);
        vImageMyQrCode.setOnClickListener(this);
        vImageScanQrCode.setOnClickListener(this);
        mTvKey.setOnClickListener(this);
    }

    private void initConfig() {
        // DialogFragment to host SublimePicker
        pickerFragStart.setCallback(mFragmentCallbackStart);
        pickerFragEnd.setCallback(mFragmentCallbackEnd);
        // Valid optionsStart
        Bundle bundleStart = new Bundle();
        bundleStart.putParcelable("SUBLIME_OPTIONS", getOptions(true));
        pickerFragStart.setArguments(bundleStart);
        pickerFragStart.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        //Valid optionsEnd
        Bundle bundleEnd = new Bundle();
        bundleEnd.putParcelable("SUBLIME_OPTIONS", getOptions(false));
        pickerFragEnd.setArguments(bundleEnd);
        pickerFragEnd.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        mPresenter.searchMyKeys(AutoAddActivity.this);
    }

    SublimePickerFragment.Callback mFragmentCallbackStart = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            //取消时候的操作
            mLlAllday.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            startTime = selectedDate.getStartDate().getTimeInMillis();
            if (selectedDate.getType() == SelectedDate.Type.SINGLE) {
                mTvStarttime.setText(DateFormat.getDateInstance().format(startTime));
                //转换日期设置时间
                try {
                    Date endDate = str2Date(mTvEndtime.getText().toString());
                    setAllDay(startTime, endDate.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else if (selectedDate.getType() == SelectedDate.Type.RANGE) {
                endTime = selectedDate.getEndDate().getTimeInMillis();

                //设置时间
                long nowTime = Calendar.getInstance().getTimeInMillis();

                if (endTime < startTime || endTime < nowTime) {
                    ToastUtil.showToast("选择的" +
                            getString(R.string.lock_auto_add_endTime) + "有误，不能小于" +
                            getString(R.string.lock_auto_add_startTime) + "或当期日期");
                } else {
                    mTvStarttime.setText(DateFormat.getDateInstance().format(startTime));
                    mTvEndtime.setText(DateFormat.getDateInstance().format(endTime));
                    setAllDay(startTime, endTime);
                }
            }
        }
    };

    SublimePickerFragment.Callback mFragmentCallbackEnd = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            //取消时候的操作
            mLlAllday.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            endTime = selectedDate.getStartDate().getTimeInMillis();
            long nowTime = Calendar.getInstance().getTimeInMillis();

            if (endTime < startTime || endTime < nowTime) {
                ToastUtil.showToast("选择的" +
                        getString(R.string.lock_auto_add_endTime) + "有误，不能小于" +
                        getString(R.string.lock_auto_add_startTime) + "或当期日期");
            } else {
                mTvEndtime.setText(DateFormat.getDateInstance().format(endTime));
                try {
                    Date startDate = str2Date(mTvStarttime.getText().toString());
                    setAllDay(startDate.getTime(), endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * @param dateString 传入str
     * @return date
     * @throws ParseException
     */
    private Date str2Date(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        return sdf.parse(dateString);
    }

    /**
     * 传入两个日期给day设置日期
     *
     * @param startTime 开始日期
     * @param endTime   结束日期
     */
    private void setAllDay(long startTime, long endTime) {
        mLlAllday.setVisibility(View.VISIBLE);
        int days = (int) ((endTime - startTime) / (24 * 60 * 60 * 1000));
        mTvAllday.setText(String.valueOf(days));
    }

    // Validates & returns SublimePicker options
    private SublimeOptions getOptions(boolean isRange) {
        //初始化设定
        SublimeOptions options = new SublimeOptions();
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(SublimeOptions.ACTIVATE_DATE_PICKER);
        options.setCanPickDateRange(isRange);

        return options;
    }

    @Override
    public void onClick(View view) {
        if (view == mBtnSend) {
            if (judgeData()) {
                //输入正确才能进入
                //得到地址通过索引找到lockNo
                AuthorizeAddBean authorizeBean = new AuthorizeAddBean(
                        mTvStarttime.getText().toString(),
                        mTvEndtime.getText().toString(),
                        mEtAllowtimes.getText().toString(),
                        mEtUsername.getText().toString(),
                        mLogBeans.get(lockIndex).getLockNo(),
                        autoState);
                mPresenter.autoAdd(getApplicationContext(), authorizeBean);
            }
        } else if (view == vImageBack) {
            finish();
        } else if (view == mRlStarttime) {
//                ToastUtil.showToast("可以长按选择日期");
            if (pickerFragStart.isAdded()) {
                pickerFragStart.dismiss();
            }
            pickerFragStart.show(getSupportFragmentManager(), "SUBLIME_PICKER");
        } else if (view == mRlEndtime) {
            if (pickerFragEnd.isAdded()) {
                pickerFragEnd.dismiss();
            }
            pickerFragEnd.show(getSupportFragmentManager(), "SUBLIME_PICKER");
        } else if (view == vImageMyQrCode) {
            Intent intent = new Intent(this, MyQrCodeActivity.class);
            startActivity(intent);
        } else if (view == vImageScanQrCode) {
            Intent intent = new Intent(this, CaptureActivity.class);
                                /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
                                 * 也可以不传这个参数
                                 * 不传的话  默认都为默认不震动  其他都为true
                                 * */
            ZxingConfig config = new ZxingConfig();
            config.setPlayBeep(true);
            config.setShake(true);
            intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        } else if (view == mTvKey) {
            ArrayList<String> dataList = new ArrayList<String>();
            for (int i=0; i < mLogBeans.size(); i++) {
                dataList.add(mLogBeans.get(i).getAddress());
            }
            mDialog = DialogUtil.showList(AutoAddActivity.this, "选择钥匙", dataList,
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            lockIndex = position;
                            mTvKey.setText(mLogBeans.get(lockIndex).getAddress());
                            mDialog.dismiss();
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                mEtUsername.setText(content);
//                result.setText("扫描结果为：" + content);
            }
        }
    }

    // 判断输入项是否为空
    private boolean judgeData() {
        if (mEtUsername.getText().toString().equals("")) {
            ToastUtil.showToast("请输入" + getString(R.string.lock_auto_add_stuNo));
            return false;
        }
        if (!TextUtils.isEmpty(mEtAllowtimes.getText().toString())
                && !mTvStarttime.getText().toString().equals(getString(R.string.lock_auto_add_default))
                && !mTvEndtime.getText().toString().equals(getString(R.string.lock_auto_add_default))){
            int i = Integer.parseInt(mEtAllowtimes.getText().toString());
            if (i == 0) {
                ToastUtil.showToast("请输入正确的次数");
                return false;
            }
            autoState = AuthorizeAddBean.AUTO_STATE_TIME_COUNT;
            return true;
        } else if (TextUtils.isEmpty(mEtAllowtimes.getText().toString())
                && !mTvStarttime.getText().toString().equals(getString(R.string.lock_auto_add_default))
                && !mTvEndtime.getText().toString().equals(getString(R.string.lock_auto_add_default))) {
            autoState = AuthorizeAddBean.AUTO_STATE_TIME;
            return true;
        } else if (!TextUtils.isEmpty(mEtAllowtimes.getText().toString())
                && mTvStarttime.getText().toString().equals(getString(R.string.lock_auto_add_default))
                && mTvEndtime.getText().toString().equals(getString(R.string.lock_auto_add_default))) {
            int i = Integer.parseInt(mEtAllowtimes.getText().toString());
            if (i == 0) {
                ToastUtil.showToast("请输入正确的次数");
                return false;
            }
            autoState = AuthorizeAddBean.AUTO_STATE_COUNT;
            return true;
        } else {
            if (mTvStarttime.getText().equals(getString(R.string.lock_auto_add_default))) {
                ToastUtil.showToast("请选择" + getString(R.string.lock_auto_add_startTime));
            } else if (mTvEndtime.getText().equals(getString(R.string.lock_auto_add_default))) {
                ToastUtil.showToast("请选择" + getString(R.string.lock_auto_add_endTime));
                return false;
            } else if (TextUtils.isEmpty(mEtAllowtimes.getText().toString())
                    || Integer.parseInt(mEtAllowtimes.getText().toString()) == 0) {
                ToastUtil.showToast("请输入" + getString(R.string.lock_auto_add_allowCount));
                return false;
            }
            return false;
        }
    }

    @Override
    public void showAuto() {
        ToastUtil.showToast("授权成功");
        finish();
    }
//11001	传入的参数data为空
//14008	autoUsername为空即被授权人用户名为空
//14009	授权次数大于99，超过上限
//14010	lockNo锁编号为空
//14001	授权人对这把锁没有权限
//14002	被授权人已经有了这把锁的权限
//14003	已经被授权过了
//14004	时间段授权，但开始时间为空
//14005	时间段授权，但结束时间为空
//14006	次数授权，但总次数为0
//14007	不是这三种授权方式
//10007	数据库更新失败
    @Override
    public void showApiError(int errorEnum) {
        Log.i("lock", "showApiError() errorEnum = " + errorEnum);
        switch (errorEnum) {
            case ApiException.LOCK_AUTO_REPEAT_EXCEPTION: // 14003
                Toast.makeText(this,"此用户已被授予权限",Toast.LENGTH_SHORT).show();
                break;
            case ApiException.LOCK_AUTO_REPEAT_EXCEPTION_2: // 14002
                Toast.makeText(this,"被授权人已经有了这把锁的权限",Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void searchMyKeysSuccess(List<LockKeysBean> logBeans) {
        mLogBeans = logBeans;
        lockIndex = 0;
        mTvKey.setText(logBeans.get(lockIndex).getAddress());
    }

    @Override
    public void showError(CharSequence msg) {
        Log.i("lock", "showError()");
        Toast.makeText(this,"未知错误",Toast.LENGTH_SHORT).show();
    }
}

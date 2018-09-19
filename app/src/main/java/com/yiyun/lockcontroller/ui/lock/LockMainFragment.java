package com.yiyun.lockcontroller.ui.lock;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trycatch.mysnackbar.Prompt;
import com.yiyun.lockcontroller.App;
import com.yiyun.lockcontroller.R;
import com.yiyun.lockcontroller.bean.lock.BlueDeviceMsgBean;
import com.yiyun.lockcontroller.bean.lock.LockBean;
import com.yiyun.lockcontroller.bean.lock.LockKeysBean;
import com.yiyun.lockcontroller.presenter.lock.LockMainPresenter;
import com.yiyun.lockcontroller.presenter.lock.contract.LockMainContract;
import com.yiyun.lockcontroller.ui.UserCenterActivity;
import com.yiyun.lockcontroller.ui.base.BaseMvpFragment;
import com.yiyun.lockcontroller.ui.login.LoginActivity;
import com.yiyun.lockcontroller.utils.ScreenUtil;
import com.yiyun.lockcontroller.utils.ToastUtil;
import com.yiyun.lockcontroller.utils.bluetooth.BlueServiceUtils;
import com.yiyun.lockcontroller.utils.bluetooth.BlueToothUtils;
import com.yiyun.lockcontroller.utils.bluetooth.BluetoothLeClass;
import com.yiyun.lockcontroller.utils.bluetooth.iBeaconClass;
import com.yiyun.lockcontroller.utils.bluetoothlock.BlueGetMsg;
import com.yiyun.lockcontroller.utils.bluetoothlock.BlueGetMsgListener;
import com.yiyun.lockcontroller.utils.bluetoothlock.BlueSendMsgBody;
import com.yiyun.lockcontroller.utils.bluetoothlock.SendAgreeClass;
import com.yiyun.lockcontroller.utils.rc4.RC4;
import com.yiyun.lockcontroller.view.LockCardOpenDialog;
import com.yiyun.lockcontroller.view.ripple_button.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_LOCK_CLOSE;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_LOCK_DEFAULT;
import static com.yiyun.lockcontroller.utils.bluetoothlock.BlueMsgBody.BT_LOCK_OPEN;
import static com.yiyun.lockcontroller.utils.bluetoothlock.SendAgreeClass.KEY_VERIFY_DEFAULT;
//

/**
 * Created by Layo on 2018-2-1.
 * 开锁页面
 */
public class LockMainFragment extends BaseMvpFragment<LockMainContract.Presenter>
        implements LockMainContract.View {

    private static final boolean DEBUG = false && App.DEBUG;
    private static final long DELAY_DISCONNECT_RUNNABLE = 120 * 1000;
    private static final long DELAY_RECONNECT_RUNNABLE = 20 * 1000;
    //  四种状态：断开中、连接中、等待开锁中、开锁中
    private static final int STATUS_DISCONNECTED = 1;
    private static final int STATUS_CONNECTING = 2;
    private static final int STATUS_WAITING_OPEN_LOCK = 3;
    private static final int STATUS_OPENING_LOCK = 4;

    private CircleImageView rdButton;
    private TextView tvLockDep;
    private TextView tvLockMsg;
    private TextView tvTip;
    private TextView tvElect;
    private LinearLayout llCard;
    private ImageView vImageUser;
    private ImageView vImageBack;

    public BluetoothLeClass mBLE; // 读写BLE终端，蓝牙设备
    private BlueGetMsg blueGetMsg; // 处理收到的蓝牙指令
    private boolean mScanning;
    private int indexLockKeySelected; // 选中的锁的索引
    private List<LockKeysBean> myLockKeys; // 我拥有的锁
    private List<LockKeysBean> myValidLockKeyList; // 我拥有的且扫描到的锁
    private String addressConnecting; // 正在连接的锁的MAC地址
    private Handler mHandler;
    private Runnable reconnectRunnable; // 重连的Runnable
    private Runnable disconnectRunnable; // 连接120s后断开连接的Runnable
    private Runnable reconnectAfterFailedRunnable; // 连接失败后重连的Runnable
    private LockBean lockBeanConnected; // 连接上的锁的信息
    private int status; // 当前连接或开锁的状态

    @Override
    protected LockMainContract.Presenter initPresenter() {
        return new LockMainPresenter(this);
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_lock_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initFindId(view);
        initConfig();
        mHandler = new Handler(Looper.getMainLooper());
        myValidLockKeyList = new ArrayList<LockKeysBean>();
        status = STATUS_DISCONNECTED;
        initBlueTooth();
    }

    private void initFindId(View v) {
        rdButton = (CircleImageView) v.findViewById(R.id.rd_button); // (RippleDiffuse)
        tvLockMsg = (TextView) v.findViewById(R.id.tv_lockMsg);
        tvLockDep = (TextView) v.findViewById(R.id.tv_lockDep);
        llCard = (LinearLayout) v.findViewById(R.id.ll_card);
        tvElect = (TextView) v.findViewById(R.id.tv_elect);
        tvTip = (TextView) v.findViewById(R.id.tv_tip);
        modifyTip(null);
        vImageUser = (ImageView) v.findViewById(R.id.vImageUser);
        vImageBack = (ImageView) v.findViewById(R.id.vImageBack);
        vImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        rdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ScreenUtil.isValidClick()) {
                    return;
                }
                if (myValidLockKeyList != null && myValidLockKeyList.size() > 0) {
//                    rdButton.setAnimationEach(1000);
//                    rdButton.showWaveAnimation();
                    if (lockBeanConnected == null) {
                        connectBle(myValidLockKeyList.get(indexLockKeySelected).getMac());
                    } else {
                        mHandler.removeCallbacks(disconnectRunnable);
                        status = STATUS_OPENING_LOCK;
                        showLoading("正在开锁…");
                        if (lockBeanConnected.getLockNo().equals("324336423744373439393037")) {
                            byte[] hexBody = BlueToothUtils.hexStringToBytes(lockBeanConnected.getCipher());
                            //二次加解密
                            byte[] encryptByte = RC4.RC4Base(hexBody, "7cc11325");
                            byte[] rawRByte = RC4.RC4Base(encryptByte, "ebd0f143");

                            openRequestSuccess(BlueToothUtils.bytesToHexString(rawRByte));
                        } else
                            mPresenter.openLock(getActivity().getApplicationContext(), lockBeanConnected);
                    }
                }
            }
        });
        vImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserCenterActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private void initConfig() {
        blueGetMsg = new BlueGetMsg<>(mActivity);
        blueGetMsg.setListener(blueGetMsgListener);
    }

    // 初始化蓝牙连接服务
    public void initBlueTooth() {
        mBLE = BluetoothLeClass.getInstance(getActivity().getApplicationContext());
        if (!mBLE.initialize()) {
            showSnackBar("请打开蓝牙", Prompt.ERROR);
        }
        mBLE.getBluetoothAdapter().enable(); // 打开蓝牙
        // 发现BLE终端的Service
        mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
        mBLE.setOnDataAvailableListener(mOnDataAvailable);
        mBLE.setOnDisconnectListener(new BluetoothLeClass.OnDisconnectListener() {
            @Override
            public void onDisconnect(BluetoothGatt gatt) {
                if (reconnectAfterFailedRunnable == null) {
                    reconnectAfterFailedRunnable = new Runnable() {
                        @Override
                        public void run() {
                            if (status == STATUS_CONNECTING) {
                                Log.i("LockMainFragment", "连接失败重新连接");
                                Toast.makeText(getActivity(), DEBUG ? "连接失败重新连接" : "网络繁忙，请等待", Toast.LENGTH_SHORT).show(); //
                                if (reconnectRunnable != null) {
                                    mHandler.removeCallbacks(reconnectRunnable);
                                    mHandler.postDelayed(reconnectRunnable, DELAY_RECONNECT_RUNNABLE);
                                }
                                modifyMsg("正在连接");
                                mBLE.connect(addressConnecting);
                            } else if (status == STATUS_OPENING_LOCK || status == STATUS_DISCONNECTED) {
                                modifyMsg("连接断开");
                                stopLoading();
                                status = STATUS_DISCONNECTED;
                            } else {
                                Log.e("lock", "异常情况：连接失败时的状态异常！status=" + status);
                            }
//                            if (System.currentTimeMillis() > lastConnectedTimestamp + DURATION_RECONNECT_RUNNABLE) {
//                            } else {
//                            }
                        }
                    };
                }
                mHandler.removeCallbacks(reconnectAfterFailedRunnable);
                mHandler.post(reconnectAfterFailedRunnable);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        modifyMsg("正在搜索蓝牙");
        scanLeDevice(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        modifyMsg("等待连接");
        scanLeDevice(false);
    }

    View.OnClickListener cardDialogOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Logger.i("点击了多个");
            if (myValidLockKeyList == null || myValidLockKeyList.size() == 0) {
                return;
            }
            LockCardOpenDialog cardOpenDialog = new LockCardOpenDialog(getContext(), new LockCardOpenDialog.OnCardSelectListener() {
                @Override
                public void cardFinished(int position) {
                    if (disconnectRunnable != null) {
                        mHandler.removeCallbacks(disconnectRunnable);
                    }
                    mBLE.disconnect();
                    mBLE.close();
                    lockBeanConnected = null;
                    rdButton.setImageResource(R.mipmap.icon_lock_close_gray);
                    indexLockKeySelected = position;
                    tvLockDep.setText(myValidLockKeyList.get(position).getAddress());
                    Log.i("lock", "mac =" + myValidLockKeyList.get(position).getAddress());
                    connectBle(myValidLockKeyList.get(position).getMac());
                }
            }, myValidLockKeyList);
            cardOpenDialog.show();
        }
    };

    /**
     * 搜索到BLE终端服务
     */
    private BluetoothLeClass.OnServiceDiscoverListener mOnServiceDiscover = new BluetoothLeClass.OnServiceDiscoverListener() {

        @Override
        public void onServiceDiscover(BluetoothGatt gatt) {
            BlueServiceUtils.setBLECharacteristic(mBLE.getSupportedGattServices(), mBLE);
            Log.i("lock", "连接上蓝牙");
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showLoading("正在连接…");
                    modifyMsg("正在连接");
                }
            });
            addressConnecting = null;
            if (reconnectRunnable != null) {
                mHandler.removeCallbacks(reconnectRunnable);
            }
            if (reconnectAfterFailedRunnable != null) {
                mHandler.removeCallbacks(reconnectAfterFailedRunnable);
            }
        }

    };


    private void scanLeDevice(boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mScanning = true;
            mPresenter.searchMyKeys(getActivity());
            myValidLockKeyList.clear();
            modifyTip(null);
            mBLE.getBluetoothAdapter().startLeScan(mLeScanCallback); // 开始扫描蓝牙设备
        } else {
            mScanning = false;
            mBLE.getBluetoothAdapter().stopLeScan(mLeScanCallback); // 停止扫描蓝牙设备
        }
    }

//    //接收系统成功打开蓝牙的广播
//    private BroadcastReceiver mBroadcastBlueReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
//            if (blueState == BluetoothAdapter.STATE_ON) {
//                // 发现BLE终端的Service
//                mBLE.setOnServiceDiscoverListener(mOnServiceDiscover);
//                myValidLockKeyList.clear();
//                mBLE.getBluetoothAdapter().startLeScan(mLeScanCallback);
//            }
//        }
//    };

    private void connectBle(final String address) {
        if (mScanning) {
            mBLE.getBluetoothAdapter().stopLeScan(mLeScanCallback);
            mScanning = false;
        }
        //连接到指定蓝牙
        showLoading("正在连接…");
        modifyMsg("正在连接");
        addressConnecting = address;
        status = STATUS_CONNECTING;
        mBLE.connect(address);
        if (reconnectRunnable == null) {
            reconnectRunnable = new Runnable() { // 连接20s未成功则重连
                @Override
                public void run() {
                    if (status == STATUS_CONNECTING) {
                        Log.i("lock", "经过10s重新连接");
                        showToast(DEBUG ? "经过10s重新连接" : "网络繁忙，请等待");
                        modifyMsg("正在连接");
                        mBLE.connect(address);
                    }
                }
            };
        }
        mHandler.removeCallbacks(reconnectRunnable);
        mHandler.postDelayed(reconnectRunnable, DELAY_RECONNECT_RUNNABLE);
    }

    // 扫描蓝牙设备的回调
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            //接收到的信号
            iBeaconClass.iBeacon ibeacon = iBeaconClass.fromScanData(device, rssi, scanRecord);
            assert ibeacon != null;
            String nowAddress = ibeacon.bluetoothAddress;
            Log.i("lock", "nowAddress =" + nowAddress);
            if (myLockKeys == null || myLockKeys.size() == 0) {
                return;
            }
            for (LockKeysBean myLockKey : myLockKeys) {
                if (myLockKey.getMac().equalsIgnoreCase(nowAddress)) {
                    boolean isContain = false;
                    for (LockKeysBean myValidLockKey : myValidLockKeyList) {
                        if (myValidLockKey.getMac().equalsIgnoreCase(nowAddress)) {
                            isContain = true;
                            break;
                        }
                    }
                    if (!isContain) {
                        myValidLockKeyList.add(myLockKey);
                    }
                    break;
                }
            }
            if (myValidLockKeyList != null && myValidLockKeyList.size() > 0) {
                if (myValidLockKeyList.size() == 1) {
                    // 扫描到第一把有效锁时
                    indexLockKeySelected = 0;
                    tvLockDep.setText(myValidLockKeyList.get(0).getAddress());
//                        if (keysBean.getUserType() == LockKeysBean.USER_TYPE_COMMON)
//                            llCard.setBackground(getActivity().getDrawable(R.drawable.bg_card_common));
//                        else
//                            llCard.setBackground(getActivity().getDrawable(R.drawable.bg_card_temporary));
                    modifyMsg("等待连接");
                    modifyTip(null);
                } else {
                    //多把锁的情况
                    modifyTip("已连接多把锁，请点击卡片选择门锁");
                    //设置card的点击事件0
                    llCard.setOnClickListener(cardDialogOnClick);
                }
//                    rdButton.showWaveAnimation();
            }
        }

    };

    /**
     * 用于接收数据
     */
    private BlueGetMsgListener blueGetMsgListener = new BlueGetMsgListener() {

        @Override
        public void sendDeviceDate(BlueDeviceMsgBean deviceMsgBean) {
            String lockNo = deviceMsgBean.getLockNo();
            String elect16 = deviceMsgBean.getElect();
            String elect10 = deviceMsgBean.getElect10();

            Log.i("lock", "lockNo=" + lockNo + "\n" + "lockElect=" + elect10 + "\n");
            tvElect.setText(elect10 + "V");
            lockBeanConnected = new LockBean(lockNo, deviceMsgBean.getR());
            stopLoading();
            rdButton.setImageResource(R.mipmap.icon_lock_close_blue);
            status = STATUS_WAITING_OPEN_LOCK;
            modifyMsg("连接成功，等待开锁");
            showSnackBar("连接成功", Prompt.SUCCESS);
            if (disconnectRunnable == null) {
                disconnectRunnable = new Runnable() {
                    @Override
                    public void run() {
                        modifyMsg("等待连接");
                        mBLE.disconnect();
                        mBLE.close();
                        stopLoading();
                        lockBeanConnected = null;
                        rdButton.setImageResource(R.mipmap.icon_lock_close_gray);
                        status = STATUS_DISCONNECTED;
                        Log.i("LockMainFragment", "连接120s后断开连接");
//                        showToast("连接120s后断开连接");
                    }
                };
            }
            mHandler.removeCallbacks(disconnectRunnable);
            mHandler.postDelayed(disconnectRunnable, DELAY_DISCONNECT_RUNNABLE); // 连接60s后断开连接
        }

        @Override
        public void sendSuccess(String strMsg) {
            switch (strMsg) {
                case BT_LOCK_DEFAULT:
                    stopLoading();
                    showSnackBar("开锁成功", Prompt.SUCCESS);
                    showLoading("开锁成功，请勿退出程序");
                    modifyMsg("开锁成功");
                    rdButton.setImageResource(R.mipmap.icon_lock_open_orange);
                    break;
                case BT_LOCK_OPEN:
                    break;
                case BT_LOCK_CLOSE:
                    if (disconnectRunnable != null) {
                        mHandler.removeCallbacks(disconnectRunnable);
                    }
                    mBLE.disconnect();
                    mBLE.close();
                    stopLoading();
                    lockBeanConnected = null;
                    rdButton.setImageResource(R.mipmap.icon_lock_close_gray);
                    status = STATUS_DISCONNECTED;
                    modifyMsg("正在搜索蓝牙");
                    scanLeDevice(true);
                    llCard.setOnClickListener(null);
                    break;
            }
        }

        @Override
        public void sendVerify(String strR) {
            //加密验证数据
            byte[] hexBody = BlueToothUtils.hexStringToBytes(strR);
            byte[] encryptByte = RC4.RC4Base(hexBody, KEY_VERIFY_DEFAULT);
            String encryptStr = BlueToothUtils.bytesToHexString(encryptByte);
            //按照协议传参发送
            BlueSendMsgBody blueSendMsgBody = new BlueSendMsgBody(encryptStr);
            String body = blueSendMsgBody.getSendMsg(BlueSendMsgBody.BT_COMMAND_RESPONSE);
            SendAgreeClass sendClass = new SendAgreeClass(body);
            //放入缓存后发送
            if (DEBUG) {
                Log.i(SendAgreeClass.class.getSimpleName(), "手机发送的数据：" + sendClass.toString());
            }
            blueGetMsg.pushCommand(sendClass.toByteRC4Byte());
            BlueToothUtils.Send_Bytes(mBLE, sendClass.toByteRC4Byte());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    BlueSendMsgBody blueSendMsgBody = new BlueSendMsgBody("00");
                    String body = blueSendMsgBody.getSendMsg(BlueSendMsgBody.BT_COMMAND_GET);
                    SendAgreeClass sendClass = new SendAgreeClass(body);
                    if (DEBUG) {
                        Log.i(SendAgreeClass.class.getSimpleName(), "手机发送的数据：" + sendClass.toString());
                    }
                    blueGetMsg.pushCommand(sendClass.toByteRC4Byte());
                    BlueToothUtils.Send_Bytes(mBLE, sendClass.toByteRC4Byte());
                }
            }, 500);
        }

        @Override
        public void sendReSend(String commandMsg) {
            if (DEBUG) {
                Log.i(SendAgreeClass.class.getSimpleName(), "手机发送的数据（密文）：" + commandMsg);
            }
            blueGetMsg.pushCommand(commandMsg);
            BlueToothUtils.Send_Bytes(mBLE, commandMsg);
        }

        @Override
        public void sendError(String strMsg) {
            showToast(strMsg);
        }

        @Override
        public void sendDisConnected() {
            Log.i("lock", "数据错误，断开连接");
            modifyMsg("等待连接");
            if (disconnectRunnable != null) {
                mHandler.removeCallbacks(disconnectRunnable);
            }
            mBLE.disconnect();
            mBLE.close();
            stopLoading();
            lockBeanConnected = null;
            rdButton.setImageResource(R.mipmap.icon_lock_close_gray);
            status = STATUS_DISCONNECTED;
        }
    };

    /**
     * 收到BLE终端数据交互
     */
    private BluetoothLeClass.OnDataAvailableListener mOnDataAvailable = new BluetoothLeClass.OnDataAvailableListener() {

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] data = characteristic.getValue();
            String uuid = characteristic.getUuid().toString();
            if (uuid.equals(BlueServiceUtils.UUID_CHAR6)) {
                // amomcu 的串口透传
                String str_Recv = BlueToothUtils.bytesToHexString(data);
                if (DEBUG) {
                    Log.i("lock", "锁回传的数据：" + str_Recv);
                }
                blueGetMsg.addMsg(str_Recv);
            }
        }

    };

    //修改显示内容
    public void modifyMsg(final String str) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(str)) {
                    tvLockMsg.setText(str);
                }
            }
        });
    }

    public void modifyTip(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvTip.setVisibility(View.VISIBLE);
            tvTip.setText(str);
        } else {
            tvTip.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(CharSequence msg) {
        ToastUtil.showToast(msg.toString());
    }

    // 当开锁网络请求成功，则继续通过蓝牙开锁
    @Override
    public void openRequestSuccess(String r) {
        //得到随机数，写入开锁指令开锁
        BlueSendMsgBody blueSendMsgBody = new BlueSendMsgBody(r);
        String body = blueSendMsgBody.getSendMsg(BlueSendMsgBody.BT_COMMAND_OPEN);
        SendAgreeClass sendClass = new SendAgreeClass(body);
        if (DEBUG) {
            Log.i(SendAgreeClass.class.getSimpleName(), "手机发送的数据：" + sendClass.toString());
        }
        blueGetMsg.pushCommand(sendClass.toByteRC4Byte());
        BlueToothUtils.Send_Bytes(mBLE, sendClass.toByteRC4Byte());
    }

    // 当开锁网络请求失败
    @Override
    public void openRequestFail(String r) {
        ToastUtil.showToast(r);
        if (disconnectRunnable != null) {
            mHandler.removeCallbacks(disconnectRunnable);
        }
        mBLE.disconnect();
        mBLE.close();
        lockBeanConnected = null;
        rdButton.setImageResource(R.mipmap.icon_lock_close_gray);
        status = STATUS_DISCONNECTED;
    }

    @Override
    public void searchMyKeysSuccess(List<LockKeysBean> logBeans) {
        myLockKeys = logBeans;
    }

    @Override
    public void gotoLoginActivity() {
        Toast.makeText(getActivity(), "登录过期，请重新登录", Toast.LENGTH_SHORT).show();
        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}

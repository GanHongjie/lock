/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yiyun.lockcontroller.utils.bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
public class BluetoothLeClass extends Service {
    private final static String TAG = "BluetoothLeClass";// BluetoothLeClass.class.getSimpleName();

    private static final String ACTION_NAME_RSSI = "AMOMCU_RSSI";
    private static final String ACTION_CONNECT = "AMOMCU_CONNECT";

    public static final UUID CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID = UUID
            .fromString("00002902-0000-1000-8000-00805f9b34fb");
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    public String mBluetoothDeviceAddress = null;
    public BluetoothGatt mBluetoothGatt;

    public interface OnConnectListener {
        void onConnect(BluetoothGatt gatt);
    }

    public interface OnDisconnectListener {
        void onDisconnect(BluetoothGatt gatt);
    }

    public interface OnReadRemoteRssiListener {
        void onReadRemoteRssi(BluetoothGatt gatt, int rssi);
    }

    public interface OnServiceDiscoverListener {
        void onServiceDiscover(BluetoothGatt gatt);
    }

    public interface OnDataAvailableListener {
//        void onCharacteristicRead(BluetoothGatt gatt,
//                                  BluetoothGattCharacteristic characteristic, int status);

        void onCharacteristicWrite(BluetoothGatt gatt,
                                   BluetoothGattCharacteristic characteristic);
    }

    private OnConnectListener mOnConnectListener;
    private OnDisconnectListener mOnDisconnectListener;
    private OnReadRemoteRssiListener mOnReadRemoteRssiListener;
    private OnServiceDiscoverListener mOnServiceDiscoverListener;
    private OnDataAvailableListener mOnDataAvailableListener;

    private Context mContext;

    // Do not place Android context classes in static fields (static reference to BluetoothLeClass
    // which has field mContext pointing to Context); TODO this is a memory leak (and also breaks Instant Run)
    private static BluetoothLeClass sBluetoothLeClass;

    public static BluetoothLeClass getInstance(Context context) {
        if (sBluetoothLeClass == null) {
            sBluetoothLeClass = new BluetoothLeClass(context);
        }
        return sBluetoothLeClass;
    }

    public BluetoothLeClass() {
        super();
    }

    public BluetoothLeClass(Context c) {
        mContext = c;
    }

    public void setOnConnectListener(OnConnectListener l) {
        mOnConnectListener = l;
    }

    public void setOnDisconnectListener(OnDisconnectListener l) {
        mOnDisconnectListener = l;
    }

    public void setReadRemoteRssiListener(OnReadRemoteRssiListener l) {
        mOnReadRemoteRssiListener = l;
    }

    public void setOnServiceDiscoverListener(OnServiceDiscoverListener l) {
        mOnServiceDiscoverListener = l;
    }

    public void setOnDataAvailableListener(OnDataAvailableListener l) {
        mOnDataAvailableListener = l;
    }

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                if (mOnConnectListener != null) {
                    mOnConnectListener.onConnect(gatt);
                }
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                boolean started = mBluetoothGatt.discoverServices(); // 这一句是耗时步骤
                Log.i(TAG, "Attempting to start service discovery:" + started);

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        if (mBluetoothGatt != null) {
                            mBluetoothGatt.readRemoteRssi();
                        }
                    }
                };
                Timer rssiTimer = new Timer();
                // rssiTimer.schedule(task, 1000, 1000);
                rssiTimer.schedule(task, 160, 160); // 500

                Intent mIntent = new Intent(ACTION_CONNECT);
                mIntent.putExtra("CONNECT_STATUC", 1);
                mContext.sendBroadcast(mIntent);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(TAG, "Disconnected from GATT server.");
                if (mOnDisconnectListener != null) {
                    mOnDisconnectListener.onDisconnect(gatt);
                }
                Intent mIntent = new Intent(ACTION_CONNECT);
                mIntent.putExtra("CONNECT_STATUC", 0);
                mContext.sendBroadcast(mIntent);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "onServicesDiscovered status == BluetoothGatt.GATT_SUCCESS");
                if (mOnServiceDiscoverListener != null) {
                    mOnServiceDiscoverListener.onServiceDiscover(gatt);
                }
            } else if (status == BluetoothGatt.GATT_FAILURE) {
                Log.i(TAG, "onServicesDiscovered status == BluetoothGatt.GATT_FAILURE");
            }
        }

//        @Override
//        public void onCharacteristicRead(BluetoothGatt gatt,
//                                         BluetoothGattCharacteristic characteristic, int status) {
//            if (mOnDataAvailableListener != null)
//                mOnDataAvailableListener.onCharacteristicRead(gatt,
//                        characteristic, status);
//        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            if (mOnDataAvailableListener != null)
                mOnDataAvailableListener.onCharacteristicWrite(gatt,
                        characteristic);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            updateRssiBroadcast(rssi);
        }
    };

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mBluetoothAdapter;
    }

    public void updateRssiBroadcast(int rssi) {
//        Log.i(TAG, "updateRssiBroadcast1 " + rssi);
// TODO 这段代码被注释了
//        Intent mIntent = new Intent(ACTION_NAME_RSSI);
//        mIntent.putExtra("RSSI", rssi);
//        mContext.sendBroadcast(mIntent);
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            return false;
        }
        Log.i(TAG, "connect(address)");
        // Previously connected device. Try to reconnect.
//       TODO 是否去掉这部分逻辑
//         if (address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
//            Log.i(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            return mBluetoothGatt.connect();
//        }
        Log.i(TAG, "mBluetoothAdapter.getRemoteDevice(address)");
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        Log.i(TAG, "device.connectGatt(mContext, false, mGattCallback);");
        mBluetoothGatt = device.connectGatt(mContext, false, mGattCallback);
        Log.i(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param characteristic The characteristic to read from.
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        if (enabled) {
            Log.i(TAG, "Enable Notification");
            mBluetoothGatt.setCharacteristicNotification(characteristic, true);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
            if (descriptor != null) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            } else {
                Log.e(TAG, "descriptor == null");
            }
        } else {
            Log.i(TAG, "Disable Notification");
            mBluetoothGatt.setCharacteristicNotification(characteristic, false);
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    CLIENT_CHARACTERISTIC_CONFIG_DESCRIPTOR_UUID);
            descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

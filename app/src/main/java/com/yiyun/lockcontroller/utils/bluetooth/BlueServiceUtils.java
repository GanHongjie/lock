package com.yiyun.lockcontroller.utils.bluetooth;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.util.List;

/**
 * Created by Layo on 2017-7-20.
 */
public class BlueServiceUtils {
    private final static String TAG = "BlueServiceUtils";// DeviceScanActivity.class.getSimpleName();
    public static final int REFRESH = 0x000001;
    private final static int REQUEST_CODE = 1;

    static private byte writeValue_char1 = 0;

    public static final String UUID_KEY_DATA = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR1 = "0000fff1-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR2 = "0000fff2-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR3 = "0000fff3-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR4 = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR5 = "0000fff5-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR6 = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static final String UUID_CHAR7 = "0000fff7-0000-1000-8000-00805f9b34fb";
    public static final String UUID_HERATRATE = "00002a37-0000-1000-8000-00805f9b34fb";
    public static final String UUID_TEMPERATURE = "00002a1c-0000-1000-8000-00805f9b34fb";
    public static final String UUID_0XFFA6 = "0000ffa6-0000-1000-8000-00805f9b34fb";

    // 读写BLE终端

    static BluetoothGattCharacteristic gattCharacteristic_char1 = null;
    static BluetoothGattCharacteristic gattCharacteristic_char5 = null;
    static BluetoothGattCharacteristic gattCharacteristic_char6 = null;
    static BluetoothGattCharacteristic gattCharacteristic_heartrate = null;
    static BluetoothGattCharacteristic gattCharacteristic_keydata = null;
    static BluetoothGattCharacteristic gattCharacteristic_temperature = null;
    static BluetoothGattCharacteristic gattCharacteristic_0xffa6 = null;

    // 字节发送
    static public void writeChar6_in_bytes(BluetoothLeClass mBLE,byte bytes[]) {
        // byte[] writeValue = new byte[1];
//        Log.i(TAG, "gattCharacteristic_char6 = " + gattCharacteristic_char6);
        if (gattCharacteristic_char6 != null) {
            gattCharacteristic_char6.setValue(bytes);
           mBLE.writeCharacteristic(gattCharacteristic_char6);
        }
    }

    static public void read_uuid_0xffa6(BluetoothLeClass mBLE) {
        Log.i(TAG, "readCharacteristic = ");
        if (gattCharacteristic_0xffa6 != null) {
            mBLE.readCharacteristic(gattCharacteristic_0xffa6);
        }
    }

    static public BluetoothGattCharacteristic setBLECharacteristic(List<BluetoothGattService> gattServices, BluetoothLeClass mBLE) {
        // -----Characteristics的字段信----//

        BluetoothGattCharacteristic Characteristic_cur = null;
//        Log.i(TAG, "BlueServiceUtils.UUID_CHAR6 = " + BlueServiceUtils.UUID_CHAR6);
        for (BluetoothGattService gattService : gattServices) {
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
//                Log.i(TAG, "gattCharacteristic.getUuid().toString() = " + gattCharacteristic.getUuid().toString());
                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_CHAR5)) {
                    gattCharacteristic_char5 = gattCharacteristic;
                }

                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_CHAR6)) {
                    // 把char1 保存起来以方便后面读写数据时使用
                    gattCharacteristic_char6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);

                }

                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_HERATRATE)) {
                    // 把heartrate 保存起来，以方便后面读写数据时使用
                    gattCharacteristic_heartrate = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的，收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_KEY_DATA)) {
                    // 把heartrate 保存起来，以方便后面读写数据时使用
                    gattCharacteristic_keydata = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的，收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_TEMPERATURE)) {
                    // 把heartrate 保存起来，以方便后面读写数据时使用
                    gattCharacteristic_temperature = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                    // 接受Characteristic被写的，收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBLE.setCharacteristicNotification(gattCharacteristic, true);
                }

                if (gattCharacteristic.getUuid().toString().equals(BlueServiceUtils.UUID_0XFFA6)) {
                    // 把heartrate 保存起来，以方便后面读写数据时使用
                    gattCharacteristic_0xffa6 = gattCharacteristic;
                    Characteristic_cur = gattCharacteristic;
                }
            }
        }
        return Characteristic_cur;
    }


}

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

package com.example.ustc.healthreps.health.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.health.util.SampleGattAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;



/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String UpData = "com.example.bluetooth.le.UpData";
    //下面几个是默示连接用的
    private static final long SCAN_PERIOD = 8000;
    private HashMap<String, byte[]> scanrecordsMap = new HashMap<String, byte[]>();
    //private BluetoothAdapter mBluetoothAdapter;//上面有了
    private Handler mHandler = new Handler();
    //private Handler m_Handler=new Handler();
   // static public boolean isSAME = false;

    //private static final int REQUEST_ENABLE_BT = 1;

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            System.out.println("=======status:" + status);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:");

                mBluetoothGatt.discoverServices();//注意
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                SampleGattAttributes.notify(gatt, UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY));//启动等待notify监听
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("onCharacteristicRead");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        /**@Override public void onDescriptorWrite(BluetoothGatt gatt,
        BluetoothGattDescriptor descriptor, int status) {
        System.out.println("onDescriptorWriteonDescriptorWrite = " + status
        + ", descriptor =" + descriptor.getUuid().toString());
        }*/
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            if (characteristic.getValue() != null) {
                System.out.println(characteristic.getStringValue(0));
            }
            System.out.println("--------onCharacteristicChanged-----");
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            System.out.println("rssi = " + rssi);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            System.out.println("--------write success----- status:" + status);

        }

        ;
    };

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action, byte[] updata) {
        final Intent intent = new Intent(action);
        intent.putExtra("data", updata);
        sendBroadcast(intent);
        System.out.println("bofang");
    }

    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        final Intent intent = new Intent(action);
        int[] value = new int[6];
        int[] format = new int[3];
        format[1] = BluetoothGattCharacteristic.FORMAT_UINT8;
        format[2] = BluetoothGattCharacteristic.FORMAT_UINT16;
        final UUID uuid, temp;
        {
            uuid = characteristic.getUuid();
            if (uuid.equals(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY))) {
                intent.putExtra("DataType", "SevenDayData");
                intent.putExtra("data", characteristic.getValue());
            }
            if (uuid.equals(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW))) {
                intent.putExtra("DataType", "CollectPeriod");

                intent.putExtra("data", characteristic.getValue());
            }
            if (uuid.equals(UUID.fromString(SampleGattAttributes.POWER))) {
                intent.putExtra("DataType", "Power");

                intent.putExtra("data", characteristic.getValue());
            }
            if (uuid.equals(UUID.fromString(SampleGattAttributes.TIMEADJUSTRW))) {//格式为：秒（1Byte）+分(1Byte)+ 时(1Byte)+ 日(1Byte)+月(Byte)+年(2Bytes)
                intent.putExtra("DataType", "TimeAdjust");

                intent.putExtra("data", characteristic.getValue());
            }/**if(uuid.equals()){
         可以接着写
         }*/
        }

        sendBroadcast(intent);
    }

    /**
     * Intent intent = getIntent();
     * String pose_name = intent.getStringExtra("pose_name");
     * String img_file_name = intent.getStringExtra("img_file_name");
     * Bundle b=this.getIntent().getExtras();
     * pose_title = b.getStringArray("pose_title");
     */

    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that
        // BluetoothGatt.close() is called
        // such that resources are cleaned up properly. In this particular
        // example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter
        // through
        // BluetoothManager.
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
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
        /*//3.判断是否支持蓝牙，并打开蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }*/
        /*m_BluetoothAdapter=mBluetoothManager.getAdapter();
        if (m_BluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }*/

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *                <p/>
     *                * @return Return true if the connection is initiated successfully. The
     *                connection result is reported asynchronously through the
     *                {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *                callback.
     */
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
      /*  if (m_BluetoothAdapter == null || address == null) {
            Log.w(TAG,
                    "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }*/

        // Previously connected device. Try to reconnect. (先前连接的设备。 尝试重新连接)
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);//可能蓝牙啥的掉了
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        /**We want to directly connect to the device, so we are setting the
         autoConnect
         parameter to false.*/
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);//真正连接代码
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
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
    public void close() {//因该是自动处理函数
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.writeCharacteristic(characteristic);//真正回调的原因

    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, android.bluetooth.BluetoothGattCharacteristic, int)}
     * callback.
     *
     * @param
     *///系统调用
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;

        return mBluetoothGatt.getServices();
    }

    /**
     * Read the RSSI for a connected remote device.
     */
    public boolean getRssiVal() {
        if (mBluetoothGatt == null)
            return false;

        return mBluetoothGatt.readRemoteRssi();//这是个int ，发现扫描回调里边有啊
    }

    boolean readMessage(UUID uuid) {
        return SampleGattAttributes.readMessage(mBluetoothGatt, uuid);
    }

    boolean writeMessage(UUID uuid, byte[] data) {
        System.out.println("--------try to write  in sevice----- ");
        return SampleGattAttributes.sendMessage(mBluetoothGatt, data, uuid);

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            Log.e(TAG, "onLeScan");
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences("share", MODE_PRIVATE);
            String device_name = sharedPreferences.getString("DEVICE_NAME", "FFFFFFFFFFF");

            Log.d(TAG, device_name + "44:A6:E5:03:5C:36");
          /*  if (device.getAddress().equals(device_name))
            {
                isSAME=true;
            }*/
            if (device.getAddress().equals(device_name)) {
                scanLeDevice(false);
                Log.e(TAG, "scanLeDevice(false)");
                if (!(Arrays.equals(scanrecordsMap.get(MainActivity.mDeviceAddress), scanRecord))) {
                    //int i;if((scanrecordsMap.get(DeviceControlActivity.mDeviceAddress)!=null))for(i=0;i<=61;i++)Log.e(TAG,Integer.toHexString(scanRecord[i] & 0x000000ff) + ""+Integer.toHexString(scanrecordsMap.get(DeviceControlActivity.mDeviceAddress)[i] & 0x000000ff));    // Sets up UI references.

                    scanrecordsMap.put(device.getAddress(), scanRecord);
                    broadcastUpdate(UpData, scanrecordsMap.get(MainActivity.mDeviceAddress));
                }
            }
            else
            {
               // Toast.makeText(getApplication(),"连接失败！请到搜索界面进行搜索设备",Toast.LENGTH_LONG).show();
            }
        }

    };

    public void scanLeDevice(final boolean enable) {
        Log.e(TAG, "scanLeDevice(final boolean enable)");
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);//after a period it will run only itself
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }


}
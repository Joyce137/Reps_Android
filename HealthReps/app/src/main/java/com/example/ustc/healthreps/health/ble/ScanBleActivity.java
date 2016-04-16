package com.example.ustc.healthreps.health.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by HBL on 2015/11/9.
 */
public class ScanBleActivity extends Activity implements View.OnClickListener {

    private RecyclerView scan_recyclerView;
    private RecyclerView.Adapter adapter;
    public List<DeviceBean> deviceBeans = new ArrayList<>();
    private Context context;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private Handler scanHandler;
    private boolean mScanning;
    private final static String TAG = ScanBleActivity.class
            .getSimpleName();
    private HashMap<String, byte[]> scanrecordsMap = new HashMap<String, byte[]>();
    private Button scan_ble_notscan, scan_ble_rescan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble);
        context = this;

        scan_recyclerView = (RecyclerView) findViewById(R.id.scan_recyclerview);
        scan_recyclerView.setHasFixedSize(true);
        scan_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScanning = true;

        adapter = new DeviceAdapter(ScanBleActivity.this, deviceBeans);
        scan_recyclerView.setAdapter(adapter);

        scan_ble_notscan = (Button) findViewById(R.id.scan_ble_notscan);
        scan_ble_notscan.setOnClickListener(this);

        scan_ble_rescan = (Button) findViewById(R.id.scan_ble_rescan);
        scan_ble_rescan.setOnClickListener(this);

        scanHandler = new Handler();
        //1.android:required="false",判断系统是否支持BLE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        //2.获取BluetoothAdapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //3.判断是否支持蓝牙，并打开蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        //4.搜索BLE设备
        //当找到对应的设备后，立即停止扫描；
        //不要循环搜索设备，为每次搜索设置适合的时间限制。避免设备不在可用范围的时候持续不停扫描，消耗电量。
        scanLeDevice(true);
    }
    private void scanLeDevice(final boolean enable) {

        if (enable) {
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceBeans.add(new DeviceBean(device.getName(), device.getAddress()));
                            scanrecordsMap.put(device.getAddress(), scanRecord);
                            scan_recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            adapter.notifyDataSetChanged();
                            Log.e(TAG, device.getName() + deviceBeans.size() + "" + scanrecordsMap.size());
                        }
                    });
                }
            };

    public void startActivity(final View v, final DeviceBean bean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScanBleActivity.this);
        // builder.setIcon(R.mipmap.ic_warning_amber);
        builder.setMessage("您选的设备是：" + bean.getName().trim() + "，是否绑定");
        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("DEVICE_NAME", bean.getName());
                intent.putExtra("DEVICE_ADDRESS", bean.getAddress());
                intent.putExtra("scan", scanrecordsMap.get(bean.getAddress()));//哈希表返回的是地址对应的扫描号
                SharedPreferences sharedPreferences = getSharedPreferences("share", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("DEVICE_NAME", bean.getAddress());
                editor.commit();
                Log.d(TAG, bean.getName() + bean.getAddress());
                startActivity(intent);
            }
        });
        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_ble_rescan:
                reScan();
                Toast.makeText(ScanBleActivity.this, "正在重新扫描中......", Toast.LENGTH_LONG).show();
                break;
            case R.id.scan_ble_notscan:
                scanLeDevice(false);//停止
                Toast.makeText(ScanBleActivity.this, "已停止扫描", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void reScan() {
        scanLeDevice(false);//停止
        scanrecordsMap.clear();
        deviceBeans.clear();
        scan_recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        adapter.notifyDataSetChanged();
        scanLeDevice(true);//开始
    }

}

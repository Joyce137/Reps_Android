package com.example.ustc.healthreps;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HBL on 2015/11/9.
 */
public class ScanBleActivity extends Activity {
    private TextView sacn_state_textView;
    private TextView scan_ing_textView;
    public static final int UPDATE_TEXT = 1;
    public static final int SCAN_FINISH=2;
    public static  int pointNum=0;
    private Handler handler;
    Thread myhead;
    private RecyclerView scan_recyclerView;
    private RecyclerView.Adapter adapter;
    public List<DeviceBean> deviceBeans = new ArrayList<DeviceBean>();



    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT=1;
    private static final long SCAN_PERIOD = 10000;
    private Handler scanHandler;
    private boolean mScanning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_ble);
        sacn_state_textView=(TextView)findViewById(R.id.scan_state_textView);
        scan_ing_textView=(TextView)findViewById(R.id.scan_ing_txtView);
        scan_recyclerView=(RecyclerView)findViewById(R.id.scan_recyclerview);
        scan_recyclerView.setHasFixedSize(true);
        scan_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mScanning=true;
      /* for (int i=0;i<1;i++)
        {
            deviceBeans.add(new DeviceBean("MI1A"+i,"88:0F:10:DA:3F:F3"+i));
        }*/
        adapter=new DeviceAdapter(ScanBleActivity.this,deviceBeans);
        scan_recyclerView.setAdapter(adapter);

        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case UPDATE_TEXT:

                        String stateStrin;
                        if (pointNum==0)
                        {
                            stateStrin=".";
                        }
                        else if (pointNum==1)
                        {
                            stateStrin="..";
                        }
                        else
                        {
                            stateStrin="...";
                        }
                        sacn_state_textView.setText(stateStrin);
                        break;
                    case SCAN_FINISH:
                       // myhead.stop();
                        sacn_state_textView.setVisibility(View.GONE);
                        scan_ing_textView.setText("扫描完成");
                       // sacn_state_textView.setText("OK了");
                        mScanning=false;
                    default:
                        break;
                }
            }
        };
        myThread mythread=new myThread();
        //new Thread(mythread).start();
        myhead=new Thread(mythread);
        myhead.start();
        scanHandler=new Handler();
        //1.android:required="false",判断系统是否支持BLE
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)){
            Toast.makeText(this, "此设备不支持BLE", Toast.LENGTH_SHORT).show();
            finish();
        }
        //2.获取BluetoothAdapter
        final BluetoothManager bluetoothManager =
                (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        //3.判断是否支持蓝牙，并打开蓝牙
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        scanLeDevice(true);
    }
    private void scanLeDevice(final boolean enable)
    {
        if (enable)
        {
            scanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning=false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            },SCAN_PERIOD);
            mScanning=true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        }
        else
        {
            mScanning=false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            deviceBeans.add(new DeviceBean(device.getName(), device.getAddress()));
                            adapter.notifyDataSetChanged();


                            Message message = new Message();
                            message.what = SCAN_FINISH;
                            handler.sendMessage(message); // 将Message对象发送出去
                            Log.e("find", device.getName());
                        }
                    });
                }
            };
    class myThread implements Runnable {
        public void run() {
            while (mScanning) {
                pointNum++;
                pointNum = pointNum % 3;
                Log.d("ScanBleActivity", "" + pointNum);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                Message message = new Message();
                message.what = UPDATE_TEXT;
                handler.sendMessage(message); // 将Message对象发送出去
            }
        }
    }
}

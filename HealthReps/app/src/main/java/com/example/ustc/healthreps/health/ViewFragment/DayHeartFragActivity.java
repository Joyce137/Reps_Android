package com.example.ustc.healthreps.health.ViewFragment;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.SleepMode;
import com.example.ustc.healthreps.health.ble.BluetoothLeService;
import com.example.ustc.healthreps.health.ble.DeviceControlService;
import com.example.ustc.healthreps.health.ble.SampleGattAttributes;

import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HBL on 2016/3/8.
 */
public class DayHeartFragActivity extends Fragment {
    View view;
    TextView heartrateText;
//    Button sleepButton;     //睡眠模式按钮
////    boolean isSleepBMode = false;   //睡眠模式
//    SleepMode sleepMode;

////    sleepMode
//    private Handler mHandler = new Handler();
//    Runnable runnable;
//    byte[] p = new byte[4];//这个用来int转换byte的暂存数组
//    final byte[] test = new byte[2];
//    boolean istestxinlv = false, istestxinlv_open = false, istestxinlv_close = false;
//    boolean checkflag2=false;
//    boolean checkflag3=false;
//    public static boolean openSleepModeSuccess = false;
//    public static boolean closeSleepModeSuccess = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_heartrate_detail, container, false);
        heartrateText = (TextView) view.findViewById(R.id.tv_heartrate_detaile_num);
        heartrateText.setText( MainActivity.str01);

//        //睡眠模式
//        sleepMode = new SleepMode(view.getContext());
//
////        sleepButton = (Button) view.findViewById(R.id.bt_sleepmode_button);
//        if(MainActivity.isSleepBMode){
//            sleepButton.setText("关闭睡眠模式");
//        }
//        else {
//            sleepButton.setText("开启睡眠模式");
//        }
//        sleepButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sleepMode.testConnect();
////                testConnect();
//
//                //当前为睡眠模式
//                if (MainActivity.isSleepBMode) {
//                    //关闭睡眠模式
////                    closeSleepMode();
//                    sleepMode.closeSleepMode();
//                    SleepMode.closeSleepModeSuccess = true;
//                    MainActivity.isSleepBMode = false;
//                    sleepButton.setText("开启睡眠模式");
//                }
//                //当前非睡眠模式
//                else {
//                    //开启睡眠模式
////                    openSleepMode();
//                    sleepMode.openSleepMode();
//                    SleepMode.openSleepModeSuccess = true;
//                    MainActivity.isSleepBMode = true;
//                    sleepButton.setText("关闭睡眠模式");
//                }
//
//                //disconnect
//                if(!SampleGattAttributes.connectedState){
//                    DeviceControlService.mBluetoothLeService.disconnect();
//                }
//
//            }
//        });
        return view;
    }

//    //测试设备连接状况
//    public boolean testConnect(){
//        if (!DeviceControlService.mBluetoothLeService.initialize()) {//引用服务类的初始化
//            Toast.makeText(view.getContext(), "未初始化", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        if(!SampleGattAttributes.connectedState) {
//            if (!DeviceControlService.mBluetoothLeService.connect(MainActivity.mDeviceAddress)) {
//                Toast.makeText(view.getContext(), "未连接蓝牙，不能开启", Toast.LENGTH_LONG).show();
//                return false;
//            }
//            else {
//                Toast.makeText(view.getContext(), "已经连接", Toast.LENGTH_LONG).show();
//            }
//        }
//        else {
//            Toast.makeText(view.getContext(), "已经连接", Toast.LENGTH_LONG).show();
//        }
//
//        //注册广播接收器
//        view.getContext().registerReceiver(mGattUpdateReceiver01, makeGattUpdateIntentFilter());
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                //要做的事情
//                System.out.println("判断中");
//                if(SampleGattAttributes.lastrecall.compareTo(SampleGattAttributes.thisrecall)==0
//                        &&SampleGattAttributes.connectedState){
//                    DeviceControlService.mBluetoothLeService.disconnect();
//                }//
//                else
//                    SampleGattAttributes.lastrecall=SampleGattAttributes.thisrecall;
//                mHandler.postDelayed(this, 30000);
//            }
//        };
//
//        return true;
//    }

//    //打开睡眠模式
//    public void openSleepMode(){
//        //心率设置为3分/次（20次/时)
//        int measuretimes = 480;
//        //写入蓝牙
//        SampleGattAttributes.trim(p, measuretimes);
//        byte[] data = {p[3], p[2]};
//        DeviceControlService.mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW), data);////写入
////        DeviceControlService.mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW));//
//
//        //设置为非连续测心率
//        istestxinlv_close = true;
//        istestxinlv_open = false;
//        test[1] = 0x55;
//        test[0] = 0x02;
//        DeviceControlService.mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);//
//    }

//    //关闭睡眠模式
//    public void closeSleepMode(){
//        //心率恢复为2小时/次
//        int measuretimes = 12;
//        //写入蓝牙
//        SampleGattAttributes.trim(p, measuretimes);
//        byte[] data = {p[3], p[2]};
//        DeviceControlService.mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW), data);////写入
////        DeviceControlService.mBluetoothLeService.readMessage(UUID.fromString(SampleGattAttributes.HEARTRATEPERIODRW));//
//
//        //设置为默认值（非连续测心率)
//        test[1] = 0x55;
//        test[0] = 0x02;
//        DeviceControlService.mBluetoothLeService.writeMessage(UUID.fromString(SampleGattAttributes.CHARACTERISTIC_NOTIFY), test);//
//    }

//    //自定义广播
//    private static IntentFilter makeGattUpdateIntentFilter() {
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
//        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
//        // intentFilter.addAction(BluetoothLeService.UpData);
//        intentFilter.addAction(BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS);
//        return intentFilter;
//    }
//
//    //定义一个广播接收器
//    private final BroadcastReceiver mGattUpdateReceiver01 = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context , Intent intent) {
//            final String action = intent.getAction();
//            //已经连接上BLE
//
//            SampleGattAttributes.thisrecall= Calendar.getInstance();//记录回掉时间
//
//            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
//                System.out.println("到ACTION_GATT_CONNECTED");
//                SampleGattAttributes.connectedState=true;//连接成功
//                if(SampleGattAttributes.isinsetactivity)
//                    mHandler.postDelayed(runnable, 30000);
//            } else if (BluetoothLeService.ACTION_GATT_WIRTE_SUCCESS.equals(action)) {
//                if (istestxinlv) {
//                    if (istestxinlv_open) {
//                        Toast.makeText(view.getContext(), "连续测心率打开成功！", Toast.LENGTH_LONG).show();
//                        SampleGattAttributes.checkflag=true;
//
//                        checkflag2=true;//告诉listener不是人操作
//                    }
//
//                    if (istestxinlv_close) {
//                        Toast.makeText(view.getContext(), "连续测心率关闭成功！", Toast.LENGTH_LONG).show();
//                        SampleGattAttributes.checkflag=false;
//                        checkflag2=true;
//                    }
//                } else {
//                    Toast.makeText(view.getContext(), "修改成功！", Toast.LENGTH_LONG).show();
//                }
//            }
//            //没有连接上BLE
//            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
//                mHandler.removeCallbacks(runnable);//关掉自动关闭连接的线程
//                System.out.println("断线重连显示，并关闭判断中");
//            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
//                    .equals(action)) {
//                Toast.makeText(view.getContext(), "服务获取", Toast.LENGTH_LONG).show();
//            }else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
//
//                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
//
//                } else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {
//                    int mode = SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 2);
//                    if(mode == 480){
//                        openSleepModeSuccess = true;
//                        MainActivity.isSleepBMode = true;
//                        sleepButton.setText("关闭睡眠模式");
//                    }
//                    if(mode == 12){
//                        closeSleepModeSuccess = true;
//                        MainActivity.isSleepBMode = false;
//                        sleepButton.setText("开启睡眠模式");
//                    }
//                }
//            }
//        }
//    };

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        SampleGattAttributes.isinsetactivity=false;//出去了
//        DeviceControlService.mBluetoothLeService.disconnect();
//        SampleGattAttributes.connectedState=false;
//        mHandler.removeCallbacks(runnable);
//
//        view.getContext().unregisterReceiver(mGattUpdateReceiver01);
//        istestxinlv = false;
//        istestxinlv_close = false;
//        istestxinlv_open = false;
//    }

}

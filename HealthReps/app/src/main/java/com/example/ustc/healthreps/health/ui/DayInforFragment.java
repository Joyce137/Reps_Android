package com.example.ustc.healthreps.health.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.ProFileActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.database.impl.BleDataDaoImpl;
import com.example.ustc.healthreps.database.impl.HeartRataDataDaoImpl;
import com.example.ustc.healthreps.health.ble.BluetoothLeService;
import com.example.ustc.healthreps.health.ble.SampleGattAttributes;
import com.example.ustc.healthreps.health.util.ComFormu;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/10/22.
 */
public class DayInforFragment extends Fragment implements View.OnClickListener {
    private TextView textView_01, textView_02, textView_03, textView_04;
    byte[] scan;
    public static String str01 = "0", str02 = "0", str03 = "0.00";
    public static  boolean flag1=true,flag2=false;

    int gengxin=2;
    LinearLayout ly_heartrate, ly_stepnum, ly_kcal, ly_sleepmode;
    TextView xinlvText, sleepText, kcalText, stepText;
    ImageView xinlvimg, sleepimg, kcalimg, stepimg;
    //private PopupWindow popupWindow;
    private TextView myInfoTextView;

    // private ChartView mChartView;
    ImageView imageView;
    //数据库
    BleDataDaoImpl bleDataDao;

    private final static String TAG = DayInforFragment.class
            .getSimpleName();

    public static DayInforFragment newInstance() {
        DayInforFragment f = new DayInforFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_myinfor, container, false);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());


        //打开数据库
//        dbManager = DBManager.getInstance(getContext());
//        dbManager.openDatabase();

        bleDataDao = new BleDataDaoImpl(getActivity().getApplicationContext(),"abc");
//        bleDataDao.insertBleData("123","13.34","80.445","0");
        bleDataDao.saveBleDataToFile(bleDataDao.queryBleData());

       // dbManager.queryNewBleData();
        ly_heartrate = (LinearLayout) view.findViewById(R.id.ly_myinfo_heartrate);
        ly_stepnum = (LinearLayout) view.findViewById(R.id.ly_myinfo_stepnum);
        ly_kcal = (LinearLayout) view.findViewById(R.id.ly_myinfo_kcal);
        ly_sleepmode=(LinearLayout)view.findViewById(R.id.ly_myinfo_sleepmode);

        ly_stepnum.setOnClickListener(this);
        ly_heartrate.setOnClickListener(this);
        ly_kcal.setOnClickListener(this);
        ly_sleepmode.setOnClickListener(this);

        xinlvText = (TextView) view.findViewById(R.id.xinlvtext);
        xinlvimg = (ImageView)view.findViewById(R.id.xinlvimg);
        sleepText = (TextView) view.findViewById(R.id.sleepmodeText);
        sleepimg = (ImageView)view.findViewById(R.id.sleepmodeimg);
        kcalText = (TextView) view.findViewById(R.id.kcalText);
        kcalimg = (ImageView)view.findViewById(R.id.kcalimg);
        stepText = (TextView) view.findViewById(R.id.stepText);
        stepimg = (ImageView)view.findViewById(R.id.stepimg);

        textView_01 = (TextView) view.findViewById(R.id.myinfo_textView_01);
        textView_02 = (TextView) view.findViewById(R.id.myinfo_textView_02);
        textView_03 = (TextView) view.findViewById(R.id.myinfo_textView_03);
        textView_04 = (TextView) view.findViewById(R.id.myinfo_textView_04);
        str01 = MainActivity.str01;
        str02 = MainActivity.str02;

        textView_01.setText(str01);
        textView_02.setText(str02);
//        DecimalFormat df = new DecimalFormat("###.00");
//        str03 = df.format(ComFormu.getKcal(Integer.valueOf(str02))).toString();
        str03 = ComFormu.getKcal(str02);
        textView_03.setText(str03);

        textView_04.setText("0");
       // MyhealthActivity.steunumDetail();

        //我的
        myInfoTextView = (TextView) view.findViewById(R.id.info_ly);
        myInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), MyActivity.class);
                startActivity(intent);
            }
        });


        setStyle(ly_heartrate, xinlvText, textView_01, true);
        xinlvimg.setImageResource(R.mipmap.ic_favorite1);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        broadcastManager.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    //自定义广播
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.UpData);
        return intentFilter;
    }

    //定义一个广播接收器
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            //已经连接上BLE
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
            }
            //没有连接上BLE
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                    .equals(action)) {

            } else if (BluetoothLeService.UpData.equals(action)) {
                scan = intent.getByteArrayExtra("data");
                //  System.out.print("~~~~");
                str01 = String.valueOf(scan[19] & 0xff);
                str02 = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));

//                DecimalFormat df = new DecimalFormat("###.00");
//                str03 = df.format(ComFormu.getKcal(Integer.valueOf(str02))).toString();
                str03 = ComFormu.getKcal(str02);

                MainActivity.str01 = str01;
                MainActivity.str02 = str02;
                textView_01.setText(str01);
                textView_02.setText(str02);
                textView_03.setText(str03);
//                dbManager.insertBleData(Integer.valueOf(str02), Integer.valueOf(str01), DataToData.getCurrentTime());
                bleDataDao.insertBleData(str02, str01, str03, "0");
                Log.d(TAG, "insert success!");
//                dbManager.query();
                Log.d(TAG, "query success!");

                Intent sendint;
                switch (gengxin)
                {
                    case 1:
                        sendint=new Intent(MyhealthActivity.ACTION_VIEW_HEART);
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(sendint);
                        //睡眠模式
                        if(MainActivity.isSleepBMode == true)
                        {
                            HeartRataDataDaoImpl rateDao = new HeartRataDataDaoImpl(getActivity().getApplicationContext());
                            rateDao.insertHeartRataData(str01);
                        }
                        break;
                    case 2:
                        sendint=new Intent(MyhealthActivity.ACTION_VIEW_STEP);
                        getContext().sendBroadcast(sendint);
                        break;
                    case 3:
                        sendint=new Intent(MyhealthActivity.ACTION_VIEW_KCAL);
                        getContext().sendBroadcast(sendint);
                        break;
                    case 4:
                        sendint=new Intent(MyhealthActivity.ACTION_VIEW_SLEEPMODEDATA);
                        getContext().sendBroadcast(sendint);
                        break;
                }
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //这块进行分类解析
                /**数据头//UINT几 是INT型占几位的新结构类型
                 0x336699DB(4Bytes)+年（2Bytes）+月（1Byte）+日（1Byte）
                 +步数（2Bytes）+心率数据（10Bytes）           */
                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
                    byte[] sevenData;
                    sevenData=intent.getByteArrayExtra("data");
                    Log.d(TAG,"hhhhhhhhhh");
                    Log.d(TAG,"hhhhhhhhhh"+ SampleGattAttributes.dataDivider(sevenData, 1, 10));
                    Log.d(TAG,"hhhhhhhhhh"+SampleGattAttributes.dataDivider(sevenData,1,8));
                } else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {
                    int mode = SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 2);
                    if(mode == 480){
                        MainActivity.isSleepBMode = true;
                    }
                    if(mode == 12){
                        MainActivity.isSleepBMode = false;
                    }
                } else if (intent.getStringExtra("DataType").equals("Power")) {
                   /* MainActivity.str01 = SampleGattAttributes.dataGetter(intent.getByteArrayExtra("data"), 0, 1)+"%";
                    textView_01.setText( MainActivity.str01 );*/

                } else if (intent.getStringExtra("DataType").equals("TimeAdjust")) {

                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        // dbManager.closeDatabase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_myinfo_heartrate:
                gengxin = 1;
                MyhealthActivity.mode = 1;
                //控件变颜色
                setStyle(ly_heartrate, xinlvText, textView_01, true);
                xinlvimg.setImageResource(R.mipmap.ic_favorite1);
                setStyle(ly_sleepmode, sleepText, textView_04, false);
                sleepimg.setImageResource(R.mipmap.ic_sleepmode2);
                setStyle(ly_kcal, kcalText, textView_03, false);
                kcalimg.setImageResource(R.mipmap.ic_whatshot2);
                setStyle(ly_stepnum, stepText, textView_02, false);
                stepimg.setImageResource(R.mipmap.ic_walk2);

                Intent sendint = new Intent(MyhealthActivity.ACTION_VIEW_HEART);
                (getActivity()).sendBroadcast(sendint);
                break;
            case R.id.ly_myinfo_sleepmode:
                gengxin = 4;
                MyhealthActivity.mode = 2;
                //控件变颜色
                setStyle(ly_heartrate, xinlvText, textView_01, false);
                xinlvimg.setImageResource(R.mipmap.ic_favorite2);
                setStyle(ly_sleepmode, sleepText, textView_04, true);
                sleepimg.setImageResource(R.mipmap.ic_sleepmode1);
                setStyle(ly_kcal, kcalText, textView_03, false);
                kcalimg.setImageResource(R.mipmap.ic_whatshot2);
                setStyle(ly_stepnum, stepText, textView_02, false);
                stepimg.setImageResource(R.mipmap.ic_walk2);

                sendint = new Intent(MyhealthActivity.ACTION_VIEW_SLEEPMODEDATA);
                (getActivity()).sendBroadcast(sendint);

                break;
            case R.id.ly_myinfo_kcal:
                gengxin = 3;
                MyhealthActivity.mode = 3;
                //控件变颜色
                setStyle(ly_heartrate, xinlvText, textView_01, false);
                xinlvimg.setImageResource(R.mipmap.ic_favorite2);
                setStyle(ly_sleepmode, sleepText, textView_04, false);
                sleepimg.setImageResource(R.mipmap.ic_sleepmode2);
                setStyle(ly_kcal, kcalText, textView_03, true);
                kcalimg.setImageResource(R.mipmap.ic_whatshot1);
                setStyle(ly_stepnum, stepText, textView_02, false);
                stepimg.setImageResource(R.mipmap.ic_walk2);

                sendint = new Intent(MyhealthActivity.ACTION_VIEW_KCAL);
                (getActivity()).sendBroadcast(sendint);
                break;
            case R.id.ly_myinfo_stepnum:
                gengxin = 2;
                MyhealthActivity.mode = 4;
                //控件变颜色
                setStyle(ly_heartrate, xinlvText, textView_01, false);
                xinlvimg.setImageResource(R.mipmap.ic_favorite2);
                setStyle(ly_sleepmode, sleepText, textView_04, false);
                sleepimg.setImageResource(R.mipmap.ic_sleepmode2);
                setStyle(ly_kcal, kcalText, textView_03, false);
                kcalimg.setImageResource(R.mipmap.ic_whatshot2);
                setStyle(ly_stepnum, stepText, textView_02, true);
                stepimg.setImageResource(R.mipmap.ic_walk1);

                sendint = new Intent(MyhealthActivity.ACTION_VIEW_STEP);
                (getActivity()).sendBroadcast(sendint);
                break;
            default:
                break;
        }
    }

    public void setStyle(LinearLayout layout, TextView textView, TextView textView1,boolean isSelected){
        if(isSelected){
            layout.setBackground(getResources().getDrawable(R.drawable.my_backgroud_selected));
            textView.setTextColor(getResources().getColor(R.color.white));
            textView1.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            layout.setBackground(getResources().getDrawable(R.drawable.my_backgroud));
            textView.setTextColor(getResources().getColor(R.color.black));
            textView1.setTextColor(getResources().getColor(R.color.black));
        }
    }
}

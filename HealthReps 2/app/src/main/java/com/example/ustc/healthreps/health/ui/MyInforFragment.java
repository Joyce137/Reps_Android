package com.example.ustc.healthreps.health.ui;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.database.impl.BleDataDaoImpl;
import com.example.ustc.healthreps.health.service.BluetoothLeService;
import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by Administrator on 2015/10/22.
 */
public class MyInforFragment extends Fragment implements View.OnClickListener {
    private TextView textView_01, textView_02, textView_03, textView_04;
    byte[] scan;
    public static String str01 = "0", str02 = "0";
    LinearLayout ly_heartrate, ly_stepnum;
    //private PopupWindow popupWindow;

    // private ChartView mChartView;
    ImageView imageView;
    //数据库
    BleDataDaoImpl bleDataDao;
    private final static String TAG = MyInforFragment.class
            .getSimpleName();

    public static MyInforFragment newInstance() {
        MyInforFragment f = new MyInforFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);

        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        //打开数据库
        bleDataDao = new BleDataDaoImpl(getActivity().getApplicationContext());

        ly_heartrate = (LinearLayout) view.findViewById(R.id.ly_myinfo_heartrate);
        ly_stepnum = (LinearLayout) view.findViewById(R.id.ly_myinfo_stepnum);

        ly_stepnum.setOnClickListener(this);
        ly_heartrate.setOnClickListener(this);

        textView_01 = (TextView) view.findViewById(R.id.myinfo_textView_01);
        textView_02 = (TextView) view.findViewById(R.id.myinfo_textView_02);
        textView_03 = (TextView) view.findViewById(R.id.myinfo_textView_03);
        textView_04 = (TextView) view.findViewById(R.id.myinfo_textView_04);
        str01 = MainActivity.str01;
        str02 = MainActivity.str02;
        textView_01.setText(str01);
        textView_02.setText(str02);
        textView_03.setText("0");
        textView_04.setText("0");
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
                MainActivity.str01 = str01;
                MainActivity.str02 = str02;
                textView_01.setText(str01);
                textView_02.setText(str02);
                bleDataDao.insertBleData("chl", Integer.valueOf(str02), Integer.valueOf(str01), Utils.getCurrentTime());
                Log.d(TAG, "insert success!");
                bleDataDao.query();

                Log.d(TAG,"query success!");
              /*  int i;
                for (i = 0; i <= 31; i++)
                    Log.e(TAG, Integer.toHexString(scan[i] & 0x000000ff) + "");  */  // Sets up UI references.*/



            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //这块进行分类解析
                /**数据头//UINT几 是INT型占几位的新结构类型
                 0x336699DB(4Bytes)+年（2Bytes）+月（1Byte）+日（1Byte）
                 +步数（2Bytes）+心率数据（10Bytes）           */

                if (intent.getStringExtra("DataType").equals("SevenDayData")) {


                } else if (intent.getStringExtra("DataType").equals("CollectPeriod")) {

                } else if (intent.getStringExtra("DataType").equals("Power")) {

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

        /*View v=LayoutInflater.from(getActivity()).inflate(R.layout.activity_myhealth,null);

        View heartV=v.findViewById(R.id.include_heartrate_detail);
        View stepV=v.findViewById(R.id.include_stepnum_detail);*/
        switch (view.getId()) {
            case R.id.ly_myinfo_heartrate:
                Toast.makeText(getActivity(), "This is 心率", Toast.LENGTH_LONG).show();
                MyhealthFragment.heartrateDetail();
                /*heartV.setVisibility(View.VISIBLE);
                stepV.setVisibility(View.GONE);*/
                break;
            case R.id.ly_myinfo_stepnum:
                Toast.makeText(getActivity(), "This is 计步", Toast.LENGTH_LONG).show();
                MyhealthFragment.steunumDetail();
              /*  heartV.setVisibility(View.GONE);
                stepV.setVisibility(View.VISIBLE);*/
               /* mCircleBar = (CircleProgressBar)
                        v.findViewById(R.id.ly_stepnum_detail);
                mCircleBar.setMax(7000);
                mCircleBar.setProgress(Integer.valueOf(str02), 700);
                TextView textview01;
                textview01=(TextView)v.findViewById(R.id.tv_stepnum_detaile_num) ;
                textview01.setText(str02);*/
                /*layoutInflater = LayoutInflater.from(getActivity());
                popup = layoutInflater.inflate(R.layout.activity_stepnum_progress, null);
                popupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, false);
                popupWindow.setBackgroundDrawable(new ColorDrawable(0));
                popupWindow.showAtLocation(popup, Gravity.CENTER | Gravity.CENTER,
                        0, 0);
                mCircleBar = (CircleProgressBar)
                        popup.findViewById(R.id.ly_stepnum_detail);

                mCircleBar.setMax(7000);
                mCircleBar.setProgress(Integer.valueOf(str02), 700);
                TextView textview01;
                textview01=(TextView)popup.findViewById(R.id.tv_stepnum_detaile_num) ;
                textview01.setText(str02);
                imageView=(ImageView)popup.findViewById(R.id.image_close) ;
                imageView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.update();*/
                break;
            default:
                break;
        }
    }
}

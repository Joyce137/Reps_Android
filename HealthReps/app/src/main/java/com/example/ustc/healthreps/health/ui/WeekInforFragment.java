package com.example.ustc.healthreps.health.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.ble.BluetoothLeService;
import com.example.ustc.healthreps.health.ble.SampleGattAttributes;
import com.example.ustc.healthreps.health.util.ComFormu;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2015/10/26.
 */
public class WeekInforFragment extends Fragment implements View.OnClickListener {

    private TextView textView_01, textView_02, textView_03, textView_04;
    private final static String TAG = WeekInforFragment.class
            .getSimpleName();
    LinearLayout ly_heartrate, ly_stepnum, ly_kcal, ly_sleepmode;
    byte[] sevenData;

    public static WeekInforFragment newInstance() {
        WeekInforFragment f = new WeekInforFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);
        getActivity().registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        textView_01 = (TextView) view.findViewById(R.id.myinfo_textView_01);
        textView_02 = (TextView) view.findViewById(R.id.myinfo_textView_02);
        textView_03 = (TextView) view.findViewById(R.id.myinfo_textView_03);
        textView_04 = (TextView) view.findViewById(R.id.myinfo_textView_04);
        ly_heartrate = (LinearLayout) view.findViewById(R.id.ly_myinfo_heartrate);
        ly_stepnum = (LinearLayout) view.findViewById(R.id.ly_myinfo_stepnum);
        ly_kcal = (LinearLayout) view.findViewById(R.id.ly_myinfo_kcal);
        ly_sleepmode = (LinearLayout) view.findViewById(R.id.ly_myinfo_sleepmode);
        textView_01.setText("60");
        textView_02.setText("3.5万");
        textView_03.setText("1.3万");
        textView_04.setText("5.0万");
        ly_heartrate.setOnClickListener(this);
        ly_stepnum.setOnClickListener(this);
        ly_kcal.setOnClickListener(this);
        ly_sleepmode.setOnClickListener(this);
        return view;
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
            if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                if (intent.getStringExtra("DataType").equals("SevenDayData")) {
                    Log.d(TAG, "hhhhhhhhhh--------");
                    sevenData = intent.getByteArrayExtra("data");
                    String a1 = SampleGattAttributes.dataDivider(sevenData, 1, 4) + "年";
                    String a2 = SampleGattAttributes.dataDivider(sevenData, 1, 6) + "月";
                    String a3 = SampleGattAttributes.dataDivider(sevenData, 1, 7) + "日";
                 /*   ((TextView) findViewById(R.id.name1)).setText(SampleGattAttributes.dataDivider(sevenData,1,4) + "年");
                    ((TextView) findViewById(R.id.data1)).setText(SampleGattAttributes.dataDivider(sevenData,1,6) + "月");
                    ((TextView) findViewById(R.id.name2)).setText(SampleGattAttributes.dataDivider(sevenData,1,7) + "日");
                    ((TextView) findViewById(R.id.data2)).setText(SampleGattAttributes.dataDivider(sevenData,1,8) + "步");
                    ((TextView) findViewById(R.id.name3)).setText(SampleGattAttributes.dataDivider(sevenData,1,10) + "次/分");*/
                    Log.d(TAG, "hhhhhhhhhh--------" + a1 + a2 + a3 + "" + SampleGattAttributes.dataDivider(sevenData, 1, 10));
                    Log.d(TAG, "hhhhhhhhhh--------" + SampleGattAttributes.dataDivider(sevenData, 1, 8));
                    textView_01.setText(SampleGattAttributes.dataDivider(sevenData, 1, 10) + "");
                    textView_02.setText(SampleGattAttributes.dataDivider(sevenData, 1, 8) + "");
                    DecimalFormat df = new DecimalFormat("###.00");
                    String str03 = df.format(ComFormu.getKcal(SampleGattAttributes.dataDivider(sevenData, 1, 8)));
                    textView_03.setText(str03);
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ly_myinfo_heartrate:
                Intent sendint = new Intent(MyhealthActivity.ACTION_VIEW_LINE_HEART);
                (getActivity()).sendBroadcast(sendint);
                break;
            case R.id.ly_myinfo_stepnum:
                sendint = new Intent(MyhealthActivity.ACTION_VIEW_COLUMN_HEART);
                (getActivity()).sendBroadcast(sendint);
                break;
            case R.id.ly_myinfo_kcal:
                sendint = new Intent(MyhealthActivity.ACTION_VIEW_BUBBLE_HEART);
                (getActivity()).sendBroadcast(sendint);
                break;
            case R.id.ly_myinfo_sleepmode:
                sendint = new Intent(MyhealthActivity.ACTION_VIEW_BUBBLE_HEART);
                (getActivity()).sendBroadcast(sendint);
                break;
        }
    }
}
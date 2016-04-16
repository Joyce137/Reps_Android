package com.example.ustc.healthreps.health.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ustc.healthreps.R;

/**
 * Created by HBL on 2016/3/6.
 */
public class MonthInforFragment extends Fragment implements View.OnClickListener{

    private final static String TAG = MonthInforFragment.class
            .getSimpleName();
    private TextView textView_01, textView_02, textView_03, textView_04;
    LinearLayout ly_heartrate, ly_stepnum, ly_kcal, ly_sleepmode;

    public static MonthInforFragment newInstance() {
        MonthInforFragment f = new MonthInforFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);
        textView_01 = (TextView) view.findViewById(R.id.myinfo_textView_01);
        textView_02 = (TextView) view.findViewById(R.id.myinfo_textView_02);
        textView_03 = (TextView) view.findViewById(R.id.myinfo_textView_03);
        textView_04 = (TextView) view.findViewById(R.id.myinfo_textView_04);

        textView_01.setText("80");
        textView_02.setText("4万");
        textView_03.setText("7万");
        textView_04.setText("9万");

        ly_heartrate = (LinearLayout) view.findViewById(R.id.ly_myinfo_heartrate);
        ly_stepnum = (LinearLayout) view.findViewById(R.id.ly_myinfo_stepnum);
        ly_kcal = (LinearLayout) view.findViewById(R.id.ly_myinfo_kcal);
        ly_sleepmode = (LinearLayout) view.findViewById(R.id.ly_myinfo_sleepmode);

        ly_heartrate.setOnClickListener(this);
        ly_stepnum.setOnClickListener(this);
        ly_kcal.setOnClickListener(this);
        ly_sleepmode.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Intent sendint = new Intent(MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART);;
        switch(view.getId())
        {
            case R.id.ly_myinfo_heartrate:
                sendint = new Intent(MyhealthActivity.ACTION_VIEW_SLIDELINE_HEART);

                break;
            case R.id.ly_myinfo_stepnum:
                sendint = new Intent(MyhealthActivity.ACTION_VIEW_SLIDECOLUMN_HEART);

                break;

        }
        (getActivity()).sendBroadcast(sendint);
    }
}

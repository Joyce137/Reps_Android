package com.example.administrator.ustc_health;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/10/26.
 */
public class MyWeekInforFragment extends Fragment {

    private TextView textView_01,textView_02,textView_03,textView_04;
    public static MyWeekInforFragment newInstance() {
        MyWeekInforFragment f = new MyWeekInforFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);

        textView_01=(TextView)view.findViewById(R.id.myinfo_textView_01);
        textView_02=(TextView)view.findViewById(R.id.myinfo_textView_02);
        textView_03=(TextView)view.findViewById(R.id.myinfo_textView_03);
        textView_04=(TextView)view.findViewById(R.id.myinfo_textView_04);

        textView_01.setText("60");
        textView_02.setText("3.5万");
        textView_03.setText("1.3万");
        textView_04.setText("5.0万");
        return view;
    }
}
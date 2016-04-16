package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.ui.DayInforFragment;
import com.example.ustc.healthreps.health.view.CircleProgressBar;

/**
 * Created by HBL on 2016/3/8.
 */
public class DayStepFragActivity extends Fragment {
   View view;
    CircleProgressBar mCircleBar;
    TextView tepnumText;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_stepnum_detail, container, false);
        mCircleBar = (CircleProgressBar)
                view.findViewById(R.id.ly_stepnum_detail);
        tepnumText = (TextView) view.findViewById(R.id.tv_stepnum_detaile_num);
        mCircleBar.setMax(6000);
        mCircleBar.setProgress(Integer.valueOf(DayInforFragment.str02), 700);
        tepnumText.setText(DayInforFragment.str02);
        return view;
    }
}

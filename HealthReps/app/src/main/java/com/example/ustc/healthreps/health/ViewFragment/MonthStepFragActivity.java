package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.SlideColumnView;

/**
 * Created by HBL on 2016/3/8.
 */
public class MonthStepFragActivity extends Fragment {
    View view;
    SlideColumnView slideColumnView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_month_step_detail,container,false);
        slideColumnView=(SlideColumnView) view.findViewById(R.id.month_step_view);
        return view;
    }
}

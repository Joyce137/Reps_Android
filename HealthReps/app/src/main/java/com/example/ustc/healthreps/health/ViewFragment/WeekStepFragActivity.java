package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.ColumnView;

/**
 * Created by HBL on 2016/3/8.
 */
public class WeekStepFragActivity extends Fragment {
    View view;
    ColumnView columnView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_week_step_detail, container, false);
        columnView = (ColumnView) view.findViewById(R.id.week_step_view);
        columnView.startAnim();
        return view;
    }
}

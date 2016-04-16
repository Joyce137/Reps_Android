package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.LineView;


/**
 * Created by HBL on 2016/3/8.
 */
public class WeekHeartFragActivity extends Fragment{
    View view;
    LineView lineView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_week_heartrate_detail,container,false);
        lineView=(LineView)view.findViewById(R.id.week_heartrate_view);
        lineView.startAnim();
        return view;
    }
}

package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.SlideLineView;

/**
 * Created by HBL on 2016/3/8.
 */
public class MonthHeartFragActivity extends Fragment {
    View view;
    SlideLineView slideLineView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_month_heartrate_detail,container,false);
        slideLineView=(SlideLineView) view.findViewById(R.id.month_heartrate_view);
        return view;
    }
}

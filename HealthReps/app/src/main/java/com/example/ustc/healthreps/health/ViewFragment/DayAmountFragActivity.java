package com.example.ustc.healthreps.health.ViewFragment;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.AmountView;

/**
 * Created by HBL on 2016/3/8.
 */
public class DayAmountFragActivity extends Fragment {
    View view;
    AmountView mAmountView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_amount_detail, container, false);
        mAmountView = (AmountView) view.findViewById(R.id.ly_amount_detail);
        mAmountView.startAnim();
        return view;
    }
}

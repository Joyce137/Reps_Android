package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.BubbleView;


/**
 * Created by HBL on 2016/3/8.
 */
public class WeekKcalFragActivity extends Fragment {
    View view;
    BubbleView bubbleView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_week_kcal_detail, container, false);
        bubbleView=(BubbleView)view.findViewById(R.id.week_kcal_view);
        bubbleView.startAnim();
        return view;
    }
}

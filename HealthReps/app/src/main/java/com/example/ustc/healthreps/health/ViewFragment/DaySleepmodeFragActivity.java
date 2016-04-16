package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.view.SleepModeDataColumnView;
import com.example.ustc.healthreps.health.view.SleepModeDataView;

/**
 * Created by CaoRuijuan on 4/8/16.
 */
public class DaySleepmodeFragActivity extends Fragment{
    View view;
    SleepModeDataColumnView sleepModeDataView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_day_sleepmodedata, container, false);
        sleepModeDataView=(SleepModeDataColumnView)view.findViewById(R.id.day_sleepmodedata_view);
        sleepModeDataView.startAnim();
        return view;
    }
}

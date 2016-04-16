package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;

/**
 * Created by CaoRuijuan on 4/12/16.
 */
public class HealthRecordFragment extends Fragment{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_healthrecord, null);
        return view;
    }
}

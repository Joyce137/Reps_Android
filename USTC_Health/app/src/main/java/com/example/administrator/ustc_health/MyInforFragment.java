package com.example.administrator.ustc_health;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/10/22.
 */
public class MyInforFragment extends Fragment {

    public static MyInforFragment newInstance() {
        MyInforFragment f = new MyInforFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_myinfor, container, false);
        return view;
    }
}

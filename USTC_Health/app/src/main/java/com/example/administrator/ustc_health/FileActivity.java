package com.example.administrator.ustc_health;


import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2015/11/4.
 */
public class FileActivity extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_file, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("文件");
        return view;
    }
}

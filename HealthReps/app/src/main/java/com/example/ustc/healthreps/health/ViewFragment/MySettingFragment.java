package com.example.ustc.healthreps.health.ViewFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.MyProFileActivity;
import com.example.ustc.healthreps.ProFileActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.ble.DeviceListActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.threads.AllThreads;

/**
 * Created by CaoRuijuan on 4/15/16.
 */
public class MySettingFragment extends Fragment implements View.OnClickListener {
    LinearLayout ly_profile_myweight, ly_profile_sport,ly_profile_mydevice,ly_logout,ly_exit;
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, null);
        context = view.getContext();

        ly_profile_myweight = (LinearLayout) view.findViewById(R.id.ly_profile_myweight);
        ly_profile_myweight.setOnClickListener(this);
        ly_profile_sport = (LinearLayout) view.findViewById(R.id.ly_profile_sport);
        ly_profile_sport.setOnClickListener(this);
        ly_profile_mydevice=(LinearLayout) view.findViewById(R.id.ly_profile_mydevice);
        ly_profile_mydevice.setOnClickListener(this);
        ly_logout=(LinearLayout) view.findViewById(R.id.layout_logout);
        ly_logout.setOnClickListener(this);
        ly_exit=(LinearLayout) view.findViewById(R.id.layout_exit);
        ly_exit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.ly_profile_mydevice:
//                intent = new Intent(ProFileActivity.this, ScanBleActivity.class);
//                startActivity(intent);
                Intent intent=new Intent(context, DeviceListActivity.class);
                startActivity(intent);
                break;
            case R.id.ly_profile_myweight:
                AlertDialog.Builder customDia1=new AlertDialog.Builder(context);
                customDia1.setIcon(R.mipmap.ic_mode_edit_black);
                final View viewDia1= LayoutInflater.from(context).inflate(R.layout.activity_custom_dialog_sport, null);
                customDia1.setTitle("请输入体重");
                customDia1.setView(viewDia1);
                customDia1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                customDia1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                customDia1.create().show();
                break;
            case R.id.ly_profile_sport:
                AlertDialog.Builder customDia=new AlertDialog.Builder(context);
                customDia.setIcon(R.mipmap.ic_mode_edit_black);
                final View viewDia= LayoutInflater.from(context).inflate(R.layout.activity_custom_dialog_weight, null);
                customDia.setTitle("请输入运动目标");
                customDia.setView(viewDia);
                customDia.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                    }
                });
                customDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });
                customDia.create().show();
                break;

            case R.id.layout_logout:
                //注销登录
                Users.sISSignout = true;
                getActivity().finish();
                intent = new Intent();
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_exit:
                //退出
                AllThreads.sReceiveThread = null;
                AllThreads.sHeartBeatTask = null;
                AllThreads.sHeatBeatTimer = null;
                AllThreads.sSendFileThread = null;
//                AppManager.getInstance().exit();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
                break;

        }
    }
}

package com.example.ustc.healthreps;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;

import com.example.ustc.healthreps.utils.AppManager;

/**
 * 基本Activity
 * Created by CaoRuijuan on 1/27/16.
 */
public class BaseActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从集合中移除
        AppManager.getInstance().finishActivity(this);
    }

}
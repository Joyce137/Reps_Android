package com.example.ustc.healthreps.health.baidumap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.example.ustc.healthreps.R;

/**
 * Created by HBL on 2016/2/1.
 */
public class MapActivity extends Activity {
    MapView mMapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
           if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                       Window window = getWindow();
                        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                               | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                       window.setStatusBarColor(Color.TRANSPARENT);
                        window.setNavigationBarColor(Color.TRANSPARENT);
                    }
        setContentView(R.layout.activity_map);
        //获取地图控件引用
       mMapView = (MapView) findViewById(R.id.bmapView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
       mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
       mMapView.onPause();
    }
}


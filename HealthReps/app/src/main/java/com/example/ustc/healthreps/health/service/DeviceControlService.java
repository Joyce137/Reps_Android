package com.example.ustc.healthreps.health.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by HBL on 2015/12/24.
 * http://www.cnblogs.com/linlf03/p/3296323.html
 */
public class DeviceControlService extends Service {
    private final static String TAG = DeviceControlService.class
            .getSimpleName();

    public BluetoothLeService mBluetoothLeService;


   // public static String mDeviceAddress;

    boolean flag2 = false;//mBluetoothLeService为null时会出错
    boolean flag3 = true;
    final Handler handler = new Handler();
    Runnable runnable;


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {


        public DeviceControlService getService() {
            Log.i("TAG", "getService " + DeviceControlService.this);
            return DeviceControlService.this;
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {


        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {//引用服务类的初始化，错误就gg
                Log.e(TAG, "Unable to initialize Bluetooth");
            }
            flag2 = true;
            scancontrolor(true);
            Log.e(TAG, "scancontrolor(true)");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
            flag2 = false;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        // TODO Auto-generated method stub
        initService();
//目前处理不了自动锁屏，主动的没事
        runnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //要做的事情
                if (flag2 && flag3) {
                    System.out.println("wo~~~~~~~~");
                    mBluetoothLeService.scanLeDevice(true);//更新扫描);
                    handler.postDelayed(this, 10000);
                }
            }
        };
    }


    //绑定服务
    private void initService() {
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        boolean isbind = bindService(gattServiceIntent, mServiceConnection,
                BIND_AUTO_CREATE);//绑定
        if (isbind) {
            Log.d(TAG, "bindService success!!");
        } else {
            Log.d(TAG, "bindService failed!!");
        }
    }

    //Handler mHandler=new Handler();
    private void scancontrolor(final boolean enable) {//在这应该搞个延时循环，没搞完
        if (enable == true) {
            if (flag2 && flag3)
                handler.postDelayed(runnable, 10000);//每两秒执行一次runnable
        } else mBluetoothLeService.scanLeDevice(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);//解除绑定，否则会报异常
        mBluetoothLeService = null;
    }


}

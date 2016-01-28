package com.example.ustc.healthreps;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.content.Context;
import android.widget.Toast;

import com.example.ustc.healthreps.friends.MyFriendsActivity;
import com.example.ustc.healthreps.health.service.DeviceControlService;
import com.example.ustc.healthreps.health.ui.MyhealthFragment;
import com.example.ustc.healthreps.health.ui.ScanBleActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.ui.FileDealActivity;
import com.example.ustc.healthreps.ui.MedicineList;
import com.example.ustc.healthreps.ui.SearchDoctor;
import com.example.ustc.healthreps.ui.SearchMedicine;
import com.example.ustc.healthreps.utils.AppManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout menu_my;
    private LinearLayout menu_doctor;
    private LinearLayout menu_medicine;
    private LinearLayout menu_file;
    private LinearLayout menu_friends;

    private ImageView iv_my;
    private ImageView iv_doctor;
    private ImageView iv_medicine;
    private ImageView iv_file;
    private ImageView iv_friends;

    private TextView tv_my;
    private TextView tv_doctor;
    private TextView tv_medicine;
    private TextView tv_file;
    private TextView tv_friends;

    private Fragment myFragment;
    private Fragment doctorFragment;
    private Fragment medicineFragment;
    private Fragment fileFragment;
    private Fragment friendsFragment;

    private TextView myNameTV;

    public static Context context;

    //蓝牙相关
    private final static String TAG = MainActivity.class
            .getSimpleName();
    byte[] scan;
    public static String mDeviceName;
    public static String mDeviceAddress;
    byte[] sevenData, adjustDate;
    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    //BLe控制服务
    private ServiceConnection serCon;
    DeviceControlService devConService;
    public static String str01 = "0", str02 = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //声明使用自定义标题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitle1);//自定义布局赋值

        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);

        context = this.getApplicationContext();

        final Intent intent = getIntent();
        if (intent != null) {
            mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
            mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
            scan = intent.getByteArrayExtra("scan");
            if (scan != null) {
                str01 = String.valueOf(scan[19] & 0xff);
                str02 = String.valueOf((scan[26] & 0xff) * 256 + (scan[25] & 0xff));
            }
            Log.d(TAG, "EXTRAS_DEVICE_NAME:" + mDeviceName + " EXTRAS_DEVICE_ADDRESS:" + mDeviceAddress);
        }

        //绑定服务
        initService();
        //注册广播接收器
        // registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());

        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        if (savedInstanceState == null) {
            initFragment(0);
        }

        //检查程序是否为第一次运行
        if (checkIsFirstRun()) {
            Intent intDet = new Intent();
            intDet.setClass(MainActivity.this, ScanBleActivity.class);
            startActivity(intDet);
        } else {
            SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
            String device_name = sharedPreferences.getString("DEVICE_NAME", "");
            Log.d(TAG, device_name);
            if (device_name.equals("")) {

            }
            else
            {
                mDeviceAddress=device_name;
               /* devConService.mBluetoothLeService.scanLeDevice(true);
                boolean isSAME=devConService.mBluetoothLeService.isSAME;*/
              /*  str01=sharedPreferences.getString("NEW_HEARTRATE", "60");
                str02=sharedPreferences.getString("NEW_STEPNUM","1000");*/
                if (true)
                {
                    Log.d(TAG,"scan success!!");
                    //   devConService.mBluetoothLeService.scanLeDevice(true);
                }
                else
                {
                    Log.d(TAG,"scan failed!!");
                    Toast.makeText(MainActivity.this,"Sorry",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //检查程序是否为第一次运行
    private boolean checkIsFirstRun() {
        // 通过检查程序中的缓存文件判断程序是否是第一次运行

        SharedPreferences sharedPreferences = this.getSharedPreferences("share", MODE_PRIVATE);
        boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d(TAG, "第一次运行");
            Toast.makeText(MainActivity.this, "第一次运行！", Toast.LENGTH_LONG).show();
            editor.putBoolean("isFirstRun", false);
            editor.commit();


        } else {
            Log.d(TAG, "不是第一次运行！");
            Toast.makeText(MainActivity.this, "不是第一次运行！", Toast.LENGTH_LONG).show();


        }

        return isFirstRun;
    }

    private void initService() {
        serCon = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder service) {
                devConService = ((DeviceControlService.LocalBinder) service).getService();

            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        Intent devConServiceIntent = new Intent(this, DeviceControlService.class);
        boolean isbind = bindService(devConServiceIntent, serCon,
                BIND_AUTO_CREATE);//绑定
        if (isbind) {
            Log.d(TAG, "bindService success!!");
        } else {
            Log.d(TAG, "bindService failed!!");
        }
    }
    private void initView() {
        // 底部菜单5个Linearlayout
        menu_my = (LinearLayout) findViewById(R.id.ly_my);
        menu_doctor = (LinearLayout) findViewById(R.id.ly_doctor);
        menu_medicine = (LinearLayout) findViewById(R.id.ly_medicine);
        menu_file = (LinearLayout) findViewById(R.id.ly_file);
        menu_friends = (LinearLayout) findViewById(R.id.ly_friends);


        // 底部菜单5个ImageView
        iv_my = (ImageView) findViewById(R.id.iv_my);
        iv_doctor = (ImageView) findViewById(R.id.iv_doctor);
        iv_medicine = (ImageView) findViewById(R.id.iv_medicine);
        iv_file = (ImageView) findViewById(R.id.iv_file);
        iv_friends = (ImageView) findViewById(R.id.iv_friends);

        // 底部菜单5个菜单标题
        tv_my = (TextView) findViewById(R.id.tv_my);
        tv_doctor = (TextView) findViewById(R.id.tv_doctor);
        tv_medicine = (TextView) findViewById(R.id.tv_medicine);
        tv_file = (TextView) findViewById(R.id.tv_file);
        tv_friends = (TextView) findViewById(R.id.tv_friends);
    }

    private void initEvent() {
        // 设置按钮监听
        menu_my.setOnClickListener(this);
        menu_doctor.setOnClickListener(this);
        menu_medicine.setOnClickListener(this);
        menu_friends.setOnClickListener(this);
        menu_file.setOnClickListener(this);
    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        if (fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0) {
            for (Fragment cf : fragmentManager.getFragments()) {
                if(cf instanceof FileDealActivity
                        || cf instanceof MyhealthFragment
                        || cf instanceof SearchDoctor
                        || cf instanceof SearchMedicine
                        || cf instanceof MedicineList)
                    transaction.hide(cf);
            }
        }

        switch (index) {
            case 0:
//                if (myFragment == null) {
//                    myFragment = new MyhealthFragment();
//                    transaction.add(R.id.frame_content, myFragment);
//                } else {
//                    transaction.show(myFragment);
//                }
                myFragment = new MyhealthFragment();
                transaction.add(R.id.frame_content, myFragment);
                break;

            //寻医
            case 1:
//                if(doctorFragment == null){
//                    doctorFragment = new SearchDoctor();
//                    transaction.add(R.id.frame_content,doctorFragment);
//                }
//                else {
//                    transaction.show(doctorFragment);
//                }
                doctorFragment = new SearchDoctor();
                transaction.add(R.id.frame_content,doctorFragment);
                break;

            //问药
            case 2:
                if(Users.sDefaultStore != null){      //设置过默认药店
                    Toast.makeText(getApplication(), "你已经设置过默认药店", Toast.LENGTH_SHORT).show();
                    medicineFragment = new MedicineList();
                    transaction.add(R.id.frame_content, medicineFragment);
                }
                else {
                    Toast.makeText(getApplication(),"你尚未设置过默认药店",Toast.LENGTH_SHORT).show();
                    medicineFragment = new SearchMedicine();
                    transaction.add(R.id.frame_content, medicineFragment);
                }

                break;

            case 3:
                fileFragment = new FileDealActivity();
                transaction.add(R.id.frame_content, fileFragment);
                break;
            case 4:
                friendsFragment = new MyFriendsActivity();
                transaction.add(R.id.frame_content, friendsFragment);
                break;
            default:
                break;
        }

        // 提交事务
        transaction.commit();

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
        if(doctorFragment != null){
            transaction.hide(doctorFragment);
        }
        if(medicineFragment != null){
            transaction.hide(medicineFragment);
        }
        if (friendsFragment != null) {
            transaction.hide(friendsFragment);
        }
        if (fileFragment!=null)
        {
            transaction.hide(fileFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            //个人设置
            case R.id.myInfo:
                Log.d("MyhealthFragment", "action_share");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;

            //我的设备
            case R.id.scan:
                intent = new Intent();
                intent.setClass(MainActivity.this, ScanBleActivity.class);
                startActivity(intent);
                break;

            //专属药店
            case R.id.myStore:

                break;

            //私人医生
            case R.id.myDoctor:

                break;

            //加好友
            case R.id.changeInput:
                intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            //注销登录
            case R.id.signOutSetting:
                Users.sISSignout = true;
                intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            //退出
            case R.id.exit:
                AllThreads.sReceiveThread = null;
                AllThreads.sHeartBeatTask = null;
                AllThreads.sHeatBeatTimer = null;
                AllThreads.sSendFileThread = null;
                AppManager.getInstance().exit();
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartBotton() {
        // ImageView置为灰色
        iv_my.setImageResource(R.mipmap.me1);
        iv_doctor.setImageResource(R.mipmap.chat1);
        iv_medicine.setImageResource(R.mipmap.chat1);
        iv_file.setImageResource(R.mipmap.find1);
        iv_friends.setImageResource(R.mipmap.contact1);

        // TextView置为灰色
        tv_my.setTextColor(0xffA6A6A6);
        tv_doctor.setTextColor(0xffA6A6A6);
        tv_medicine.setTextColor(0xffA6A6A6);
        tv_file.setTextColor(0xffA6A6A6);
        tv_friends.setTextColor(0xffA6A6A6);
    }

    @Override
    public void onClick(View v) {
        restartBotton();
        switch (v.getId()) {
            case R.id.ly_my:
                iv_my.setImageResource(R.mipmap.me);
                tv_my.setTextColor(0xff008000);
                initFragment(0);
                break;
            case R.id.ly_doctor:
                iv_doctor.setImageResource(R.mipmap.chat);
                tv_doctor.setTextColor(0xff008000);
                initFragment(1);
                break;
            case R.id.ly_medicine:
                iv_medicine.setImageResource(R.mipmap.chat);
                tv_medicine.setTextColor(0xff008000);
                initFragment(2);
                break;
            case R.id.ly_file:
                iv_file.setImageResource(R.mipmap.find);
                tv_file.setTextColor(0xff008000);
                initFragment(3);
                break;
            case R.id.ly_friends:
                iv_friends.setImageResource(R.mipmap.contact);
                tv_friends.setTextColor(0xff008000);
                initFragment(4);
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serCon);//解除绑定，否则会报异常
    }
}




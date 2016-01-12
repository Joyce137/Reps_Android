package com.example.ustc.healthreps;

import android.content.Intent;
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
import com.example.ustc.healthreps.health.DeviceListActivity;
import com.example.ustc.healthreps.health.MyhealthActivity;
import com.example.ustc.healthreps.health.ScanBleActivity;
import com.example.ustc.healthreps.model.MedicStore;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.ui.FileDealActivity;
import com.example.ustc.healthreps.ui.MedicineList;
import com.example.ustc.healthreps.ui.SearchDoctor;
import com.example.ustc.healthreps.ui.SearchMedicine;

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

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this.getApplicationContext();

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        if (savedInstanceState == null) {
            initFragment(0);
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
                        || cf instanceof MyhealthActivity
                        || cf instanceof SearchDoctor
                        || cf instanceof SearchMedicine
                        || cf instanceof MedicineList)
                    transaction.hide(cf);
            }
        }

        switch (index) {
            case 0:
//                if (myFragment == null) {
//                    myFragment = new MyhealthActivity();
//                    transaction.add(R.id.frame_content, myFragment);
//                } else {
//                    transaction.show(myFragment);
//                }
                myFragment = new MyhealthActivity();
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
                Log.d("MyhealthActivity", "action_share");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;

            //
            case R.id.changeInput:
                intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.scan:
                 intent = new Intent();
                intent.setClass(MainActivity.this, ScanBleActivity.class);
                startActivity(intent);
                break;

            case R.id.exit:
                intent =new Intent();
                intent.setClass(MainActivity.this,DeviceListActivity.class);
                startActivity(intent);
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
}




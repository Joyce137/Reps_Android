package com.example.administrator.ustc_health;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout menu_my;
    private LinearLayout menu_file;
    private LinearLayout menu_friends;

    private ImageView iv_my;
    private ImageView iv_file;
    private ImageView iv_friends;

    private TextView tv_my;
    private TextView tv_file;
    private TextView tv_friends;

    private Fragment myFragment;
    private Fragment fileFragment;
    private Fragment friendsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        // 初始化控件
        initView();
        // 初始化底部按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
    }

    private void initView() {

        // 底部菜单5个Linearlayout
        menu_my = (LinearLayout) findViewById(R.id.ly_my);
        menu_file = (LinearLayout) findViewById(R.id.ly_file);
        menu_friends = (LinearLayout) findViewById(R.id.ly_friends);


        // 底部菜单5个ImageView
        iv_my = (ImageView) findViewById(R.id.iv_my);
        iv_file = (ImageView) findViewById(R.id.iv_file);
        iv_friends = (ImageView) findViewById(R.id.iv_friends);

        // 底部菜单5个菜单标题
        tv_my = (TextView) findViewById(R.id.tv_my);
        tv_file = (TextView) findViewById(R.id.tv_file);
        tv_friends = (TextView) findViewById(R.id.tv_friends);


    }

    private void initEvent() {
        // 设置按钮监听
        menu_my.setOnClickListener(this);
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
        switch (index) {
            case 0:
                if (myFragment == null) {
                    myFragment = new MyhealthActivity();
                    transaction.add(R.id.frame_content, myFragment);
                } else {
                    transaction.show(myFragment);
                }
                break;

            case 3:
                if (fileFragment == null) {
                    fileFragment = new FileActivity();
                    transaction.add(R.id.frame_content, fileFragment);
                } else {
                    transaction.show(fileFragment);
                }
                break;
            case 4:
                if (friendsFragment == null) {
                    friendsFragment = new MyFriendsActivity();
                    transaction.add(R.id.frame_content, friendsFragment);
                } else {
                    transaction.show(friendsFragment);
                }
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
            case R.id.action_share:
                Log.d("MyhealthActivity", "action_share");
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetActivity.class);
                startActivity(intent);
                break;

            case R.id.changeInput:
                intent = new Intent();
                intent.setClass(MainActivity.this, ScanBleFailedActivity.class);
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
        iv_file.setImageResource(R.mipmap.find1);
        iv_friends.setImageResource(R.mipmap.contact1);

        // TextView置为灰色
        tv_my.setTextColor(0xffA6A6A6);
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




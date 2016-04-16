package com.example.ustc.healthreps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ustc.healthreps.health.ViewFragment.HealthRecordFragment;
import com.example.ustc.healthreps.health.ViewFragment.MyInforFragment;
import com.example.ustc.healthreps.health.ViewFragment.MySettingFragment;
import com.example.ustc.healthreps.health.ble.DeviceListActivity;
import com.example.ustc.healthreps.health.ble.ScanBleActivity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.ui.MessageFragment;
import com.example.ustc.healthreps.ui.RecordFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ProFileActivity extends AppCompatActivity {

    TextView nameText;
    private ViewPager adViewPager;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private MyPageAdapter adapter;
    private List<Fragment> fragments;
    private List<String> titles;
    LinearLayout linearLayout;

    private Fragment mySettingFragment;
    private Fragment myInforFragment;

    private ImageView[] imageViews;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        nameText = (TextView)findViewById(R.id.usernameText);
        nameText.setText(Users.sLoginUsername);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("个人资料");

        linearLayout = (LinearLayout) findViewById(R.id.ly_profile_my);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProFileActivity.this, MyProFileActivity.class);
                startActivity(intent);
            }
        });

        initViewPager();
    }

    private void initViewPager() {
        //创建ViewPager
        adViewPager = (ViewPager) findViewById(R.id.viewpager);

        initPageAdapter();

        initCirclePoint();

        adViewPager.setAdapter(adapter);
        adViewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private void initPageAdapter() {
        fragments = new ArrayList<>();
        mySettingFragment = new MySettingFragment();
        myInforFragment = new MyInforFragment();
//        fileRecordFragment = new RecordFragment();
        fragments.add(mySettingFragment);
        fragments.add(myInforFragment);
//        fragments.add(fileRecordFragment);

        adapter = new MyPageAdapter(getSupportFragmentManager(),fragments);
    }

    private void initCirclePoint(){
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        imageViews = new ImageView[fragments.size()];
        //广告栏的小圆点图标
        for (int i = 0; i < fragments.size(); i++) {
            //创建一个ImageView, 并设置宽高. 将该对象放入到数组中
            imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(30,30));
            imageViews[i] = imageView;

            //初始值, 默认第0个选中
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.point_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.point_unfocused);
            }
            //将小圆点放入到布局中
            group.addView(imageViews[i]);
        }
    }

    /**
     *  ViewPager 页面改变监听器
     */
    private final class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 页面滚动状态发生改变的时候触发
         */
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        /**
         * 页面滚动的时候触发
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        /**
         * 页面选中的时候触发
         */
        @Override
        public void onPageSelected(int arg0) {
            //获取当前显示的页面是哪个页面
            atomicInteger.getAndSet(arg0);
            //重新设置原点布局集合
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[arg0]
                        .setBackgroundResource(R.drawable.point_focused);
                if (arg0 != i) {
                    imageViews[i]
                            .setBackgroundResource(R.drawable.point_unfocused);
                }
            }
        }
    }


    private final class MyPageAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        FragmentManager fm;

        public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            mFragments = fragments;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }


        @Override
        public int getCount() {
            return 2;
        }

    }


}

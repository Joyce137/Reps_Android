package com.example.ustc.healthreps.health.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.ViewFragment.HealthRecordFragment;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.ui.MessageFragment;
import com.example.ustc.healthreps.ui.RecordFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by CaoRuijuan on 4/12/16.
 */
public class MyActivity extends AppCompatActivity {
    TextView nameText;
    private ViewPager adViewPager;
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    private MyPageAdapter adapter;
    private List<Fragment> fragments;
    private List<String> titles;

    private Fragment healthRecordFragment;
    private Fragment messageRecordFragment;
    private Fragment fileRecordFragment;

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
        getSupportActionBar().setTitle("我的记录");

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
        titles = new ArrayList<>();
        titles.add("健康");
        titles.add("消息");
        titles.add("文件");

        fragments = new ArrayList<>();
        healthRecordFragment = new HealthRecordFragment();
        messageRecordFragment = new MessageFragment();
        fileRecordFragment = new RecordFragment();
        fragments.add(healthRecordFragment);
        fragments.add(messageRecordFragment);
        fragments.add(fileRecordFragment);

        adapter = new MyPageAdapter(getSupportFragmentManager(),titles,fragments);
    }

    private void initCirclePoint(){
        ViewGroup group = (ViewGroup) findViewById(R.id.viewGroup);
        imageViews = new ImageView[fragments.size()];
        //广告栏的小圆点图标
        for (int i = 0; i < fragments.size(); i++) {
            //创建一个ImageView, 并设置宽高. 将该对象放入到数组中
            imageView = new ImageView(this);
            imageView.setLayoutParams(new LayoutParams(30,30));
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
        private List<String> mTitles;
        FragmentManager fm;

        public MyPageAdapter(FragmentManager fm, List<String> titles, List<Fragment> fragments) {
            super(fm);
            this.fm = fm;
            mFragments = fragments;
            mTitles = titles;
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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}

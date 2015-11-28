package com.example.administrator.ustc_health;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/25.
 */
public class MyFriendsActivity extends Fragment {

    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myfriends, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("好友");


        tabLayout = (TabLayout) view.findViewById(R.id.friends_tab);
        mViewPager = (ViewPager) view.findViewById(R.id.friends_viewpager);

        List<String> titles = new ArrayList<>();
        titles.add("好友");
        titles.add("最近联系");

        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new FriendsListFragment());
        fragments.add(new FriendsListFragment());

        FragmentAdapter adapter =
                new FragmentAdapter(getActivity().getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);



        return view;
    }
    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;

        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fm=fm;
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {

            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }
    }
}

package com.example.ustc.healthreps.health.ui;

import android.content.Intent;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.database.impl.UserDaoImpl;
import com.example.ustc.healthreps.health.view.CircleProgressBar;
import com.example.ustc.healthreps.health.view.HeartView;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.ui.DetailUserInfoActivity;
import com.example.ustc.healthreps.ui.PersonSetting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class MyhealthFragment extends Fragment {

    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    HeartView heartview;

    static CircleProgressBar mCircleBar;

    static TextView tepnumText;

    private Fragment dayFragment;
    private Fragment weekFragment;
    private Fragment monthFragment;

    static View heartV,stepV;

    private TextView myNameTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myhealth, container, false);

        //title
        myNameTV = (TextView) view.findViewById(R.id.mytitle1Text);
        if(Users.sLoginUsername != null)
            myNameTV.setText(Users.sLoginUsername);
        else
            myNameTV.setText("HealthReps");
        myNameTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Users.sISDetailUserInfo){
                    //更新user数据库
                    new UserDaoImpl(getActivity().getApplicationContext()).updateUserByUserInfo(Users.sLoginUser);

                    Intent intent = new Intent(getActivity().getApplicationContext(), DetailUserInfoActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"正在搜索个人信息，请稍后",Toast.LENGTH_SHORT).show();
                }
            }
        });

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        heartview = (HeartView) view.findViewById(R.id.heart_view);

        heartV=view.findViewById(R.id.include_heartrate_detail);
        stepV=view.findViewById(R.id.include_stepnum_detail);
        mCircleBar = (CircleProgressBar)
                view.findViewById(R.id.ly_stepnum_detail);

        tepnumText=(TextView)view.findViewById(R.id.tv_stepnum_detaile_num) ;

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        heartrateDetail();
        List<String> titles = new ArrayList<>();
        titles.add("日");
        titles.add("周");
        titles.add("月");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));
        List<Fragment> fragments = new ArrayList<>();

        dayFragment = MyInforFragment.newInstance();

        weekFragment = MyWeekInforFragment.newInstance();

        monthFragment = MyWeekInforFragment.newInstance();

        fragments.add(dayFragment);
        fragments.add(weekFragment);
        fragments.add(monthFragment);
        FragmentAdapter adapter =
                new FragmentAdapter(getChildFragmentManager(), titles);
        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);

        return view;
    }


    public static void  steunumDetail()
    {
        heartV.setVisibility(View.GONE);
        stepV.setVisibility(View.VISIBLE);
        mCircleBar.setMax(7000);
        mCircleBar.setProgress(Integer.valueOf(MyInforFragment.str02), 700);
        tepnumText.setText(MyInforFragment.str02);

    }
    public static void  heartrateDetail()
    {
        heartV.setVisibility(View.VISIBLE);
        stepV.setVisibility(View.GONE);
    }
    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragments;
        private List<String> mTitles;
        FragmentManager fm;

        public FragmentAdapter(FragmentManager fm, List<String> titles) {
            super(fm);
            this.fm = fm;
            // mFragments = fragments;
            mTitles = titles;
        }

        /*  public Object instantiateItem(ViewGroup container, int position) {
              return adapter.instantiateItem( container, position);;
          }*/
        @Override
        public Fragment getItem(int position) {
            Fragment fgment = dayFragment;
            FragmentTransaction transaction = fm.beginTransaction();

            switch (position) {
                case 0:
                    transaction.remove(dayFragment);

                    fgment = dayFragment;

                    break;
                case 1:
                    transaction.remove(weekFragment);
                    fgment = weekFragment;
                    break;

                case 2:
                    transaction.remove(monthFragment);
                    fgment = monthFragment;
                    break;
                default:
                    break;

            }
            View v=LayoutInflater.from(getActivity()).inflate(R.layout.activity_myinfor,null);
            TextView ly=(TextView)v.findViewById(R.id.info_ly);
            transaction.addSharedElement(ly,"shareLy");
            transaction.commit();


            return fgment;
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

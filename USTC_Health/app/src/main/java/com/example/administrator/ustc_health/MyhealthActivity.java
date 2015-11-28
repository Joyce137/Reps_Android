package com.example.administrator.ustc_health;

import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/21.
 */
public class MyhealthActivity extends Fragment {

    View view;
    ViewPager mViewPager;
    TabLayout tabLayout;
    HeartView heartview;

    private Fragment dayFragment;
    private Fragment weekFragment;
    private Fragment monthFragment;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myhealth, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        heartview = (HeartView) view.findViewById(R.id.heart_view);

        //view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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

        monthFragment = MyInforFragment.newInstance();



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

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_share:
                Log.d("MyhealthActivity","action_share");
            break;

            case R.id.changeInput:
                Log.d("MyhealthActivity","changeInput");
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

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
           // hideFragment(transaction);
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

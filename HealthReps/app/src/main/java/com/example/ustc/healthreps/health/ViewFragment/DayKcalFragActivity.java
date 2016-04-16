package com.example.ustc.healthreps.health.ViewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.health.ui.DayInforFragment;
import com.example.ustc.healthreps.health.view.KcalView;


/**
 * Created by HBL on 2016/3/8.
 */
public class DayKcalFragActivity extends Fragment {
    View view;
    TextView kcalText;
    static Animation scaleAnimation;
    KcalView mkcaiView;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,  Bundle savedInstanceState) {
       view=inflater.inflate(R.layout.activity_kcal_detail,container,false);
        kcalText = (TextView) view.findViewById(R.id.tv_kcal_detaile_num);
        scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale_anim_kcal);
        mkcaiView = (KcalView) view.findViewById(R.id.ly_kcal_detail);
        mkcaiView.startAnimation(scaleAnimation);
        kcalText.setText(DayInforFragment.str03);
        return view;
    }
}

package com.example.ustc.healthreps.health.util;



/**
 * Created by HBL on 2016/1/25.
 */
public class ComFormu {

    static public double getKcal(int stepnum)
    {
        //已知体重、距离
        //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        double mKcal;
        mKcal=55*stepnum*1.306/1000;
        return mKcal;
    }

    static public String getKcal(String stepnum)
    {
        //已知体重、距离
        //跑步热量（kcal）＝体重（kg）×距离（公里）×1.036
        int stepNum = Integer.valueOf(stepnum);
        double mKcal;
        mKcal=55*stepNum*1.306/1000;
        int kal = (int) mKcal;
        return String.valueOf(kal);
    }

}

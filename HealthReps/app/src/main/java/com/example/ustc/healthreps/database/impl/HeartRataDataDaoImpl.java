package com.example.ustc.healthreps.database.impl;

import android.content.Context;


import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.entity.HeartRateData;
import com.example.ustc.healthreps.database.support.DaoSupportImpl;
import com.example.ustc.healthreps.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 3/20/16.
 */
public class HeartRataDataDaoImpl extends DaoSupportImpl<HeartRateData> {
    private Context context;
    private final static String TAG = HeartRataDataDaoImpl.class.getSimpleName();

    public HeartRataDataDaoImpl(Context context){
        super(context, "ble", false);
        //新建heartRateData表
        try {
            getDb().execSQL(DBConstants.HEARTBEAT_TABLE_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean insertHeartRataData(String heartrate) {
        HeartRateData heartRateData = new HeartRateData();
        heartRateData.heartrate = heartrate;
        heartRateData.datatime = Utils.getDataAndTime();

        //插入数据
        if(checkDateTimeExistInHeartRateDatadate(Utils.getDataAndTime())){
            return false;
        }
         if(checkNowThreeMinute())
            return insert(heartRateData);
        else {
            return false;
        }
    }

    //判断某个datetime是否存在在heartRateData表中
    public boolean checkDateTimeExistInHeartRateDatadate(String datetime){
        String selector = DBConstants.BLE_DATATIME + "=?";
        String[] selectorargs = new String[]{datetime.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }

    //判断当前datetime是否符合三分钟一次
    public boolean checkNowThreeMinute(){
        String now = Utils.getDataAndTime();
        String lastTime = now;

        String sqlStr = "select "+DBConstants.BLE_DATATIME + " from "+DBConstants.TABLE_HEARTRATE+
                " order by "+ DBConstants.TABLE_BLE_KEY+" desc";
        ArrayList<HeartRateData> dateTimeResult = executeSql(sqlStr);
        if(dateTimeResult.size() == 0){
            return true;
        }
        lastTime = dateTimeResult.get(0).datatime;
        return Utils.checkThreeMinute(lastTime,now);
    }

    //查询now之前的心率数据
    public List<HeartRateData> queryHeartRateData(){
        String now = Utils.getDataAndTime();
        String selector = DBConstants.BLE_DATATIME + "<?";
        String[] selectorargs = new String[]{now.trim()};

        return findEntity(selector,selectorargs);
    }
}

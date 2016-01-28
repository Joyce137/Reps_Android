package com.example.ustc.healthreps.database.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.entity.Cookie;
import com.example.ustc.healthreps.database.support.DaoSupportImpl;
import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by CaoRuijuan on 1/27/16.
 */
public class BleDataDaoImpl extends DaoSupportImpl<Cookie> {
    private Context context;
    private final static String TAG = BleDataDaoImpl.class.getSimpleName();

    public BleDataDaoImpl(Context context){
        super(context,"ble",true);
        this.context = context;
    }

    public boolean insertBleData(String name, int stepnum, int heartrate, String time) {
        DBConstants.TABLE_BLE = name + DBConstants.TABLE_BLE;
        //插入数据
        ContentValues cv = new ContentValues();
        cv.put(DBConstants.BLE_DATATIME, time);
        cv.put(DBConstants.BLE_STEPNUM, stepnum);
        cv.put(DBConstants.BLE_HEARTRATE, heartrate);
        Log.d(TAG, "insert data success!");
        getDb().insert(DBConstants.TABLE_BLE, null, cv);

        return true;
    }

    public void query() {
        // 查询获得游标

        Cursor cursor = getDb().rawQuery("select * from "+DBConstants.TABLE_BLE
                +" where "+DBConstants.BLE_DATATIME+">? and "+DBConstants.TABLE_BLE
                +"< ?", new String[]{"2016-01-10 20:50:30", Utils.getCurrentTime()});
        int count = cursor.getCount();
        Log.d(TAG, "-------------------------------  " + count + "  ---------------------------");
    }

    public void queryNewBleData() {
        Cursor cursor = getDb().rawQuery("select * from "+DBConstants.TABLE_BLE
                + " order by "+DBConstants.BLE_DATATIME+" desc limit 0,1",null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        MainActivity.str02 = cursor.getString(2);
        MainActivity.str01 = cursor.getString(3);
        Log.d(TAG, "----------  " + count + "  -----------" + " " + cursor.getString(2) + " " + cursor.getString(1));
    }
}

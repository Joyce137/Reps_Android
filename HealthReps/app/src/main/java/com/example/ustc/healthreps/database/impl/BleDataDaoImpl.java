package com.example.ustc.healthreps.database.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.entity.BleData;
import com.example.ustc.healthreps.database.support.DaoSupportImpl;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoRuijuan on 1/27/16.
 */
public class BleDataDaoImpl extends DaoSupportImpl<BleData> {
    private Context context;
    private final static String TAG = BleDataDaoImpl.class.getSimpleName();
    private String userName;

    public BleDataDaoImpl(Context context,String userName){
        super(context, "ble", false);
        this.userName = userName;
        this.context = context;
        //新建username_bledata表
        try {
            String createSqlStr = DBConstants.createBleStr(userName);
            getDb().execSQL(createSqlStr);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean insertBleData(String stepnum, String heartrate, String calorie, String amutoferce) {
        BleData bleData = new BleData();
        bleData.stepnum = stepnum;
        bleData.heartrate = heartrate;
        bleData.calorie = calorie;
        bleData.amutoferce = amutoferce;

        //插入数据
        if(checkDateTimeExistInBledate(Utils.getDataAndTime())){
            return false;
        }
        return insert(bleData);
    }

    //判断某个datetime是否在bledata表中
    public boolean checkDateTimeExistInBledate(String datetime){
        String selector = DBConstants.BLE_DATATIME + "=?";
        String[] selectorargs = new String[]{datetime.trim()};

        if(findEntity(selector,selectorargs).size() == 0){
            return false;
        }
        return true;
    }

    //查询now之前的蓝牙数据
    public List<BleData> queryBleData(){
        String now = Utils.getDataAndTime();
        String selector = DBConstants.BLE_DATATIME + "<?";
        String[] selectorargs = new String[]{now.trim()};

        return findEntity(selector,selectorargs);
    }

    //存储蓝牙数据到txt文件
    public boolean saveBleDataToFile(List<BleData> bleDatas){
        String filePath = AppPath.getPathByFileType("bleFile");
        AppPath.CheckAndMkdirPathExist(filePath);
        filePath += "/tempBleData.txt";
        File file = new File(filePath);
        //如果不存在，则创建
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建文件失败");
                e.printStackTrace();
                return false;
            }
        }

        //若文件不可读写，则false
        if(!(file.isFile()|file.canWrite())){
            return false;
        }

        FileWriter fw = null;

        //true-表示追加式写文件
        try {
            fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for(BleData bleData : bleDatas){
                bw.write(bleData.datatime + " "+bleData.heartrate + " "
                +bleData.stepnum + " "+ bleData.calorie + " "+bleData.amutoferce+"\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {

            try {
                fw.close();
            } catch (Exception e) {

                e.printStackTrace();

            }

        }
        return true;
    }

    public void query() {
        // 查询获得游标
        Cursor cursor = getDb().rawQuery("select * from "+DBConstants.TABLE_BLE
                +" where "+DBConstants.BLE_DATATIME+">? and "+DBConstants.TABLE_BLE
                +"< ?", new String[]{"2016-01-10 20:50:30", Utils.getDataAndTime()});
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

    @Override
    public String getTableName() {
        return userName + DBConstants.TABLE_BLE;
    }
}

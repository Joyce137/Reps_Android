package com.example.ustc.healthreps.database.entity;


import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.annotation.ColumnName;
import com.example.ustc.healthreps.database.annotation.PrimaryKey;
import com.example.ustc.healthreps.database.annotation.TableName;
import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by CaoRuijuan on 3/15/16.
 */
@TableName("bleData")
public class BleData {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_BLE_KEY)
    public int id;

    @ColumnName(DBConstants.BLE_DATATIME)
    public String datatime;

    @ColumnName(DBConstants.BLE_HEARTRATE)
    public String heartrate;

    @ColumnName(DBConstants.BLE_STEPNUM)
    public String stepnum;

    @ColumnName(DBConstants.BLE_CALORIE)
    public String calorie;

    @ColumnName(DBConstants.BLE_AMUTOFERCE)
    public String amutoferce;

    public BleData(){
        datatime = Utils.getDataAndTime();
    }
}

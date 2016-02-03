package com.example.ustc.healthreps.database;

import android.database.sqlite.SQLiteDatabase;

import com.example.ustc.healthreps.utils.AppPath;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
public class DBConstants {
    //药品数据库，已存在，导入
    public static final String DB_NAME_MEDICINE = "medicine.db";

    //data表
    public static final String TABLE_Medicine = "data";
    //表项名
    public static final String MEDICINE_ID = "ID";    //ID
    public static final String MEDICINE_spec = "spec";     //规格
    public static final String MEDICINE_drugID = "drugID";      //货品ID
    public static final String MEDICINE_name = "name";     //通用名称
    public static final String MEDICINE_unit = "unit";     //单位
    public static final String MEDICINE_address = "address";  //产地
    public static final String MEDICINE_category = "category"; //类别
    public static final String MEDICINE_usage = "usage";    //用法用量
    public static final String MEDICINE_pinyin = "pinyin";   //拼音简码
    public static final String MEDICINE_taboo = "taboo";    //禁忌
    public static final String MEDICINE_disease = "disease";  //对应病症1

    //用户数据库
    public static final String DB_NAME_USER = "user.db";
    //cookie表
    public static final String TABLE_COOKIE = "cookie";
    public static final String TABLE_KEY = "_id";
    public static final String COOKIE_USERNAME = "username";
    public static final String COOKIE_PASSWORD = "password";
    public static final String COOKIE_TYPE = "type";
    public static final String COOKIE_DATE = "lastDate";
    //创建cookie表
    public static final String COOKIE_DB_CREATE = "create table "+
            TABLE_COOKIE + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            COOKIE_USERNAME + " text not null, "+
            COOKIE_PASSWORD + " text not null, "+
            COOKIE_TYPE + " text not null, "+
            COOKIE_DATE + " text not null);";
    //删除cookie表
    public static final String COOKIE_DB_DROP = "drop table if exists "+TABLE_COOKIE;

    //user表
    public static final String TABLE_USER = "user";
    public static final String USER_USERNAME = "username";
    public static final String USER_TYPE = "type";
    public static final String USER_SEX = "sex";
    public static final String USER_AGE = "age";
    public static final String USER_STATUS = "status";
    public static final String USER_ZHICHENG = "zhicheng";
    public static final String USER_REALNAME = "realname";
    public static final String USER_KESHI = "keshi";
    public static final String USER_CW = "cw";
    public static final String USER_ADDRESS = "address";
    public static final String USER_SHOPNAME = "shopname";
    public static final String USER_DOCPHAADDRESS = "docpha_address";
    public static final String USER_VIP = "vip";
    public static final String USER_OFF = "off";
    public static final String USER_PHONE = "phone";
    public static final String USER_DEFAULTSTORE = "defaultstore";
    public static final String USER_PASSWORD = "password";
    public static final String USER_DIANZHANG = "dianzhang";
    public static final String USER_CAOZUOYUAN = "caozuoyuan";
    public static final String USER_IDNUM = "ID_num";
    public static final String USER_YIBAONUM = "yibao_num";
    public static final String USER_SELFINTRODUCTION = "selfintroduction";
    public static final String USER_IMAGEPATH = "imagepath";
    public static final String USER_EMAIL = "email";

    //创建user表
    public static final String USER_TABLE_CREATE = "create table "+
            TABLE_USER + " ("+
            TABLE_KEY + " integer primary key autoincrement, "+
            USER_USERNAME + " text not null, "+
            USER_TYPE + " text not null, "+
            USER_SEX + " text, "+
            USER_AGE + " text, "+
            USER_STATUS + " text, "+
            USER_ZHICHENG + " text, "+
            USER_REALNAME + " text, "+
            USER_KESHI + " text, "+
            USER_CW + " text, "+
            USER_ADDRESS + " text, "+
            USER_SHOPNAME + " text, "+
            USER_DOCPHAADDRESS + " text, "+
            USER_VIP + " text, "+
            USER_OFF + " text, "+
            USER_PHONE + " text, "+
            USER_DEFAULTSTORE + " text, "+
            USER_PASSWORD + " text, "+
            USER_DIANZHANG + " text, "+
            USER_CAOZUOYUAN + " text, "+
            USER_IDNUM + " text, "+
            USER_YIBAONUM + " text, "+
            USER_SELFINTRODUCTION + " text, "+
            USER_IMAGEPATH + " text, "+
            USER_EMAIL + " text);";

    //删除user表
    public static final String USER_TABLE_DROP = "drop table if exists "+TABLE_USER;

    //蓝牙数据库
    public static final String DB_NAME_BLE = "ble.db";
    //用户蓝牙数据
    public static String TABLE_BLE = "_bledata";
    public static final String TABLE_BLE_KEY = "id";
    public static final String BLE_DATATIME = "datatime";
    public static final String BLE_HEARTRATE = "heartrate";
    public static final String BLE_STEPNUM = "stepnum";
    public static final String BLE_CALORIE = "calorie";
    public static final String BLE_AMUTOFERCE = "amutoferce";
    //创建_bledata表
    public static String BLE_DB_CREATE = "create table "+
            TABLE_BLE + " ("+
            TABLE_BLE_KEY + " integer primary key autoincrement, "+
            BLE_DATATIME + " text not null, "+
            BLE_HEARTRATE + " text not null, "+
            BLE_STEPNUM + " text not null, "+
            BLE_CALORIE + " text not null);"+
            BLE_AMUTOFERCE + " text not null);";
    //删除ble表
    public static final String BLE_DB_DROP = "drop table if exists "+TABLE_BLE;

    //数据库版本
    public static final int DB_VERSION = 1;


//    //检查表是是否存在在数据库中
//    public static boolean checkTableIfExistsInDatabase(SQLiteDatabase database,String table){
//        String checkStr = "if not exists (select * from sysobjects where id = object_id('"+table
//                +"') and OBJECTPROPERTY(id, 'IsUserTable') = 1)";
//        database.execSQL(checkStr);
//
//    }
}

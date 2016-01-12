package com.example.ustc.healthreps.database;

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

    //数据库版本
    public static final int DB_VERSION = 1;
}

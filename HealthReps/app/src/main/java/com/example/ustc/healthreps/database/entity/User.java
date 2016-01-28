package com.example.ustc.healthreps.database.entity;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.annotation.ColumnName;
import com.example.ustc.healthreps.database.annotation.PrimaryKey;
import com.example.ustc.healthreps.database.annotation.TableName;

/**
 * Created by CaoRuijuan on 1/28/16.
 */
@TableName(DBConstants.TABLE_USER)
public class User {
    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.USER_USERNAME)
    public String username;

    @ColumnName(DBConstants.USER_TYPE)
    public String type;

    @ColumnName(DBConstants.USER_SEX)
    public String sex;

    @ColumnName(DBConstants.USER_AGE)
    public String age;

    @ColumnName(DBConstants.USER_STATUS)
    public String status;

    @ColumnName(DBConstants.USER_ZHICHENG)
    public String zhicheng;

    @ColumnName(DBConstants.USER_REALNAME)
    public String realname;

    @ColumnName(DBConstants.USER_KESHI)
    public String keshi;

    @ColumnName(DBConstants.USER_CW)
    public String cw;

    @ColumnName(DBConstants.USER_ADDRESS)
    public String address;

    @ColumnName(DBConstants.USER_SHOPNAME)
    public String shopname;

    @ColumnName(DBConstants.USER_DOCPHAADDRESS)
    public String docpha_address;

    @ColumnName(DBConstants.USER_VIP)
    public String vip;

    @ColumnName(DBConstants.USER_OFF)
    public String off;

    @ColumnName(DBConstants.USER_PHONE)
    public String phone;

    @ColumnName(DBConstants.USER_DEFAULTSTORE)
    public String defaultstore;

    @ColumnName(DBConstants.USER_PASSWORD)
    public String password;

    @ColumnName(DBConstants.USER_DIANZHANG)
    public String dianzhang;

    @ColumnName(DBConstants.USER_CAOZUOYUAN)
    public String caozuoyuan;

    @ColumnName(DBConstants.USER_IDNUM)
    public String ID_num;

    @ColumnName(DBConstants.USER_YIBAONUM)
    public String yibao_num;

    @ColumnName(DBConstants.USER_SELFINTRODUCTION)
    public String selfintroduction;

    @ColumnName(DBConstants.USER_IMAGEPATH)
    public String imagepath;

    @ColumnName(DBConstants.USER_EMAIL)
    public String email;
}

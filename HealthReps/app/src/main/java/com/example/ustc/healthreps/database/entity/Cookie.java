package com.example.ustc.healthreps.database.entity;

import com.example.ustc.healthreps.database.DBConstants;
import com.example.ustc.healthreps.database.annotation.ColumnName;
import com.example.ustc.healthreps.database.annotation.PrimaryKey;
import com.example.ustc.healthreps.database.annotation.TableName;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.utils.Utils;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
@TableName(DBConstants.TABLE_COOKIE)
public class Cookie {

    @PrimaryKey(autoincrement=true)
    @ColumnName(DBConstants.TABLE_KEY)
    public int id;

    @ColumnName(DBConstants.COOKIE_USERNAME)
    public String username;

    @ColumnName(DBConstants.COOKIE_PASSWORD)
    public String pwd;

    @ColumnName(DBConstants.COOKIE_DATE)
    public String cookieDate;

    @ColumnName(DBConstants.COOKIE_TYPE)
    public String type;


    public Cookie(){
        username = null;
        pwd = null;
        cookieDate = Utils.getDate();
    }

    public Cookie(String username,String pwd, String type){
        this.username = username;
        this.pwd = pwd;
        this.cookieDate = Utils.getDate();
        this.type = type;
    }

    public Cookie(int id, String username,String pwd, String type,  String cookieDate){
        this.id = id;
        this.username = username;
        this.pwd = pwd;
        this.type = type;
        this.cookieDate = cookieDate;
    }

    //返回真实的类型
    public String getRealType(){
        if(type.equals(String.valueOf(Types.USER_TYPE_PATIENT))) {
            return "患者";
        }
        else if (type.equals(String.valueOf(Types.USER_TYPE_DOCTOR))) {
            return "医生";
        }
        else if (type.equals(String.valueOf(Types.USER_TYPE_PHA))) {
            return "药师";
        }
        else if (type.equals(String.valueOf(Types.USER_TYPE_STORE))) {
            return "药监局";
        }
        else {
            return "药店";
        }
    }
}

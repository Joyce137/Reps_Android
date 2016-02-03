package com.example.ustc.healthreps.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by CaoRuijuan on 12/22/15.
 */
public class DBHelperForNew extends SQLiteOpenHelper {
    private String mDBName;
    private String mDBCreateStr;
    private String mDBDropStr;
    //创建数据库
    public DBHelperForNew(Context context,String dbName,String createStr,String dropStr){
        super(context,dbName,null,DBConstants.DB_VERSION);
        mDBName = dbName;
        mDBCreateStr = createStr;
        mDBDropStr = dropStr;
    }

    //仅仅第一次时执行，创建表；；getWritableDatabase或getReadableDatabase时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //新建cookie表
        db.execSQL(mDBCreateStr);
    }

    //版本升级
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(mDBDropStr);
        onCreate(db);
    }
}

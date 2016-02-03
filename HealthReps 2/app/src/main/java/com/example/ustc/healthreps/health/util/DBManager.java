package com.example.ustc.healthreps.health.util;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.ustc.healthreps.MainActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by HBL on 2015/12/21.
 */
public class DBManager {
    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "ustchealth"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.example.administrator.ustc_health";
    public static final String DB_PATH = AppPath.DB_PATH;
    private final static String TAG = DBManager.class
            .getSimpleName();
    private SQLiteDatabase database;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void openDatabase() {
        Log.d(TAG, DB_PATH + "/" + DB_NAME);
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
    }

    public static DBManager getInstance(Context cnt) {
        final DBManager INSTANCE = new DBManager(cnt);
        return INSTANCE;
    }


    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {
                Log.d(TAG, "dbfile is not exists");
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.ustchealth); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }

                fos.close();
                is.close();
            }
            Log.d(TAG, "dbfile is exists");
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return db;

        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.database.close();
    }

    public boolean insertBleData(int stepnum, int heartrate, String time) {
        //插入数据
        ContentValues cv = new ContentValues();
        cv.put("datatime", time);
        cv.put("stepnum", stepnum);
        cv.put("heartrate", heartrate);
        Log.d(TAG, "insert data success!");
        database.insert("chl_bledata", null, cv);


        return true;
    }

    public void query() {
        // 查询获得游标

        Cursor cursor = database.rawQuery("select * from chl_bledata where datatime>? and datatime< ?", new String[]{"2016-01-10 20:50:30", Utils.getCurrentTime()});
        int count = cursor.getCount();
        Log.d(TAG, "-------------------------------  " + count + "  ---------------------------");
    }

    public void queryNewBleData() {
        Cursor cursor = database.rawQuery("select * from chl_bledata order by datatime desc limit 0,1", null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        MainActivity.str02 = cursor.getString(2);
        MainActivity.str01 = cursor.getString(3);
        Log.d(TAG, "----------  " + count + "  -----------" + " " + cursor.getString(2) + " " + cursor.getString(1));
    }
}

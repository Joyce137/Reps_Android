package com.example.ustc.healthreps.database;

/**
 * Created by CaoRuijuan on 12/2/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.utils.AppPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBManagerForExist {

    private final int BUFFER_SIZE = 400000;

    private SQLiteDatabase database;
    private Context context;

    public DBManagerForExist(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void openDatabase() {
        AppPath.CheckAndMkdirPathExist(AppPath.DB_PATH);
        //药品数据库
        this.database = this.openDatabase(AppPath.DB_PATH + "/" + DBConstants.DB_NAME_MEDICINE);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            File f = new File(dbfile);
            if (!f.exists()) {
                //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.medicine); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            long x = f.length();
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,null);
            return db;

        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.database.close();
    }
}

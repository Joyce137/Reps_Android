package com.example.ustc.healthreps.medicine;

/**
 * Created by CaoRuijuan on 12/2/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.ustc.healthreps.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DBManager {

    private final int BUFFER_SIZE = 400000;
    public static final String DB_NAME = "medicine.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.example.ustc.healthreps";

    //在手机里存放数据库的位置(/data/data/com.example.ustc.healthreps/databases/medicine.db)
    public static final String DB_PATH = "/data"
            +Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME + "/databases";

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
        File file=new File(DB_PATH);
        if(!file.exists())
            file.mkdir();
        this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
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

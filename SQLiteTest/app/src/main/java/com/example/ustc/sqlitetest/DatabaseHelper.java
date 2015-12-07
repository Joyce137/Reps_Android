package com.example.ustc.sqlitetest;

/**
 * Created by CaoRuijuan on 12/7/15.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by CaoRuijuan on 12/5/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static String DB_NAME ="medicine";
    private final Context context;
    private final String assetPath;
    private final String dbPath;
    private SQLiteDatabase mDataBase;


    public DatabaseHelper(Context context, String dbName, String assetPath)
            throws IOException {
        super(context, dbName, null, 1);
        this.context = context;
        this.assetPath = assetPath;
        this.dbPath = "/data/data/"
                + context.getApplicationContext().getPackageName() + "/databases/"
                + dbName;
        checkExists();
    }

    public Cursor getTestData()
    {
        try
        {
            String sql = "SELECT * FROM data";

            Cursor mCur = mDataBase.rawQuery(sql, null);
            if (mCur!=null)
            {
                mCur.moveToNext();
            }
            return mCur;
        }
        catch (SQLException mSQLException)
        {
            Log.e(TAG, "getTestData >>" + mSQLException.toString());
            throw mSQLException;
        }
    }

    private void checkExists() throws IOException {
        Log.i(TAG, "checkExists()");

        File dbFile = new File(dbPath);

        if (!dbFile.exists()) {

            Log.i(TAG, "creating database..");

            dbFile.getParentFile().mkdirs();
            copyStream(context.getAssets().open(assetPath), new FileOutputStream(
                    dbFile));

            Log.i(TAG, assetPath + " has been copied to " + dbFile.getAbsolutePath());
        }
    }

    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte buf[] = new byte[1024];
        int c = 0;
        while (true) {
            c = is.read(buf);
            if (c == -1)
                break;
            os.write(buf, 0, c);
        }
        is.close();
        os.close();
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null)
            mDataBase.endTransaction();
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}


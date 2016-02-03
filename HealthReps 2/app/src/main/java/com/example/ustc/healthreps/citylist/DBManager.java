package com.example.ustc.healthreps.citylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.utils.AppPath;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * rawеlдdata
 * @author gugalor
 *
 */
public class DBManager
{
	private final int BUFFER_SIZE = 400000;
	private static final String PACKAGE_NAME = "com.example.ustc.healthreps";
	public static final String DB_NAME = "china_city_name.db";
	public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases" ; // ·
	private Context mContext;
	private SQLiteDatabase database;

	public DBManager(Context context)
	{
		this.mContext = context;
	}

	/**
	 * ÷
	 */
	public void openDateBase()
	{
		AppPath.CheckAndMkdirPathExist(DB_PATH);
		this.database = this.openDateBase(DB_PATH + "/" + DB_NAME);

	}

	/**
	 * 
	 * 
	 * @param dbfile
	 * @return SQLiteDatabase
	 * @author gugalor
	 */
	private SQLiteDatabase openDateBase(String dbfile)
	{
		try {
			File f = new File(dbfile);
			if (!f.exists()) {
				//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				InputStream is = this.mContext.getResources().openRawResource(
						R.raw.china_city_name); //欲导入的数据库
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

	public void closeDatabase()
	{
		if (database != null && database.isOpen())
		{
			this.database.close();
		}
	}
}

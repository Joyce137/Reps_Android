package com.example.ustc.sqlitetest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by CaoRuijuan on 12/7/15.
 */
public class MedicineRepo {
    //药品数据库的处理逻辑类

    private SQLiteDatabase db;
    public MedicineRepo(SQLiteDatabase db){
        this.db = db;
    }

    //查询
    public ArrayList<Medicine> getMedicineByCategory(String category){
        ArrayList<Medicine> list = new ArrayList<Medicine>();

        db.beginTransaction();

        //查询语句
        String selectStr =  "select *" +
                " from " + Medicine.TABLE
                + " where " + Medicine.KEY_category
                + " like \"%" + category + "%\"";

        Cursor cursor = db.rawQuery(selectStr,null);

        while(cursor.moveToNext()){
            //药品对象
            Medicine medicine = new Medicine();
            medicine.ID = cursor.getInt(cursor.getColumnIndex(Medicine.KEY_ID));
            medicine.spec = cursor.getString(cursor.getColumnIndex(Medicine.KEY_spec));
            medicine.drugID = cursor.getInt(cursor.getColumnIndex(Medicine.KEY_drugID));
            medicine.name = cursor.getString(cursor.getColumnIndex(Medicine.KEY_name));
            medicine.unit = cursor.getString(cursor.getColumnIndex(Medicine.KEY_unit));
            medicine.address = cursor.getString(cursor.getColumnIndex(Medicine.KEY_address));
            medicine.category = cursor.getString(cursor.getColumnIndex(Medicine.KEY_category));
            medicine.usage = cursor.getString(cursor.getColumnIndex(Medicine.KEY_usage));
            medicine.pinyin = cursor.getString(cursor.getColumnIndex(Medicine.KEY_pinyin));
            medicine.taboo = cursor.getString(cursor.getColumnIndex(Medicine.KEY_taboo));
            medicine.disease = cursor.getString(cursor.getColumnIndex(Medicine.KEY_disease));

            list.add(medicine);
        }

        db.endTransaction();

        return list;
    }
}

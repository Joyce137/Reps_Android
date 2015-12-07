package com.example.ustc.sqlitetest;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ListActivity implements android.view.View.OnClickListener{

    Button btnGetAll;
    TextView student_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetAll = (Button) findViewById(R.id.button);
        btnGetAll.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //药品查询接口
        String queryCategoryStr = "感";      //查询条件字符串
        queryMedicinesByCategory(queryCategoryStr);
    }

    public void queryMedicinesByCategory(String queryCategoryStr){
        DBManager dbManager = new DBManager(this);
        dbManager.openDatabase();

        SQLiteDatabase database = dbManager.getDatabase();

        //Medicine逻辑对象
        MedicineRepo mp = new MedicineRepo(database);

        //查询结果列表
        ArrayList<Medicine> list = mp.getMedicineByCategory(queryCategoryStr);
        int count = list.size();

        dbManager.closeDatabase();
    }
}

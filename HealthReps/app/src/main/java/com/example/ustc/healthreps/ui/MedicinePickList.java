package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.TabMedicListAdapter;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.Medicine_Info_List;

import java.util.ArrayList;
import java.util.List;
/*
   选购药品信息(名称，数量等)管理类
 */

public class MedicinePickList extends Activity {

    private ListView lv;
    private List<Medicine> list = new ArrayList<Medicine>();
    private TabMedicListAdapter medicListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.medicine_pick_list);

        initView();

    }

    public void initView(){
        lv = (ListView)findViewById(R.id.lv_medic_info);

        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(65, 105, 225));

        Intent intent = getIntent();
        Medicine_Info_List medicineInfoList = (Medicine_Info_List)intent.getSerializableExtra("info");
        list = medicineInfoList.getArrayList();


        medicListAdapter = new TabMedicListAdapter(this,list);
        lv.setAdapter(medicListAdapter);

    }

}

package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.TabMedicListAdapter;
import com.example.ustc.healthreps.model.Medicine;
import com.example.ustc.healthreps.model.Medicine_Info_List;
import com.example.ustc.healthreps.repo.PrelistContent;
import com.example.ustc.healthreps.repo.PrelistRepo;

import java.util.ArrayList;
import java.util.List;
/*
   选购药品信息(名称，数量等)管理类
 */

public class MedicinePickList extends Activity {
    public static Handler sPrelistResultHandler = null;

    private ListView lv;
    private List<Medicine> list = new ArrayList<Medicine>();
    private TabMedicListAdapter medicListAdapter;
    private Button sendPrelistBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.medicine_pick_list);

        initView();

        sPrelistResultHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String data = (String) msg.obj;
                onRecvPrelistResult(data);
            }
        };
    }

    //处理清单结果
    public void onRecvPrelistResult(String data){
        Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
    }

    //生成清单
    public PrelistContent getPrelistContent(){
        String feibie = "自费";
        String contentPost = "这是清单的备注......";
        PrelistContent prelistContent = new PrelistContent(list,feibie,contentPost);

        return prelistContent;
    }

    public void initView(){
        sendPrelistBtn = (Button)findViewById(R.id.sendPrelistBtn);
        sendPrelistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PrelistRepo().sendPrelist(getPrelistContent());
            }
        });

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

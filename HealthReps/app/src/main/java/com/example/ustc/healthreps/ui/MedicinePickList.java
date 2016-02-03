package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
//                new PrelistRepo().sendPrelist(getPrelistContent());
                //设置备注
                new AlertDialog.Builder(MedicinePickList.this, R.style.CustomDialog).setTitle("添加备注")
                        .setView(new EditText(getApplication()))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new PrelistRepo().sendPrelist(getPrelistContent());
                                Toast.makeText(getApplication(),"send success!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", null).show();
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

        medicListAdapter = new TabMedicListAdapter(this,list);
        lv.setAdapter(medicListAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Medicine medicine = (Medicine) medicListAdapter.getItem(position);

                //自定义弹出药店数量对话框
                final AlertDialog.Builder builder = new AlertDialog.Builder(MedicinePickList.this, R.style.CustomDialog);
                builder.setTitle("订单修改");
                View v = LayoutInflater.from(getApplication()).inflate(R.layout.medic_amount_dialog, null);
                builder.setView(v);

                Button btnAdd = (Button) v.findViewById(R.id.add);
                Button btnSub = (Button) v.findViewById(R.id.sub);
                final EditText etAmount = (EditText) v.findViewById(R.id.amount);
                final TextView etName = (TextView) v.findViewById(R.id.medic_name);

                etName.setText(medicine.name);

                String str = medicine.num + "";
                Toast.makeText(getApplication(), str, Toast.LENGTH_SHORT).show();

                etAmount.setText(str);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        etAmount.setText(String.valueOf(++num));
                    }
                });
                btnSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        if (0 == num) {
                            etAmount.setText("0");
                        } else {
                            etAmount.setText(String.valueOf(--num));
                        }
                    }
                });
                builder.setPositiveButton("确定修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int num = Integer.parseInt(etAmount.getText().toString());
                        if (num > 0) {
                            medicine.num = num;
                        }
                        medicListAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });
    }

}

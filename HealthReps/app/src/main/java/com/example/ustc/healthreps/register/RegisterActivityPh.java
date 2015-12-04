package com.example.ustc.healthreps.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.ustc.healthreps.R;

import java.util.Calendar;

public class RegisterActivityPh extends AppCompatActivity {
    private Button mLastbtn,mNextbtn;
    private Button btn_date;
    private Spinner spin_gender;
    private int year,month,day;//后面可以封装到住户注册的类里面
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_ph);
        mLastbtn=(Button)findViewById(R.id.reg_back1);
        mNextbtn=(Button)findViewById(R.id.reg_ph_button);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_ph_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        mLastbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RegisterActivityPh.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        mNextbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setClass(RegisterActivityPh.this,RegisterActivityFin.class);
                startActivity(intent);
            }
        });


        btn_date = (Button) findViewById(R.id.select_date);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        btn_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                DatePickerDialog datePick = new DatePickerDialog(RegisterActivityPh.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        btn_date.setText(year+"-"+(month+1)+"-"+dayOfMonth);
                    }
                },year,month,day);
                datePick.show();
            }
        });

    }
}

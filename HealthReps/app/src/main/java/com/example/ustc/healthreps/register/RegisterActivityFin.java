package com.example.ustc.healthreps.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.LoginActivity;
import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.LoginRepo;
import com.example.ustc.healthreps.repo.RegisterRepo;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.utils.AppManager;

public class RegisterActivityFin extends AppCompatActivity {
    private Button loginBtn;
    private TextView regResultTV;
    public static Handler sRegisterResultHandler = null;
    private RegisterRepo repo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_fin);
        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);

        initView();
        // 接收消息
        sRegisterResultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int resulttype = (int)msg.obj;
                handleRegisterResult(resulttype);
            }
        };

        repo = new RegisterRepo();
        repo.register(Users.sRegisterUser);
    }

    //处理接收到的消息
    public void handleRegisterResult(int resultType){
        String type = null;
        //-1-socket初始化失败; 0-注册成功；1-密码或账号错误；2-已经在线; 3-账号与客户端不匹配; type-审核未通过
        switch (resultType){
            //socket初始化失败
            case -1:
                type = "网络故障，Socket初始化失败，请检查网络";
                break;
            case 7:
                type = "注册成功！";
                Users.sISSignout = true;
                break;
            case 1:
                type = "用户名已存在！";
                break;
            case 2:
                type = "头像上传失败！";
                break;
            default:
                type = "未知错误";
                break;
        }

        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        Toast.makeText(getApplicationContext(), type, Toast.LENGTH_SHORT).show();
        regResultTV.setText(type);
        Looper.loop();
    }

    public void initView(){
        regResultTV = (TextView)findViewById(R.id.registerResult);


        final Toolbar toolbar = (Toolbar) findViewById(R.id.reg_fin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sign Up");
        loginBtn = (Button)findViewById(R.id.loginIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivityFin.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}

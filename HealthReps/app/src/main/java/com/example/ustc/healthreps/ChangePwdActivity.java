package com.example.ustc.healthreps;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.ustc.healthreps.repo.ChangePwdRepo;
import com.example.ustc.healthreps.utils.AndroidNetAccess;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class ChangePwdActivity extends Activity{
    private Button submitBtn;
    private String oldPwd, newPwd;
    public ChangePwdRepo changePwdRepo = new ChangePwdRepo();
    public static Handler sMsgHandler = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 添加网络驻主线程网络访问权限
        AndroidNetAccess.netAccess();

        // 初始化界面
        initView();

        //接收
        receiveMsg();
    }

    public void initView(){
        submitBtn = (Button)findViewById(R.id.submit_changepwd);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    changePwdRepo.submitPwd(oldPwd, newPwd);
                }

            }
        });
    }

    //判断输入是否有效
    public boolean checkInput(){
        return true;
    }

    //接收
    public void receiveMsg(){
        sMsgHandler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                Toast.makeText(ChangePwdActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        };
    }

}

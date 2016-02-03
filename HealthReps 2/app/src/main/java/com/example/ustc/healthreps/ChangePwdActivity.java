package com.example.ustc.healthreps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.register.RegisterActivityFin;
import com.example.ustc.healthreps.repo.ChangePwdRepo;
import com.example.ustc.healthreps.utils.AndroidNetAccess;
import com.example.ustc.healthreps.utils.AppManager;
import com.example.ustc.healthreps.utils.Utils;

import org.json.JSONObject;

import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by CaoRuijuan on 12/8/15.
 */
public class ChangePwdActivity extends Activity{
    private Button submitBtn, loginBtn, smscodeBtn, vertifySMSCodeBtn;
    private TextView changeNumber;
    private String username, newPwd;
    public ChangePwdRepo changePwdRepo;
    public static Handler sMsgHandler = null;
    private LinearLayout mPasswordLayout;
    private boolean smartSMSCode = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);
        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);

        // 初始化界面
        initView();

        //接收
        receiveMsg();

        changePwdRepo = new ChangePwdRepo();
    }

    public void initView(){
        smscodeBtn = (Button) findViewById(R.id.pwdSMSCodeBtn);
        changeNumber=(EditText)findViewById(R.id.changeNumberText);
        smscodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //检查电话号码合法性
                String t_changeNumber=changeNumber.getText().toString().trim();
                if(t_changeNumber.length()!=11)
                {
                    Toast.makeText(getApplication(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                    return;
                }
                if(Utils.checkNumber(t_changeNumber)==false)
                {
                    Toast.makeText(getApplication(),"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                    return;
                }
                //发送验证码
                SMSSDK.getVerificationCode("86", t_changeNumber);
//                isSendedSMSCode = true;
            }
        });
        vertifySMSCodeBtn = (Button)findViewById(R.id.ConfirmPwdSMSCodeBtn);
        vertifySMSCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText smscodeNumber = (EditText)findViewById(R.id.pwdSMSCodeText);
                String t_smscode = smscodeNumber.getText().toString().trim();

                if(t_smscode.length()==0)
                {
                    Toast.makeText(getApplication(),"请输入手机验证码",Toast.LENGTH_LONG).show();
                    return;
                }
                //验证（短信验证码)
                SMSSDK.submitVerificationCode("86", changeNumber.getText().toString().trim(), t_smscode);
            }
        });

        mPasswordLayout = (LinearLayout)findViewById(R.id.passwordLinerLayout);
        //默认没有密码输入框
//        mPasswordLayout.setVisibility(View.INVISIBLE);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setEnabled(false);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitBtn = (Button)findViewById(R.id.changePWD_button);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    changePwdRepo.submitPwd(username, newPwd);
                }
            }
        });
    }

    //接收
    public void receiveMsg(){
        sMsgHandler= new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = msg.obj.toString();
                loginBtn.setEnabled(true);
                Toast.makeText(ChangePwdActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        };
    }


    //验证短信验证码
    Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                System.out.println("--------result"+event);

                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();

                }
                else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    Toast.makeText(getApplicationContext(), "智能验证成功", Toast.LENGTH_SHORT).show();
                    smartSMSCode = true;
                }
                mPasswordLayout.setVisibility(View.VISIBLE);
            } else {
                try {
                    ((Throwable) data).printStackTrace();
                    Throwable throwable = (Throwable) data;

                    JSONObject object = new JSONObject(throwable.getMessage());
                    String des = object.optString("detail");
                    if (!TextUtils.isEmpty(des)) {
                        Toast.makeText(getApplicationContext(), des, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }


        };
    };

    //检查输入的合法性
    private boolean check()
    {
        int[] anArray;
        int[] bnArray;
        int i,m;
        int j=0,n=0,k=0,l;
        //检查用户名的合法性
        EditText changeUsername=(EditText)findViewById(R.id.ChangeUsernameText);
        String t_changeUsername=changeUsername.getText().toString().trim();
        String s =t_changeUsername;
        bnArray=new int[s.length()];
        anArray=new int[s.length()];
        if(t_changeUsername.length()==0)
        {
            Toast.makeText(getApplication(),"用户名不能为空",Toast.LENGTH_LONG).show();
            return false;
        }
        //转成ASCii码
        for(i=0;i<s.length();i++)
        {
            anArray[i]=(int) s.charAt(i);
        }
        //判断第一个字符是否是字母开头
        if(anArray[0]>=65&anArray[0]<=90||anArray[0]>=97&
                anArray[0]<=122)
            l=0;
        else l=1;
        //检查用户名中是否包含A~Z，a~z的字
        for(i=0;i<s.length();i++)
            if(anArray[i]<65||anArray[i]>90&anArray[i]<97
                    ||anArray[i]>122)
            {
                bnArray[j]=anArray[i];
                j++;

            }
        //检测数字
        for(m=0;m<bnArray.length;m++)
        {
            if(bnArray[m]!=0&(bnArray[m]>47&bnArray[m]<58))
                n++;
            else if(bnArray[m]!=0&(bnArray[m]<48||bnArray[m]>57))
                k++;
        }
        if(k>0)
        {
            Toast.makeText(getApplication(),"用户名非法，用户名应是字母或者字母和数字的组合",Toast.LENGTH_LONG).show();
            return false;
        }
        if(n==s.length())
        {
            Toast.makeText(getApplication(),"用户名非法，用户名不能全为数字",Toast.LENGTH_LONG).show();
            return false;
        }

        if(l==1)
        {
            Toast.makeText(getApplication(),"用户名非法，用户名需以字母为开始",Toast.LENGTH_LONG).show();
            return false;
        }

        //检查修改的密码的合法性
        EditText changePassword=(EditText)findViewById(R.id.changePasswordText);
        String t_changePassword=changePassword.getText().toString().trim();
        EditText changeConfirmPassword=(EditText)findViewById(R.id.changeConfirmPasswordText);
        String t_changeConfirmPassword=changeConfirmPassword.getText().toString().trim();
        if(t_changePassword.length()<=5||t_changePassword.length()>=21)
        {
            Toast.makeText(getApplication(),"密码长度需大于6位小于20位",Toast.LENGTH_LONG).show();
            return false;
        }
//        if(Utils.checkUsername(t_changePassword)==false)
//        {
//            Toast.makeText(getApplication(),"密码需由字母和数字组成",Toast.LENGTH_LONG).show();
//            return false;
//        }
        if(t_changeConfirmPassword.equals(t_changePassword)==false)
        {
            Toast.makeText(getApplication(),"两次密码不一致",Toast.LENGTH_LONG).show();
            return false;
        }

        username = t_changeUsername;
        newPwd = t_changeConfirmPassword;
        return true;
    }
}

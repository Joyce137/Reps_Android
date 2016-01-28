package com.example.ustc.healthreps.register;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.sms.SMS;
import com.example.ustc.healthreps.utils.Utils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

public class RegisterActivityPh extends AppCompatActivity {
    private Button mLastbtn,mNextbtn;
    private Button btn_date, mSMSbtn;
    private Spinner spin_gender;
    private EditText mSMScodeEditText, postInstruction, cellphoneNumber;

    private int mYear,mMonth,mDay;//后面可以封装到住户注册的类里面
//    private boolean isSendedSMSCode = false;    //是否发验证码
    boolean smartSMSCode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_ph);

        initView();
    }

    public void initView(){
        spin_gender = (Spinner)findViewById(R.id.spinner_gender);
        postInstruction = (EditText)findViewById(R.id.myInstructionEditText);
        cellphoneNumber=(EditText)findViewById(R.id.cellphoneNumberText);

        mSMScodeEditText  = (EditText)findViewById(R.id.smscodeText);

        mLastbtn=(Button)findViewById(R.id.reg_back1);
        mNextbtn=(Button)findViewById(R.id.reg_ph_button);

        //初始化短信验证码sdk
        SMSSDK.initSDK(this, SMS.APPKEY, SMS.APPPSECRET);



        EventHandler eh=new EventHandler(){

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);


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
                String smscodeStr = mSMScodeEditText.getText().toString().trim();
                if(check(Users.sRegisterUser)!=null){
                    if(smscodeStr.length()==0){
                        Toast.makeText(getApplication(),"请输入短信验证码",Toast.LENGTH_LONG).show();
                    }
                    else{
                        //智能验证成功
                        if(smartSMSCode){
                            Toast.makeText(getApplication(),"智能验证成功，无需再输入短信验证码",Toast.LENGTH_LONG).show();
                            mSMScodeEditText.setVisibility(View.INVISIBLE);
                            Users.sRegisterUser.phone = cellphoneNumber.getText().toString().trim().getBytes();
                            Intent intent = new Intent();
                            intent.setClass(RegisterActivityPh.this, RegisterActivityFin.class);
                            startActivity(intent);
                        }
                        //验证（短信验证码)
                        SMSSDK.submitVerificationCode("86", cellphoneNumber.getText().toString().trim(), mSMScodeEditText.getText().toString().trim());
                    }
                }
            }
        });


        btn_date = (Button) findViewById(R.id.select_date);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        btn_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                DatePickerDialog datePick = new DatePickerDialog(RegisterActivityPh.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        btn_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                },mYear,mMonth,mDay);
                datePick.show();
            }
        });


        //手机短信验证码
        mSMSbtn = (Button) findViewById(R.id.smscodeBtn);
        mSMSbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送验证码
                SMSSDK.getVerificationCode("86", cellphoneNumber.getText().toString().trim());
//                isSendedSMSCode = true;
            }
        });
    }

    private UserInfo check(UserInfo userInfo)
    {
        int i=0,j=0;
        //检查身份证号的合法性
        EditText ID=(EditText)findViewById(R.id.IDText);
        String IDnumber=ID.getText().toString().trim();
        if(IDnumber.length()!=18 && IDnumber.length()!=15)
        {
            Toast.makeText(getApplication(),"身份证号码长度不合法",Toast.LENGTH_LONG).show();
            return null;
        }
        //正则表达式
        String IDCard1 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
        String IDCard2 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|x|X)$";
//        String idStr = "/^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X))$/";
//        Pattern pattern = Pattern.compile(idStr);
        if(!Pattern.compile(IDCard1).matcher(IDnumber).matches() && !Pattern.compile(IDCard2).matcher(IDnumber).matches()){
            Toast.makeText(getApplication(),"身份证号码不合法",Toast.LENGTH_SHORT).show();
            return null;
        }
//        for(i=0;i<IDnumber.length()-1;i++)
//        {
//            if(IDnumber.charAt(i)>='0'&IDnumber.charAt(i)<='9')
//                j++;
//            else
//            {
//                Toast.makeText(getApplication(),"身份证号码不合法",Toast.LENGTH_LONG).show();
//                return null;
//            }
//        }
//        if(j!=IDnumber.length()-1){
//            Toast.makeText(getApplication(),"身份证号码不合法",Toast.LENGTH_LONG).show();
//            return null;
//        }
//
//        if(IDnumber.charAt(17)>='0'&IDnumber.charAt(17)<=9||IDnumber.charAt(17)=='X'){}
//        else {
//            Toast.makeText(getApplication(),"身份证号码不合法",Toast.LENGTH_LONG).show();
//            return null;
//        }

        //检查医保号
        EditText Insurance=(EditText)findViewById(R.id.InsuranceText);
        String InsuranceNumber=Insurance.getText().toString().trim();
        if(InsuranceNumber.length()==0||InsuranceNumber.length()>20)
        {
            Toast.makeText(getApplication(),"医保号不能为空或者过长",Toast.LENGTH_LONG).show();
            return null;
        }
        if(Utils.checkNumber(InsuranceNumber)==false)
        {
            Toast.makeText(getApplication(),"请填写正确医保号",Toast.LENGTH_LONG).show();
            return null;
        }
        //检查地址是否为空
        EditText Address=(EditText)findViewById(R.id.AddressText);
        String t_address=Address.getText().toString().trim();
        if(t_address.length()==0)
        {
            Toast.makeText(getApplication(),"地址不能为空",Toast.LENGTH_LONG).show();
            return null;
        }
        //检查电话号码

        String t_cellphoneNumber=cellphoneNumber.getText().toString().trim();
        if(t_cellphoneNumber.length()!=11)
        {
            Toast.makeText(getApplication(),"请填写11位手机号码",Toast.LENGTH_LONG).show();
            return null;
        }
        if(Utils.checkNumber(t_cellphoneNumber)==false)
        {
            Toast.makeText(getApplication(),"请填写正确手机号码",Toast.LENGTH_LONG).show();
            return null;
        }

        //填充UserInfo
        try {
            String sex = spin_gender.getSelectedItem().toString().trim();
            userInfo.sex = sex.getBytes("GBK");
            userInfo.age = getAge().getBytes("GBK");
            userInfo.ID_Num = IDnumber.getBytes("GBK");
            userInfo.yibao_Num = InsuranceNumber.getBytes("GBK");
            userInfo.address = t_address.getBytes("GBK");
            userInfo.pastDiseaseHistory = postInstruction.getText().toString().trim().getBytes("GBK");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return userInfo;
    }

    public String getAge(){
        return String.valueOf(Utils.getYear() - mYear);
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
                    if(check(Users.sRegisterUser) != null) {
                        Users.sRegisterUser.phone = cellphoneNumber.getText().toString().trim().getBytes();
                        Intent intent = new Intent();
                        intent.setClass(RegisterActivityPh.this, RegisterActivityFin.class);
                        startActivity(intent);
                    }
                }
                else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    //获取验证码成功
                    Toast.makeText(getApplicationContext(), "智能验证成功", Toast.LENGTH_SHORT).show();
                    smartSMSCode = true;
                    Toast.makeText(getApplication(),"智能验证成功，无需再输入短信验证码",Toast.LENGTH_LONG).show();
                    mSMScodeEditText.setVisibility(View.INVISIBLE);
                }

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


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SMSSDK.unregisterAllEventHandler();
    }
}

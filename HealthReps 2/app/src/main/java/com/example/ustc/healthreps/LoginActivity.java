package com.example.ustc.healthreps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.database.entity.Cookie;
import com.example.ustc.healthreps.database.entity.User;
import com.example.ustc.healthreps.database.impl.CookieDaoImpl;
import com.example.ustc.healthreps.database.impl.UserDaoImpl;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.LoginRepo;
import com.example.ustc.healthreps.register.RegisterActivity;
import com.example.ustc.healthreps.repo.UserRepo;
import com.example.ustc.healthreps.serverInterface.LoginBackInfo;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.ReqMsgUserInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.HeartBeatTask;
import com.example.ustc.healthreps.utils.AppManager;
import com.example.ustc.healthreps.utils.Utils;

import java.util.Timer;

public class LoginActivity extends Activity {

    private EditText mUsernametText, mPwdText;
    private Button mLoginBtn;
    private TextView mForgetPwd, mRegister;
    private RadioGroup mUserTypes;
    private RadioButton mChoosedType;

    private InputMethodLayout mLayout01;

    TextView textView;
    FrameLayout.LayoutParams layoutParams;
    LinearLayout linearLayout;
    FrameLayout frameLayout;

    private LoginRepo repo;
    public static Handler sLoginResultHandler = null;
    private CookieDaoImpl cookieDao;
    private UserDaoImpl userDao;

    String username,password,type;

    //心跳包相关
    private Timer mHeatBeatTimer = AllThreads.sHeatBeatTimer;
    private HeartBeatTask mHeartBeatTask = AllThreads.sHeartBeatTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //添加到Activity集合
//        AppManager.getInstance().addActivity(this);

//        byte b[] = new byte[2];
//        b[0] = -39;
//        b[1] = -121;
//        int x = Utils.vtolh(b);
        // 接收消息
        sLoginResultHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int resulttype = (int)msg.obj;
                handleLoginResult(resulttype);
            }
        };

        if(repo == null)
            repo = new LoginRepo();

        //用户数据库
        if(userDao == null)
            userDao = new UserDaoImpl(this);

        //cookie数据库
        if(cookieDao == null)
            cookieDao = new CookieDaoImpl(this);

        //不是主动sign out时，检测cookie
        if(!Users.sISSignout){
            //判断cookie是否有效,有效直接跳到主页面
            Cookie cookie = repo.validCookie(cookieDao);
            if(cookie != null){
                Users.sLoginUsername = cookie.username;
                Users.sLoginPassword = cookie.pwd;
                Users.sLoginUserType = cookie.getRealType();

                //添加到用户数据库
                User newUser = new User();
                newUser.username = Users.sLoginUsername;
                newUser.password = Users.sLoginPassword;
                newUser.type = Users.sLoginUserType;
                userDao.addNewUserToUser(newUser);

                cookieDao.updateDate(1);

                //请求详细信息
                new UserRepo().reqUserInfo(Users.sLoginUsername, Users.sLoginUserType, false);
                goToNextActivity(Users.sLoginUserType);
            }
        }

        //初始化布局
        initLayout();
        // 初始化界面
        initView();

    }

    private void initLayout() {
        linearLayout = (LinearLayout) findViewById(R.id.layout_lin01);
        frameLayout = (FrameLayout) findViewById(R.id.layout_fra01);
        mLoginBtn = (Button) findViewById(R.id.loginOk);
        mLayout01 = (InputMethodLayout) findViewById(R.id.layout_login01);
        textView = (TextView) findViewById(R.id.layout_textview01);
        layoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
        mLayout01.setOnkeyboarddStateListener(new InputMethodLayout.onKeyboardsChangeListener() {// 监听软键盘状态

            @Override
            public void onKeyBoardStateChange(int state) {
                switch (state) {
                    case InputMethodLayout.KEYBOARD_STATE_SHOW:


                        mLoginBtn.setVisibility(View.INVISIBLE);
                        frameLayout.setVisibility(View.GONE);
                        linearLayout.setVisibility(View.VISIBLE);

                        break;
                    case InputMethodLayout.KEYBOARD_STATE_HIDE:
                        linearLayout.setVisibility(View.GONE);
                        frameLayout.setVisibility(View.VISIBLE);

                        mLoginBtn.setVisibility(View.VISIBLE);

                        break;
                }
            }
        });
    }

    private void initView() {

        mUsernametText = (EditText) findViewById(R.id.usernameText);
        mPwdText = (EditText) findViewById(R.id.pwdText);
        mUserTypes = (RadioGroup) findViewById(R.id.userTypeRadio);
        mChoosedType = (RadioButton) findViewById(mUserTypes
                .getCheckedRadioButtonId());
        mLoginBtn = (Button) findViewById(R.id.loginOk);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                login();
            }
        });
        mForgetPwd = (TextView) findViewById(R.id.forgotPwdText);
        mForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        ChangePwdActivity.class);
                startActivity(intent);
            }
        });

        mRegister = (TextView) findViewById(R.id.registerText);
        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

   private void login(){
       username = mUsernametText.getText().toString().trim();
       password = mPwdText.getText().toString().trim();
       type = mChoosedType.getText().toString().trim();
       // 用户名和密码为空
       if (username.length() == 0 || password.length() == 0) {
           Toast.makeText(getApplicationContext(), "请输入用户名和密码",
                   Toast.LENGTH_SHORT).show();
       }
       else if(username.length()>20){
           Toast.makeText(getApplicationContext(), "输入的用户名过长，<20",
                   Toast.LENGTH_SHORT).show();
       }
       else {
           repo.login(username, password, type);
       }
   }



    //登录结果处理
    public void handleLoginResult(int resultType){
        //0-登录成功；1-密码或账号错误；2-已经在线; 3-账号与客户端不匹配; type-审核未通过
        switch (resultType){
            //socket初始化失败
            case -1:
                Toast.makeText(this, "网络故障，Socket初始化失败，请检查网络", Toast.LENGTH_LONG).show();
                break;
            //登录成功
            case 0:
                Users.sLoginUsername = username;
                Users.sLoginPassword = password;
                Users.sLoginUserType = type;
                //添加到cookie
                repo.addToCookie(cookieDao);

                //添加到user table
                User newUser = new User();
                newUser.username = Users.sLoginUsername;
                newUser.password = Users.sLoginPassword;
                newUser.type = Users.sLoginUserType;
                userDao.addNewUserToUser(newUser);

                //请求详细信息
                new UserRepo().reqUserInfo(Users.sLoginUsername,Users.sLoginUserType,false);

                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                goToNextActivity(Users.sLoginUserType);
                Looper.loop();
                break;
            //登录失败
            default:
                String login_result = null;
                switch (resultType) {
                    case 1:
                        login_result = "账号或者密码错误";
                        break;
                    case 2:
                        login_result = "该账号已经在线";
                        break;
                    case 3:
                        login_result = "账号与客户端不匹配";
                        break;
                    case 4:
                        login_result = "审核未通过";
                        break;
                    default:
                        break;
                }

                Log.e("login error", login_result);
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Toast.makeText(LoginActivity.this, login_result, Toast.LENGTH_SHORT)
                        .show();
                Looper.loop();
                break;
        }
    }

    //判断用户类型，决定跳转界面
    public  void goToNextActivity(String userType){
        Intent intent = null;

        if(userType.equals("患者")){
            intent = new Intent(LoginActivity.this, MainActivity.class);
        }
        else if (userType == "医生"){
            //医生界面
        }
        else if (userType == "药师"){
            //药剂师界面
        }
        else{
            //药监局
        }

        //启动心跳包线程
        Sockets.socket_center.sendHeartBeat();
        startHeartBeat();

        startActivity(intent);
    }

    //心跳包设计
    public void startHeartBeat(){
        if(mHeatBeatTimer == null){
            mHeatBeatTimer = new Timer();
        }
        if(mHeartBeatTask == null){
            mHeartBeatTask = new HeartBeatTask();
        }
        mHeatBeatTimer.schedule(mHeartBeatTask, 1000, 5000);
    }
}

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

import com.example.ustc.healthreps.model.Sockets;
import com.example.ustc.healthreps.model.TCPSocket;
import com.example.ustc.healthreps.patient.PatientActivity;
import com.example.ustc.healthreps.register.RegisterActivity;
import com.example.ustc.healthreps.serverInterface.LoginBackInfo;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.utils.AndroidNetAccess;
import com.example.ustc.healthreps.utils.CRC4;

public class LoginActivity extends Activity {

    public static Handler sLoginHandler = null;
    public static String mLoginUsername;
    private TCPSocket mLoginSocket = Sockets.socket_center;
    private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 添加网络驻主线程网络访问权限
        AndroidNetAccess.netAccess();

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
                // TODO Auto-generated method stub
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
        mForgetPwd = (TextView) findViewById(R.id.registerText);
        mForgetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
            }
        });

        mRegister = (TextView) findViewById(R.id.registerText);
        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    // 登录事件
    protected void login() {
        // 初始化socket
        mLoginSocket.initSocket();

        // 启动接收线程
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread();
            mReceiveThread.start();
        }

        // 接收消息
        sLoginHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                NetPack data = (NetPack) msg.obj;
                onRecvLoginMessage(data);
            }
        };

        String username = mUsernametText.getText().toString().trim();
        String pwd = mPwdText.getText().toString().trim();
        String type = mChoosedType.getText().toString().trim();
        // 用户名和密码为空
        if (username.length() == 0 || pwd.length() == 0) {
            Toast.makeText(getApplicationContext(), "请输入用户名和密码",
                    Toast.LENGTH_SHORT).show();
        } else {
            // 密码加密
            String str = pwd;
            byte crcPwd[] = str.getBytes();
            for (int i = 0; i < crcPwd.length; i++)
                crcPwd[i] = 0;
            byte strb[] = str.getBytes();
            for (int i = 0; i < str.length(); i++) {
                crcPwd[i] = strb[i];
            }
            CRC4 crc = new CRC4();
            byte b[] = Types.AES_KEY.getBytes();
            crc.Encrypt(crcPwd, b);

            // 判断用户类型
            if (type.equals("患者")) {
                mLoginSocket.sendUserinfo(username, crcPwd,
                        Types.USER_TYPE_PATIENT, Types.USER_LOGIN_FLAG);
            } else if (type.equals("医生")) {
                mLoginSocket.sendUserinfo(username, crcPwd,
                        Types.USER_TYPE_DOCTOR, Types.USER_LOGIN_FLAG);
            } else if (type.equals("药师")) {
                mLoginSocket.sendUserinfo(username, crcPwd,
                        Types.USER_TYPE_PHA, Types.USER_LOGIN_FLAG);
            } else {
                mLoginSocket.sendUserinfo(username, crcPwd,
                        Types.USER_TYPE_PATIENT, Types.USER_LOGIN_FLAG);
            }
        }
    }

    // 收到login登录结果信息
    public void onRecvLoginMessage(NetPack data) {
        System.out.println("onRecvLoginMessage-----------");
        String login_result = "";
        LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data.getM_buffer());
        mLoginUsername = y.getUsername();
        mLoginSocket.mUsername = y.getUsername();
        boolean yesno = y.isYesno();
        if (yesno) {
            // 登录成功
            login_result = "yes";
            Log.e("login error", login_result);

            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            // Toast.makeText(MainActivity.this, login_result,
            // Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this,
                    PatientActivity.class);
            startActivity(intent);
            // 关闭接收线程
            mReceiveThread.isTrue = false;
            mReceiveThread.interrupt();
            mReceiveThread = null;
            Looper.loop();
        } else {
            // 1、登陆的时候 type = 1 密码和账号错误；type = 2 已经在线; type = 3 使用错了客户端; type =
            // 4 审核未过
            int type = y.getType();
            switch (type) {
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
            // 关闭接收线程
            mReceiveThread.isTrue = false;
            mReceiveThread.interrupt();
            mReceiveThread = null;
            Looper.loop();
        }
    }

}

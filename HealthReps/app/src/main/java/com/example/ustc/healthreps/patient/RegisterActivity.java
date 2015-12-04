package com.example.ustc.healthreps.patient;

import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.Sockets;
import com.example.ustc.healthreps.model.TCPSocket;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.ReceiveThread;
import com.example.ustc.healthreps.threads.SendFileThread;
import com.example.ustc.healthreps.utils.AndroidNetAccess;
import com.example.ustc.healthreps.utils.CRC4;
import com.example.ustc.healthreps.utils.Utils;

/**
 * 注册
 * @author CaoRuijuan
 * 
 */
public class RegisterActivity extends Activity {
	public static Handler sRegisterHandler = null;	
	private TCPSocket mRegisterSocket = Sockets.socket_center;
	private ReceiveThread mReceiveThread = AllThreads.sReceiveThread;
	private SendFileThread mSendFileThread = AllThreads.sSendFileThread;
	
	public static final int REG_INFO_TYPE = 0x0003;
	private int m_dlgType = REG_INFO_TYPE;
	private String m_picPath = null;
	
	private String sex, age, realName, userName, userPwd, defaultStore, phoneNum, province, city, street, address, IDNum, yibaoNum, pastDiseaseHistory;
	int vip;
	
	//View
	private EditText mUsernameText,mRealNameText,mPwdText,mConfirmPwdText;
	private Button mRegBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		AndroidNetAccess.netAccess();
		
		initView();
	}

	//界面初始化
	private void initView() {
		mUsernameText = (EditText) findViewById(R.id.Registration_user);
		mRealNameText = (EditText) findViewById(R.id.Registration_name);
		mPwdText = (EditText) findViewById(R.id.Registration_password);
		mConfirmPwdText = (EditText) findViewById(R.id.Registration_password2);
		mRegBtn = (Button)findViewById(R.id.Registration_OK);
		mRegBtn.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				register();
			}
		});
	}

	//注册方法
	protected void register() {
	 	mRegisterSocket.initSocket();
		//启动接收线程
		if(mReceiveThread == null){
			mReceiveThread = new ReceiveThread();
			mReceiveThread.start();
		}
				
		//启动发文件线程
		if(mSendFileThread == null){
			mSendFileThread = new SendFileThread();
			mSendFileThread.start();
		}
		
		// 接收消息
		sRegisterHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				ControlMsg data = (ControlMsg) msg.obj;
				onRecvRegMessage(data);
			}
		};
		//检查输入
		if(!checkInput()){
			return;
		}
		//发头像
		sendHeadPhoto();
		
		//发文本
		sendTextInfo();
	}
	//发头像
	private void sendHeadPhoto() {
		String userName = mUsernameText.getText().toString();
		String fileName = userName+".jpg";
		m_picPath = "/sdcard2/1.jpg";
		Sockets.socket_center.sendFile(m_picPath, fileName, Types.FILE_TYPE_PIC_REG);
	}
	
	//发文本
	private void sendTextInfo() {
		UserInfo user = new UserInfo();
		try{
			user.realName = realName.getBytes("GBK");
			user.loginName = userName.getBytes();
			
			byte[] strPwd = new byte[100];
			strPwd = userPwd.getBytes();
			CRC4 crc4 = new CRC4();
			crc4.Encrypt(strPwd, Types.AES_KEY.getBytes());
			user.password = strPwd;
				
			user.sex = sex.getBytes();
			user.age = age.getBytes();
			user.defaultStore = defaultStore.getBytes();
			user.vip = vip;
			user.phone = phoneNum.getBytes();
			user.address = address.getBytes("GBK");
			user.type = Types.USER_TYPE_PATIENT;
			user.ID_Num = IDNum.getBytes();
			user.yibao_Num = yibaoNum.getBytes();
			user.pastDiseaseHistory = pastDiseaseHistory.getBytes("GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
			
		NetPack pack = new NetPack(-1, UserInfo.size, m_dlgType == REG_INFO_TYPE?Types.Reg_Center:Types.Mod_Info, user.getMSG_USER_INFOBytes());
		pack.CalCRC();
		Sockets.socket_center.sendPack(pack);
	}

	//获取输入
	private void getInput(){
		userName = mUsernameText.getText().toString().trim();
		realName = mRealNameText.getText().toString().trim();
		userPwd = mPwdText.getText().toString().trim();

		sex = "male";
		age = "20";
		defaultStore = "store1";
		vip = 1;
		phoneNum = "05121234567";
		province = "江苏省";
		city = "苏州市";
		street = "吴中区";
		IDNum = "1234567890777X";
		yibaoNum = "23455664";
		pastDiseaseHistory = "nothing";
		address = province+'|'+city+'|'+'&'+street;
	}
			
	//检查输入
	private boolean checkInput() {
		getInput();
		if(userName.length() == 0||realName.length() == 0||userPwd.length() == 0){
			Toast.makeText(getApplicationContext(), "用户名、真实名、密码不可为空",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!mConfirmPwdText.getText().toString().equals(mPwdText.getText().toString())){
			Toast.makeText(getApplicationContext(), "前后密码不一致",Toast.LENGTH_SHORT).show();
			return false;
		}	
		if(userName.length() > 10){
			Toast.makeText(getApplicationContext(), "用户名不能大于10个字符",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!Utils.onlyIncludingNumber_Letter(userName)){
			Toast.makeText(getApplicationContext(), "用户名只能包含数字、字母和下划线",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(userPwd.length() > 15){
			Toast.makeText(getApplicationContext(), "密码不能大于15个字符",Toast.LENGTH_SHORT).show();
			return false;
		}
	
		if(phoneNum.length() > 11){
			Toast.makeText(getApplicationContext(), "手机号码不能大于11个字符",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(IDNum.length() > 18){
			Toast.makeText(getApplicationContext(), "身份证号不能大于18个字符",Toast.LENGTH_SHORT).show();
			return false;
		}
		if(yibaoNum.length() >= 10){
			Toast.makeText(getApplicationContext(), "医保卡号不能大于10个字符",Toast.LENGTH_SHORT).show();
			return false;
		}	
		return true;
	}
	
	
	//接收注册结果信息
	public void onRecvRegMessage(ControlMsg msg) {
		if(msg.getFlag() == Types.Reg_is){
			if(Looper.myLooper() == null){
				Looper.prepare();
			}
			if (msg.isYesno())
			{
				Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
				
			}
			else if(msg.getType() == 1 ||msg.getType() == 2){
				if(msg.getType() == 1){
					Toast.makeText(getApplicationContext(), "用户名已存在！", Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getApplicationContext(), "图片上传失败！", Toast.LENGTH_SHORT).show();
				}
			}
			//关闭发送文件线程
			mSendFileThread.isTrue = false;
			mSendFileThread.interrupt();
			mSendFileThread = null; 
			
			//关闭接收线程
	        mReceiveThread.isTrue = false;
	        mReceiveThread.interrupt();
	        mReceiveThread = null;  
	        
			Looper.loop();
		}
	}
}

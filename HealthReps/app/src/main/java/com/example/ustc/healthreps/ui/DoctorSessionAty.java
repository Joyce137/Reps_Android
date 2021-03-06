package com.example.ustc.healthreps.ui;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.ChatMsgViewAdapter;
import com.example.ustc.healthreps.model.ChatMsgEntity;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.ChatRepo;
import com.example.ustc.healthreps.serverInterface.P2PInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.SendFileThread;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

public class DoctorSessionAty extends Activity implements OnClickListener{

	public static Handler sChatHandler = null, sConectionStatusHandler = null;
	private ChatRepo repo;

	private Button mBtnSend; // 发送btn
	private Button mBtnBack; // 返回btn
	private Button mBtnOther;// 弹出图片、文件、视频、音频btn
	
	private EditText mEditTextContent;   //发送文字
	private TextView mEditTextName,mEditTextDep;
	private ListView mListView;
	private ChatMsgViewAdapter mAdapter;// 消息视图的Adapter
	private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();// 消息对象数组
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.doctor_session);

		sChatHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				P2PInfo info = (P2PInfo) msg.obj;
				showMsg(info);
			}
		};

		sConectionStatusHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				String data = (String) msg.obj;
				handleConnectionStatus(data);
			}
		};

		if(repo == null)
			repo = new ChatRepo();

		init();
		initdata();
		mListView.setSelection(mAdapter.getCount() - 1);
	}

	//显示连接状况
	public void handleConnectionStatus(String data){
		//对方断开连接
		if(data.startsWith("offc")){
			Toast.makeText(getApplicationContext(),data.substring(4),Toast.LENGTH_SHORT).show();
			this.finish();
		}
	}

	//显示消息
	public void showMsg(P2PInfo info){
		String name1 = "",msg1 = "",toUsername = "";
		try {
			name1 = new String(info.username,"GBK").trim();
			toUsername = new String(info.toUsername,"GBK").trim();
			msg1 = new String(info.info,"GBK").trim();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setName(name1);
		entity.setDate(getDate());
		entity.setMessage(msg1);
		entity.setMsgType(true);
		mDataArrays.add(entity);
		mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
		mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
	}

	private String[] msgArray = new String[] { "hi", "hi", "who?", "me",
			"how are you", "i am fine,and you?", "me too.", "too long no see u",
			"是吗", "干啥哩", "滚去写作业吧，快考试了", "哦" };

	private String[] dataArray = new String[] { "2015-12-07 18:00:02",
			"2015-12-07 18:10:22", "2015-12-07 18:11:24",
			"2015-12-07 18:20:23", "2015-12-07 18:30:31",
			"2015-12-07 18:35:37", "2015-12-07 18:40:13",
			"2015-12-07 18:50:26", "2015-12-07 18:52:57",
			"2015-12-07 18:55:11", "2015-12-07 18:56:45",
			"2015-12-07 18:57:33", };
	private final static int COUNT = 12;// 初始化数组总数
	
	/**
	 * 模拟加载消息历史，实际开发可以从数据库中读出
	 */
	private void initdata() {
		for (int i = 0; i < COUNT; i++) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setDate(dataArray[i]);
			if (i % 2 == 0) {
				entity.setName("小贱");
				entity.setMsgType(true);// 收到的消息
			} else {
				entity.setName("大头");
				entity.setMsgType(false);// 自己发送的消息
			}
			entity.setMessage(msgArray[i]);
			mDataArrays.add(entity);
		}

		mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
		mListView.setAdapter(mAdapter);
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.listview);
		mBtnSend = (Button) findViewById(R.id.btn_send);
		mBtnSend.setOnClickListener(this);
		mBtnBack = (Button) findViewById(R.id.btn_back11);
		mBtnBack.setOnClickListener(this);
//		mBtnOther = (Button) findViewById(R.id.btn_other);
//		mBtnOther.setOnClickListener(this);
		
		mEditTextContent = (EditText) findViewById(R.id.et_sendmessage);
		
		Intent intent = getIntent();
		String dName = intent.getStringExtra("doctor_name");
		String gName = intent.getStringExtra("grade_name");

		mEditTextName = (TextView) findViewById(R.id.doctor_name);
		mEditTextDep = (TextView) findViewById(R.id.doctor_gradeName);

		mEditTextName.setText(dName);
		mEditTextDep.setText(gName);

	}

	
	/**
	 * 发送消息时，获取当前事件
	 * 
	 * @return 当前时间
	 */
	private String getDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return format.format(new Date());
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_send:// 发送按钮点击事件
				send();
				break;
			case R.id.btn_back11:// 返回按钮点击事件
				back();// 结束,实际开发中，可以返回主界面
				break;
			case R.id.btn_other://弹出图片、文件、音频、视频选择
				other();
//				sendFile();
				break;
		}
	}

	public void sendFile(){
		//启动发文件线程
		if(AllThreads.sSendFileThread == null){
			AllThreads.sSendFileThread = new SendFileThread();
			AllThreads.sSendFileThread.start();
		}
		String filePath = Utils.copyDefaultHeadPhoto(this);
//		String filePath = "/1.jpg";
//		String filePath = "/sdcard2/1.jpg";
		File file = new File(filePath);
		if(file.exists()&&file.isFile()&&file.canRead()){
			String fileName = Users.sLoginUsername+Utils.getDataAndTime()+".jpg";
			Sockets.socket_center.sendFile(filePath, fileName, Types.FILE_TYPE_CHAT_PICTURE);
		}
		else {
			Toast.makeText(getApplicationContext(),"文件不存在或不合法,请重新选择",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& ((AddRelativeLayout) findViewById(R.id.allRelativeLayout))
						.hideFaceView()) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void other() {

	}

	private void back() {
		finish();
	}

	private void send() {
		String contString = mEditTextContent.getText().toString();
		if (contString.length() > 0) {
			ChatMsgEntity entity = new ChatMsgEntity();
			entity.setName("大头");
			entity.setDate(getDate());
			entity.setMessage(contString);
			entity.setMsgType(false);
			mDataArrays.add(entity);
			mAdapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
			mEditTextContent.setText("");// 清空编辑框数据
			mListView.setSelection(mListView.getCount() - 1);// 发送一条消息时，ListView显示选择最后一项
		}
		repo.sendMsg("doctor1",contString);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeSendFileThread();
	}

	//关闭发文件线程
	public void closeSendFileThread(){
		AllThreads.sSendFileThread.isTrue = false;
		AllThreads.sSendFileThread.interrupt();
		AllThreads.sSendFileThread = null;
	}
}

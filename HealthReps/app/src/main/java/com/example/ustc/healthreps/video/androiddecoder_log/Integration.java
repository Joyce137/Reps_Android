package com.example.ustc.healthreps.video.androiddecoder_log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.videoAudio.codec.AvcEncoder;
import com.example.ustc.healthreps.videoAudio.codec.Decoder;
import com.example.ustc.healthreps.videoAudio.packages.HANDSHAKE_MESSAGE;
import com.example.ustc.healthreps.videoAudio.packages.PT_MESSAGE;
import com.example.ustc.healthreps.videoAudio.packages.UDP_ADDR_PACK;
import com.example.ustc.healthreps.videoAudio.packages.UDP_FORWARD_PACK;
import com.example.ustc.healthreps.videoAudio.packages.UDP_LOGIN_PACK;
import com.example.ustc.healthreps.videoAudio.threads.comb_test;
import com.example.ustc.healthreps.videoAudio.threads.div_test;
import com.example.ustc.healthreps.videoAudio.threads.newclass;
import com.example.ustc.healthreps.videoAudio.threads.newclassrev;
import com.example.ustc.healthreps.videoAudio.threads.rev_test;
import com.example.ustc.healthreps.videoAudio.threads.send_test_log;
import com.example.ustc.healthreps.videoAudio.utils.Utils;


public class Integration extends Activity implements Callback// SurfaceHolder.
{
	private Button btnExit;
	private boolean isRecording = false; // 是否边录边放
	// 音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
	private static final int frequency = 22050;// 44100 11025
	// 这里采用的是单声道，尝试用双声道会时会出错
	private static final int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_MONO;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	private int recBufSize, playBufSize;
	private AudioRecord audioRecord; // 调用麦克风，用于采样
	private AudioTrack audioTrack; // 对音频数据进行播放

	private MediaCodec encoder; // 编码器
	private MediaCodec decoder; // 解码器

	private String IP_audio; // 接收端IP地址，最终是需要从服务器动态获取
	private int localPort; // 接收端端口
	private int audio_mode; //#######################################

	// --------------------------------------------------------------------------------------new
	// added!
	/*
	 * 实际统计中，每秒发送44个左右的UDP包，每个包的大小设定为530 bytes
	 * 每一帧包含：帧号、帧数据的长度、帧数据，接收时设定接收包的大小为530 bytes 发送的时候按压缩后的大小发送，因为压缩后的每一帧大小为200
	 * bytes左右远小于530 每一帧在缓冲区的存放位置为 num%50
	 */
	private int num = -1; // 发送的每个音频帧的序号，这里每秒发送44个左右的帧，用int表示足够了
	private int stored = 0; // 缓冲数组中存放的音频帧个数 ，当大于等于10个时，才可以开始播放
	private byte[] buffers = null; // 缓冲区，大小设置为50*530，可以50个帧，每个帧大小为530字节
	private int point = -1; // 缓冲区帧位置指针，point*530即第point帧的起始位置，用来指向当前播放帧
	private boolean[] flag; // 标志数组，用来标志缓冲区中的个帧是否有数据
	private boolean canBePlayed = false; // 用于播放线程中，标志是否能进行播放
	// --------------------------------加互斥锁-----------------------------------------------？？？？？？
	private ReadWriteLock rwl = new ReentrantReadWriteLock();
	private Lock readLock = rwl.readLock();
	private Lock writeLock = rwl.writeLock();
	// audio above
	static final String TAG = "StudyCamera";
	AvcEncoder avcCodec;
	SurfaceView m_prevewview;
	SurfaceHolder m_surfaceHolder;
	int width = 640;
	int height = 480;
	int framerate = 20;// 25
	int bitrate = 220500;//241000
	int MAXDATALEN = 512;

	Camera m_camera;
	Decoder decode;
	div_test divtest;

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private Surface mSurface;
	private int mSurfaceWidth, mSurfaceHeight;

//	EditText ipAddr;
	newclass lock = new newclass();
	newclassrev lock_rev = new newclassrev();
	boolean received = false; // 是否开启了接收

	public final static int LOGIN_FLAG = 8988;
	public final static int ADDR_FLAG = 8986;
	public final static int HANDSHAKE = 1004; // 对方发送的试探信号
	public final static int PENTRATE = 1003; // 客户端发送请求，请求服务器让对方联系自己
	public final static int PANTRATEFAILED = 1005; // 穿透失败
	boolean isRun = true;
	boolean isRun_rev = true;
	boolean isLogin = false;// 是否登录成功
	boolean isLogin_rev = false;// 是否登录成功
	boolean isGetname = false;// 得到对方用户信息
	boolean isPentrate = false;// 是否穿透成功
	boolean isRun_audio = true;
	boolean isRun_rev_audio = true;
	boolean isLogin_audio = false;// 是否登录成功
	boolean isLogin_rev_audio = false;// 是否登录成功
	boolean isGetname_audio = false;// 得到对方用户信息
	boolean isPentrate_audio = false;// 是否穿透成功
	String IP_peer;
	int port_peer;

	String nameM, nameY, myName, yourName;
	String nameM_audio, nameY_audio;
//	EditText edittext_Mname, edittext_Yname;
	DatagramSocket socket_login;
	DatagramSocket socket_rev;
	DatagramSocket socket_login_audio;
	DatagramSocket socket_rev_audio;

	UDP_LOGIN_PACK loginpack = new UDP_LOGIN_PACK();
	UDP_LOGIN_PACK loginpack_rev = new UDP_LOGIN_PACK();
	UDP_LOGIN_PACK loginpack_audio = new UDP_LOGIN_PACK();
	UDP_LOGIN_PACK loginpack_rev_audio = new UDP_LOGIN_PACK();
	byte[] loginbyte = new byte[536];
	byte[] loginbyte_audio = new byte[536];
	byte[] revbyte = new byte[536];
	byte[] revbyte_audio = new byte[536];
	byte[] myname_ori;
	byte[] peername_ori;
	byte[] myname_ori_audio;
	byte[] peername_ori_audio;
	byte[] myname = new byte[100];
	byte[] myname_audio = new byte[100];
	UDP_ADDR_PACK addrpack = new UDP_ADDR_PACK();
	UDP_ADDR_PACK addrpack_audio = new UDP_ADDR_PACK();
	byte[] addrbyte = new byte[552];
	byte[] addrbyte_audio = new byte[552];
	byte[] peername = new byte[100];
	byte[] peername_audio = new byte[100];
	PT_MESSAGE message = new PT_MESSAGE();
	PT_MESSAGE PT_pack = new PT_MESSAGE();
	PT_MESSAGE PT_pack_rev = new PT_MESSAGE();
	PT_MESSAGE PT_pack_audio = new PT_MESSAGE();
	PT_MESSAGE PT_pack_rev_audio = new PT_MESSAGE();
	byte[] msgbyte = new byte[12];
	byte[] peerAddr = new byte[16];// 对方的IP，端口信息结构体
	//	byte[] peerAddr_audio = new byte[16];// 对方的IP，端口信息结构体
	byte[] peerIP = new byte[4];// 对方的IP，端口信息结构体
	byte[] peerPort = new byte[2];// 对方的IP，端口信息结构体
	int port;

	sndThread dt = new sndThread();
	send_test_log st = new send_test_log(lock);
	rev_test rt = new rev_test(lock_rev);

	byte[] sndpack = new byte[526];
	byte[] failed = new byte[2];
	byte[] fpack_b;
	String ip_confirm = "192.168.1.105";// 192.168.1.121 123.57.231.105 192.168.1.112
	//	private byte[] peerPort_audio = new byte[2];
	private byte[] peerAddr_audio = new byte[16];
	int port_audio = 8887;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_videoaudio);


		//安卓2.3以后访问网络增加内容
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads()
				.detectDiskWrites()
				.detectNetwork()
				.penaltyLog()
				.build());
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
				.penaltyDeath()
				.build());


		//获取传递的双方用户名
		Intent intent = getIntent();
		myName = intent.getStringExtra("mName");
		yourName = intent.getStringExtra("yName");

		findViewById(R.id.recvBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rev_Thread();
			}
		});

		findViewById(R.id.sendBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				begin_Thread();
			}
		});


		findViewById(R.id.stopBtn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isRecording = false;
				Integration.this.finish();
			}
		});

//		findViewById(R.id.button1).setOnClickListener(this);
		try {
			if (socket_login == null) {
				socket_login = new DatagramSocket(null);
				socket_login.setReuseAddress(true);
				socket_login.bind(new InetSocketAddress(8880));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (socket_rev == null) {
				socket_rev = new DatagramSocket(null);
				socket_rev.setReuseAddress(true);
				socket_rev.bind(new InetSocketAddress(8881));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//audio TODO
		try {
			if (socket_login_audio == null) {
				socket_login_audio = new DatagramSocket(null);
				socket_login_audio.setReuseAddress(true);
				socket_login_audio.bind(new InetSocketAddress(8886));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (socket_rev_audio == null) {
				socket_rev_audio = new DatagramSocket(null);
				socket_rev_audio.setReuseAddress(true);
				socket_rev_audio.bind(new InetSocketAddress(8887));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//audio TODO

		recBufSize = AudioRecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		playBufSize = AudioTrack.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);
		// 初始化audioRecord，用于录制音频原始
		audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize);
		// 初始化audioTrack，用于播放音频
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
				channelConfiguration, audioEncoding, playBufSize,
				AudioTrack.MODE_STREAM);

		// 初始化编解码器
		initEncoder();
		initDecoder();

		buffers = new byte[50 * 530]; // 缓冲区大小--------------------------------------------------------------
		flag = new boolean[50]; // 标志数组，用来标志缓冲区中的个帧是否有数据
		for (int i = 0; i < 50; i++) {
			flag[i] = false;
		}// -----------------------------------------------------------------------------------------------

//		btnExit = (Button) this.findViewById(R.id.btnExit);
//		btnExit.setOnClickListener(this);
		// audio abpve
//		ipAddr = (EditText) findViewById(R.id.IPaddress);
//		findViewById(R.id.btnOpen_send).setOnClickListener(this);
		divtest = new div_test(lock, width, height, MAXDATALEN, framerate,
				bitrate);
		avcCodec = new AvcEncoder(width, height, framerate, bitrate);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		m_prevewview = (SurfaceView) findViewById(R.id.myView);
		m_prevewview.setZOrderOnTop(true);			//顶层
		m_surfaceHolder = m_prevewview.getHolder();
		m_surfaceHolder.setFormat(PixelFormat.TRANSPARENT);	//透明
//		m_surfaceHolder.setFixedSize(width, height);
//		m_surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		if (null != surfaceHolder) {
			surfaceHolder.removeCallback(this);
			surfaceView = null;
		}
		if (null != surfaceView) {
			surfaceView = null;
		}

		surfaceView = (SurfaceView) findViewById(R.id.yourView);
		surfaceView.setZOrderMediaOverlay(true);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		//开始传输
//		rev_Thread();
//		begin_Thread();
	}

	//对话框


	// TODO开始登陆过程
	// TODO
	private void begin_Thread() {
		begin_thread bt = new begin_thread();
		new Thread(bt).start();
	}

	public class begin_thread implements Runnable {
		public begin_thread() {

		}

		@Override
		public void run() {
			process();
		}
	}

	private void process() {
		sendMyname();
		startThread();
		for (int i = 0; i < 10; i++) {
			SystemClock.sleep(1000);//等待接收登录成功信号
			if (!isLogin || !isLogin_audio) {// 未登录成功
				sendMyname();
				SystemClock.sleep(500);
			} else {
				break;
			}
		}
		pentrate();
		for (int i = 0; i < 10; i++) {
			SystemClock.sleep(1000);// 等待接收对方信息
			if (!isGetname || !isGetname_audio) {// 获取失败
				pentrate();
				SystemClock.sleep(500);
			} else {
				break;
			}
		}
		handshake();
		for (int i = 0; i < 10; i++) {
			SystemClock.sleep(1000);// 等待接收对方信息
			if (!isPentrate) {// 获取失败
				handshake();
				SystemClock.sleep(500);
			} else {
				break;
			}
		}
		send_proc();
//TODO
		if (isRecording == false) {
			isRecording = true;
			startSendH264Socket();
			new RecordSendThread().start();
		}
	}

	// 登陆
	private void sendMyname() {
//		nameM = edittext_Mname.getText().toString();
		nameM = myName;
		nameM += "VS";// TODO

		myname_ori = nameM.getBytes();
		System.arraycopy(myname_ori, 0, myname, 0, myname_ori.length);
		System.arraycopy(myname, 0, loginpack.name, 0, 100);
		loginpack.len = 100;
		loginbyte = loginpack.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(loginbyte,
					loginbyte.length, InetAddress.getByName(ip_confirm), 4678);// 123.57.231.105 10.0.2.2 127.0.0.1
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

			System.out.println("packet length : " + pack_datagram.getLength());
			socket_login.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//audio
//		nameM_audio = edittext_Mname.getText().toString();
		nameM_audio = myName;
		nameM_audio += "AS";// TODO

		myname_ori_audio = nameM_audio.getBytes();
		System.arraycopy(myname_ori_audio, 0, myname_audio, 0, myname_ori_audio.length);
		System.arraycopy(myname_audio, 0, loginpack_audio.name, 0, 100);
		loginpack_audio.len = 100;
		loginbyte_audio = loginpack_audio.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(loginbyte_audio,
					loginbyte_audio.length, InetAddress.getByName(ip_confirm), 4678);// 123.57.231.105 10.0.2.2 127.0.0.1
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

			System.out.println("packet length : " + pack_datagram.getLength());
			socket_login_audio.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 穿透
	void pentrate() {
//		nameY = edittext_Yname.getText().toString();
		nameY = yourName;
		nameY += "VR";// TODO
		peername_ori = nameY.getBytes();
		System.arraycopy(peername_ori, 0, peername, 0, peername_ori.length);
		System.arraycopy(peername, 0, addrpack.name, 0, 100);
		addrpack.flag = ADDR_FLAG;
		addrpack.len = 100;
		addrbyte = addrpack.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(addrbyte,
					addrbyte.length, InetAddress.getByName(ip_confirm), 4678);
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

			System.out.println("packet length : " + pack_datagram.getLength());
			socket_login.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//audio
//		nameY_audio = edittext_Yname.getText().toString();
		nameY_audio = yourName;
		nameY_audio += "AR";// TODO
		peername_ori_audio = nameY_audio.getBytes();
		System.arraycopy(peername_ori_audio, 0, peername_audio, 0, peername_ori_audio.length);
		System.arraycopy(peername_audio, 0, addrpack_audio.name, 0, 100);
		addrpack_audio.flag = ADDR_FLAG;
		addrpack_audio.len = 100;
		addrbyte_audio = addrpack_audio.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(addrbyte_audio,
					addrbyte_audio.length, InetAddress.getByName(ip_confirm), 4678);
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

			System.out.println("packet length : " + pack_datagram.getLength());
			socket_login_audio.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void handshake() {
		message.type = HANDSHAKE;
		System.arraycopy(peername, 0, message.username, 0, 10);
		msgbyte = message.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(msgbyte,
					msgbyte.length, InetAddress.getByName(IPconvert(peerIP)),
					8881);// port 2016.2.27 //TODO
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号
			String ip_test = IPconvert(peerIP);
			System.out.println("packet length : " + pack_datagram.getLength());
			socket_login.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void send_proc() {
		/************************************ 地址设置开始begin *************************************/
		if (isPentrate) {// 端对端
			// 赋值sender
			setPeerAddr(peerIP, peerPort, 1);
			System.out.println("connect result:" + "端对端!!!");
		} else {// 服务器转发
			setPeerAddr(peerIP, peerPort, 0);
			System.out.println("connect result:" + "穿透失败!!!");
			System.out.println("connect result:" + "服务器转发!!!");

			UDP_FORWARD_PACK fpack = new UDP_FORWARD_PACK();
			failed = Utils.shortToLH(PANTRATEFAILED);
			System.arraycopy(failed, 0, sndpack, 0, 2);
			System.arraycopy(peerAddr_audio, 0, fpack.addr, 0, 16);//peerAddr
			System.arraycopy(sndpack, 0, fpack.data, 0, sndpack.length);
			fpack.len = sndpack.length;
			fpack_b = fpack.tobyte();
			isRun = false;
			try {
				// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
				DatagramPacket pack_datagram = new DatagramPacket(fpack_b,
						fpack_b.length, InetAddress.getByName(ip_confirm), 4678);
				// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

				System.out.println("packet length : "
						+ pack_datagram.getLength());
				socket_login.send(pack_datagram);// 发送数据包
				pack_datagram = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/************************************ 地址设置完毕end *************************************/
	}

	void setPeerAddr(byte[] ip, byte[] port, int mode) {
		if (mode == 1) {
			IP_audio = IPconvert(ip); //###############################################
			localPort = port_audio;
			audio_mode = mode;
			st.setIP(IPconvert(ip), 8881);//Utils.vtolh(port)
			st.setMode(mode);
		} else {
			IP_audio = ip_confirm; //#####################################################
			localPort = 4678;
			audio_mode = mode;
//			System.arraycopy(peerAddr, 0, peerAddr_audio, 0, 16);
			st.setIP(ip_confirm, 4678);
			st.setMode(mode);
			// TODO 是否可以预先对peerAddr中的port进行设置 如8881
			st.setPeerIP(peerAddr, ip, port);
		}
	}

	private void startThread() {
		login_proc_thread lpt = new login_proc_thread();
		new Thread(lpt).start();
		login_proc_thread_audio lpta = new login_proc_thread_audio();
		new Thread(lpta).start();
	}

	public class login_proc_thread implements Runnable {
		private String re = null;

		int revMAXSIZE = 1500;
		byte[] revpack;
		int revtype;
		byte[] type = new byte[2];
		int flag;
		byte[] flag_b = new byte[2];
		int len;
		byte[] len_b = new byte[4];

		public login_proc_thread() {

		}

		@Override
		public void run() {
			try {
				byte[] buf = new byte[revMAXSIZE];// 创建一个byte类型的数组，用于存放接收到得数据
				while (isRun) {
					System.out.println("begin....");
					DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
					socket_login.receive(pack);// 读取接收到得数据包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
					// //////////////////////////////////
					// or maybe receive data from here
					revpack = pack.getData();
					dealwithpack(revpack);
					// //////////////////////////////////
					String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					re = ip + "发送!\n";
					System.out.println(re);
				}
				// 注意不需要关闭服务器的socket，因为它一直等待接收数据
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void dealwithpack(byte[] pack) {
			PT_pack = PT_MESSAGE.topack(pack);
			switch (PT_pack.type) {
				// 登录成功消息
				case LOGIN_FLAG:
					isLogin = true;
					System.out.println("登陆成功！！！");
					Log.v("登陆：", "登录成功\n");
					break;
				// 获取对方信息成功
				case ADDR_FLAG:
					isGetname = true;
					UDP_ADDR_PACK lppack = UDP_ADDR_PACK.topack(pack);
					System.arraycopy(pack, 0, flag_b, 0, 2);
					System.arraycopy(pack, 2, lppack.addr, 0, 16);// length of addr roughly 8.tobyte()
					System.arraycopy(pack, 18, len_b, 0, 4);
					System.arraycopy(lppack.addr, 0, peerAddr, 0, 16);
					System.arraycopy(peerAddr, 2, peerPort, 0, 2);
					// port的数值可否手动定义？//TODO
					System.arraycopy(pack, 6, peerIP, 0, 4);
					String ip = IPconvert(peerIP);
					port = Utils.vtolh(peerPort);
					System.out.println("获取对方信息成功！！！\n");
					Log.v("对方信息：", "获取对方信息成功\n");
					break;
				// 收到对方的试探信号，说明建立连接成功
				case HANDSHAKE:
					isPentrate = true;
					isRun = false;// 结束线程
					isRun_audio = false;// 结束线程
					HANDSHAKE_MESSAGE lp = HANDSHAKE_MESSAGE.topack(pack);
					byte[] hs_byte_message = new byte[12];
					HANDSHAKE_MESSAGE hs_message = new HANDSHAKE_MESSAGE();
					hs_message.type = HANDSHAKE;
					hs_message.username = lp.username;
					hs_byte_message = hs_message.tobyte();
					// 向对方发送一个消息
					try {
						// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
						DatagramPacket pack_datagram = new DatagramPacket(
								hs_byte_message, hs_byte_message.length,
								InetAddress.getByName(IPconvert(peerIP)), port);// peerIP.toString()
						// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号

						System.out.println("packet length : "
								+ pack_datagram.getLength());
						socket_login.send(pack_datagram);// 发送数据包 socket_thread
						pack_datagram = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
			}
		}
	}
	//audio
	public class login_proc_thread_audio implements Runnable {
		private String re = null;

		int revMAXSIZE = 1500;
		byte[] revpack;
		int revtype;
		byte[] type = new byte[2];
		int flag;
		byte[] flag_b = new byte[2];
		int len;
		byte[] len_b = new byte[4];

		public login_proc_thread_audio() {

		}

		@Override
		public void run() {
			try {
				byte[] buf = new byte[revMAXSIZE];// 创建一个byte类型的数组，用于存放接收到得数据
				while (isRun_audio) {
					System.out.println("begin....");
					DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
					socket_login_audio.receive(pack);// 读取接收到得数据包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
					// //////////////////////////////////
					// or maybe receive data from here
					revpack = pack.getData();
					dealwithpack(revpack);
					// //////////////////////////////////
					String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					re = ip + "发送!\n";
					System.out.println(re);
				}
				// 注意不需要关闭服务器的socket，因为它一直等待接收数据
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void dealwithpack(byte[] pack) {
			PT_pack_audio = PT_MESSAGE.topack(pack);
			switch (PT_pack_audio.type) {
				// 登录成功消息
				case LOGIN_FLAG:
					isLogin_audio = true;
					System.out.println("登陆成功！！！");
					Log.v("登陆：", "登录成功\n");
					break;
				// 获取对方信息成功
				case ADDR_FLAG:
					isGetname_audio = true;
					UDP_ADDR_PACK lppack = UDP_ADDR_PACK.topack(pack);
					System.arraycopy(pack, 0, flag_b, 0, 2);
					System.arraycopy(pack, 2, lppack.addr, 0, 16);// length of addr roughly 8.tobyte()
					System.arraycopy(pack, 18, len_b, 0, 4);
					System.arraycopy(lppack.addr, 0, peerAddr_audio, 0, 16);
					System.arraycopy(peerAddr_audio, 2, peerPort, 0, 2);
					// port的数值可否手动定义？//TODO
					System.arraycopy(pack, 6, peerIP, 0, 4);
					String ip = IPconvert(peerIP);
					port = Utils.vtolh(peerPort);
					System.out.println("获取对方信息成功！！！\n");
					Log.v("对方信息：", "获取对方信息成功\n");
					break;
				// 收到对方的试探信号，说明建立连接成功
			}
		}
	}

	public String IPconvert(byte[] ip) {
		String re = "";
		for (int i = 0; i < 4; i++) {
			if (ip[i] < 0) {
				re += String.valueOf(ip[i] + 256);
			} else
				re += String.valueOf(ip[i]);
			if (i < 3)
				re += '.';
		}
		return re;
	}

	// TODO
	// TODO登陆过程结束
	// TODO
	private void rev_Thread() {
		rev_thread rt = new rev_thread();
		new Thread(rt).start();
	}

	public class rev_thread implements Runnable {
		public rev_thread() {

		}

		@Override
		public void run() {
			rev_process();
		}
	}

	private void rev_process() {
		sendMyname_rev();
		revThread();
		for (int i = 0; i < 10; i++) {
			SystemClock.sleep(1000);//等待接收登录成功信号
			if (!isLogin_rev ||!isLogin_rev_audio) {// 未登录成功
				sendMyname_rev();
				SystemClock.sleep(500);
			} else {
				break;
			}
		}
		while (isRun_rev) {
			SystemClock.sleep(200);
		}
		isRun_rev = false;
//TODO
		startPlayH264Socket();
		new ReceivePlayThread().start(); //##############################################
	}

	// 登陆
	private void sendMyname_rev() {
//		nameM = edittext_Mname.getText().toString();
		nameM = myName;
		nameM += "VR";// TODO
		myname_ori = nameM.getBytes();
		System.arraycopy(myname_ori, 0, myname, 0, myname_ori.length);
		System.arraycopy(myname, 0, loginpack_rev.name, 0, 100);
		loginpack_rev.len = 100;
		revbyte = loginpack_rev.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(revbyte,
					revbyte.length, InetAddress.getByName(ip_confirm), 4678);// 123.57.231.105 10.0.2.2 127.0.0.1
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号
			System.out.println("packet length : " + pack_datagram.getLength());
			socket_rev.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		//audio
//		nameM_audio = edittext_Mname.getText().toString();
		nameM_audio = myName;
		nameM_audio += "AR";// TODO
		myname_ori_audio = nameM_audio.getBytes();
		System.arraycopy(myname_ori_audio, 0, myname_audio, 0, myname_ori_audio.length);
		System.arraycopy(myname_audio, 0, loginpack_rev_audio.name, 0, 100);
		loginpack_rev_audio.len = 100;
		revbyte_audio = loginpack_rev_audio.tobyte();
		try {
			// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
			DatagramPacket pack_datagram = new DatagramPacket(revbyte_audio,
					revbyte_audio.length, InetAddress.getByName(ip_confirm), 4678);// 123.57.231.105 10.0.2.2 127.0.0.1
			// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号
			System.out.println("packet length : " + pack_datagram.getLength());
			socket_rev_audio.send(pack_datagram);// 发送数据包
			pack_datagram = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void revThread() {
		rev_proc_thread rpt = new rev_proc_thread();
		new Thread(rpt).start();
		rev_proc_thread_audio rpta = new rev_proc_thread_audio();
		new Thread(rpta).start();
	}

	public class rev_proc_thread implements Runnable {
		private String re = null;
		int revMAXSIZE = 1500;
		byte[] revpack;
		int revtype;
		byte[] type = new byte[2];
		String PackIP = null;

		public rev_proc_thread() {

		}

		@Override
		public void run() {
			try {
				byte[] buf = new byte[revMAXSIZE];// 创建一个byte类型的数组，用于存放接收到得数据
				while (isRun_rev) {
					System.out.println("begin....");
					DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
					socket_rev.receive(pack);// 读取接收到得数据包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
					// //////////////////////////////////
					PackIP = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					// or maybe receive data from here
					revpack = pack.getData();
					dealwithpack(revpack);
					// //////////////////////////////////
					String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					re = ip + "发送!\n";
					System.out.println(re);
				}
				// 注意不需要关闭服务器的socket，因为它一直等待接收数据
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void dealwithpack(byte[] pack) {
			PT_pack_rev = PT_MESSAGE.topack(pack);
			switch (PT_pack_rev.type) {
				// 登录成功消息
				case LOGIN_FLAG:
					isLogin_rev = true;
					System.out.println("接收端登陆成功！！！");
					break;
				// 收到对方的试探信号，说明建立连接成功
				case HANDSHAKE:
					isRun_rev = false;// 结束线程
					HANDSHAKE_MESSAGE lp = HANDSHAKE_MESSAGE.topack(pack);
					byte[] hs_byte_message = new byte[12];
					HANDSHAKE_MESSAGE hs_message = new HANDSHAKE_MESSAGE();
					hs_message.type = HANDSHAKE;
					hs_message.username = lp.username;
					hs_byte_message = hs_message.tobyte();
					// 向对方发送一个消息
					try {
						// 创建DatagramSocket对象并绑定一个本地端口号，注意，如果客户端需要接收服务器的返回数据,还需要使用这个端口号来接收数据，所以一定要记住
						DatagramPacket pack_datagram = new DatagramPacket(
								hs_byte_message, hs_byte_message.length,
								InetAddress.getByName(PackIP), 8880);//IPconvert(peerIP) port 2016.2.28 peerIP.toString()
						// 参数分别为：发送数据的字节数组对象、数据的长度、目标主机的网络地址、目标主机端口号，发送数据时一定要指定接收方的网络地址和端口号
						// 得到发送数据包的主机的ip地址
						System.out.println("packet length : " + pack_datagram.getLength());
						socket_rev.send(pack_datagram);// 发送数据包 socket_thread
						pack_datagram = null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case PANTRATEFAILED:
					isRun_rev = false;// 结束线程
					System.out.println("接收端穿透失败\n");
					System.out.println("接收端服务器转发\n");
					break;
			}
		}
	}
	//audio
	public class rev_proc_thread_audio implements Runnable {
		private String re = null;
		int revMAXSIZE = 1500;
		byte[] revpack;
		int revtype;
		byte[] type = new byte[2];
		String PackIP = null;

		public rev_proc_thread_audio() {

		}

		@Override
		public void run() {
			try {
				byte[] buf = new byte[revMAXSIZE];// 创建一个byte类型的数组，用于存放接收到得数据
				while (isRun_rev_audio) {
					System.out.println("begin....");
					DatagramPacket pack = new DatagramPacket(buf, buf.length);// 创建一个DatagramPacket对象，并指定DatagramPacket对象的大小和长度
					socket_rev_audio.receive(pack);// 读取接收到得数据包,如果客户端没有发送数据包，那么该线程就停滞不继续，这个同样也是阻塞式的
					// //////////////////////////////////
					PackIP = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					// or maybe receive data from here
					revpack = pack.getData();
					dealwithpack(revpack);
					// //////////////////////////////////
					String ip = pack.getAddress().getHostAddress();// 得到发送数据包的主机的ip地址
					re = ip + "发送!\n";
					System.out.println(re);
				}
				// 注意不需要关闭服务器的socket，因为它一直等待接收数据
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void dealwithpack(byte[] pack) {
			PT_pack_rev = PT_MESSAGE.topack(pack);
			switch (PT_pack_rev.type) {
				// 登录成功消息
				case LOGIN_FLAG:
					isLogin_rev_audio = true;
					System.out.println("接收端登陆成功！！！");
					break;
				// 收到对方的试探信号，说明建立连接成功
			}
		}
	}

	// TODO
	// TODO登陆过程结束
	class RecordSendThread extends Thread { // 发送线程：采集音频数据，并编码压缩发送

		public void run() {
			DatagramSocket dsSend = null;
			try {
				byte[] buffer = new byte[recBufSize];
				audioRecord.startRecording(); // 开始录制
				try {
					if (dsSend == null) {
						dsSend = new DatagramSocket(null);
						dsSend.setReuseAddress(true);
						dsSend.bind(new InetSocketAddress(8886));
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}
				while (isRecording) {
					// 从MIC保存数据到缓冲区
					int bufferReadResult = audioRecord.read(buffer, 0,
							recBufSize);
					// 将数据拷贝到tmpBuf中去
					byte[] tmpBuf = new byte[bufferReadResult];
					System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
					// encodeData返回发送包的个数----------------------------------------------------------
					encodeData(tmpBuf,dsSend); // 编解码处理，包括发送UDP包
				}
				audioRecord.stop(); // 停止录制

			} catch (Throwable t) {
				Toast.makeText(Integration.this, t.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	class ReceivePlayThread extends Thread { // 接收线程：接收数据，并写入缓冲区
		byte[] revpack_audio;
		@SuppressWarnings("resource")
		@Override
		public void run() {
			DatagramSocket dsReceive = null;
			byte[] buffer;
			byte[] num_b = new byte[4]; // 存放帧号
			// ---------------------------------------------------
			DatagramPacket packet;
			int num_int = 0; // 存放帧号，整型
			byte[] cur_b = new byte[4]; // 为当前正在或已经播放的最大帧号
			int cur_int;

			audioTrack.play(); // 准备开始播放
			try {
				if (dsReceive == null) {
					dsReceive = new DatagramSocket(null);
					dsReceive.setReuseAddress(true);
					dsReceive.bind(new InetSocketAddress(8887));
				}
			} catch (SocketException e) {
				e.printStackTrace();
			}
			while (true) { // isRecording为主线程中的变量，为真则一直监听端口，并接收数据
				buffer = new byte[526]; // 接收大小固定为530-------------------------------------------------------------
				packet = new DatagramPacket(buffer, buffer.length);
				try {
					dsReceive.receive(packet); // 接收网络数据socket_rev
				} catch (IOException e) {
					e.printStackTrace();
				}
				revpack_audio=packet.getData();
				System.arraycopy(revpack_audio, 2, num_b, 0, 4);//packet.getOffset()
				num_int = Utils.vtolh(num_b); // 当前接收的帧的帧号

				if (point == -1) { // 如果是刚开始写，则直接写入
					writeBuffer(packet, num_int);
				} else { // 不是，则进一步判断
					System.arraycopy(buffers, point * 530 + 2, cur_b, 0, 4);
					cur_int = Utils.vtolh(cur_b); // 播放的最大帧的帧号
					// Log.w("tag", "最大播放帧号为："+cur_int+"，接收的帧号为："+num_int); //
					// 测试用
					if (num_int > cur_int) { // 比较当前要写入帧的帧号是否比当前播放最大帧的帧号大，大则写入缓冲区；否则不写
						writeBuffer(packet, num_int);
					}
				}
				if (isRecording && canBePlayed) {
					decodeDataAndPlay(readBuffer());
				}
				if ((!isRecording) && stored > 0) {
					while (stored > 0) { // 录制停止则把剩下的数据播放
						decodeDataAndPlay(readBuffer());
					}
				}
			}
		}
	}

	private void writeBuffer(DatagramPacket packet, int num) { // 写入操作，加写互斥锁
		writeLock.lock();
		try {
			System.arraycopy(packet.getData(), packet.getOffset(), buffers,
					(num % 50) * 530, packet.getLength());
			flag[num % 50] = true; // 存入标记为真
			stored++; // 存入数量加1
			if (stored >= 10 && canBePlayed == false) {
				canBePlayed = true;
			}
		} finally {
			writeLock.unlock();
		}
	}

	private byte[] readBuffer() { // 读缓冲区，加互斥锁，返回纯音频数据（去掉头）
		byte[] outData = null;
		readLock.lock();
		try {
			point = (point + 1) % 50;
			while (!flag[point]) { // 一直读到下一个有数据的位置
				point = (point + 1) % 50;
			}
			// 下面三行作测试用------------------------------------------------------------
			byte[] num_b = new byte[4];
			System.arraycopy(buffers, point * 530 + 2, num_b, 0, 4);
			byte[] length_b = new byte[2];
			System.arraycopy(buffers, point * 530 + 8, length_b, 0, 2);
			int length = Utils.vtolh(length_b);
			outData = new byte[length];
			System.arraycopy(buffers, point * 530 + 14, outData, 0, length);

			flag[point] = false;
			stored--;
			if (stored <= 5 && canBePlayed == true) {
				canBePlayed = false;
			}
			return outData;
		} finally {
			readLock.unlock();
		}
	}

	// 将数据从PCM格式编码为AAC格式，并使用UDP协议发送数据
	private void encodeData(byte[] buffer,DatagramSocket dsSend) {
		int VIDEO_FLAG=9090;//AUDIO_FLAG=9191
		int flag_audio=VIDEO_FLAG;//AUDIO_FLAG
		byte[] flag_b=new byte[2];

		byte[] outData = null;
		DatagramPacket packet;

		ByteBuffer[] inputBuffers = encoder.getInputBuffers();
		ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
		int inputBufferIndex = encoder.dequeueInputBuffer(-1);
		if (inputBufferIndex >= 0) {
			ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
			inputBuffer.clear();
			inputBuffer.put(buffer);
			encoder.queueInputBuffer(inputBufferIndex, 0, buffer.length, 0, 0);
		}

		MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
		int outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);

		while (outputBufferIndex >= 0) {
			ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];

			outputBuffer.position(bufferInfo.offset);
			outputBuffer.limit(bufferInfo.offset + bufferInfo.size);

			outData = new byte[bufferInfo.size];
			outputBuffer.get(outData);

			// 将压缩后的数据用UDP打包网络传输，每个UDP包设置为530字节
			try {
				num++; // 发送的帧号--------------------------------------------------------------------------
				byte[] outdataAddHeader = new byte[526]; // 帧号用int类型占4个字节，帧长用short占2个字节 outData.length + 6
				flag_b=Utils.shortToLH(flag_audio);
				System.arraycopy(flag_b, 0, outdataAddHeader, 0, 2);
				System.arraycopy(Utils.toLH(num), 0, outdataAddHeader, 2, 4);
				System.arraycopy(Utils.shortToLH(0), 0, outdataAddHeader, 6, 2);
				System.arraycopy(Utils.shortToLH(outData.length), 0, outdataAddHeader, 8, 2);
				System.arraycopy(Utils.toLH(0), 0, outdataAddHeader, 10, 4);
//				System.arraycopy(Utils.toLH(num), 0, outdataAddHeader, 0, 4); // 添加帧号
//				System.arraycopy(Utils.shortToLH(outData.length), 0, outdataAddHeader, 4, 2); // 添加帧长
				System.arraycopy(outData, 0, outdataAddHeader, 14, outData.length); // 添加帧数据

				if (audio_mode==1) {
					packet = new DatagramPacket(outdataAddHeader,
							outdataAddHeader.length, InetAddress.getByName(IP_audio),
							localPort);// IP_audio
					dsSend.send(packet); // 发送UDP包
				} else {
					UDP_FORWARD_PACK fpack=new UDP_FORWARD_PACK();
					System.arraycopy(peerAddr_audio, 0, fpack.addr, 0, 16);

					System.arraycopy(outdataAddHeader, 0, fpack.data, 0, outdataAddHeader.length);
					fpack.len=outdataAddHeader.length;
					byte[] snd_buf_audio=new byte[552];
					snd_buf_audio = fpack.tobyte();
					packet = new DatagramPacket(snd_buf_audio, snd_buf_audio.length, InetAddress.getByName(IP_audio), localPort);// IP_audio
					dsSend.send(packet); // 发送UDP包
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			encoder.releaseOutputBuffer(outputBufferIndex, false);
			outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);
		}
	}

	// 将数据从AAC格式解码为PCM格式，并调用audioTrack在本地播放
	private void decodeDataAndPlay(byte[] buffer) {
		byte[] outData = null;
		ByteBuffer[] inputBuffers = decoder.getInputBuffers();
		ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
		int inputBufferIndex = decoder.dequeueInputBuffer(-1);
		if (inputBufferIndex >= 0) {
			ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
			inputBuffer.clear();
			inputBuffer.put(buffer);

			decoder.queueInputBuffer(inputBufferIndex, 0, buffer.length, 0, 0);
		}

		MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
		int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);

		while (outputBufferIndex >= 0) {
			ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];

			outputBuffer.position(bufferInfo.offset);
			outputBuffer.limit(bufferInfo.offset + bufferInfo.size);

			outData = new byte[bufferInfo.size];
			outputBuffer.get(outData);

			// 在这里播放
			audioTrack.write(outData, 0, outData.length);

			decoder.releaseOutputBuffer(outputBufferIndex, false);
			outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
		}
	}

	// 初始化编码器
	private void initEncoder() {
		try{
			encoder = MediaCodec.createEncoderByType("audio/mp4a-latm"); // AAC格式
		}catch (IOException e){
			e.printStackTrace();
		}

		MediaFormat format = new MediaFormat();
		format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
		format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
		format.setInteger(MediaFormat.KEY_SAMPLE_RATE, frequency);
		format.setInteger(MediaFormat.KEY_BIT_RATE, 64 * 1024);// 64kbps
		format.setInteger(MediaFormat.KEY_AAC_PROFILE,
				MediaCodecInfo.CodecProfileLevel.AACObjectLC);
		encoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
		encoder.start();
	}

	// 初始化解码器
	private void initDecoder() {
		try{
			decoder = MediaCodec.createDecoderByType("audio/mp4a-latm"); // AAC格式
		}catch (IOException e){
			e.printStackTrace();
		}
		MediaFormat format = new MediaFormat();
		format.setString(MediaFormat.KEY_MIME, "audio/mp4a-latm");
		format.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
		format.setInteger(MediaFormat.KEY_SAMPLE_RATE, frequency);
		format.setInteger(MediaFormat.KEY_BIT_RATE, 64 * 1024);// 64kbps
		format.setInteger(MediaFormat.KEY_AAC_PROFILE,
				MediaCodecInfo.CodecProfileLevel.AACObjectLC);
		decoder.configure(format, null, null, 0);
		decoder.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		socket_login = null;
		isRecording = false;
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surfaceCreated.");
		mSurface = surfaceHolder.getSurface();
		mSurfaceWidth = width;
		mSurfaceHeight = height;
		decode = new Decoder(mSurface, "video/avc", mSurfaceWidth,
				mSurfaceHeight, bitrate, framerate);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
							   int height) {
		Log.i(TAG, "surfaceChanged w:" + width + " h:" + height);
		mSurface = surfaceHolder.getSurface();
		mSurfaceWidth = width;
		mSurfaceHeight = height;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "surfaceDestroyed");
		mSurface = null;
	}

	private void startSendH264Socket() {
		new Thread(dt).start();
		new Thread(st).start();
	}

	private void startPlayH264Socket() {
		new Thread(rt).start();
		comb_test ct = new comb_test(lock_rev, framerate, decode);
		new Thread(ct).start();
	}

	public class sndThread implements Runnable {
		int pos = 0;// /LZ
		int p_num = 0;
		long currenttime = 0;

		@Override
		public void run() {
			try {
				m_camera = Camera.open(1);
				m_camera.setDisplayOrientation(90);
				m_camera.setPreviewDisplay(m_surfaceHolder);
				Camera.Parameters parameters = m_camera.getParameters();
				parameters.setPreviewSize(width, height);
				parameters.setPictureSize(width, height);
				parameters.setPreviewFrameRate(framerate);
				parameters.setPreviewFormat(ImageFormat.YV12);// NV21
				m_camera.setParameters(parameters);
				m_camera.setPreviewCallback(new Camera.PreviewCallback() {
					@Override
					public void onPreviewFrame(byte[] data, Camera camera) {
						Log.v("h264", "h264 start");
						divtest.div_proc(data);// ,IP
						Log.v("h264", "h264 end");
					}
				});
				m_camera.startPreview();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
package com.example.ustc.healthreps.socket;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.Map.Entry;

import android.util.Log;

import com.example.ustc.healthreps.database.impl.UserDaoImpl;
import com.example.ustc.healthreps.gps.CurLocation;
import com.example.ustc.healthreps.model.DocPha;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.patient.State;
import com.example.ustc.healthreps.repo.ChangePwdRepo;
import com.example.ustc.healthreps.repo.ChatRepo;
import com.example.ustc.healthreps.repo.DocPhaRepo;
import com.example.ustc.healthreps.repo.FileRepo;
import com.example.ustc.healthreps.repo.LoginRepo;
import com.example.ustc.healthreps.repo.PrelistRepo;
import com.example.ustc.healthreps.repo.RegisterRepo;
import com.example.ustc.healthreps.serverInterface.BindStoreDoctor;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.ErrorNo;
import com.example.ustc.healthreps.serverInterface.FileInfo;
import com.example.ustc.healthreps.serverInterface.HeartBeat;
import com.example.ustc.healthreps.serverInterface.LoginBackInfo;
import com.example.ustc.healthreps.serverInterface.NetPack;
import com.example.ustc.healthreps.serverInterface.PostFileInfo;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.serverInterface.ReqDoc;
import com.example.ustc.healthreps.serverInterface.ReqFileInfo;
import com.example.ustc.healthreps.serverInterface.SearchUser;
import com.example.ustc.healthreps.serverInterface.SearchUserInfo;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.serverInterface.UserInfo;
import com.example.ustc.healthreps.serverInterface.UserLogin;
import com.example.ustc.healthreps.threads.AllThreads;
import com.example.ustc.healthreps.threads.HeartBeatTask;
import com.example.ustc.healthreps.threads.SendFileThread;
import com.example.ustc.healthreps.ui.DoctorSessionAty;
import com.example.ustc.healthreps.ui.RecordFragment;
import com.example.ustc.healthreps.ui.SearchDoctor;
import com.example.ustc.healthreps.ui.SearchMedicine;
import com.example.ustc.healthreps.utils.AppPath;
import com.example.ustc.healthreps.utils.Utils;

public class TCPSocket {
	public Socket mSocket;
	public String mUsername;
	private Timer mHeartBeatTimer = AllThreads.sHeatBeatTimer;
	private HeartBeatTask mHeartBeatTask = AllThreads.sHeartBeatTask;
	
	Map<String, byte[]>filenametofile = new HashMap<>();
	Map<String, Set<Integer>>filenametoid = new HashMap<>();
	Set<String> fail_filename;

	public ArrayList<FileInfo> fileInfoList = new ArrayList<FileInfo>();
	
	// 初始化Socket
	// InitSocket
	public boolean initSocket(int DefaultPort, String DefaultIP) {
		Log.e("InitSocket", "0");
		InetAddress addr = null;
		try {
			Log.e("InitSocket", "1");
			addr = InetAddress.getByName(DefaultIP);
			if(mSocket == null){
//				mSocket = new Socket(addr, DefaultPort);
//				mSocket.setSoTimeout(1000*5);
				//设置超时时间为5s
				mSocket = new Socket();
				InetSocketAddress address = new InetSocketAddress(addr,DefaultPort);
				mSocket.connect(address,1000*5);
			}
			if(mSocket.isConnected())
				return true;
			else
				return false;

//			mSocket = new Socket(addr, DefaultPort);
//
//			return true;
		} catch (UnknownHostException e) {
			Log.e("InitSocket", "2");
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			Log.e("InitSocket", "3");
			e.printStackTrace();
			return false;
		}
	}

	public boolean initSocket() {
		if (!initSocket(Types.center_Port, Types.version_IP))
			if (!initSocket(Types.center_Port, Types.version_IP))
				if (!initSocket(Types.center_Port, Types.version_IP)) {
					System.out.println("网络故障，请稍后重试");
					return false;
				}
		Log.e("initSocket", "initSocket成功！");
		return true;
	}
	
	//ShutSocket
	public void shutSocket(){
		if(!mSocket.isClosed()){
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		// 终止对套接字库的使用
		mSocket = null;
	}
	
	//ReLogin
	public void reLogin(String username){
		UserLogin user = new UserLogin(username, "".getBytes(), 0, Types.USER_RECONNECT_FLAG);
		
		//NET_PACK
		NetPack pack = new NetPack(-1,UserLogin.size,Types.LoginUP,user.getBuf());
		pack.CalCRC();
		sendPack(pack);
	}

	// 发送数据包sendPack
	public boolean sendPack(NetPack data) {
		int flag = data.getM_Start();
		// 循环发送
		while (true) {
			int retVal;
			retVal = sendMsg(mSocket, data.getBuf(), data.size, flag);
			if (retVal != 1) {
				Log.e("SendPack", "SendPack: " + retVal);
				return false;
			}
			Log.e("SendPack", "Yes__SendPack: " + retVal);
			return true;
		}
	}

	// 发送消息
	private int sendMsg(Socket socket, byte[] data, int len, int flag) {
		int resultNumber = 1;
		if (socket == null) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.SOCKET_NULL");
			return ErrorNo.SOCKET_NULL;
		}
		if (data == null) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.DATA_NULL");
			return ErrorNo.DATA_NULL;
		}
		if (len < 0) {
			Log.e("SendMsg", "SendMsg: +ErrorNo.MINUSVALUE");
			return ErrorNo.MINUSVALUE;
		}

		int available = 0;
		try {
			if ((available = socket.getInputStream().available()) >= 0) {
				socket.getInputStream().skip(available);
			}

			socket.getOutputStream().write(data);
			socket.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.e("SendMsg", "SendMsg: " + resultNumber);
		return resultNumber;
	}

	// SendUserinfo - 发送用户名和密码
	public void sendUserinfo(String username, byte[] key, int type, int flag) {
		UserLogin user = new UserLogin(username, key, type, flag);
		NetPack p = new NetPack(-1, UserLogin.size, Types.LoginUP,
				user.getBuf());
		p.size = NetPack.infoSize + UserLogin.size;
		p.CalCRC();
		sendPack(p);
	}

	//SendHeartBeat - 发送心跳包
	public boolean sendHeartBeat(){
		mUsername = Users.sLoginUsername;
//		mUsername = "store1";
		Log.e("HearBeat", "SendHeartBeat()");
		if(mUsername.length() == 0){
			return false;
		}
		
		HeartBeat heart = new HeartBeat(mUsername.getBytes());
		NetPack p = new NetPack(-1,HeartBeat.size,Types.HeartBeat,heart.getBuf());
		p.size = NetPack.infoSize + HeartBeat.size;
		p.CalCRC();
		
		if(sendPack(p)){
			return true;
		}
		else{
			return false;
		}
	}
	
	//SendFile -post msg到发文件线程
	public boolean postFileInfo(String filepath, String filename, int type){
		//检查文件是否存在 
		File f = new File(filepath);
		if(!f.exists()){
			Log.e("SendFile", "文件不存在");
			return false;
		}
		PostFileInfo pFileInfo = null;
		try {
			pFileInfo = new PostFileInfo(filename.getBytes("GBK"), filepath.getBytes("GBK"), type);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		synchronized (this) {
			try {
				wait(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
		
		//发送消息，发送到这个Message内部持有的Handler对象，加入它的MessageQueue
		SendFileThread.mFileHandler.obtainMessage(Types.MY_MSG, pFileInfo).sendToTarget();
		
		return true;
	}
	
	//SendFile_thread -发送文件
	public boolean sendFile(String filepath, String filename, int type) {
		if(type == Types.FILE_TYPE_PRES_CHECK || type == Types.FILE_TYPE_PRES_UNCHECK
				|| type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK || type == Types.FILE_TYPE_PRES_CHECK_REJECT){
			Sockets.socket_center.sendHeartBeat();
		}
		String random_str = Utils.getRandomStr();
		//判断文件是否存在并可读
		File f = new File(filepath);
		if(!f.exists()||!f.canRead()){
			return false;
		}
		
		//读取文件到filebuf中
		int size  =  (int) f.length();
		byte[] filebuf = new byte[size];
		//文件IO流
		FileInputStream reader = null;
		try {
			reader = new FileInputStream(f);
			reader.read(filebuf, 0, size);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		//文件拆包
		int packCount = 0;
		if(size % Types.FILE_MAX_BAG == 0){
			packCount = size / Types.FILE_MAX_BAG;
		}
		else{
			packCount = size / Types.FILE_MAX_BAG+1;
		}
		for(int i = 0;i<packCount;i++){
			//文件包设置
			FileInfo fpinfo = new FileInfo();
			fpinfo.filename = filename.getBytes();
			fpinfo.id = i+1;
			//文件结尾
			if(i == packCount - 1){
				fpinfo.flag = Types.FILEEND;
			}
			if(i == 0){
				fpinfo.flag = Types.FILESTRAT;
			}
			fpinfo.len = size;
			fpinfo.type = (short)type;
			fpinfo.start = i * Types.FILE_MAX_BAG;
			fpinfo.idnum = packCount;
			fpinfo.random_str = random_str.getBytes();
			if(i == packCount - 1){
				System.arraycopy(filebuf, i*Types.FILE_MAX_BAG,fpinfo.content, 0,  size - Types.FILE_MAX_BAG*i);
				fpinfo.content_len = (short) (size%Types.FILE_MAX_BAG);
			}
			else{
				System.arraycopy(filebuf, i*Types.FILE_MAX_BAG, fpinfo.content, 0, Types.FILE_MAX_BAG);
			}
			
			//封装成NET_PACK包
			NetPack p = new NetPack(-1,fpinfo.size,Types.FILETAG,fpinfo.getFileInfoBytes());
			p.size = NetPack.infoSize + fpinfo.size;
			p.CalCRC();
			
			//发送文件包
			if(!Sockets.socket_center.sendPack(p)){
				return false;
			}
		}
		return true;
	}
	
	// 分发数据包RecvPack
	public void recvPack(NetPack data) {
		Log.e("RecvPack", "RecvPack------");
		// 登录标志
		if (data.getM_nFlag() == Types.Login_is) {
			LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data
					.getM_buffer());
			if (y.getRecon() == Types.USER_LOGIN_FLAG) {
				// 登录-向登录界面发消息
				LoginRepo.sLoginHandler.obtainMessage(0, data)
						.sendToTarget();
			}
			//上线标志
			else if(y.getRecon() == Types.USER_ONLINE_FLAG){
				//设置用户在线
				Users.sOnline = true;
			}
		}
		//返回搜索消息
		else if(data.getM_nFlag() == Types.Search_Users_By_Rules){
			SearchUserInfo searchUserInfo = SearchUserInfo.getSearchUserInfo(data.getM_buffer());
			//医生消息
			if(searchUserInfo.sortType == Types.USER_TYPE_DOCTOR){
				SearchDoctor.sDocHandler.obtainMessage(0, searchUserInfo).sendToTarget();
			}
			//药店消息
			else if(searchUserInfo.sortType == Types.USER_TYPE_STORE){
				SearchMedicine.sMedicineHandler.obtainMessage(0, searchUserInfo).sendToTarget();
			}
		}

		//请求单个用户信息
		else if (data.getM_nFlag() == Types.REQ_SINGLE_USER_INFO){
			//DocPhaRepo.sDocPhaRepoHandler.obtainMessage(0, data).sendToTarget();
		}

		//请求个人用户信息
		else if(data.getM_nFlag() == Types.MODIFY_USER_INFO){
			//User Info
			UserInfo userInfo = UserInfo.getMSG_USER_INFO(data.getM_buffer());
			Users.sLoginUser = userInfo;
			Users.sLoginUser.loginName = Users.sLoginUsername.getBytes();
			Users.sLoginUser.password = Users.sLoginPassword.getBytes();
			Users.sLoginUser.type = Utils.changeTypeToInt(Users.sLoginUserType);
			Users.sISDetailUserInfo = true;
		}

		//请求所有医生信息
		else if(data.getM_nFlag() == Types.Req_AllDoctors){
			ReqDoc docInfo = ReqDoc.getReqDoc(data.getM_buffer());
			DocPha doc = new DocPha();
			System.arraycopy(docInfo.docName,0,doc.username,0,docInfo.docName.length);
			System.arraycopy(docInfo.realName,0,doc.realname,0,docInfo.realName.length);
			Users.sAllDoctors.add(doc);
		}
		//请求清单状态
		else if(data.getM_nFlag() == Types.PATIENT_REQ_PRELIST_STATUS){
			PreList preList = PreList.getPreList(data.getM_buffer());
			RecordFragment.sPrelistStatusHandler.obtainMessage(0, preList).sendToTarget();
		}

		//绑定结果消息
		else if(data.getM_nFlag() == Types.BangDing){
			BindStoreDoctor bindStoreDoctor = BindStoreDoctor.getBangDingPhaInfo(data.getM_buffer());
			if(bindStoreDoctor.yesno){
				//绑定药店成功
				SearchMedicine.sBindStoreResultHandler.obtainMessage(0, "成功绑定药店").sendToTarget();
			}
		}

		//文件标志
		else if(data.getM_nFlag() == Types.FILETAG){
			saveFile(data);
		}
		else if(data.getM_nFlag() == Types.FileList){
			saveFile(data);
		}
		//一些控制信息
		else if (data.getM_nFlag() == Types.INFONOYES) {
			doControlMsg(data);
		}
		
		//心跳包等信息
		else {
			onRecvNetPack(data);
		}
	}

	//接收心跳包等信息
	public void onRecvNetPack(NetPack data){
		int packFlag = data.getM_nFlag();
		switch(packFlag){
			//接收到的是心跳包
			case Types.HeartBeat:
	//			synchronized (lock) {
					HeartBeatTask.mBeatTimes = 0;
					System.out.println("--------0--------"+HeartBeatTask.mBeatTimes);
	//			}
				break;
				
			//接收到的是重连包
			case Types.RECONNTECT:
				LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data
						.getM_buffer());
				//重连成功
				if(y.isYesno()){
					Users.sOnline = true;
					LoginRepo.mBeatTimes = 0;
					sendHeartBeat();
				}
				//该用户已在别处登陆，断线重连失败
				else{
					Users.sOnline = false;
					String reconAlert = "该用户已在别处登陆，断线重连失败，如需请重新登陆";
					//重连失败，向根页面发消息
					LoginRepo.sAlertHandler.obtainMessage(0, reconAlert)
							.sendToTarget();
					
					if(mHeartBeatTimer != null){
						mHeartBeatTimer.cancel();
						mHeartBeatTimer = null;
					}
					if(mHeartBeatTask != null){
						mHeartBeatTask.cancel();
						mHeartBeatTask = null;
					}
				}
				break;
			//收到的是请求用户信息包
			case Types.REQ_USER_INFO:
				//搜索时显示的用户信息
				DocPhaRepo.sDocPhaRepoHandler.obtainMessage(0, data).sendToTarget();
				break;
			//聊天信息
			case Types.ForwardInfo:
				ChatRepo.sChatHandler.obtainMessage(0, data).sendToTarget();
				break;
			default:
				break;
		}
	}

	//控制信息
	public void doControlMsg(NetPack data) {
		ControlMsg msg = ControlMsg.getControl_MsgInfo(data.getM_buffer());
		//注册
		if(msg.getFlag() == Types.Reg_is){
			//Reg_Dlg->OnrecvRegMessage(y); - 注册-向注册界面发消息
			RegisterRepo.sRegisterHandler.obtainMessage(0, msg).sendToTarget();
		}
		//修改信息
		else if(msg.getFlag() == Types.Mod_Info){
			//modInfo_Dlg->OnrecvRegMessage(y);

		}
		//修改密码
		else if(msg.getFlag() == Types.Mod_Pass){
			//changePW_dlg->onRecvChangePWMessage(y);
			ChangePwdRepo.sChangePwdRepoHandler.obtainMessage(0,msg).sendToTarget();
		}
		//修改图片
		else if (msg.getFlag() == Types.MOD_FILE_TYPE_DOCPHA_STAMP
				|| msg.getFlag() == Types.MOD_FILE_TYPE_PIC_REG 
				|| msg.getFlag() == Types.MOD_FILE_TYPE_USER_FP){
	        //modPicFp_Dlg->onRecvControlMsg(y);
		}

		//清单发送之后处理结果
		else if(msg.getFlag() == Types.PreList_Content && msg.isYesno()) {
			PrelistRepo.sPrelistRepoControlmsgHandler.obtainMessage(0, msg).sendToTarget();
		}

	    else{
			//Main_Dlg->OnRecvControlMsg(y);
			//连接医生消息
			if(msg.getFlag() == Types.To_User||msg.getFlag() == Types.Off_Link)
				DocPhaRepo.sDocPhaRepoControlmsgHandler.obtainMessage(0, msg).sendToTarget();



			//文件发送结果
			else if(msg.getFlag() == Types.FILESENDNOYES)
				FileRepo.sFileRepoControlmsgHandler.obtainMessage(0,msg).sendToTarget();

	    }       
	}

	public void TimerProc(){
		synchronized (this){
			Iterator<Entry<String, byte[]>> iter = filenametofile.entrySet().iterator();
			//for(iter = filenametofile.entrySet().)
		}
	}
	//保存文件SaveFile
	public void saveFile(NetPack data) {
		//临界区操作
		synchronized (this) {
			FileInfo p = FileInfo.getFileInfo(data.getM_buffer());
			//添加到文件列表
			fileInfoList.add(p);
			Log.e("saveFile", "pack_id: " + p.id);

			int len = p.len,i,idnum = p.idnum;
			String random_str = new String(p.random_str).trim();
			//Map与迭代器
			Iterator<Entry<String, byte[]>> iter = filenametofile.entrySet().iterator();
			Entry<String,byte[]> entry = null;

			//寻找random_str	--->iter指向对应的filenametofile
			while(iter.hasNext()){
				entry = iter.next();
				String key = entry.getKey();
				if(key == random_str){
					break;
				}
			}

			//找不到且包flag为文件开始
			if(!filenametofile.containsKey(random_str)&&p.flag == Types.FILESTRAT){
				//filenametofile处理，添加开始包
				byte[] file = new byte[len];
				filenametofile.put(random_str, file);

				//指定该文件的其他内容包索引到filenametoid
				Set<Integer> s = new HashSet<>();
				for(i = 1;i<=idnum;i++){
					if(i!=p.id){
						s.add(new Integer(i));
					}
					else{
						System.arraycopy(p.content, 0, file, p.start, p.content_len);
					}
				}
				filenametoid.put(random_str, s);

				//如果文件只有一个包
				if(idnum == 1){
					//写文件
					if(!writeFile(p, entry.getValue())){
						//写文件失败
						System.out.println("写文件失败");
					}
					//清除filenametoid中的random_str
					filenametoid.remove(random_str);

					//清除filenametofile中的random_str
					filenametofile.remove(random_str);
					return;
				}
			}
			//找到了random_str
			else{
				//写进filenametofile的random_str对应的value相应位置中
				byte[] tempValue = entry.getValue();
				System.arraycopy(p.content,0,tempValue,p.start,p.content_len);
				entry.setValue(tempValue);

				//处理filenametoid
				Iterator<Entry<String, Set<Integer>>> iter1 = filenametoid.entrySet().iterator();
				Entry<String,Set<Integer>> entry1 = null;
				//寻找random_str
				while (iter1.hasNext()) {
					entry1 = iter1.next();
					String key = entry1.getKey();
					if (key == random_str) {
						break;
					}
				}

				//没找到
				if (!filenametoid.containsKey(random_str)) {
					return;
				}

				//找到了
				//判断对应value是否包括id，有则去除
				if (entry1.getValue().contains(p.id)) {
					entry1.getValue().remove(p.id);
				}

				//对应value清除完后没有数据,则写文件
				if (entry1.getValue().size() == 0) {
					if (!writeFile(p, entry.getValue())) {
						//写文件失败
						System.out.println("写文件失败");
					}
					//删除对应的iterator---即key==random_str
					filenametoid.remove(random_str);
				 	filenametofile.remove(random_str);
					return;
				}
				if (p.flag == Types.FILEEND) {
					filenametoid.remove(random_str);
					filenametofile.remove(random_str);
					return;
				}
			}
		}		
	}

	//写文件WriteFile
	public boolean writeFile(FileInfo p, byte[] file) {
		String filename = new String(p.filename).trim();
		File fp;
		//保存到file路径下
		String path = AppPath.getPathByFileType("file");
		AppPath.CheckAndMkdirPathExist(path);
		//图片
		if(p.type == Types.FILE_TYPE_PIC_REG || p.type == Types.FILE_TYPE_PIC_REG_SMALL){
			path += "/pic";
			AppPath.CheckAndMkdirPathExist(path);
		}
		//文件
		else if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK || p.type == Types.FILE_TYPE_PRES_CHECK
				||p.type == Types.FILE_TYPE_PRES_CHECK_REJECT){
			//提取文件名中的data
			String dateTime = Utils.getFileNameDate(filename);
			//保存在本地文件下
			path += "/本地文件";
			AppPath.CheckAndMkdirPathExist(path);

			//登录名下
			path = path + "/" + Users.sLoginUsername;
			AppPath.CheckAndMkdirPathExist(path);

			//分类存储
			if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK)
				path += "/已收处方(未审)";
			else if(p.type == Types.FILE_TYPE_PRES_CHECK)
				path += "/已收处方(已审)";
			else if(p.type == Types.FILE_TYPE_PRES_CHECK_REJECT)
				path += "/已收处方(拒审)";
			AppPath.CheckAndMkdirPathExist(path);

			//按日期分类存储
			path = path + "/" + dateTime;
			AppPath.CheckAndMkdirPathExist(path);
		}

		//路径已设置好，开始存储
		path += filename;
		fp = new File(path);
		//如果不存在，则创建
		if(!fp.exists()){
			try {
				fp.createNewFile();
			} catch (IOException e) {
				System.out.println("创建文件失败");
				e.printStackTrace();
				return false;
			}
		}
		//若文件不可读写，则false
		if(!(fp.isFile()|fp.canWrite())){
			return false;
		}
		
		//true-表示追加式写文件
		try {
			FileOutputStream fos = new FileOutputStream(fp,true);
			fos.write(file);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		//自动更新文件列表

		//如果当前list中显示的是待收处方，则删除该条记录
		if(State.sFilelistType == 3){
			//Main_Dlg->RemoveSingleItemFromFileList(pFile->filename);
			//LoginRepo.sLoginHandler.obtainMessage(0, data).sendToTarget();
		}

		//收到的文件为处方
		if(p.type != Types.FILE_TYPE_PIC_REG && p.type != Types.FILE_TYPE_PIC_REG_SMALL){
			//已收处方（已审）
			if(p.type == Types.FILE_TYPE_PRES_CHECK){
				//自动更新列表
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV);
			}
			//已收处方（拒审）
			else if(p.type == Types.FILE_TYPE_PRES_CHECK_REJECT){
				//自动更新列表
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV_REJECT); 
			}
			//已收处方（未审）
			else if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK){
				//自动更新列表
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV_UNAUTH);
			}
			
			//更新文件列表
			//如果当前list中显示的是已收处方，则更新
			if(State.sFilelistType == 4||State.sFilelistType == 5||State.sFilelistType == 6){
				//LoginRepo.sLoginHandler.obtainMessage(0, data).sendToTarget();
			}

			//发送反馈消息
			ControlMsg cm = new ControlMsg(new String(p.filename).trim(), mUsername, Types.Chufang_Content, true, p.type);
			sendControlMsg(cm);
			return true;
		}

		//类型为图片
		if(p.type == Types.FILE_TYPE_PIC_REG){
			//Main_Dlg->ShowInfoPicture(path);
		}
		//小头像
		else if(p.type == Types.FILE_TYPE_PIC_REG_SMALL){
			//Main_Dlg->clientInfoDlg
		}
		return true;
	}

	//复制文件Copy
	//Send_ControlMsg
	public void sendControlMsg(ControlMsg msg) {
		NetPack p = new NetPack(-1,ControlMsg.size,Types.INFONOYES,msg.getControlMsgBytes());
		p.CalCRC();
		
		sendPack(p);		
	}


	//保存清单文件
	public boolean savePrelist(String path,PreList p){
		byte[] prelist = new byte[2000];
		prelist = p.getPreListBytes();
		File f = new File(path.trim());
		if (f.exists()){
			return false;
		}
		try {
			FileOutputStream fos = new FileOutputStream(f,false);
			fos.write(prelist);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	//copy文件
	//CopyFile--边读边写
	public void copyFile(String inputPath, String outputPath){
		File inputFile = new File(inputPath);
		File outputFile = new File(outputPath);
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new FileInputStream(inputFile);
			os = new FileOutputStream(outputFile);
			//边读边写
			byte[] temp=new byte[(int) inputFile.length()];
			while((is.read(temp)) != -1){
				os.write(temp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}finally {
			try {
				is.close();
				os.close();
			}
			catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	//读取清单内容
	public ArrayList<PreList> readPrelist(String filePath){
		ArrayList<PreList> preLists = new ArrayList<>();
		if(filePath.length()>0){
			File file = new File(filePath);
			if(file != null){
				//如果path是传递过来的参数，可以做一个非目录的判断
				if (file.isDirectory())
				{
					File[] files = file.listFiles();// 列出所有文件
					// 读取所有文件
					if(files != null){
						int count = files.length;// 文件个数
						for (int i = 0; i < count; i++) {
							File curfile = files[i];
							String curFilename = curfile.getName();
							byte[] curFilecontent = new byte[PreList.size];

							if(curfile.isDirectory()){
								Log.d("readPrelist", "The File doesn't not exist.");
							}
							else {
								try {
									FileInputStream fis = new FileInputStream(curfile);

									fis.read(curFilecontent);
									fis.close();
								} catch (IOException e) {
									e.printStackTrace();
								}

								PreList preList = PreList.getPreList(curFilecontent);
								try {
									preList.filename = curFilename.getBytes("GBK");
								}catch (UnsupportedEncodingException e){
									e.printStackTrace();
								}
								preLists.add(preList);
							}
						}
					}
				}
			}
		}
		return preLists;
	}

	//请求文件列表
	public void reqFileList(String mUsername, boolean send_recv, int fileType){
		ReqFileInfo info = new ReqFileInfo();
		try {
			info.username = mUsername.getBytes("GBK");
			info.send_recv = send_recv;
			info.type = fileType;
		}
		catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}

		NetPack pack = new NetPack(-1,ReqFileInfo.SIZE,Types.FileList,info.getReqFileInfoBytes());
		pack.CalCRC();

		sendPack(pack);
	}

	//请求连接
	public void reqOffLink(String usrname, boolean reqOrOffLink){
		ControlMsg msg = new ControlMsg();
		msg.setUsername(usrname);
		if(reqOrOffLink == true){
			msg.setFlag(Types.To_User);
		}
		else {
			msg.setFlag(Types.Off_Link);
		}
		msg.setType(1);


		sendControlMsg(msg);
	}

	//发送搜索条件
	public void sendSearchUser(SearchUser searchUser){
		//添加上经纬度
		searchUser.longitude = (float)CurLocation.lontitude;
		searchUser.latitude = (float)CurLocation.latitude;

		//发送
		NetPack pack = new NetPack(-1,SearchUser.SIZE,Types.Search_Users_By_Rules,searchUser.getSearchUserBytes());
		pack.CalCRC();

		sendPack(pack);
	}

	//发送绑定消息
	public void sendBindStoreDoctorInfo(boolean storeOrDoctor){
		BindStoreDoctor bindStoreDoctor;
		if(!storeOrDoctor){
			bindStoreDoctor = new BindStoreDoctor(Users.sLoginUsername,Users.sDefaultDoctor,true);
		}
		else {
			bindStoreDoctor = new BindStoreDoctor(Users.sLoginUsername,Users.sDefaultStore,false);
		}

		//发送
		NetPack pack = new NetPack(-1,BindStoreDoctor.size,Types.BangDing,bindStoreDoctor.getBuf());
		pack.CalCRC();

		sendPack(pack);
	}
}
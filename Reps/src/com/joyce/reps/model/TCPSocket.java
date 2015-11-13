package com.joyce.reps.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.Map.Entry;

import com.joyce.reps.BaseActivity;
import com.joyce.reps.LoginActivity;
import com.joyce.reps.MainActivity;
import com.joyce.reps.patient.RegisterActivity;
import com.joyce.reps.serverInterface.ControlMsg;
import com.joyce.reps.serverInterface.ErrorNo;
import com.joyce.reps.serverInterface.FileInfo;
import com.joyce.reps.serverInterface.HeartBeat;
import com.joyce.reps.serverInterface.LoginBackInfo;
import com.joyce.reps.serverInterface.NetPack;
import com.joyce.reps.serverInterface.PostFileInfo;
import com.joyce.reps.serverInterface.Types;
import com.joyce.reps.serverInterface.UserLogin;
import com.joyce.reps.threads.AllThreads;
import com.joyce.reps.threads.HeartBeatTask;
import com.joyce.reps.threads.SendFileThread;
import com.joyce.reps.utils.Utils;

import android.content.Context;
import android.util.Log;

public class TCPSocket {
	public Socket mSocket;
	public String mUsername;
	private Timer mHeatBeatTimer = AllThreads.sHeatBeatTimer;
	private HeartBeatTask mHeartBeatTask = AllThreads.sHeartBeatTask;
	
	Map<String, byte[]>filenametofile;
	Map<String, Set<Integer>>filenametoid;
	Set<String> fail_filename;
	
	// 初始化Socket
	// InitSocket
	public boolean initSocket(int DefaultPort, String DefaultIP) {
		Log.e("InitSocket", "0");
		InetAddress addr = null;
		try {
			Log.e("InitSocket", "1");
			addr = InetAddress.getByName(DefaultIP);
			mSocket = new Socket(addr, DefaultPort);

			return true;
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

	public void initSocket() {
		if (!initSocket(Types.center_Port, Types.version_IP))
			if (!initSocket(Types.center_Port, Types.version_IP))
				if (!initSocket(Types.center_Port, Types.version_IP)) {
					System.out.println("网络故障，请稍后重试");
					return;
				}
		Log.e("initSocket", "initSocket成功！");
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
		UserLogin user = new UserLogin();
		user.setUsername(username);
		user.setRecon(Types.USER_RECONNECT_FLAG);
		user.setKey(new byte[20]);
		
		//NET_PACK
		NetPack p = new NetPack();
		p.size = NetPack.infoSize + UserLogin.size;
		p.setM_nFlag(Types.LoginUP);
		p.setnDataLen(UserLogin.size);
		p.setM_buffer(user.getBuf());
		p.CalCRC();
		sendPack(p);
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
		mUsername = "store1";
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
				LoginActivity.sLoginHandler.obtainMessage(0, data)
						.sendToTarget();
			}
			//上线标志
			else if(y.getRecon() == Types.USER_ONLINE_FLAG){
				//
			}
		} 
		//文件标志
		else if(data.getM_nFlag() == Types.FILETAG){
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
					BaseActivity.mBeatTimes = 0;
					System.out.println("--------0--------"+BaseActivity.mBeatTimes);
	//			}
				break;
				
			//接收到的是重连包
			case Types.RECONNTECT:
				LoginBackInfo y = LoginBackInfo.getLogin_Back_Info(data
						.getM_buffer());
				//重连成功
				if(y.isYesno()){
					BaseActivity.mBeatTimes = 0;
					sendHeartBeat();
				}
				//该用户已在别处登陆，断线重连失败
				else{
					String reconAlert = "该用户已在别处登陆，断线重连失败，如需请重新登陆";
					//重连失败，向根页面发消息
					BaseActivity.sAlertHandler.obtainMessage(0, reconAlert)
							.sendToTarget();
					
					if(mHeatBeatTimer != null){
						mHeatBeatTimer.cancel();
						mHeatBeatTimer = null;
					}
					if(mHeartBeatTask != null){
						mHeartBeatTask.cancel();
						mHeartBeatTask = null;
					}
				}
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
			RegisterActivity.sRegisterHandler.obtainMessage(0, msg).sendToTarget();
		}
		//修改信息
		else if(msg.getFlag() == Types.Mod_Info){
			//modInfo_Dlg->OnrecvRegMessage(y);
		}
		//修改密码
		else if(msg.getFlag() == Types.Mod_Pass){
			//changePW_dlg->onRecvChangePWMessage(y);
		}
		//修改图片
		else if (msg.getFlag() == Types.MOD_FILE_TYPE_DOCPHA_STAMP
				|| msg.getFlag() == Types.MOD_FILE_TYPE_PIC_REG 
				|| msg.getFlag() == Types.MOD_FILE_TYPE_USER_FP){
	        //modPicFp_Dlg->onRecvControlMsg(y);
		}
	    else{
	    	//Main_Dlg->OnRecvControlMsg(y);
	    }       
	}
	
	//保存文件SaveFile
	public void saveFile(NetPack data) {
		//临界区操作
		synchronized (this) {
			FileInfo p = FileInfo.getFileInfo(data.getM_buffer());
			int len = p.len,i,idnum = p.idnum;
			String filename = new String(p.filename);
			String random_str = new String(p.random_str);
			//Map与迭代器
			filenametofile = new HashMap<String, byte[]>();
			Iterator<Entry<String, byte[]>> iter = filenametofile.entrySet().iterator();
			
			//寻找random_str			
			while(iter.hasNext()){
				String key = iter.next().getKey();
				if(key == random_str){
					break;
				}
			}			
			
			//找不到且包flag为文件开始
			if(!filenametofile.containsKey(random_str)&&p.flag == Types.FILESTRAT){
				//filenametofile处理
				byte[] file = new byte[len];
				filenametofile.put(random_str, file);
				
				Set<Integer> s = null;
				for(i = 1;i<=idnum;i++){
					if(i!=p.id){
						s.add(i);
					}
					else{
						System.arraycopy(p.content, 0, file, p.start, p.content_len);
					}
				}
				
				//filenametoid处理
				filenametoid = new HashMap<String, Set<Integer>>();
				filenametoid.put(random_str, s);
				
				if(idnum == 1){
					if(!writeFile(p, iter.next().getValue())){
						//文件发送失败
						System.out.println("文件发送失败");
					}
					Iterator<Entry<String, Set<Integer>>> iter1 = filenametoid.entrySet().iterator();
					//寻找random_str			
					while(iter1.hasNext()){
						String key = iter1.next().getKey();
						if(key == random_str){
							filenametoid.remove(iter1);
							break;
						}
					}		
					
					iter = filenametofile.entrySet().iterator();
					
					//寻找random_str			
					while(iter.hasNext()){
						String key = iter.next().getKey();
						if(key == random_str){
							filenametofile.remove(iter);
							break;
						}
					}			
					return;
				}
			}
			//找到了random_str
			else{
				System.arraycopy(p.content, 0, iter.next().getValue(), p.start, p.content_len);
				Iterator<Entry<String, Set<Integer>>> iter1 = filenametoid.entrySet().iterator();
				//寻找random_str			
				while(iter1.hasNext()){
					String key = iter1.next().getKey();
					if(key == random_str){
						break;
					}
				}
				//没找到
				if(iter1 == null){
					return;
				}
				if(iter1.next().getValue().contains(p.id)){
					iter1.next().getValue().remove(p.id);
				}
				if(iter1.next().getValue().size() == 0){
					if(!writeFile(p, iter.next().getValue())){
						//文件发送失败
						System.out.println("文件发送失败");
					}
					filenametoid.remove(iter1);
					filenametofile.remove(iter);
					return;
				}
				if(p.flag == Types.FILEEND){
					filenametoid.remove(iter1);
					filenametofile.remove(iter);
					return;
				}
			}
		}		
	}

	//写文件WriteFile
	public boolean writeFile(FileInfo p, byte[] file) {
		String filename = new String(p.filename);
		File fp;
		String path = getPath("file");
		if(p.type == Types.FILE_TYPE_PIC_REG || p.type == Types.FILE_TYPE_PIC_REG_SMALL){
			path += "pic\\";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
		}
		else if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK || p.type == Types.FILE_TYPE_PRES_CHECK
				||p.type == Types.FILE_TYPE_PRES_CHECK_REJECT){
			String dateTime = Sockets.socket_center.getFileNameDate(filename);
			path += "本地文件\\";
			File f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			path = path + LoginActivity.mLoginUsername+"\\";
			f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			
			if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK)
				path += "已收处方(未审)\\";
			else if(p.type == Types.FILE_TYPE_PRES_CHECK)
				path += "已收处方(已审)\\";
			else if(p.type == Types.FILE_TYPE_PRES_CHECK_REJECT)
				path += "已收处方(拒审)\\";
			
			f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
			path = path + dateTime + "\\";
			f = new File(path);
			if(!f.exists()){
				f.mkdirs();
			}
		}
		path += filename;
		fp = new File(path);
		//如果不存在，则创建
		if(!fp.exists()){
			try {
				fp.createNewFile();
			} catch (IOException e) {
				System.out.println("创建文件失败");
				e.printStackTrace();
			}
		}
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
		}
		
		//删除文件列表中的文件记录
		//????
		
		if(p.type != Types.FILE_TYPE_PIC_REG && p.type != Types.FILE_TYPE_PIC_REG_SMALL){
			if(p.type == Types.FILE_TYPE_PRES_CHECK){
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV);
			}
			else if(p.type == Types.FILE_TYPE_PRES_CHECK_REJECT){
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV_REJECT); 
			}
			else if(p.type == Types.FILE_TYPE_PRES_LOCAL_UNCHECK){
				//Main_Dlg->FileStatusButtonShining(true, IDC_MFCBUTTON_MAIN_RECV_UNAUTH);
			}
			
			//更新文件列表
			//??Main_Dlg->m_filelist_type
			
			ControlMsg cm = new ControlMsg(new String(p.filename), mUsername, Types.Chufang_Content, true, p.type);
			sendControlMsg(cm);
			return true;
		}
		
		if(p.type == Types.FILE_TYPE_PIC_REG){
			//Main_Dlg->ShowInfoPicture(path);
		}
		else if(p.type == Types.FILE_TYPE_PIC_REG_SMALL){
			//Main_Dlg->clientInfoDlg
		}
		return true;
	}

	//Send_ControlMsg
	public void sendControlMsg(ControlMsg msg) {
		NetPack p = new NetPack();
		p.size = NetPack.infoSize + ControlMsg.size;
		p.setM_nFlag(Types.INFONOYES);
		p.setnDataLen(ControlMsg.size);
		p.CalCRC();
		p.setM_buffer(msg.getBuf());
		
		sendPack(p);		
	}

	//GetPath -- databases : 存放位置
	private String getPath(String type) {
		Context c = MainActivity.context.getApplicationContext();
		String path = c.getDatabasePath(type).toString()+"/";
		return path;
	}

	private String getFileNameDate(String filename) {
		//拆分
		String[] fileNameArray = filename.split("-");
		String dataStr = fileNameArray[4];
		
		//判断是否为时间串
		if(Utils.filterUnNumber(dataStr)==dataStr && dataStr.length() == 8){
			return dataStr;
		}
		//如果不是，则使用现在时间
		return Utils.getDate();
	}

}
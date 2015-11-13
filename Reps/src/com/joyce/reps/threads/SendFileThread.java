package com.joyce.reps.threads;

import com.joyce.reps.model.Sockets;
import com.joyce.reps.serverInterface.PostFileInfo;
import com.joyce.reps.serverInterface.Types;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

//文件发送线程SendFileThread
public class SendFileThread extends Thread{
	public String threadName = "SendFileThread";
	public boolean isTrue = true;
	public static Handler mFileHandler = null; 
	
	public SendFileThread() {
		this.setName(threadName);
	}
	
	@Override
	public void run() {
		while(isTrue){
			synchronized (this) {
				try {
					this.wait(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			//建立消息循环
			if (Looper.myLooper() == null){
				Looper.prepare();//1、初始化Looper
			}
			mFileHandler  = new Handler(){//2、绑定handler到SendFileThread实例的Looper对象
                @Override
				public void handleMessage (Message msg) {//3、定义处理消息的方法
                    switch(msg.what) {
                    case Types.MY_MSG:
                    	PostFileInfo p = (PostFileInfo) msg.obj;
                    	String filename = new String(p.filename);
                    	String filepath = new String(p.filepath);
                    	int type = p.type;
                    	if(!Sockets.socket_center.sendFile(filepath, filename, type)){
                    		Log.e("sendFile", "发送文件失败");
                    	}
                    }
                }
            };
            Looper.loop();//4、启动消息循环
		}		
	}
}

package com.example.ustc.healthreps.videoAudio.thread_video;

import com.example.ustc.healthreps.videoAudio.androiddecoder_log.Utils;
import com.example.ustc.healthreps.videoAudio.codec.AvcEncoder;

import android.util.Log;

public class div_test {
	AvcEncoder avcCodec;
	int pack_len=526;
	
//	short devices;//android
//  byte[] chosedev=new byte[2];
	int VIDEO_FLAG=9090;
    int flag=VIDEO_FLAG;
    byte[] flag_b=new byte[2];
    
    int frame_id=0;
    byte[] fid=new byte[4];
    int slice_id=0;//short
    byte[] sid=new byte[2];
    int slicelen=0;//short
    byte[] slen=new byte[2];
    int framelen=0;
    byte[] flen=new byte[4];
    
    int pos = 0;// /LZ
	int p_num = 0;
	long currenttime = 0;
	newclass lock;
    
    int width;
    int height;
    int MAXDATALEN;
    byte[] tempbuf=new byte[512];
    byte[] h264;
//    String IP;
    
    public div_test(newclass L,int wid,int hei,int maxdatalen,int framerate,int bitrate){
    	this.lock=L;
    	this.width=wid;
    	this.height=hei;
    	this.MAXDATALEN=maxdatalen;
    	h264 = new byte[width*height*3/2];
    	avcCodec = new AvcEncoder(width,height,framerate,bitrate);
    	flag_b= Utils.shortToLH(flag);
    }
	public void div_proc(byte[] data){//,String IP
		
		int ret = avcCodec.offerEncoder(data, h264);

		Log.v("length:", String.valueOf(ret));
		if (ret > 0)// /LZ
					// �����ret<100000֮ǰ���е�120000��С���һ�����***��Ӻ��κ���ʾlengthΪ0(xiaomi)//
					// && ret < 100000
		{
			/**/
			int framelen_m = 0;
			byte[] pack = new byte[pack_len];
			fid = Utils.toLH(frame_id);
			framelen = ret;
			flen = Utils.toLH(framelen);

			long sliceTime = System.currentTimeMillis();
			while (framelen_m < ret) {
				// SystemClock.sleep(1);
				slice_id = framelen_m / MAXDATALEN;
				sid = Utils.shortToLH(slice_id);
				if (ret - framelen_m >= MAXDATALEN) {
					System.arraycopy(h264, framelen_m, tempbuf, 0, MAXDATALEN);
					framelen_m += MAXDATALEN;
					slicelen = MAXDATALEN;
					slen = Utils.shortToLH(slicelen);
				} else {
					System.arraycopy(h264, framelen_m, tempbuf, 0, ret - framelen_m);
					slicelen = ret - framelen_m;
					slen = Utils.shortToLH(slicelen);
					framelen_m = ret;
				}
				// send
//				System.arraycopy(chosedev, 0, pack, 0, 2);
				System.arraycopy(flag_b, 0, pack, 0, 2);
				System.arraycopy(fid, 0, pack, 2, 4);
				System.arraycopy(sid, 0, pack, 6, 2);
				System.arraycopy(slen, 0, pack, 8, 2);
				System.arraycopy(flen, 0, pack, 10, 4);
				System.arraycopy(tempbuf, 0, pack, 14, slicelen);// MAXDATALEN
				// SystemClock.sleep(1);
				// sndP.UDP_send(pack);///ori LZ
				p_num++;// /////////////////////////////////////////
				if ((System.currentTimeMillis() - currenttime) >= 1000) {
					Log.v("pack_num:", String.valueOf(p_num));
					currenttime = System.currentTimeMillis();
					p_num = 0;
				}
				synchronized (lock) {
					// /LZ
					if (lock.n < 3)
						lock.n++;
					else
						lock.n = 3;
					System.arraycopy(pack, 0, lock.lock_buf, pos * pack_len, pack_len);
					lock.flag[pos] = 1;
					if (++pos == 500) {
						// if (lock.flag[0] == 1)
						// ;
						pos = 0;
					}
				}

				Log.v("frameid:", String.valueOf(frame_id));
				Log.v("sliceid:", String.valueOf(slice_id));
			}
			Log.v("SliceSendTime: ",
					String.valueOf(System.currentTimeMillis() - sliceTime));
			Log.v("length:", String.valueOf(ret));

			frame_id++;
			// fid=int2Byte(frame_id);

		}
		
	}
	
//	/** 
//     * intתbyte���� 
//     *  
//     * @param a 
//     * @return 
//     */  
//    public static byte[] int2Byte(int a) {  
//        byte[] b = new byte[4];  
//        b[0] = (byte) (a >> 24);  
//        b[1] = (byte) (a >> 16);  
//        b[2] = (byte) (a >> 8);  
//        b[3] = (byte) (a);  
//  
//        return b;  
//    }
//    /** 
//     * ��shortת��byte[2] 
//     * @param a 
//     * @return 
//     */  
//    public static byte[] short2Byte(short a){  
//        byte[] b = new byte[2];  
//          
//        b[0] = (byte) (a >> 8);  
//        b[1] = (byte) (a);  
//          
//        return b;  
//    }
}

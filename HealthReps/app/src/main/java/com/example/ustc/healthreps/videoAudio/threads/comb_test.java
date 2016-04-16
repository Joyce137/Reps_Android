package com.example.ustc.healthreps.videoAudio.threads;

import android.util.Log;

import com.example.ustc.healthreps.videoAudio.codec.Decoder;
import com.example.ustc.healthreps.videoAudio.utils.Utils;

public class comb_test implements Runnable {
	newclassrev lock_rev;
	int VIDEO_FLAG=9090;
	int flag_v=0;
	byte[] flag_v_b=new byte[2];
	int frame_id_rev=0;
	byte[] fid_rev=new byte[4];
	int slice_id_rev=0;//short
	byte[] sid_rev=new byte[2];
	int slicelen_rev=0;//short
	byte[] slen_rev=new byte[2];
	int framelen_rev=0;
	byte[] flen_rev=new byte[4];


	int content=0;
	byte[] cont=new byte[4];
	byte[] tempbuf_rev=new byte[512];
	int MAXCACHE=3;
	int MAXFRAME=200000;
	int BUFFERSIZE=100000;
	int currentFrameID=0;
	byte[][] array=new byte[MAXCACHE][BUFFERSIZE];
	int[] datalen=new int[MAXCACHE];
	int[] full=new int[MAXCACHE];
	int[] count=new int[MAXCACHE];
	int MAXSIZE = 530;
	int pack_len=526;
	int MAXDATALEN = 512;
	int framerate;
	Decoder decode;

	int i_rev = 0;
	int p_num_deal = 0;
	long currenttime_deal = 0;
	byte[] dealpack = new byte[pack_len];//530
	boolean isRev = false;

	public comb_test(newclassrev lock, int frate, Decoder dec){
		this.lock_rev=lock;
		this.framerate=frate;
		this.decode=dec;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
//			try {
//				Thread.sleep(5);// /LZ
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			synchronized (lock_rev) {
				// /LZ
				if (lock_rev.n == 3 && lock_rev.flag_rev[i_rev] == 1) {
					System.arraycopy(lock_rev.lock_buf_rev, i_rev * pack_len, dealpack, 0, pack_len);
					isRev = true;
					lock_rev.flag_rev[i_rev] = 0;
					if (++i_rev == 500)
						i_rev = 0;
				} else {
					isRev = false;
				}
			}
			if (isRev) {
				System.arraycopy(dealpack, 0, flag_v_b, 0, 2);
				flag_v = Utils.vtolh(flag_v_b);
				if(flag_v==VIDEO_FLAG){
					dealwithpack(dealpack);
				}
				else{
					Log.v("error:", "Error!!!");
				}
				p_num_deal++;// /////////////////////////////////////////
				if ((System.currentTimeMillis() - currenttime_deal) >= 1000) {
					Log.v("pack_num_deal:", String.valueOf(p_num_deal));
					currenttime_deal = System.currentTimeMillis();
					p_num_deal = 0;
				}
			}
		}
	}

	public void dealwithpack(byte[] pack) {
		System.arraycopy(pack, 2, fid_rev, 0, 4);
		frame_id_rev = Utils.vtolh(fid_rev);//byte2Int
		System.arraycopy(pack, 6, sid_rev, 0, 2);
		slice_id_rev = Utils.vtolh(sid_rev);//byte2Short
		System.arraycopy(pack, 8, slen_rev, 0, 2);
		slicelen_rev = Utils.vtolh(slen_rev);//byte2Short
		System.arraycopy(pack, 10, flen_rev, 0, 4);
		framelen_rev = Utils.vtolh(flen_rev);//byte2Int

		System.arraycopy(pack, 48, cont, 0, 4);
		content = Utils.vtolh(cont);//byte2Int
		Log.v("verify:", String.valueOf(content));// slice_id

		System.arraycopy(pack, 14, tempbuf_rev, 0, slicelen_rev);

		Log.v("slice id(ori):", String.valueOf(slice_id_rev));// slice_id
		Log.v("frame length:", String.valueOf(framelen_rev));// 接收到的包所在帧的长度
		// /LZ 2015.11.18
		if (framelen_rev > 100000) {
			Log.v("error!", "a frame with large size!!!");
			return;
		}
		// if(currentFrameID==0){
		// currentFrameID=frame_id_rev;
		// }
		// 判断当前frameid的位置，以currentFrameID，当前即将处理的frameID为基准
		int frameid = (frame_id_rev + MAXFRAME - currentFrameID) % MAXFRAME;
		Log.v("frame id-current:", String.valueOf(frameid));// frameid：接收到的帧减去currentFrameID
		if (frameid < MAXCACHE) {// in B
			// the buff is NULL, alloc it
			if (datalen[frame_id_rev % MAXCACHE] == 0) {
				if (framelen_rev > 0) {
					datalen[frame_id_rev % MAXCACHE] = framelen_rev;
				} else {
					return;
				}
			}
			Log.v("frame id_rev:", String.valueOf(frame_id_rev));// frame_id
			Log.v("slice id_rev:", String.valueOf(slice_id_rev));// slice_id
			System.arraycopy(tempbuf_rev, 0, array[frame_id_rev % MAXCACHE], slice_id_rev * MAXDATALEN, slicelen_rev);
			count[frame_id_rev % MAXCACHE]++;
			if (getSliceCount(framelen_rev) == count[frame_id_rev % MAXCACHE]) {// full
				full[frame_id_rev % MAXCACHE] = 1;
				Log.v("full------------",String.valueOf(frame_id_rev % MAXCACHE));
				Log.v("total packs:",String.valueOf(getSliceCount(framelen_rev)));// 预计需要接收到的包的个数
				Log.v("total received packs:",String.valueOf(count[frame_id_rev % MAXCACHE]));// 目前收到的包的个数
			} else {
				return;
			}

		} else if (frameid < 2 * MAXCACHE) {// in C
			while (frameid >= MAXCACHE) {
				// deal with the frame without considering the fufill event
				// todo
				Log.v("frame", String.valueOf(frame_id_rev));
				decode.offerDecoder(array[currentFrameID % MAXCACHE], datalen[currentFrameID % MAXCACHE], framerate);//2015 12 24 TODO 2016.3.3
				if (full[currentFrameID % MAXCACHE] != 1) {
					Log.v("没满!", String.valueOf(currentFrameID));
				}
				init(currentFrameID % MAXCACHE);
				currentFrameID = (currentFrameID + 1) % MAXFRAME;
				frameid = (frame_id_rev + MAXFRAME - currentFrameID) % MAXFRAME;
				Log.v("frame id-current(in c):", String.valueOf(frameid));
			}
			// alloc the buffer
			if (framelen_rev > 0) {
				datalen[frame_id_rev % MAXCACHE] = framelen_rev;
			} else {
				return;
			}
			System.arraycopy(tempbuf_rev, 0, array[frame_id_rev % MAXCACHE], slice_id_rev * MAXDATALEN, slicelen_rev);

			count[frame_id_rev % MAXCACHE]++;
			if (getSliceCount(framelen_rev) == count[frame_id_rev % MAXCACHE]) {// full
				full[frame_id_rev % MAXCACHE] = 1;
			} else {
				return;
			}

		} else {// in A
			// drop it
			// printf("------------------------------------------drop,%d,%d\n",
			// pack->frame_id, pack->frame_len);
			// printf("current,frame %d, drop frame %d\n", currentFrameID,
			// pack->frame_id);
			currentFrameID = frame_id_rev;// to do;
			return;
		}
		// deal with the current frame
		Log.v("todo here", String.valueOf(currentFrameID));
		if (full[currentFrameID % MAXCACHE] == 1) {// TODO
			// pop this frame
			// todo
			Log.v("----- frame", String.valueOf(frame_id_rev));
			Log.v("decode length:",	String.valueOf(datalen[currentFrameID % MAXCACHE]));// 编码时数据的长度
			decode.offerDecoder(array[currentFrameID % MAXCACHE], datalen[currentFrameID % MAXCACHE], framerate);
			if (full[currentFrameID % MAXCACHE] != 1) {
				// printf("没满%d\n", currentFrameID);
				return;
			}
			init(currentFrameID % MAXCACHE);
			currentFrameID = (currentFrameID + 1) % MAXFRAME;

		}
	}

	// /////////////////////////////////////////////////////
	public int getSliceCount(int framelen) {
		if ((framelen % MAXDATALEN) != 0)
			return framelen / MAXDATALEN + 1;
		else
			return framelen / MAXDATALEN;
	}

	// /////////////////////////////////////////////////////
	public void init(int k) {
		for (int i = 0; i < 100000; i++) {
			array[k][i] = 0;
		}
		datalen[k] = 0;
		full[k] = 0;
		count[k] = 0;
	}

//	/**
//	 * 将byte[2]转换成short
//	 *
//	 * @param b
//	 * @return
//	 */
//	public short byte2Short(byte[] b) {
//		return (short) (((b[0] & 0xff) << 8) | (b[1] & 0xff));
//	}
//
//	/**
//	 * byte数组转int
//	 *
//	 * @param b
//	 * @return
//	 */
//	public int byte2Int(byte[] b) {
//		return ((b[0] & 0xff) << 24) | ((b[1] & 0xff) << 16)
//				| ((b[2] & 0xff) << 8) | (b[3] & 0xff);
//	}
}
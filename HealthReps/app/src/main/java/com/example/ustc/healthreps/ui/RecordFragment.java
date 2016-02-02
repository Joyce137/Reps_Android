package com.example.ustc.healthreps.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.TabRecordAdapter;
import com.example.ustc.healthreps.model.PrelistName;
import com.example.ustc.healthreps.model.Users;
import com.example.ustc.healthreps.repo.PrelistContent;
import com.example.ustc.healthreps.serverInterface.ControlMsg;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.serverInterface.Types;
import com.example.ustc.healthreps.socket.Sockets;
import com.example.ustc.healthreps.utils.AppPath;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/*
 * 文件管理类中的记录类
 * 2015/12/12
 * hzy
 */
public class RecordFragment extends Fragment implements OnItemClickListener{
	public static android.os.Handler sPrelistStatusHandler = null;
	
	private MyListView r_list_view;
	private TabRecordAdapter adapter;
	//save all record message
	List<PreList> list = new ArrayList<PreList>();
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_record, null);
        r_list_view = (MyListView) view.findViewById(R.id.RecordlistView);
        r_list_view.setOnItemClickListener(this);
        
		ViewGroup tableTitle = (ViewGroup) view.findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(65, 105, 225));
        init();

		sPrelistStatusHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				PreList preList = (PreList)msg.obj;
				updatePrelist(preList);
			}
		};
        return view;
	}

	//更新清单列表
	public void updatePrelist(PreList preList){
		//添加或更新
		int x = checkPrelistExist(preList);
		if(x == -1){
			list.add(preList);
		}
		else {
			list.set(x,preList);
		}

		String prelistNameStr = "", prelistContentStr = "";
		try {
			prelistNameStr = new String(preList.filename,"GBK").trim();
			prelistContentStr = new String(preList.content,"GBK");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		//更新本地文件
		String prelistPath = AppPath.getPathByFileType("本地文件");
		prelistPath = prelistPath +"/" + Users.sLoginUsername+"/已发清单/"+prelistNameStr;
		File prelistFile = new File(prelistPath);
		if(prelistFile.isFile()&prelistFile.exists()){
			prelistFile.delete();
		}

		Sockets.socket_center.savePrelist(prelistPath,preList);

		adapter.notifyDataSetChanged();// 通知ListView，数据已发生改变
		r_list_view.onRefreshComplete();
	}

	//判断是否在
	int checkPrelistExist(PreList preList){
		String prelistNameStr = "", prelistContentStr = "";
		try {
			prelistNameStr = new String(preList.filename,"GBK").trim();
			prelistContentStr = new String(preList.content,"GBK");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		preList.prelistName = PrelistName.getPrelistFromName(prelistNameStr);
		preList.prelistContent = PrelistContent.getPrelistContentByStr(prelistContentStr);

		for(int i = 0;i<list.size();i++){
			PreList curPrelist = list.get(i);
			String curprelistNameStr = "";
			try {
				curprelistNameStr = new String(curPrelist.filename,"GBK").trim();
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
			if(curprelistNameStr.equals(prelistNameStr)){
				return i;
			}
		}
		return -1;
	}

	private void init() {
		adapter = new TabRecordAdapter(this.getActivity(),list);
		r_list_view.setAdapter(adapter);

		String filePath = AppPath.getPathByFileType("本地文件");
//		filePath = filePath + "/" +Users.sLoginUsername + "/已发清单";
		filePath = filePath + "/" +"patient" + "/已发清单";
		ArrayList<PreList> preLists = Sockets.socket_center.readPrelist(filePath);
		//读取prelist
		String filename="",fileContent="";
		for (PreList curPrelist:preLists) {
			try {
				filename = new String(curPrelist.filename,"GBK");
				fileContent = new String(curPrelist.content,"GBK");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}

			PrelistContent prelistContent = PrelistContent.getPrelistContentByStr(fileContent);
			PrelistName prelistName = PrelistName.getPrelistFromName(filename);
			curPrelist.prelistName = prelistName;
			curPrelist.prelistContent = prelistContent;
			try {
				curPrelist.content = prelistContent.getPrelistcontentStr().getBytes("GBK");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}

			list.add(curPrelist);
		}


		r_list_view.setonRefreshListener(new MyListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				//请求当前用户所有清单最新状态
//				ControlMsg controlMsg = new ControlMsg("",Users.sLoginUsername, Types.PATIENT_REQ_PRELIST_STATUS,false,0);
//				Sockets.socket_center.sendControlMsg(controlMsg);
				//异步刷新
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {

						//请求当前用户所有清单最新状态
						ControlMsg controlMsg = new ControlMsg("",Users.sLoginUsername, Types.PATIENT_REQ_PRELIST_STATUS,false,0);
						Sockets.socket_center.sendControlMsg(controlMsg);

						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						adapter.notifyDataSetChanged();
						r_list_view.onRefreshComplete();
					}
				}.execute(null, null, null);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		PreList record = (PreList) adapter.getItem(position-1);

		Intent intent = new Intent(getActivity(),RecordDetail.class);

		//传递对象
		Bundle bundle = new Bundle();
		bundle.putSerializable("prelist",record);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}

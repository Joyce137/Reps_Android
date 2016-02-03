package com.example.ustc.healthreps.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.adapter.MessHistoryAdapter;
import com.example.ustc.healthreps.model.MessHistory;

import java.util.ArrayList;

/*
 * 文件管理类中的消息管理类
 * 2015/12/12
 * hzy
 */
public class MessageFragment extends Fragment {

	private ListView listView;
	private MessHistoryAdapter messHistoryAdapter = null;
	@Override
	public View onCreateView(LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_message, null);

		return view;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		listView = (ListView)view.findViewById(R.id.listView1);
		messHistoryAdapter = new MessHistoryAdapter(this.getActivity(),getInfo());
		listView.setAdapter(messHistoryAdapter);
		listView.setCacheColorHint(0);
		init();
	}

	public void init(){
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MessHistory messHistory = (MessHistory)messHistoryAdapter.getItem(position);
				Intent i = new Intent(getActivity(),DoctorSessionAty.class);
				i.putExtra("mess_name", messHistory.getName1());
				i.putExtra("mess_time", messHistory.getLastTime());
				startActivity(i);
			}
		});
	}

	//初始化消息记录,后期网络获得
	public ArrayList<MessHistory> getInfo(){
		ArrayList<MessHistory> mhList = new  ArrayList<MessHistory>();
		MessHistory mh1 = new MessHistory();
		mh1.setTxPath(R.drawable.headshow1+"");
		mh1.setName1("i wanto to fly");
		mh1.setLastContent("i am a bird...");
		mh1.setLastTime("12:23");

		MessHistory mh2 = new MessHistory();
		mh2.setTxPath(R.drawable.headshow2+"");
		mh2.setName1("会飞的猪");
		mh2.setLastContent("bye");
		mh2.setLastTime("13:25");

		MessHistory mh3 = new MessHistory();
		mh3.setTxPath(R.drawable.headshow3+"");
		mh3.setName1("邮箱提箱");
		mh3.setLastContent("12306订票通知");
		mh3.setLastTime("14:35");

		MessHistory mh4 = new MessHistory();
		mh4.setTxPath(R.drawable.headshow4+"");
		mh4.setName1("微信支付");
		mh4.setLastContent("微信支付凭证");
		mh4.setLastTime("周五");

		MessHistory mh5 = new MessHistory();
		mh5.setTxPath(R.drawable.headshow5 + "");
		mh5.setName1("红包群");
		mh5.setLastContent("嗯嗯");
		mh5.setLastTime("周三");

		mhList.add(mh1);
		mhList.add(mh2);
		mhList.add(mh3);
		mhList.add(mh4);
		mhList.add(mh5);

		return  mhList;
	}

}

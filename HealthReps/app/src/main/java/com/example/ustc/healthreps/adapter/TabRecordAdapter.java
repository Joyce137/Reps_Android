package com.example.ustc.healthreps.adapter;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.PrelistName;
import com.example.ustc.healthreps.repo.PrelistContent;
import com.example.ustc.healthreps.serverInterface.PreList;
import com.example.ustc.healthreps.utils.Utils;

/*
 * 文件管理之记录界面listview适配器
 * 2015/13/13
 * hzy
 */
public class TabRecordAdapter extends BaseAdapter {

	private List<PreList> list;
	private  LayoutInflater inflater;
	
	public TabRecordAdapter(FragmentActivity fragmentActivity,List<PreList> list){
		this.list = list;
		inflater = LayoutInflater.from(fragmentActivity);
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		PreList record = (PreList) this.getItem(pos);
		ViewHolder viewHolder;
		if(view == null)
		{
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.file_record_list, null);
			viewHolder.mTextTime = (TextView)view.findViewById(R.id.rec_time);
			viewHolder.mTextStore = (TextView)view.findViewById(R.id.rec_store_doc);
			viewHolder.mTextRecord = (TextView)view.findViewById(R.id.rec_mess);
			viewHolder.mTextFlag = (TextView)view.findViewById(R.id.rec_flag);
			view.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.mTextTime.setText(record.prelistName.date);
		viewHolder.mTextStore.setText(record.prelistName.doctorName);
		String prelistContentStr = "";

		try {
			prelistContentStr = new String(record.content,"GBK");
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
		if(prelistContentStr.length() > 5){
			prelistContentStr = prelistContentStr.substring(0,5)+"...";
		}
		viewHolder.mTextRecord.setText(prelistContentStr);
		viewHolder.mTextFlag.setText(Utils.getStatus(record.status));
		return view;
	}
	public static class ViewHolder{
		public TextView mTextTime;
		public TextView mTextStore;
		public TextView mTextRecord;
		public TextView mTextFlag;
	}

}

package com.example.ustc.healthreps.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.FileRecord;

/*
 * 文件管理之记录界面listview适配器
 * 2015/13/13
 * hzy
 */
public class TabRecordAdapter extends BaseAdapter {

	private List<FileRecord> list;
	private  LayoutInflater inflater;
	
	public TabRecordAdapter(FragmentActivity fragmentActivity,List<FileRecord> list){
		this.list = list;
		inflater = LayoutInflater.from(fragmentActivity);
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int pos, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		FileRecord record = (FileRecord) this.getItem(pos);
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
		viewHolder.mTextTime.setText(record.getTime());
		viewHolder.mTextStore.setText(record.getMstore()+"-"+record.getDoctor());
		viewHolder.mTextRecord.setText(record.getRecord());
		viewHolder.mTextFlag.setText(record.getFlag());
		return view;
	}
	public static class ViewHolder{
		public TextView mTextTime;
		public TextView mTextStore;
		public TextView mTextRecord;
		public TextView mTextFlag;
	}

}

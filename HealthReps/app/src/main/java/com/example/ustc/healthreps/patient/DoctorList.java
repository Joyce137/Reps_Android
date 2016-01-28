package com.example.ustc.healthreps.patient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ustc.healthreps.R;
import com.example.ustc.healthreps.model.DocPha;
import com.example.ustc.healthreps.repo.DocPhaRepo;
import com.example.ustc.healthreps.repo.DoctorResult;
import com.example.ustc.healthreps.repo.SearchInfo;

import java.util.ArrayList;

public class DoctorList extends Activity implements OnItemClickListener {

	private ListView d_list_view;
	private ArrayAdapter<DocPha> adapter;
	private ArrayList<DocPha> mDocList;
	private DocPhaRepo mDocRepo = new DocPhaRepo();
	private SearchInfo mSearch = new SearchInfo();
	public static Handler sDocHandler = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doctorlist);
		adapter = new ArrayAdapter<DocPha>(this,
				android.R.layout.simple_list_item_1);

		d_list_view = (ListView) findViewById(R.id.DoctorlistView);
		d_list_view.setAdapter(adapter);

		sDocHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				DoctorResult result = (DoctorResult)msg.obj;
				insertIntoList(result);
			}
		};

		d_list_view.setOnItemClickListener(this);

		//发送
		mDocRepo.searchDoctor(mSearch);
	}

	//将得到的结果插入到list中
	public void insertIntoList(DoctorResult req){

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		DocPha doctor = adapter.getItem(position);

		Intent i = new Intent();
		i.setComponent(new ComponentName("com.joyce.reps",
				"com.joyce.reps.patient.DoctorSessionAty"));
		i.putExtra("doctor_name", doctor.realname);
		i.putExtra("grade_name", doctor.zhicheng);
		startActivity(i);
	}

}

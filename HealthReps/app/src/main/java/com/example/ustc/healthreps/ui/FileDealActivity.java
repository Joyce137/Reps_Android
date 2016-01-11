package com.example.ustc.healthreps.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.ustc.healthreps.R;

/*
 * 文件管理类，包含消息和记录两项
 * 2015/12/12
 * hzy
 */
public class FileDealActivity extends Fragment {
	View view;
	
    private Button btn_message,btn_record;
	
	private RecordFragment recordFragment;
    private MessageFragment messageFragment;
	
	public static final int MESSAGE_FRAGMENT_TYPE = 1;
	public static final int RECORD_FRAGMENT_TYPE = 2;
	public int currentFragmentType = -1;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view  = inflater.inflate(R.layout.filedeal, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initview();
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		if (savedInstanceState != null) {
			int type = savedInstanceState.getInt("currentFragmentType");
			messageFragment = (MessageFragment)fragmentManager.findFragmentByTag("message");
			recordFragment = (RecordFragment)fragmentManager.findFragmentByTag("record");
			if(type > 0)
				loadFragment(type);
		} else {
			FragmentTransaction transaction = fragmentManager
					.beginTransaction();
			Fragment mainFragment = fragmentManager.findFragmentByTag("message");
			if (mainFragment != null) {
				transaction.replace(R.id.fl_content, mainFragment);
				transaction.commit();
			} else {
				loadFragment(MESSAGE_FRAGMENT_TYPE);
			}
		}
	}

	private void initview() {
		// TODO Auto-generated method stub
		btn_message = (Button)view.findViewById(R.id.btn_message);
		btn_record = (Button)view.findViewById(R.id.btn_record);
		btn_message.setOnClickListener(onClicker);
		btn_record.setOnClickListener(onClicker);
		
	}
	private OnClickListener onClicker = new OnClickListener(){
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()){
			case R.id.btn_message:
				btn_message.setTextColor(Color.parseColor("#df3031"));
				btn_record.setTextColor(Color.WHITE);
				btn_message
						.setBackgroundResource(R.drawable.baike_btn_pink_left_f_96);
				btn_record
						.setBackgroundResource(R.drawable.baike_btn_trans_right_f_96);
				switchFragment(MESSAGE_FRAGMENT_TYPE);
				break;
			case R.id.btn_record:
				btn_message.setTextColor(Color.WHITE);
				btn_record.setTextColor(Color.parseColor("#df3031"));
				btn_message
						.setBackgroundResource(R.drawable.baike_btn_trans_left_f_96);
				btn_record
						.setBackgroundResource(R.drawable.baike_btn_pink_right_f_96);
				switchFragment(RECORD_FRAGMENT_TYPE);
				break;
			}
		}
		
	};
	
	private void switchFragment(int type) {
		switch (type) {
		case MESSAGE_FRAGMENT_TYPE:
			loadFragment(MESSAGE_FRAGMENT_TYPE);
			break;
		case RECORD_FRAGMENT_TYPE:
			loadFragment(RECORD_FRAGMENT_TYPE);
			break;
		}
	}
	
	private void loadFragment(int type) {
		FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if (type == RECORD_FRAGMENT_TYPE) {
			if (recordFragment == null) {
				recordFragment = new RecordFragment();

				transaction.add(R.id.fl_content, recordFragment, "record");
			} else {
				transaction.show(recordFragment);
			}
			if (messageFragment != null) {
				transaction.hide(messageFragment);
			}
			currentFragmentType = MESSAGE_FRAGMENT_TYPE;
		} else {
			if (messageFragment == null) {
				messageFragment = new MessageFragment();
				transaction.add(R.id.fl_content, messageFragment, "message");
			} else {
				transaction.show(messageFragment);
			}
			if (recordFragment != null) {
				transaction.hide(recordFragment);
			}
			currentFragmentType = RECORD_FRAGMENT_TYPE;
		}
		transaction.commitAllowingStateLoss();
	}
}

package com.example.ustc.healthreps.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.ustc.healthreps.R;

/*
 * 完整的清单信息界面
 * 2015/12/13
 * hzy
 */
public class RecordDetail extends Activity {
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.record_detail);
		
		init();
	}

	private void init() {

		Intent intent = getIntent();
		String rTime = intent.getStringExtra("record_time");
		String rFlag = intent.getStringExtra("record_flag");
		
	}
}

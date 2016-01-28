package com.example.ustc.healthreps.patient;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.example.ustc.healthreps.R;

import java.util.logging.Handler;

public class State extends Activity {
	public static Handler sFileStateHandler = null;

	//当前list中显示的文件类型
	public static int sFilelistType = 1; //默认为待发清单1
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_state);
	}

}

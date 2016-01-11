package com.example.ustc.healthreps.ui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ustc.healthreps.R;

/*
 * 文件管理类中的消息管理类
 * 2015/12/12
 * hzy
 */
public class MessageFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_message, null);
	}
}

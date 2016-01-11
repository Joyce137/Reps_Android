package com.example.ustc.healthreps.ui;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

import com.example.ustc.healthreps.R;

public class AddRelativeLayout extends RelativeLayout implements OnClickListener{

	private Context context;
	//显示布局
	private View view;
	private EditText et_sendmess;
	private Button btnSend;
	private ImageButton ib_pic,ib_file,ib_audio,ib_video;


	public AddRelativeLayout(Context context) {
		super(context);
		this.context = context;
	}
	public AddRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public AddRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		ib_pic = (ImageButton) findViewById(R.id.ib1);
		ib_file = (ImageButton) findViewById(R.id.ib2);
		ib_audio = (ImageButton) findViewById(R.id.ib3);
		ib_video = (ImageButton) findViewById(R.id.ib4);

		et_sendmess = (EditText) findViewById(R.id.et_sendmessage);
		btnSend = (Button) findViewById(R.id.btn_send);

		findViewById(R.id.btn_other).setOnClickListener(this);
		view = findViewById(R.id.rl_other);

		//打开图片
		ib_pic.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				et_sendmess.setText("已发送……");
				Intent intent = new Intent(getContext(),Picture_select.class);
                getContext().startActivity(intent);
				view.setVisibility(View.GONE);

			}
		});
	}

	/**
	 * 隐藏表情选择框
	 */
	public boolean hideFaceView() {
		// 隐藏表情选择框
		if (view.getVisibility() == View.VISIBLE) {
			view.setVisibility(View.GONE);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.btn_other:
				// 隐藏表情选择框
				if (view.getVisibility() == View.VISIBLE)
					view.setVisibility(View.GONE);
				else
					view.setVisibility(View.VISIBLE);
				break;
			case R.id.et_sendmessage:
				// 隐藏表情选择框
				if (view.getVisibility() == View.VISIBLE)
					view.setVisibility(View.GONE);
				break;
			default :
				break;
		}
	}
}
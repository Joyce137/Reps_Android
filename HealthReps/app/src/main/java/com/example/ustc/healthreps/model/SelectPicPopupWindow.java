package com.example.ustc.healthreps.model;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.ustc.healthreps.R;

/*
 * 问药：选择药品清单，选择药品数量，类似饿了么弹出菜单的实现
 * 2015/12/24
 * hzy
 */
public class SelectPicPopupWindow extends PopupWindow {

	private Button btn_sub, btn_add,btn_sumbit;
	private EditText edt_amount;
	private TextView tv_medName;
	private View mMenuView;

	public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.medic_detail_popup, null);

		init();
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x55000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);

		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}
				return true;
			}
		});
	}

	private void init() {
		btn_sub = (Button)mMenuView.findViewById(R.id.sub);
		btn_add = (Button)mMenuView.findViewById(R.id.add);
		edt_amount = (EditText) mMenuView.findViewById(R.id.amount);
		tv_medName = (TextView) mMenuView.findViewById(R.id.medic_name);
		btn_sumbit = (Button)mMenuView.findViewById(R.id.sumbit);

		//数量减1
		btn_sub.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				int num = Integer.parseInt(edt_amount.getText().toString());
				if(0 == num){
					edt_amount.setText("0");
				}
				else{
					edt_amount.setText(String.valueOf(--num));
				}
			}
		});

		//数量加1
		btn_add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				int num = Integer.parseInt(edt_amount.getText().toString());
				edt_amount.setText(String.valueOf(++num));
			}

		});

		btn_sumbit.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String medName = tv_medName.getText().toString();
				String num = edt_amount.getText().toString();

				dismiss();
			}
		});
	}
}
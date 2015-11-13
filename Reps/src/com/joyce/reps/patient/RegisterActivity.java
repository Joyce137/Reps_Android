package com.joyce.reps.patient;

import com.joyce.reps.R;
import com.joyce.reps.utils.AndroidNetAccess;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
      
/**
 * 注册
 * @author CaoRuijuan
 * 
 */
public class RegisterActivity extends Activity {
	public static Handler sRegisterHandler = null;
	
	//View
	private EditText mUsernameText,mRealNameText,mPwdText,mConfirmPwdText;
	private Button mRegBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		AndroidNetAccess.netAccess();
		
		initView();
	}

	private void initView() {
		mUsernameText = (EditText) findViewById(R.id.Registration_user);
		mRealNameText = (EditText) findViewById(R.id.Registration_name);
		mPwdText = (EditText) findViewById(R.id.Registration_password);
		mConfirmPwdText = (EditText) findViewById(R.id.Registration_password2);
		mRegBtn = (Button)findViewById(R.id.Registration_OK);
	}
}

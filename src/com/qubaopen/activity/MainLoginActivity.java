package com.qubaopen.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;

public class MainLoginActivity extends Activity implements View.OnClickListener {
	/** 电话账号 */
	/***/

	private MainLoginActivity _this;
	private Context context;

	private LinearLayout tencentLogin;
	private LinearLayout wxLogin;
	private LinearLayout sinaLogin;
	private LinearLayout phoneLogin;
	private TextView register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_login);
		_this = this;
		initView();
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		tencentLogin = (LinearLayout) findViewById(R.id.layout_tencent_login);
		tencentLogin.setOnClickListener(this);
		wxLogin = (LinearLayout) findViewById(R.id.layout_wx_login);
		wxLogin.setOnClickListener(this);
		sinaLogin = (LinearLayout) findViewById(R.id.layout_sina_login);
		sinaLogin.setOnClickListener(this);
		phoneLogin = (LinearLayout) findViewById(R.id.layout_phone_login);
		phoneLogin.setOnClickListener(this);
		register = (TextView) findViewById(R.id.txt_regiset);
		register.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.layout_tencent_login:

			v.setEnabled(true);
			break;
		case R.id.layout_wx_login:

			v.setEnabled(true);
			break;
		case R.id.layout_sina_login:

			v.setEnabled(true);
			break;
		case R.id.layout_phone_login:
			intent = new Intent(_this, PhoneLoginActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.txt_regiset:
			intent = new Intent(_this, RegistPhoneActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;

		}

	}
}

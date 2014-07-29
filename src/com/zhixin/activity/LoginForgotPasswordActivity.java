package com.zhixin.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.datasynservice.LoginForgotPasswordService;
import com.zhixin.datasynservice.LoginForgotPhoneService;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.utils.MatcherUtil;

/**
 * Created by duel on 14-3-20.
 */
public class LoginForgotPasswordActivity extends FragmentActivity implements
		View.OnClickListener {

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	
	private TextView regist_confirm_password;
	
	private TextView txtInputPwd1;
	private TextView txtInputPwd2;
	private TextView txtInputToken;
	private ImageButton btnClearText;
	// private Button btnConfirm;
	// private TextView txtRemainTime;
	private int recLen = 60;
	private String recTime = "";
	// private TextView txtResend;
	// 获取验证码
	private ImageButton im_retrieve_code;
	private TextView tv_retrieve_code;

	private String phoneFromPreview;
	private LoginForgotPasswordService loginForgotPasswordService;

	private LoginForgotPasswordActivity _this;

	// 。。。。。。。。

	private LoginForgotPhoneService loginForgotPhoneService;

	// private Button btnNext;
	private EditText txtInputPhone;
	private ImageButton btnClearText1;
	private String phone = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_forgot_password);
		_this = this;
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_set_pwd));
		txtInputPwd1 = (TextView) this.findViewById(R.id.txtInputPwd1);
		txtInputPwd2 = (TextView) this.findViewById(R.id.txtInputPwd2);
		txtInputToken = (TextView) this.findViewById(R.id.txtInputToken);
		
		regist_confirm_password = (TextView) findViewById(R.id.regist_confirm_password);
		
		// txtRemainTime = (TextView) this.findViewById(R.id.txtRemainTime);
		// txtResend = (TextView) this.findViewById(R.id.txtResend);
		btnClearText = (ImageButton) this.findViewById(R.id.btnClearText);
		// txtResend.setOnClickListener(this);
		btnClearText.setOnClickListener(this);
		// btnConfirm.setOnClickListener(this);
		// txtResend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// txtResend.setTextColor(this.getResources().getColor(R.color.text_grey));
		// txtResend.setEnabled(false);
		// 获取验证码
		im_retrieve_code = (ImageButton) findViewById(R.id.im_retrieve_code);
		im_retrieve_code.setOnClickListener(this);
		tv_retrieve_code = (TextView) findViewById(R.id.tv_retrieve_code);

//		 loginForgotPasswordService = new LoginForgotPasswordService(this);
//		 phoneFromPreview = getIntent().getStringExtra("phone");
//		 handler.postDelayed(runnable, 1000);

		// 。。。。。。。
		// btnNext = (Button) this.findViewById(R.id.btnNext);
		// btnNext.setOnClickListener(this);
		txtPageTitle.setText(this
				.getString(R.string.head_title_activity_regist_phone));

		txtInputPhone = (EditText) this.findViewById(R.id.txtInputPhone);
		btnClearText1 = (ImageButton) this.findViewById(R.id.btnClearText1);
		btnClearText1.setOnClickListener(this);
		loginForgotPhoneService = new LoginForgotPhoneService(this);

	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			recLen--;
			if (recLen < 10) {
				recTime = "00:0" + recLen;
			} else {
				recTime = "00:" + recLen;
			}
			tv_retrieve_code.setText(recTime);
			handler.postDelayed(this, 1000);
			if (recLen == 0) {
				handler.removeCallbacks(runnable);
				// txtResend.setTextColor(_this.getResources().getColor(
				// R.color.text_blue));
				// txtResend.setEnabled(true);
				recLen = 60;
				im_retrieve_code.setEnabled(true);
				tv_retrieve_code.setText("重新发送");

			}
		}
	};

	@Override
	public void onClick(View v) {

		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		// 获取验证码
		case R.id.im_retrieve_code:
			sendCode();
			handler.postDelayed(runnable, 1000);
			im_retrieve_code.setEnabled(false);
			break;
		// case R.id.txtResend:
		// showToast("重新发送验证码");
		// txtResend.setTextColor(this.getResources().getColor(
		// R.color.text_grey));
		// recLen = 60;
		// handler.postDelayed(runnable, 1000);
		// break;
		case R.id.btnClearText:
			txtInputToken.setText("");
			v.setEnabled(true);
			break;
		// case R.id.btnClearText1:
		// txtInputPhone.setText("");
		case R.id.regist_confirm_password:
			sendRequest();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 修改密码
	 * @author Administrator
	 *
	 */
	private class LoadDataTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				return loginForgotPhoneService.getForgotToken(params[0]);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				return loginForgotPasswordService.setNewPwd(params[0],
						params[1], params[2]);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(JSONObject jbo) {
			if (jbo != null) {
				try {
					if (jbo.has("success")
							&& jbo.getString("success").equals("1")) {
						Toast.makeText(
								_this,
								_this.getString(R.string.toast_forget_password_and_modify_it_successfully),
								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(_this, LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						_this.finish();

					} else if (jbo.getString("success").equals("0")) {
						String context = ErrHashMap.getErrMessage(jbo
								.getString("message"));
						context = context == null ? _this
								.getString(R.string.toast_unknown) : context;
						String errMsg = jbo.getString("message");
						Toast.makeText(_this, errMsg, 5).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			// btnConfirm.setEnabled(true);
		}

	}
	/**
	 * 发送验证码
	 */
	private void sendCode(){
		if (txtInputPhone.getText() == null) {
			showToast(this.getString(R.string.logon_toast_phone_empty));
			return;
		}
		phone = txtInputPhone.getText().toString();
		if (phone.equals("")) {
			showToast(this.getString(R.string.logon_toast_phone_empty));
			return;
		}
		if (!MatcherUtil.validateMobile(phone)) {
			showToast(this
					.getString(R.string.logon_toast_phone_format_incorrect));
			return;
		}
		new LoadDataTask().execute(phone);
	}
	/**
	 * 修改密码
	 */
	private void sendRequest() {
		// 。。。。。。。
		if (txtInputPwd1.getText() == null || txtInputPwd2.getText() == null) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		String sPwd1 = txtInputPwd1.getText().toString().trim();
		String sPwd2 = txtInputPwd2.getText().toString().trim();
		if (sPwd1.equals("") || sPwd2.equals("")) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		if (!MatcherUtil.validatePwd(sPwd1) || !MatcherUtil.validatePwd(sPwd2)) {
			showToast(this.getString(R.string.toast_more_reset_wrong_input_pwd));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}

		if (!sPwd1.equals(sPwd2)) {
			showToast(this.getString(R.string.toast_more_reset_not_same));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}

		if (txtInputToken.getText() == null) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		String sToken = txtInputToken.getText().toString().trim();
		if (sToken.equals("")) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}

		new LoadDataTask().execute(phone, sPwd1, sToken);

	}

	private void showToast(String content) {
		Toast.makeText(_this, content, 3).show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
}

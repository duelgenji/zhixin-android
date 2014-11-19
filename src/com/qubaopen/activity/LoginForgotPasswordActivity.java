package com.qubaopen.activity;

import java.text.ParseException;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
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
import com.qubaopen.R;
import com.qubaopen.datasynservice.LoginForgotPasswordService;
import com.qubaopen.datasynservice.LoginForgotPhoneService;
import com.qubaopen.settings.ErrHashMap;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.MatcherUtil;

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
	private int recLen = 60;
	private String recTime = "";
	// 获取验证码
	private ImageButton im_retrieve_code;
	private TextView tv_retrieve_code;

	private Context context;

	// private String phoneFromPreview;
	private LoginForgotPasswordService loginForgotPasswordService;

	private LoginForgotPasswordActivity _this;
	// 。。。。。。。。
	private LoginForgotPhoneService loginForgotPhoneService;
	// private Button btnNext;
	private EditText txtInputPhone;
	private ImageButton btnClearText1;
	private String phone = null;
	private String password = null;
	private String sPwd2 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_forgot_password);
		_this = this;
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_forget_pwd));
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		regist_confirm_password = (TextView) findViewById(R.id.regist_submit);
		regist_confirm_password.setOnClickListener(this);

		txtInputPhone = (EditText) this.findViewById(R.id.txtInputPhone);
		btnClearText1 = (ImageButton) this.findViewById(R.id.btnClearText1);
		btnClearText1.setOnClickListener(this);
		txtInputPwd1 = (TextView) this.findViewById(R.id.txtInputPwd1);
		txtInputPwd2 = (TextView) this.findViewById(R.id.txtInputPwd2);
		// 填写验证码
		txtInputToken = (TextView) this.findViewById(R.id.txtInputToken);
		// 清除验证码
		btnClearText = (ImageButton) this.findViewById(R.id.btnClearText);
		btnClearText.setOnClickListener(this);
		// 获取验证码
		tv_retrieve_code = (TextView) findViewById(R.id.tv_retrieve_code);
		im_retrieve_code = (ImageButton) findViewById(R.id.im_retrieve_code);
		im_retrieve_code.setOnClickListener(this);

		loginForgotPhoneService = new LoginForgotPhoneService(this);
		context = this.getApplicationContext();
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
				recLen = 60;
				im_retrieve_code.setEnabled(true);
				tv_retrieve_code.setText("重新发送");

			}
		}
	};

	@Override
	public void onClick(View v) {

		phone = txtInputPhone.getText().toString().trim();
		password = txtInputPwd1.getText().toString().trim();
		sPwd2 = txtInputPwd2.getText().toString().trim();
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		// 获取验证码
		case R.id.im_retrieve_code:

			if (phone == null && phone.equals("")) {
				showToast(this.getString(R.string.login_toast_phone_empty));
				return;
			}

			if (txtInputPwd1.getText() == null
					|| txtInputPwd2.getText() == null) {
				showToast(this.getString(R.string.login_toast_password_empty));
				im_retrieve_code.setEnabled(true);
				regist_confirm_password.setEnabled(true);
				return;
			}

			if (password.equals("") || sPwd2.equals("")) {
				showToast(this.getString(R.string.login_toast_password_empty));
				im_retrieve_code.setEnabled(true);
				regist_confirm_password.setEnabled(true);
				return;
			}

			if (!password.equals(sPwd2)) {
				showToast(this.getString(R.string.toast_more_reset_not_same));
				im_retrieve_code.setEnabled(true);
				return;
			}

			if (MatcherUtil.validateMobile(phone)) {

				if (MatcherUtil.validatePassword(password)
						|| MatcherUtil.validatePwd(sPwd2)) {
					try {
						sendCode(phone);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					// 密码格式不正确
					Toast.makeText(
							_this,
							getString(R.string.login_toast_password_format_incorrect),
							5).show();
				}

			} else {
				// 手机格式不正确
				Toast.makeText(_this,
						getString(R.string.login_toast_phone_format_incorrect),
						5).show();
			}
			v.setEnabled(true);
			break;
		case R.id.btnClearText:
			txtInputToken.setText("");
			v.setEnabled(true);
			break;
		case R.id.btnClearText1:
			txtInputPhone.setText("");
			v.setEnabled(true);
		case R.id.regist_submit:

			if (txtInputToken.getText() == null) {
				showToast(this.getString(R.string.toast_input_cant_be_null));
				regist_confirm_password.setEnabled(true);
				return;
			}
			String captcha = txtInputToken.getText().toString().trim();
			if (captcha.equals("")) {
				showToast(this.getString(R.string.toast_input_cant_be_null));

				regist_confirm_password.setEnabled(true);

				return;
			}

			if (MatcherUtil.validateMobile(phone)) {

				if (MatcherUtil.validatePassword(password)
						|| MatcherUtil.validatePwd(sPwd2)) {
					try {
						sendRequest(phone, password, captcha);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					// 密码格式不正确
					Toast.makeText(
							_this,
							getString(R.string.login_toast_password_format_incorrect),
							5).show();
				}

			} else {
				// 手机格式不正确
				Toast.makeText(_this,
						getString(R.string.login_toast_phone_format_incorrect),
						5).show();
			}

			v.setEnabled(true);
			break;
		default:
			break;
		}
	}

	public JSONObject sendCode(String phone) throws ParseException {
		JSONObject result = new JSONObject();
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
		requestUrl += "?phone=" + phone + "&activated=" + true;
		new LoadDataTask().execute(1, requestUrl, null, HttpClient.TYPE_GET);
		return result;
	}

	public JSONObject sendRequest(String phone, String password, String captcha)
			throws ParseException {

		JSONObject result = new JSONObject();
		AjaxParams obj = new AjaxParams();
		obj.put("phone", phone);
		obj.put("password", password);
		obj.put("captcha", captcha);
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_LOGIN_FORGOT_SET_NEW_PWD);
		new LoadDataTask().execute(2, requestUrl, obj,
				HttpClient.TYPE_POST_NORMAL);
		return result;
	}

	private void showToast(String content) {
		Toast.makeText(_this, content, 3).show();
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

	// new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_POST);
	// 参数0——此actuvuty调的第几个后台接口.1——连接后台的Url.2.3
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					// null。。。。传参方式是get
					// (Integer)params[3]对应上面的HttpClient.TYPE_GET
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					result.put("syncType", syncType);
					break;
				case 2:
					// (JSONObject)params[2]。。。Json解析，post方式
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result != null && result.getInt("success") == 1) {
						Toast.makeText(_this,
								getString(R.string.retrieve_code_already_send),
								Toast.LENGTH_SHORT).show();
						handler.postDelayed(runnable, 1000);
					} else {
						Toast.makeText(_this, getString(R.string.request_fail),
								Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					if (result != null
							&& result.getString("success").equals("1")) {
						Toast.makeText(
								_this,
								_this.getString(R.string.toast_modify_password_successfully),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(_this, MainActivity.class);
						startActivity(intent);
						finish();
					} else if (result.getString("success").equals("0")) {
						String content = ErrHashMap.getErrMessage(result
								.getString("message"));
						Toast.makeText(_this, content, 5).show();
					}
					break;
				default:
					break;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

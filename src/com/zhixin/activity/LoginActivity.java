package com.zhixin.activity;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.common.RequestLogic;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.ImeiUtils;
import com.zhixin.utils.MatcherUtil;

public class LoginActivity extends Activity implements View.OnClickListener{
	/**电话账号*/
	private EditText phone;
	/**密码*/
	private EditText password;
	/**登陆按钮*/
	private ImageButton btnLogin;
	/**注册按钮*/
	private ImageButton btnRegister;
	/**忘记密码*/
	private TextView txtForgot;
	/**吐司电话账号为空*/
	private Toast phoneEmptyToast;
	/**吐司无效的登陆*/
	private Toast invalidLogonToast;
	/**吐司请求失败*/
	private Toast requestFailToast;
	/**用户信息Dao*/
	private UserInfoDao userInfoDao;
	/***/
	private ImageView imgRegisterTips;

	private LoginActivity _this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_login);
		_this = this;
		userInfoDao = new UserInfoDao();

		phone = (EditText) this.findViewById(R.id.phone);
		password = (EditText) this.findViewById(R.id.password);

		btnRegister = (ImageButton) this.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);

//		imgRegisterTips = (ImageView) this.findViewById(R.id.imgRegisterTips);
//		imgRegisterTips.setOnClickListener(this);

		txtForgot = (TextView) this.findViewById(R.id.txtForgot);
		txtForgot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtForgot.setOnClickListener(this);

		btnLogin = (ImageButton) this.findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
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

	public void loginAction() {

		if (StringUtils.isEmpty(phone.getText())) {

			if (phoneEmptyToast == null) {
				phoneEmptyToast = Toast.makeText(this, getResources()
						.getString(R.string.logon_toast_phone_empty), 3);

			}
			phoneEmptyToast.show();
			btnLogin.setEnabled(true);
			return;
		}

		if (StringUtils.isEmpty(password.getText())) {

			Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
			btnLogin.setEnabled(true);
			return;
		}

		final String logonUrl = SettingValues.URL_PREFIX
				+ getResources().getString(R.string.URL_USER_LOGON);

		final String phoneStr = phone.getText().toString();
		final String passwordStr = password.getText().toString();
		if (!MatcherUtil.validateMobile(phoneStr)) {
			Toast.makeText(
					this,
					getResources().getString(
							R.string.logon_toast_phone_format_incorrect), 3)
					.show();
			btnLogin.setEnabled(true);
			return;
		}

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", phoneStr);
			jsonParams.put("pwd", passwordStr);
			String code = ImeiUtils.getImeiCode(this);
			if (code != null) {
				jsonParams.put("imei", code);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		HttpClient.request(logonUrl, jsonParams, new RequestLogic() {

			@Override
			public void onLoading(long count, long current) {

			}
//登陆成功
			@Override
			public void whenSuccess(final JSONObject result) {

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						CurrentUserHelper.saveCurrentPhone(phoneStr);
						try {
							userInfoDao.saveUserForFirsttime(result,
									passwordStr);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void params) {
						Intent intent = new Intent(_this, MainActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						_this.finish();

					}
				}.execute();

			}
//登陆失败
			@Override
			public void whenFail(JSONObject message) {

				try {
					String errMessage = message.getString("message");
					if (StringUtils.isAsciiPrintable(errMessage)) {
						errMessage = ErrHashMap.getErrMessage(message
								.getString("message"));
					}

					Toast.makeText(_this, errMessage, Toast.LENGTH_SHORT)
							.show();
				} catch (JSONException e) {
					e.printStackTrace();
				} finally {

					btnLogin.setEnabled(true);
				}

			}
//			如果是请求失败就吐司
			@Override
			public void whenRequestFail(String errcode) {
				if (requestFailToast == null) {
					requestFailToast = Toast.makeText(
							LoginActivity.this,
							getResources().getString(
									R.string.toast_request_fail), 5);
				}
				requestFailToast.show();
				btnLogin.setEnabled(true);
			}
		});

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btnRegister:
//		case R.id.imgRegisterTips:
			v.setEnabled(false);
			intent = new Intent(this, RegistPhoneActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.txtForgot:
			v.setEnabled(false);
			intent = new Intent(this, LoginForgotPasswordActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.btnLogin:
			v.setEnabled(false);
			loginAction();
			break;
		default:
			break;

		}

	}
}

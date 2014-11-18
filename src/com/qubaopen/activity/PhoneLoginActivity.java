package com.qubaopen.activity;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.MatcherUtil;

public class PhoneLoginActivity extends Activity implements
		View.OnClickListener {

	private ImageButton btnBack;
	private TextView title;
	/** 电话账号 */
	private EditText phoneStr;
	/** 密码 */
	private EditText passwordStr;
	/** 登陆按钮 */
	private ImageButton btnLogin;
	/** 忘记密码 */
	private TextView txtForgot;
	/** 吐司电话账号为空 */
	private Toast phoneEmptyToast;
	/** 用户信息Dao */
	private UserInfoDao userInfoDao;
	/***/

	private PhoneLoginActivity _this;
	private Context context;

	private String phone;
	private ImageButton phoneClear;
	private String password;
	private ToggleButton showPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.phone_login);
		_this = this;
		userInfoDao = new UserInfoDao();

		initView();
	}

	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.backup_btn);
		btnBack.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title_of_the_page);
		title.setText(getString(R.string.login_title));
		phoneStr = (EditText) findViewById(R.id.phone);
		phoneClear = (ImageButton) findViewById(R.id.btn_clear);
		phoneClear.setOnClickListener(this);
		passwordStr = (EditText) findViewById(R.id.password);
		showPassword = (ToggleButton) findViewById(R.id.btn_show_pwd);
		showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					passwordStr.setInputType(InputType.TYPE_CLASS_TEXT);
				} else {
					passwordStr.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});

		txtForgot = (TextView) this.findViewById(R.id.txtForgot);
		txtForgot.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		txtForgot.setOnClickListener(this);

		context = this.getApplicationContext();

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

	public JSONObject loginAction(String phone, String password)
			throws ParseException {
		JSONObject result = new JSONObject();
		JSONObject obj = new JSONObject();
		try {
			obj.put("phone", phone);
			obj.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_USER_LOGON);

		new LoadDataTask().execute(1, requestUrl, obj,
				HttpClient.TYPE_POST_FORM);
		return result;
	}

	// new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_POST);
	// 参数0——此actuvuty调的第几个后台接口.1——连接后台的Url.2.3
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
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
						// 。。。。。。。。。
						long userId = result.getLong("userId");
						CurrentUserHelper.saveCurrentUserId(userId);

						CurrentUserHelper.saveCurrentPhone(phone);
						CurrentUserHelper.saveCurrentUserId(userId);
						try {
							userInfoDao.saveUserForFirsttime(result, password);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Toast.makeText(_this,
								getString(R.string.login_toast_success),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(_this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(
								_this,
								getString(R.string.login_toast_invalid_password_or_phone),
								Toast.LENGTH_SHORT).show();
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

	@Override
	public void onClick(View v) {
		Intent intent;
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.btn_clear:
			phoneStr.setText("");
			v.setEnabled(true);
			break;
		case R.id.txtForgot:
			intent = new Intent(this, LoginForgotPasswordActivity.class);
			startActivity(intent);
			finish();
			v.setEnabled(true);
			break;
		case R.id.btnLogin:
			phone = phoneStr.getText().toString();
			password = passwordStr.getText().toString();
			if (StringUtils.isEmpty(phone)) {

				if (phoneEmptyToast == null) {
					phoneEmptyToast = Toast.makeText(this, getResources()
							.getString(R.string.login_toast_phone_empty), 3);
				}
				phoneEmptyToast.show();
				btnLogin.setEnabled(true);
				return;
			}

			if (StringUtils.isEmpty(password)) {

				Toast.makeText(this,
						getString(R.string.login_toast_password_empty),
						Toast.LENGTH_SHORT).show();
				btnLogin.setEnabled(true);
				return;
			}
			if (MatcherUtil.validateMobile(phone)) {
				if (MatcherUtil.validatePassword(password)) {
					try {
						loginAction(phone, password);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					// 密码格式不正确
					Toast.makeText(
							_this,
							getString(R.string.login_toast_password_format_incorrect),
							Toast.LENGTH_SHORT).show();
					btnLogin.setEnabled(true);
				}
			} else {
				// 手机格式不正确
				Toast.makeText(_this,
						getString(R.string.login_toast_phone_format_incorrect),
						Toast.LENGTH_SHORT).show();
				btnLogin.setEnabled(true);
			}
			// btnRegister.setEnabled(false);
			btnLogin.setEnabled(true);
			break;
		default:
			break;

		}

	}
}

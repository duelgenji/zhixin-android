package com.zhixin.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
	private EditText phoneStr;
	/**密码*/
	private EditText passwordStr;
	/**登陆按钮*/
	private ImageButton btnLogin;
	/**注册按钮*/
	private ImageButton btnRegister;
	/**忘记密码*/
	private TextView txtForgot;
	/**吐司电话账号为空*/
	private Toast phoneEmptyToast;
	/**吐司无效的登陆*/
//	private Toast invalidLogonToast;
	/**吐司请求失败*/
	private Toast requestFailToast;
	/**用户信息Dao*/
	private UserInfoDao userInfoDao;
	/***/
//	private ImageView imgRegisterTips;

	private LoginActivity _this;
	
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_login);
		_this = this;
		userInfoDao = new UserInfoDao();

		phoneStr = (EditText) this.findViewById(R.id.phone);
		passwordStr = (EditText) this.findViewById(R.id.password);

		btnRegister = (ImageButton) this.findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);

//		imgRegisterTips = (ImageView) this.findViewById(R.id.imgRegisterTips);
//		imgRegisterTips.setOnClickListener(this);

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

	
	
	
	public JSONObject loginAction(String phone,String password) throws ParseException {
        JSONObject result=new JSONObject();
        JSONObject obj = new JSONObject();
        //String sobj="";
        try {
        	obj.put("phone", phone);
        	obj.put("password", password);
        	//sobj+="{\"phone\":\"13621673989\",\"password\":\"123456aa\"}";
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
        String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_USER_LOGON);
        
        Log.i(SettingValues.URL_PREFIX, obj.toString());
        
        new LoadDataTask().execute(1,requestUrl,obj,HttpClient.TYPE_PUT);
		return result;
	}
	
	 // new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_POST);
    //参数0——此actuvuty调的第几个后台接口.1——连接后台的Url.2.3
    private class LoadDataTask extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result=null;
			Integer syncType=(Integer)params[0];
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST

					result = HttpClient.requestSync(params[1].toString(), (JSONObject)params[2],(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				default :
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
				Integer syncType=result.getInt("syncType");
				switch(syncType){
				case 1:
					if (result != null && result.getInt("success") == 1) {
		                //。。。。。。。。。
						Toast.makeText(_this, "登陆成功", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(_this,MainActivity.class);
						startActivity(intent);
					}else {
						Toast.makeText(_this, "账号或密码有误！", Toast.LENGTH_SHORT).show();
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
	
	
	
	public void loginAction() {

		if (StringUtils.isEmpty(phoneStr.getText())) {

			if (phoneEmptyToast == null) {
				phoneEmptyToast = Toast.makeText(this, getResources()
						.getString(R.string.logon_toast_phone_empty), 3);

			}
			phoneEmptyToast.show();
			btnLogin.setEnabled(true);
			return;
		}

		if (StringUtils.isEmpty(passwordStr.getText())) {

			Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
			btnLogin.setEnabled(true);
			return;
		}

		final String logonUrl = SettingValues.URL_PREFIX
				+ getResources().getString(R.string.URL_USER_LOGON);

		final String phone = phoneStr.getText().toString();
		final String password = passwordStr.getText().toString();
		if (!MatcherUtil.validateMobile(phone)) {
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
						CurrentUserHelper.saveCurrentPhone(phone);
						try {
							userInfoDao.saveUserForFirsttime(result,
									password);
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
			String phone = phoneStr.getText().toString();
			String password = passwordStr.getText().toString();
			if (StringUtils.isEmpty(phone)) {

				if (phoneEmptyToast == null) {
					phoneEmptyToast = Toast.makeText(this, getResources()
							.getString(R.string.logon_toast_phone_empty), 3);
				}
				phoneEmptyToast.show();
				btnLogin.setEnabled(true);
				return;
			}

			if (StringUtils.isEmpty(password)) {

				Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
				btnLogin.setEnabled(true);
				return;
			}
			
			
			
			
		if(MatcherUtil.validateMobile(phone)){	
			if(MatcherUtil.validatePassword(password)){
				try {
					loginAction(phone,password);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				//密码格式不正确
				Toast.makeText(_this, "密码格式有误，密码至少8位,且只能包含字母或者数字和_", 5).show();
				btnLogin.setEnabled(true);
			}
		}else {
			
			//手机格式不正确
			Toast.makeText(_this,"您填写的手机号码错误", 5).show();
			btnLogin.setEnabled(true);
		}	
//		btnRegister.setEnabled(false);	
			
		
			
		btnLogin.setEnabled(true);
			
//			if (!MatcherUtil.validateMobile(phone)) {
//				Toast.makeText(this,getResources().getString(R.string.logon_toast_phone_format_incorrect), 3).show();
//				btnLogin.setEnabled(true);
//				return;
//			}
			
			break;
		default:
			break;

		}

	}
}

package com.zhixin.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
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
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
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
	
	private Context context;

//	private String phoneFromPreview;
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
		phone = txtInputPhone.getText().toString();
		password = txtInputPwd1.getText().toString().trim();
		sPwd2 = txtInputPwd2.getText().toString().trim();
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		// 获取验证码
		case R.id.im_retrieve_code:
			
			
			
			
			
			if (phone == null && phone.equals("")) {
				showToast(this.getString(R.string.logon_toast_phone_empty));
				return;
			}
//			if (phone.equals("")) {
//				showToast(this.getString(R.string.logon_toast_phone_empty));
//				return;
//			}
//			if (!MatcherUtil.validateMobile(phone)) {
//				showToast(this
//						.getString(R.string.logon_toast_phone_format_incorrect));
//				return;
//			}
			if (txtInputPwd1.getText() == null || txtInputPwd2.getText() == null) {
				showToast(this.getString(R.string.toast_input_cant_be_null));
				im_retrieve_code.setEnabled(true);
				regist_confirm_password.setEnabled(true);
				return;
			}
			
			if (password.equals("") || sPwd2.equals("")) {
				showToast(this.getString(R.string.toast_input_cant_be_null));
				im_retrieve_code.setEnabled(true);
				regist_confirm_password.setEnabled(true);
				return;
			}
	//		if (!MatcherUtil.validatePwd(password) || !MatcherUtil.validatePwd(sPwd2)) {
	//			showToast(this.getString(R.string.toast_more_reset_wrong_input_pwd));
	//			im_retrieve_code.setEnabled(true);
	//			regist_confirm_password.setEnabled(true);
	//			return;
	//		}

			if (!password.equals(sPwd2)) {
				showToast(this.getString(R.string.toast_more_reset_not_same));
				im_retrieve_code.setEnabled(true);
				
				
				return;
			}
			
			
			
			if(MatcherUtil.validateMobile(phone)){
				
					if(MatcherUtil.validatePassword(password) || MatcherUtil.validatePwd(sPwd2)){
						try {
							sendCode(phone, password);
							handler.postDelayed(runnable, 1000);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else{
						//密码格式不正确
						Toast.makeText(_this, "密码格式有误，密码至少8位,且只能包含字母或者数字和_", 5).show();
					}
				
			}else{
				//手机格式不正确
				Toast.makeText(_this,"您填写的手机号码错误", 5).show();
			}
			
			
			im_retrieve_code.setEnabled(true);
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
		case R.id.btnClearText1:
			txtInputPhone.setText("");
		case R.id.regist_confirm_password:
			
			
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
			
			try {
				sendRequest(phone, password, captcha);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			regist_confirm_password.setEnabled(true);
//			Intent intent = new Intent(_this, LoginActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
//修改密码
	 
	private class LoadDataTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(String... params) {

			try {
				return loginForgotPhoneService.getForgotToken(params[0]);
			} catch (ParseException e1) {
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
//修改密码成功的提示和跳转						
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


//	  发送验证码

	private void sendCode(){
		phone = txtInputPhone.getText().toString();
		password = txtInputPwd1.getText().toString().trim();
		sPwd2 = txtInputPwd2.getText().toString().trim();
		if (phone == null) {
			showToast(this.getString(R.string.logon_toast_phone_empty));
			return;
		}
		if (phone.equals("")) {
			showToast(this.getString(R.string.logon_toast_phone_empty));
			return;
		}
		if (!MatcherUtil.validateMobile(phone)) {
			showToast(this
					.getString(R.string.logon_toast_phone_format_incorrect));
			return;
		}
		if (txtInputPwd1.getText() == null || txtInputPwd2.getText() == null) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		
		if (password.equals("") || sPwd2.equals("")) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		if (!MatcherUtil.validatePwd(password) || !MatcherUtil.validatePwd(sPwd2)) {
			showToast(this.getString(R.string.toast_more_reset_wrong_input_pwd));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}

		if (!password.equals(sPwd2)) {
			showToast(this.getString(R.string.toast_more_reset_not_same));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
//		new LoadDataTask().execute(phone);
		
	}
	
	 */
	 
	public JSONObject sendCode(String phone,String password) throws ParseException {
        JSONObject result=new JSONObject();
        JSONObject obj = new JSONObject();
        try {
			obj.put("phone", phone);
			obj.put("password", password);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
        requestUrl+="?phone="+phone;
		new LoadDataTask().execute(1,requestUrl,obj,HttpClient.TYPE_GET);
		return result;
	}
	/**
	 * 判断密码、验证码是否为空或合法
	 */
	private void sendRequest() {
		// 。。。。。。。
		if (txtInputPwd1.getText() == null || txtInputPwd2.getText() == null) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		String password = txtInputPwd1.getText().toString().trim();
		String sPwd2 = txtInputPwd2.getText().toString().trim();
		if (password.equals("") || sPwd2.equals("")) {
			showToast(this.getString(R.string.toast_input_cant_be_null));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}
		if (!MatcherUtil.validatePwd(password) || !MatcherUtil.validatePwd(sPwd2)) {
			showToast(this.getString(R.string.toast_more_reset_wrong_input_pwd));
			
			regist_confirm_password.setEnabled(true);
			
			return;
		}

		if (!password.equals(sPwd2)) {
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

//		new LoadDataTask().execute(phone, sPwd1, sToken);

	}
	public JSONObject sendRequest(String phone,String password,String captcha) throws ParseException {
	
		JSONObject result=new JSONObject();
        JSONObject obj = new JSONObject();
        try {
			obj.put("phone", phone);
			obj.put("password", password);
			obj.put("captcha", captcha);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_LOGIN_FORGOT_SET_NEW_PWD);
		new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_POST);
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
					result = HttpClient.requestSync(params[1].toString(), null,(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				case 2:
					//(JSONObject)params[2]。。。Json解析，post方式
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
						Toast.makeText(_this, "验证码已发送！", Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					if (result != null && result.getInt("success") == 1) {
		                //。。。。。。。。。
						Log.i("login","request");
						Toast.makeText(
								_this,
								_this.getString(R.string.toast_forget_password_and_modify_it_successfully),
								Toast.LENGTH_SHORT).show();
		 //不知道这个地方是不是用这个               
					}else if (result.getString("success").equals("0")) {
						String context = ErrHashMap.getErrMessage(result
								.getString("message"));
						context = context == null ? _this
								.getString(R.string.toast_unknown) : context;
						String errMsg = result.getString("message");
						Toast.makeText(_this, errMsg, 5).show();
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

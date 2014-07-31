package com.zhixin.activity;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.common.RequestLogic;
import com.zhixin.datasynservice.RegistService;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.MatcherUtil;

public class RegistPhoneActivity extends Activity implements View.OnClickListener  {
	// component in layout
	private EditText txtPhone;
	private ToggleButton checkAgreement;
    private TextView txtAgreeTips;
    private TextView txtAgreePrivacyTips;

    private ImageButton btnClearText;
    private RegistPhoneActivity _this;
	// toast
	private Toast iDontAgreeToast;
	private Toast phoneInvalidToast;
	private Toast requestFailRespondToast;
	
	//获取验证码
	private ImageButton ib_get_reg_code;
	//倒计时
	private TextView reg_code_time;
//	。。。。。。。。。。
//	/**验证码接受的时间*/
//	private TextView countDownTimerText;
	/**页面的名称*/
	private TextView txtPageTitle;
	/**验证码输入框*/
	private EditText validateCodeEditText;
	/**重新发送验证码*/
//	private TextView txtResend;
//	/**下一步*/
//	private Button nextBtn;
	private String phone;
	/**上一页*/
	private ImageButton iBtnPageBack;
	/**消除验证码的X小图标*/
	private ImageButton clearTextviewBtn;
	private int recLen = 60;
	private String recTime = "";
	
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
			reg_code_time.setText(recTime);
			handler.postDelayed(this, 1000);
			if (recLen == 0) {
				handler.removeCallbacks(runnable);
//				txtResend.setTextColor(_this.getResources().getColor(
//						R.color.text_blue));
//				txtResend.setEnabled(true);
				recLen = 60;
				ib_get_reg_code.setEnabled(true);
				reg_code_time.setText("重新发送");
			}
		}
	};
	
//	。。。。。。。。
	private EditText firstLinePassword;
//	private EditText secondLinePassword;
	private TextView registConfirmPassword;

	private RegistService registService;

	private Context context;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhuce);
		
		//倒计时
		reg_code_time = (TextView) findViewById(R.id.reg_code_time);
        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        txtPageTitle.setText(this.getString(R.string.head_title_activity_regist_phone));
        btnClearText =(ImageButton) this.findViewById(R.id.clearTextviewBtn);
        btnClearText.setOnClickListener(this);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        _this=this;
        txtPhone = (EditText) this.findViewById(R.id.txtPhone);
		checkAgreement = (ToggleButton) this.findViewById(R.id.regist_i_agree);
        txtAgreeTips=(TextView)this.findViewById(R.id.txtAgreeTips);
        txtAgreePrivacyTips=(TextView)this.findViewById(R.id.txtAgreePrivacyTips);
        String tips="<font color=#8d8d8d>已经阅读并同意</font>　　　　　　　　　";
//                +"<font color=#000000>并了解没有您的许可,我们绝不擅自联系您,或者泄露您的资料!</font>";
        String tips2="<font color=#269BF6>　　　　　　<u>《知心使用条款和隐私政策》</u></font>";
        txtAgreeTips.setText(Html.fromHtml(tips));
        txtAgreePrivacyTips.setText(Html.fromHtml(tips2));
        txtAgreePrivacyTips.setOnClickListener(this);

        
//     。。。。。。。   
        //获取验证码
        ib_get_reg_code = (ImageButton) findViewById(R.id.ib_get_reg_code);
        ib_get_reg_code.setOnClickListener(this);
        clearTextviewBtn = (ImageButton)findViewById(R.id.clearTextviewBtn);
		clearTextviewBtn.setOnClickListener(this);
	
//		countDownTimerText = (TextView) this
//				.findViewById(R.id.regist_countdown_timer);
//		txtResend = (TextView) this.findViewById(R.id.regist_validte_resend);
		validateCodeEditText = (EditText) this.findViewById(R.id.validate_code);

		SharedPreferences sharedPref = this.getSharedPreferences(
				SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
		phone = sharedPref.getString(
				SettingValues.KEY_TEMP_USER_PHONE_FOR_REGIST_USE, null);
//		nextBtn.setOnClickListener(this);
//		txtResend.setOnClickListener(this);

//		sendValidateCode();
//		txtResend.setTextColor(this.getResources().getColor(R.color.text_grey));
//		txtResend.setEnabled(false);
//		handler.postDelayed(runnable, 1000);
		
//		。。。。。。。。。
		context = this.getApplicationContext();

		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_set_pwd));
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);

		registConfirmPassword = (TextView) this
				.findViewById(R.id.regist_confirm_password);
		firstLinePassword = (EditText) this
				.findViewById(R.id.password_first_line);

		registConfirmPassword.setOnClickListener(this);
		registService = new RegistService(this);
		

    }

	
	
	public void registAction(View view) {
//        btnNext.setEnabled(false);
		String phone = validateFields();
		String validateCode = validateCodeEditText.getText().toString();
		if (phone != null && !StringUtils.isEmpty(validateCode)) {
			String requestUrl = SettingValues.URL_PREFIX
					+ getResources().getString(R.string.URL_REGIST_PHONE);
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("phone", phone);
				jsonParams.put("token", validateCode);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			RequestLogic rl = new RequestLogic() {

				@Override
				public void onLoading(long count, long current) {

				}

				@Override
				public void whenSuccess(JSONObject result) {
					SharedPreferences sharedPref = getSharedPreferences(
							SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					String phone = txtPhone.getText().toString();
					editor.putString(
							SettingValues.KEY_TEMP_USER_PHONE_FOR_REGIST_USE,
							phone);
					editor.commit();
//
//					Intent intent = new Intent(RegistPhoneActivity.this,
//							RegistInviteActivity.class);
//					startActivity(intent);
//					btnNext.setEnabled(true);

				}

				@Override
				public void whenFail(JSONObject message) {

					if (requestFailRespondToast == null) {
						try {
							requestFailRespondToast = Toast.makeText(
									RegistPhoneActivity.this,
									message.getString("message"), 5);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					requestFailRespondToast.show();
					try {
						if (message.getString("success").equals("0")) {
							String content = message.getString("message");
							if (StringUtils.isAsciiPrintable(content)) {
								content = ErrHashMap.getErrMessage(content);
							}
							Toast.makeText(_this, content, Toast.LENGTH_SHORT)
									.show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} 
//					btnNext.setEnabled(true);
				}

				@Override
				public void whenRequestFail(String errcode) {
					Toast.makeText(_this,
							_this.getString(R.string.toast_request_fail),
							Toast.LENGTH_SHORT).show();
//					nextBtn.setEnabled(true);
				}
			};

			HttpClient.request(requestUrl, jsonParams, rl);

		}else{
			Toast.makeText(this,
					this.getString(R.string.toast_validate_code_not_empty),
					Toast.LENGTH_SHORT).show();
		}
	}

	private String validateFields() {
		// check user has agree or not
		if (!checkAgreement.isChecked()) {
			if (iDontAgreeToast == null) {
				iDontAgreeToast = Toast.makeText(this, getResources()
						.getString(R.string.regist_i_dont_agree), 5);
			}
			iDontAgreeToast.show();
//            btnNext.setEnabled(true);
			return null;
		}
		// checkThePhoneValidity

		if (!MatcherUtil.validateMobile(txtPhone.getText().toString())) {
			if (phoneInvalidToast == null) {
				phoneInvalidToast = Toast.makeText(this, getResources()
						.getString(R.string.regist_phone_invalid), 5);
			}
			phoneInvalidToast.show();
//            btnNext.setEnabled(true);
			return null;
		} else {
			return txtPhone.getText().toString();
		}

	}

    
	@Override
	public void onClick(View v) {
//		v.setEnabled(false);
		String phone;
		String password;
		String captcha;
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.clearTextviewBtn:
			validateCodeEditText.setText("");
			v.setEnabled(true);
			break;
//		case R.id.regist_validte_resend:
//			sendValidateCode();
//			txtResend.setTextColor(this.getResources().getColor(
//					R.color.text_grey));
//			handler.postDelayed(runnable, 1000);
//			break;
        case R.id.btnClearText:
            txtPhone.setText("");
            v.setEnabled(true);
            break;
        case R.id.txtAgreePrivacyTips:
            Intent intent = new Intent(RegistPhoneActivity.this,
                    MorePrivacyActivity.class);
            startActivity(intent);
            break;
        //获取验证码
        case R.id.ib_get_reg_code:
//        	actionConfirm();
        	phone = txtPhone.getText().toString().trim();
        	password = firstLinePassword.getText().toString().trim();
        	//石头
//        	if(!(password ==null && "".equals(password))){
        		try {
        			sendValidateCode(phone);
        		} catch (ParseException e) {
        			e.printStackTrace();
        		}
 //       	}
        	handler.postDelayed(runnable, 1000);
        	ib_get_reg_code.setEnabled(false);
        	break;
        case R.id.regist_confirm_password:
        	phone = txtPhone.getText().toString().trim();
        	password = firstLinePassword.getText().toString().trim();
        	captcha = validateCodeEditText.getText().toString().trim();
        	if(!(captcha ==null && "".equals(captcha))){
				try {
					actionConfirm(phone, password, captcha);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
			break;
		default:
			break;
		}
//		v.setEnabled(true);
	}
	
    
    
	/**
	 * 发送验证码
	 */
	
	//szs
	public JSONObject sendValidateCode(String phone) throws ParseException {
        JSONObject result=new JSONObject();
        JSONObject obj = new JSONObject();
        try {
			obj.put("phone", phone);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
        String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
        requestUrl+="?phone="+phone;
		try {
			result = HttpClient.requestSync(requestUrl, obj,HttpClient.TYPE_GET);
			if (result != null && result.getInt("success") == 1) {
                //。。。。。。。。。
				Log.i("login","request");
				Toast.makeText(this, "123456689", Toast.LENGTH_SHORT).show();
                return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public JSONObject actionConfirm(String phone,String password,String captcha) throws ParseException {
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
				+ context.getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
		try {
			result = HttpClient.requestSync(requestUrl, obj,HttpClient.TYPE_POST);
			if (result != null && result.getInt("success") == 1) {
                //。。。。。。。。。
				Log.i("to login","request");
				Toast.makeText(this, "szs4sb", Toast.LENGTH_SHORT).show();
                return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
//重新获取验证码和检验验证码是否正确	
    public void sendValidateCode() {
//		txtResend.setEnabled(false);

		JSONObject jsonParams = new JSONObject();
		try {
			jsonParams.put("phone", phone);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//获取验证码的接口
		String requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
		
		RequestLogic rl = new RequestLogic() {

			@Override
			public void onLoading(long count, long current) {
				
			}

			@Override
			public void whenSuccess(JSONObject result) {

			}

			@Override
			public void whenFail(JSONObject jbo) {

				try {
					if (jbo.getString("success").equals("0")) {
						String content = jbo.getString("message");
						if (StringUtils.isAsciiPrintable(content)) {
							content = ErrHashMap.getErrMessage(content);
						}
						Toast.makeText(_this, content, 5).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				} 
//				finally {
//					nextBtn.setEnabled(true);
//				}
			}

			@Override
			public void whenRequestFail(String errcode) {
				Toast.makeText(_this,
						_this.getString(R.string.toast_request_fail),
						Toast.LENGTH_SHORT).show();
//				nextBtn.setEnabled(true);
			}
		};
		HttpClient.request(requestUrl, jsonParams, rl);

	}

    
//    。。。。。。。这里只有1次输入密码的机会所以没有secondLinePassword
	public void actionConfirm() {
		registConfirmPassword.setEnabled(false);
		final String firstLine = firstLinePassword.getText().toString();
//		final String secondLine = secondLinePassword.getText().toString();

//		if (validateTwoLinePasswordIsSame(firstLine, secondLine)) {

			if (MatcherUtil.validatePassword(firstLine)) {
				//注册设置密码的接口
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_REGIST_SETUP_PASSWORD);
				JSONObject jsonParams = new JSONObject();
				try {
					jsonParams.put("phone", phone);
					jsonParams.put("pwd", firstLine);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				RequestLogic rl = new RequestLogic() {

					@Override
					public void onLoading(long count, long current) {

					}

					@Override
					public void whenSuccess(JSONObject result) {
						
						new AsyncTask<Void, Void, Boolean>() {
							@Override
							protected Boolean doInBackground(Void... params) {
								try {

									CurrentUserHelper.saveCurrentPhone(phone);
									registService.saveUserPassword(firstLine);
									return registService.logOnAction();
								} catch (JSONException e) {
									e.printStackTrace();
								} catch (ParseException e) {
									e.printStackTrace();
								}
								return false;
							}

							@Override
							protected void onPostExecute(Boolean result) {
								if (result) {
									Intent intent = new Intent(
											RegistPhoneActivity.this,
											MainActivity.class);
									intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
											| Intent.FLAG_ACTIVITY_NEW_TASK);
									startActivity(intent);
									RegistPhoneActivity.this.finish();
								} else {
									Toast.makeText(
											context,
											context.getString(R.string.toast_log_on_fail),
											Toast.LENGTH_SHORT).show();
									registConfirmPassword.setEnabled(true);

								}

							}
						}.execute();
					}

					@Override
					public void whenFail(JSONObject jbo) {

						try {
							String errMessage = jbo.getString("message");
							if (StringUtils.isAsciiPrintable(errMessage)) {
								errMessage = ErrHashMap.getErrMessage(jbo
										.getString("message"));
							}

							Toast.makeText(RegistPhoneActivity.this,
									errMessage, Toast.LENGTH_SHORT).show();
						} catch (JSONException e) {
							e.printStackTrace();
						} finally {

							registConfirmPassword.setEnabled(true);
						}
					}

					@Override
					public void whenRequestFail(String errcode) {

						Toast.makeText(RegistPhoneActivity.this,
								context.getString(R.string.toast_request_fail),
								Toast.LENGTH_SHORT).show();
						registConfirmPassword.setEnabled(true);

					}
				};
				HttpClient.request(requestUrl, jsonParams, rl);

			} else {

				Toast.makeText(RegistPhoneActivity.this,
						this.getString(R.string.toast_invalid_password_tips),
						Toast.LENGTH_SHORT).show();
				registConfirmPassword.setEnabled(true);

			}
//		} else {
//
//			Toast.makeText(this,
//					this.getString(R.string.toast_tow_line_password_not_match),
//					Toast.LENGTH_SHORT).show();
//			registConfirmPassword.setEnabled(true);
//		}
	}
/**
 * 检验第一二次的密码是否一样，这一版本没有需要
 */
//	private boolean validateTwoLinePasswordIsSame(String firstLine,
//			String secondLine) {
//		return firstLine.equals(secondLine);
//	}


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

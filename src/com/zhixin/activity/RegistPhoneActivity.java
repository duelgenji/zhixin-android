package com.zhixin.activity;

import java.io.File;
import java.text.ParseException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.provider.InternalStorageContentProvider;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.MatcherUtil;
import com.zhixin.utils.RecToCircleTask;

public class RegistPhoneActivity extends Activity implements View.OnClickListener  {
	// component in layout
	private EditText txtPhone;
	private ToggleButton checkAgreement;
    private TextView txtAgreeTips;
    private TextView txtAgreePrivacyTips;
    private ImageButton btnClearText;
    private RegistPhoneActivity _this;
	//获取验证码
	private ImageButton ib_get_reg_code;
	//倒计时
	private TextView reg_code_time;
	/**页面的名称*/
	private TextView txtPageTitle;
	/**验证码输入框*/
	private EditText validateCodeEditText;
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
	private EditText firstLinePassword;
	private TextView registConfirmPassword;
	private Context context;
	private Toast phoneInvalidToast;
//	private Toast passwordInvalidToast;
	private ImageView take_pic;
	private ImageView head_img;
	private Handler closePicImageDialogHander;
	static final int PICK_PIC_FROM_CAMERA_ACTION = 10;
	static final int PICK_PIC_FORM_GALLERY_ACTION = 20;
	static final int CROP_IMAGE_ACTION = 30;
	public static final String TEMP_PHOTO_FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ SettingValues.PATH_USER_TX_PREFIX
			+ "temp.jpg";
	private class RecToCircleTaskInQushejiao extends
	AsyncTask<String, Void, Bitmap> {
	protected Bitmap doInBackground(String... urls) {
		Bitmap bitmap = BitmapFactory.decodeFile(urls[0]);
		return RecToCircleTask.transferToCircle(bitmap);
	}
	
	protected void onPostExecute(Bitmap result) {
		
			CurrentUserHelper.saveBitmap(result);
			head_img.setImageResource(R.drawable.head_white_ring_background);
			head_img.setImageBitmap(result);
	}
}
	private Toast iDontAgreeToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhuce);
		//倒计时
		reg_code_time = (TextView)findViewById(R.id.reg_code_time);
        txtPageTitle= (TextView)findViewById(R.id.title_of_the_page);
        txtPageTitle.setText(this.getString(R.string.head_title_activity_regist_phone));
        btnClearText =(ImageButton)findViewById(R.id.clearTextviewBtn);
        btnClearText.setOnClickListener(this);
        iBtnPageBack =(ImageButton)findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        _this=this;
        
        take_pic = (ImageView) findViewById(R.id.take_pic);
        take_pic.setOnClickListener(this);
        head_img = (ImageView) findViewById(R.id.head_img);
        head_img.setOnClickListener(new ClickImageToChangeHeadIcon());
        
        txtPhone = (EditText)findViewById(R.id.txtPhone);
		checkAgreement = (ToggleButton)findViewById(R.id.regist_i_agree);
        txtAgreeTips=(TextView)findViewById(R.id.txtAgreeTips);
        txtAgreePrivacyTips=(TextView)findViewById(R.id.txtAgreePrivacyTips);
        String tips="<font color=#8d8d8d>已经阅读并同意</font>　　　　　　　　　";
//                +"<font color=#000000>并了解没有您的许可,我们绝不擅自联系您,或者泄露您的资料!</font>";
        String tips2="<font color=#269BF6>　　　　　　<u>《知心使用条款和隐私政策》</u></font>";
        txtAgreeTips.setText(Html.fromHtml(tips));
        txtAgreePrivacyTips.setText(Html.fromHtml(tips2));
        txtAgreePrivacyTips.setOnClickListener(this);
        //获取验证码
        ib_get_reg_code = (ImageButton) findViewById(R.id.ib_get_reg_code);
        ib_get_reg_code.setOnClickListener(this);
//        clearTextviewBtn = (ImageButton)findViewById(R.id.clearTextviewBtn);
//		clearTextviewBtn.setOnClickListener(this);
		validateCodeEditText = (EditText) this.findViewById(R.id.validate_code);

		SharedPreferences sharedPref = this.getSharedPreferences(
				SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
		phone = sharedPref.getString(
				SettingValues.KEY_TEMP_USER_PHONE_FOR_REGIST_USE, null);
		context = this.getApplicationContext();

		txtPageTitle = (TextView)findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_set_pwd));
		

		registConfirmPassword = (TextView)findViewById(R.id.regist_confirm_password);
		registConfirmPassword.setOnClickListener(this);
		firstLinePassword = (EditText)findViewById(R.id.password_first_line);
    }
	@Override
	public void onClick(View v) {
//		v.setEnabled(false);
		Intent intent;
		String phone;
		String password;
		String captcha;
		switch (v.getId()) {
		case R.id.backup_btn:
			v.setEnabled(false);
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.clearTextviewBtn:
			v.setEnabled(false);
			validateCodeEditText.setText("");
			v.setEnabled(true);
			break;
        case R.id.btnClearText:
        	v.setEnabled(false);
            txtPhone.setText("");
            v.setEnabled(true);
            break;
        case R.id.txtAgreePrivacyTips:
        	v.setEnabled(false);
            intent = new Intent(RegistPhoneActivity.this,
                    MorePrivacyActivity.class);
            startActivity(intent);
            break;
        //获取验证码
        case R.id.ib_get_reg_code:
       	v.setEnabled(false);
        	phone = txtPhone.getText().toString();
        	password = firstLinePassword.getText().toString();
        	//石头
//        	if(!(password == null && "".equals(password))){
        			if(MatcherUtil.validateMobile(phone)){
        				
        				if(checkAgreement.isChecked()){
        					if(MatcherUtil.validatePassword(password)){
        						try {
	        						sendValidateCode(phone);
//	        						handler.postDelayed(runnable, 1000);
        						} catch (ParseException e) {
        						    e.printStackTrace();
        						}
        					}else{
        						//密码格式不正确
        						Toast.makeText(_this, "密码格式有误，密码至少8位,且只能包含字母或者数字和_", 5).show();
        					}
        				}else{
        					//没有勾选
        					Toast.makeText(_this,"您还没有同意条款！", 5).show();
        				}
        			}else{
        				//手机格式不正确
        				Toast.makeText(_this,"您填写的手机号码错误", 5).show();
        			}
        		
 //      	}
        	ib_get_reg_code.setEnabled(true);
        	break;
        case R.id.regist_confirm_password:
        	v.setEnabled(false);
        	phone = txtPhone.getText().toString().trim();
        	password = firstLinePassword.getText().toString().trim();
        	captcha = validateCodeEditText.getText().toString().trim();
        	if(!(captcha ==null && "".equals(captcha))){
				try {
					actionConfirm(phone,password,captcha);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
        	}else {
				Toast.makeText(_this, "验证码有误！", Toast.LENGTH_SHORT).show();
				
        	}
       
			break;
		default:
			break;
		}
//		v.setEnabled(true);
	}
	public JSONObject sendValidateCode(String phone) throws ParseException {
//		phone = validateFields();
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
		Log.i("login","ready");
		
		new LoadDataTask().execute(1,requestUrl,obj,HttpClient.TYPE_GET);
		return result;
	}
	
	public JSONObject actionConfirm(String phone,String password,String captcha) throws ParseException {
        JSONObject result=new JSONObject();
        JSONObject obj = new JSONObject();
			try {
				obj.put("phone", phone);
				obj.put("password", password);
				obj.put("captcha", captcha);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_SETUP_PASSWORD);
        new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_POST_FORM);
		return result;
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
			Integer syncType=(Integer)params[0];
			JSONObject result = null;
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST
					result = HttpClient.requestSync(params[1].toString(),null,(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				case 2:
					//(JSONObject)params[2]。。。Json解析，post方式
					result = HttpClient.requestSync(params[1].toString(),params[2],(Integer)params[3]);
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
						handler.postDelayed(runnable, 1000);
					}else {
						Toast.makeText(_this, "请求出错！", Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					if (result != null && result.getInt("success") == 1) {
						Toast.makeText(_this, "注册成功，登录首页！", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(_this,MainActivity.class);
						startActivity(intent);
					}else {
						Toast.makeText(_this, "验证码输入有误！", Toast.LENGTH_SHORT).show();
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
    
    
    
    
    
    private class ClickImageToChangeHeadIcon implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			File file = new File(Environment.getExternalStorageDirectory()
					+ SettingValues.PATH_USER_TX_PREFIX);
			if (!file.exists()) {
				file.mkdirs();
			}
			final Dialog dialog = new Dialog(RegistPhoneActivity.this,
					android.R.style.Theme_Translucent_NoTitleBar);
			dialog.setContentView(R.layout.dialog_pick_head_image);

			Window window = dialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.dimAmount = 0.7f;
			dialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			window.setAttributes(lp);
			Button cameraBtn = (Button) dialog
					.findViewById(R.id.dialogCameraBtn);
			Button galleryBtn = (Button) dialog
					.findViewById(R.id.dialogGalleryBtn);
			Button cancelBtn = (Button) dialog
					.findViewById(R.id.dialogCancelBtn);

			closePicImageDialogHander = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					dialog.dismiss();
				}
			};

			cancelBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();

				}
			});
			
			cameraBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					try {
						Uri mImageCaptureUri = null;
						String state = Environment.getExternalStorageState();

						if (Environment.MEDIA_MOUNTED.equals(state)) {
							mImageCaptureUri = Uri.fromFile(new File(
									TEMP_PHOTO_FILE_PATH));
						} else {
							mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
						}
						intent.putExtra(
								android.provider.MediaStore.EXTRA_OUTPUT,
								mImageCaptureUri);

						startActivityForResult(intent,
								PICK_PIC_FROM_CAMERA_ACTION);
					} catch (ActivityNotFoundException e) {

						e.printStackTrace();
					}

				}
			});

			galleryBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");

					startActivityForResult(photoPickerIntent,
							PICK_PIC_FORM_GALLERY_ACTION);

				}
			});

			dialog.show();

		}

	}
    
    private void updateContentAfterLoading(Cursor cursor) {
		cursor.moveToFirst();
	
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ SettingValues.PATH_USER_TX_PREFIX);
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}

		// Program exploit:if there is no external storage,program will crash
		final String target = Environment.getExternalStorageDirectory()
				+ SettingValues.PATH_USER_TX_PREFIX
				+ FilenameUtils.getName(cursor.getString(cursor
						.getColumnIndex("picUrl")));
		File file = new File(target);
		if (!file.exists()) {
			String downloadUrl = SettingValues.URL_PREFIX
					+ cursor.getString(cursor.getColumnIndex("picUrl"));
			FinalHttp fh = new FinalHttp();
			fh.download(downloadUrl, null, target + ".temp", false,
					new AjaxCallBack<File>() {
						@Override
						public void onSuccess(File t) {
							
								if (t.renameTo(new File(target))) {

									new RecToCircleTaskInQushejiao()
											.execute(target);
								}
							
						}

					});
		} else {
			if (CurrentUserHelper.getBitmap() == null) {
				new RecToCircleTaskInQushejiao().execute(target);
			} else {
				head_img
						.setImageResource(R.drawable.head_white_ring_background);
				head_img.setImageBitmap(CurrentUserHelper.getBitmap());
			}

		}

	}
 

}

package com.qubaopen.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.customui.CircleImageView;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.provider.InternalStorageContentProvider;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.MatcherUtil;
import com.qubaopen.utils.RecToCircleTask;

import eu.janmuller.android.simplecropimage.CropImage;

public class RegistPhoneActivity extends Activity implements
		View.OnClickListener {
	// component in layout
	private EditText txtPhone;
	private ToggleButton checkAgreement;
	private ToggleButton showPassword;
	private TextView txtAgreeTips;
	private ImageButton btnClearText;
	private RegistPhoneActivity _this;
	// 获取验证码
	private ImageButton ib_get_reg_code;
	// 倒计时
	private TextView reg_code_time;
	/** 页面的名称 */
	private TextView txtPageTitle;
	/** 验证码输入框 */
	private EditText validateCodeEditText;
	// private String phone;
	/** 上一页 */
	private ImageButton iBtnPageBack;
	/** 消除验证码的X小图标 */
	private int recLen = 60;
	private String recTime = "";
	private File fileToSend;
	private String phone;
	private String password;
	private String captcha;
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
				recLen = 60;
				ib_get_reg_code.setEnabled(true);
				reg_code_time.setText("重新发送");
			}
		}
	};
	private EditText firstLinePassword;
	private TextView registConfirmPassword;
	private Context context;
	private ImageView take_pic;
	private CircleImageView head_img;
	private Handler closePicImageDialogHander;
	static final int PICK_PIC_FROM_CAMERA_ACTION = 10;
	static final int PICK_PIC_FORM_GALLERY_ACTION = 20;
	static final int CROP_IMAGE_ACTION = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhuce);

		txtPageTitle = (TextView) findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this
				.getString(R.string.head_title_activity_regist_phone));
		iBtnPageBack = (ImageButton) findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		_this = this;

		take_pic = (ImageView) findViewById(R.id.take_pic);
		take_pic.setOnClickListener(this);
		head_img = (CircleImageView) findViewById(R.id.head_img);
		head_img.setOnClickListener(new ClickImageToChangeHeadIcon());

		txtPhone = (EditText) findViewById(R.id.txtPhone);
		firstLinePassword = (EditText) findViewById(R.id.password_first_line);
		// 显示密码
		showPassword = (ToggleButton) findViewById(R.id.btn_show_password);
		showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					firstLinePassword.setInputType(InputType.TYPE_NULL);
				} else {
					firstLinePassword.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		// 获取验证码
		validateCodeEditText = (EditText) this.findViewById(R.id.validate_code);
		btnClearText = (ImageButton) findViewById(R.id.clearTextviewBtn);
		btnClearText.setOnClickListener(this);
		ib_get_reg_code = (ImageButton) findViewById(R.id.ib_get_reg_code);
		ib_get_reg_code.setOnClickListener(this);
		// 倒计时
		reg_code_time = (TextView) findViewById(R.id.reg_code_time);
		// 同意条款
		checkAgreement = (ToggleButton) findViewById(R.id.regist_i_agree);
		txtAgreeTips = (TextView) findViewById(R.id.txtAgreeTips);
		String tips = "<font color=#F3542D>已经阅读并同意</font>";
		String tips2 = "<font color=#F3542D>" + "《" + "<u>知心使用条款和隐私政策</u>"
				+ "》" + "</font>";
		txtAgreeTips.setText(Html.fromHtml(tips + tips2));
		txtAgreeTips.setOnClickListener(this);

		context = this.getApplicationContext();

		txtPageTitle = (TextView) findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_set_pwd));

		registConfirmPassword = (TextView) findViewById(R.id.regist_submit);
		registConfirmPassword.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// v.setEnabled(false);
		Intent intent;

		switch (v.getId()) {
		// 返回
		case R.id.backup_btn:
			v.setEnabled(false);
			finish();
			v.setEnabled(true);
			break;
		// 相机
		case R.id.take_pic:
			v.setEnabled(false);
			takePicFromCamera();
			v.setEnabled(true);
			break;
		// 清除验证码
		case R.id.clearTextviewBtn:
			v.setEnabled(false);
			validateCodeEditText.setText("");
			v.setEnabled(true);
			break;
		// 查看条款
		case R.id.txtAgreePrivacyTips:
			v.setEnabled(false);
			intent = new Intent(RegistPhoneActivity.this,
					MorePrivacyActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		// 获取验证码
		case R.id.ib_get_reg_code:
			v.setEnabled(false);
			phone = txtPhone.getText().toString();
			password = firstLinePassword.getText().toString();
			// 石头
			if (MatcherUtil.validateMobile(phone)) {
				try {
					sendValidateCode(phone);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				// 手机号格式不正确
				Toast.makeText(_this, "您填写的手机号码错误", 5).show();
			}

			v.setEnabled(true);
			break;
		case R.id.regist_submit:
			v.setEnabled(false);
			phone = txtPhone.getText().toString().trim();
			password = firstLinePassword.getText().toString().trim();
			captcha = validateCodeEditText.getText().toString().trim();
			Log.i("regist", "验证码:......" + captcha);
			if (checkAgreement.isChecked()) {
				if (MatcherUtil.validatePassword(password)) {
					if (!(captcha == null || captcha.equals(""))) {
						try {
							actionConfirm(phone, password, captcha);

						} catch (ParseException e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(_this, "请输入验证码！", Toast.LENGTH_SHORT)
								.show();

					}
				} else {
					// 密码格式不正确
					Toast.makeText(_this, "密码格式有误，密码至少8位,且只能包含字母或者数字和_", 5)
							.show();
				}
			} else {
				// 没有勾选
				Toast.makeText(_this, "您还没有同意条款！", 5).show();
			}
			v.setEnabled(true);
			break;
		default:
			break;
		}
	}

	public JSONObject sendValidateCode(String phone) throws ParseException {
		JSONObject result = new JSONObject();
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_REQUEST_VALIDATE_CODE);
		requestUrl += "?phone=" + phone;
		Log.i("regist", "sendcode......" + requestUrl);

		new LoadDataTask().execute(1, requestUrl, null, HttpClient.TYPE_GET);
		return result;
	}

	public JSONObject actionConfirm(String phone, String password,
			String captcha) throws ParseException {
		JSONObject result = new JSONObject();
		AjaxParams params = new AjaxParams();
		params.put("phone", phone);
		params.put("password", password);
		params.put("captcha", captcha);
		if (fileToSend != null) {
			Log.i("regist", "avatar....." + fileToSend.toString());
			try {
				params.put("avatar", fileToSend);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_REGIST_SETUP_PASSWORD);
		Log.i("regist", "注册信息" + params);
		new LoadDataTask().execute(2, requestUrl, params,
				HttpClient.TYPE_POST_NORMAL);
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
	// 参数0——此actuvuty调的第几个后台接口.1——连接后台的Url.2.3
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			Integer syncType = (Integer) params[0];
			JSONObject result = null;
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					result.put("syncType", syncType);
					Log.i("regiset", "获取验证码" + result);
					break;
				case 2:
					// (JSONObject)params[2]。。。Json解析，post方式
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					Log.i("regist", "注册结果:" + result);
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
						Toast.makeText(_this, "验证码已发送！", Toast.LENGTH_SHORT)
								.show();
						handler.postDelayed(runnable, 1000);
					} else {
						Toast.makeText(_this, "获取失败，请重新获取！", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case 2:
					if (result != null && result.getInt("success") == 1) {
						Toast.makeText(_this, "注册成功，登录首页！", Toast.LENGTH_SHORT)
								.show();
						String requestUrl = SettingValues.URL_PREFIX
								+ context.getString(R.string.URL_USER_LOGON);
						JSONObject obj = new JSONObject();
						try {
							obj.put("phone", phone);
							obj.put("password", password);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						new LoadLoginTask().execute(1, requestUrl, obj,
								HttpClient.TYPE_POST_FORM);
						finish();
					} else {
						Toast.makeText(_this, "验证码输入有误！", Toast.LENGTH_SHORT)
								.show();
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

	private class LoadLoginTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					// null。。。。传参方式是get
					// (Integer)params[3]对应上面的HttpClient.TYPE_POST
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
							UserInfoDao userInfoDao = new UserInfoDao();
							userInfoDao.saveUserForFirsttime(result, password);
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						Toast.makeText(_this, "登陆成功", Toast.LENGTH_SHORT)
								.show();
						Intent intent = new Intent(_this, MainActivity.class);
						startActivity(intent);
					} else {
						Toast.makeText(_this, "账号或密码有误！", 3).show();
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
					+ SettingValues.PATH_USER_PREFIX);
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
					new RecToCircleTaskInQushejiao()
							.execute(SettingValues.TEMP_PHOTO_FILE_PATH);
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
					takePicFromCamera();
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

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		switch (requestCode) {
		case PICK_PIC_FROM_CAMERA_ACTION:
			if (resultCode == Activity.RESULT_OK) {
				startCropImage();
			}
			break;
		case PICK_PIC_FORM_GALLERY_ACTION:
			if (resultCode == Activity.RESULT_OK) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						InputStream inputStream;
						try {
							inputStream = _this.getContentResolver()
									.openInputStream(data.getData());
							FileOutputStream fileOutputStream = new FileOutputStream(
									new File(SettingValues.TEMP_PHOTO_FILE_PATH));
							copyStream(inputStream, fileOutputStream);
							fileOutputStream.close();
							inputStream.close();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						_this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								startCropImage();
							}
						});
					}
				}).start();
			}
			break;

		case CROP_IMAGE_ACTION:
			if (resultCode == Activity.RESULT_OK) {

				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {
					return;
				}
				fileToSend = new File(SettingValues.TEMP_PHOTO_FILE_PATH);
				Log.i("regist", "fileToSend......" + fileToSend);
				if (closePicImageDialogHander != null) {
					closePicImageDialogHander.sendEmptyMessage(0);
				}
			}
			break;
		}
	}

	private void copyStream(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	private void startCropImage() {
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH,
				SettingValues.TEMP_PHOTO_FILE_PATH);
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 1);
		intent.putExtra(CropImage.ASPECT_Y, 1);
		startActivityForResult(intent, CROP_IMAGE_ACTION);
	}

	private void takePicFromCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			Uri mImageCaptureUri = null;
			String state = Environment.getExternalStorageState();

			if (Environment.MEDIA_MOUNTED.equals(state)) {
				mImageCaptureUri = Uri.fromFile(new File(
						SettingValues.TEMP_PHOTO_FILE_PATH));
			} else {
				mImageCaptureUri = InternalStorageContentProvider.CONTENT_URI;
			}
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					mImageCaptureUri);

			startActivityForResult(intent, PICK_PIC_FROM_CAMERA_ACTION);
		} catch (ActivityNotFoundException e) {

			e.printStackTrace();
		}
	}

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
			head_img.setBorderWidth(1);
		}
	}
}

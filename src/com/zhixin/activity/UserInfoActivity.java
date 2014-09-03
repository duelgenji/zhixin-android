package com.zhixin.activity;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.UserInfo;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-20.
 */
public class UserInfoActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private Activity _this;
	private Boolean pickerExist;
	private Dialog currentDialog;
	/** 性别*/
	private LinearLayout layoutSexPersonalProfile;
	/** 血型*/
	private LinearLayout layoutBloodTypePersonalProfile;
	/** 身份认证*/
	private LinearLayout layoutAuthenticationPersonalProfile;
	/** 收货地址*/
	private LinearLayout layoutAddressPersonalProfile;
	/** 出生日期*/
	private LinearLayout layoutBirthdayPersonalProfile;
	/** 电子邮箱*/
	private LinearLayout layoutEmailPersonalProfile;
	/** 性别显示框*/
	private TextView txtSexPersonalProfile;
	/** 出生日期显示框*/
	private TextView txtBirthPersonalProfile;
	/** 血型显示框*/
	private TextView txtBloodTypePersonalProfile;
	/** 地址显示框*/
	private TextView txtAddressPersonalProfile;
	/** 邮箱显示框*/
	private TextView txtEmailPersonalProfile;
	/** 身份认证显示框*/
	private TextView txtAuthenticationPersonalProfile;
	/** 邮箱*/
	private String email;
	private QubaopenProgressDialog progressDialog;
	String birthday,birthDay,defaultAddress,IdCard,iSex,bloodType,sex,blood;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if(sex.equals("MALE")){
					txtSexPersonalProfile.setText("男");
				}else if(sex.equals("FEMALE")){
					txtSexPersonalProfile.setText("女");
				}
				if (bloodType.equals("A")) {
					txtBloodTypePersonalProfile.setText("A");
				}else if (bloodType.equals("B")) {
					txtBloodTypePersonalProfile.setText("B");
				}else if (bloodType.equals("O")) {
					txtBloodTypePersonalProfile.setText("O");
				}else if (bloodType.equals("AB")) {
					txtBloodTypePersonalProfile.setText("AB");
				}else if (bloodType.equals("OTHER")) {
					txtBloodTypePersonalProfile.setText("其他");
				}
				txtBirthPersonalProfile.setText(birthday);
				txtAuthenticationPersonalProfile.setText(IdCard);
				txtEmailPersonalProfile.setText(email);
				txtAddressPersonalProfile.setText(defaultAddress);
				break;
			case 1:
				if(iSex!=null && iSex.equals("MALE")){
					txtSexPersonalProfile.setText(" 男");
				}else if(iSex!=null && iSex.equals("FEMALE")){
					txtSexPersonalProfile.setText("女");
				}
				if (blood!=null && blood.equals("A")) {
					txtBloodTypePersonalProfile.setText("A");
				}else if (blood!=null && blood.equals("B")) {
					txtBloodTypePersonalProfile.setText("B");
				}else if (blood!=null && blood.equals("O")) {
					txtBloodTypePersonalProfile.setText("O");
				}else if (blood!=null && blood.equals("AB")) {
					txtBloodTypePersonalProfile.setText("AB");
				}else if (blood!=null && blood.equals("OTHER")) {
					txtBloodTypePersonalProfile.setText("其他");
				}
				if (birthDay!=null) {
					txtBirthPersonalProfile.setText(birthDay);
				}
				if (IdCard!=null) {
					txtAuthenticationPersonalProfile.setText(IdCard);
				}
				if (email!=null) {
					txtEmailPersonalProfile.setText(email);
				}
				if (defaultAddress!=null) {
					txtAddressPersonalProfile.setText(defaultAddress);
				}
				break;
			default:
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_information);

		_this = this;
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this
				.getString(R.string.title_user_personal_profile));
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);

		txtSexPersonalProfile = (TextView) this
				.findViewById(R.id.txtSexPersonalProfile);
		
		txtBirthPersonalProfile = (TextView) this
				.findViewById(R.id.txtBirthPersonalProfile);
		
		txtBloodTypePersonalProfile = (TextView) this
				.findViewById(R.id.txtBloodTypePersonalProfile);
		
		txtAddressPersonalProfile = (TextView) this
				.findViewById(R.id.txtAddressPersonalProfile);
		
		txtEmailPersonalProfile = (TextView) this
				.findViewById(R.id.txtEmailPersonalProfile);
		
		txtAuthenticationPersonalProfile = (TextView) this
				.findViewById(R.id.txtAuthenticationPersonalProfile);

		layoutSexPersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutSexPersonalProfile);
		layoutSexPersonalProfile.setOnClickListener(this);
		
		layoutBloodTypePersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutBloodTypePersonalProfile);
		layoutBloodTypePersonalProfile.setOnClickListener(this);
		
		layoutBirthdayPersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutBirthdayPersonalProfile);
		layoutBirthdayPersonalProfile.setOnClickListener(this);
		
		layoutAuthenticationPersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutAuthenticationPersonalProfile);
		layoutAuthenticationPersonalProfile.setOnClickListener(this);
		
		layoutAddressPersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutAddressPersonalProfile);
		layoutAddressPersonalProfile.setOnClickListener(this);
		
		layoutEmailPersonalProfile = (LinearLayout) this
				.findViewById(R.id.layoutEmailPersonalProfile);
		layoutEmailPersonalProfile.setOnClickListener(this);
		
		progressDialog = new QubaopenProgressDialog(this);
		pickerExist = false;

	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
//		progressDialog.show();
//		getSupportLoaderManager().restartLoader(0, null, this);
//		long userId = CurrentUserHelper.getCurrentUserId();
		 String requestUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_USER_INFO_ADD);
//	        requestUrl+="/"+userId;
		new LoadDataTask().execute(1,requestUrl,null,HttpClient.TYPE_GET);
	}
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
						if (result != null && result.getString("success").equals("1")) {
			                //。。。。。。。。。
							Toast.makeText(_this, "获取个人资料成功！", Toast.LENGTH_SHORT).show();
							sex = result.getString("sex");
							bloodType = result.getString("bloodType");
							birthday = result.getString("birthday");
							defaultAddress = result.getString("defaultAddress");
							email = result.getString("email");
							IdCard = result.getString("IdCard");
							Message msg = Message.obtain();
							msg.what = 0;
							handler.sendMessage(msg);
						}else {
							Toast.makeText(_this, "获取数据失败！", Toast.LENGTH_SHORT).show();
						}
						break;
					case 2:
						if (result != null && result.getInt("success") == 1) {
							//。。。。。。。。。
							int code = result.getInt("success");
							if(code == 1){
								Message msg = Message.obtain();
								msg.what = 1;
								handler.sendMessage(msg);
							}
							Toast.makeText(_this, "获取个人资料成功！", Toast.LENGTH_SHORT).show();
						}else {
							Toast.makeText(_this, "获取数据失败！", Toast.LENGTH_SHORT).show();
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
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.layoutSexPersonalProfile:
			if (!pickerExist) {
				pickerExist = true;
				this.SexPicker();
			}
			break;
		case R.id.layoutBloodTypePersonalProfile:
			if (!pickerExist) {
				pickerExist = true;
				this.BloodTypePicker();
			}
			break;
		case R.id.layoutBirthdayPersonalProfile:
			if (!pickerExist) {
				pickerExist = true;
				this.DatePicker();
			}
			break;
		case R.id.layoutEmailPersonalProfile:
			if (!pickerExist) {
				intent = new Intent(_this, UserInfoEmailActivity.class);
				if (email != null && !email.equals("")) {
					intent.putExtra(UserInfoEmailActivity.INTENT_EMAIL, email);
				}
				startActivity(intent);
				v.setEnabled(false);
			}
			break;
		case R.id.layoutAuthenticationPersonalProfile:
			if (!pickerExist) {
				intent = new Intent(_this, UserInfoAuthenticationActivity.class);
				startActivity(intent);
				v.setEnabled(false);
			}
			break;
		case R.id.layoutAddressPersonalProfile:
			if (!pickerExist) {
				SharedPreferences sharedPref = this.getSharedPreferences(
						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				Boolean isAddressSaved = sharedPref.getBoolean(
						SettingValues.KEY_CURRENT_ADDRESS_SAVED, false);
//				if (!isAddressSaved) {
//					showToast("正在加载数据中");
//					v.setEnabled(true);
//					return;
//				}
				intent = new Intent(_this, UserInfoAddressActivity.class);
				startActivity(intent);
				v.setEnabled(false);
			}
		default:
			break;

		}
		v.setEnabled(true);
	}

	public void SexPicker() {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar) {
			@Override
			public void dismiss() {
				super.dismiss();
				pickerExist = false;
			}
		};
		dialog.setContentView(R.layout.dialog_pick_sex);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		Button btnMale = (Button) dialog.findViewById(R.id.btnMaleSexPicker);
		Button btnFemale = (Button) dialog
				.findViewById(R.id.btnFemaleSexPicker);
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btnCancelSexPicker);
		currentDialog = dialog;
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnMale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					iSex = Integer.parseInt("0");
					iSex = "MALE";
					obj.put("sex", iSex);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});
		btnFemale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					iSex = Integer.parseInt("1");
					iSex = "FEMALE"; 
					obj.put("sex", iSex);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	public void BloodTypePicker() {
		final Dialog dialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar) {
			@Override
			public void dismiss() {
				super.dismiss();
				pickerExist = false;
			}
		};
		dialog.setContentView(R.layout.dialog_pick_blood_type);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		Button btnA = (Button) dialog.findViewById(R.id.btnABloodTypePicker);
		Button btnB = (Button) dialog.findViewById(R.id.btnBBloodTypePicker);
		Button btnO = (Button) dialog.findViewById(R.id.btnOBloodTypePicker);
		Button btnAB = (Button) dialog.findViewById(R.id.btnABBloodTypePicker);
		Button btnOther = (Button) dialog
				.findViewById(R.id.btnOtherBloodTypePicker);
		Button btnCancel = (Button) dialog
				.findViewById(R.id.btnCancelBloodTypePicker);

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnA.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					blood = Integer.parseInt("0");
					blood = "A";
					obj.put("bloodType", blood);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});

		btnB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					blood = Integer.parseInt("1");
					blood = "B";
					obj.put("bloodType", blood);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});
		btnO.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					blood = Integer.parseInt("2");
					blood = "O";
					obj.put("bloodType", blood);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});

		btnAB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					blood = Integer.parseInt("3");
					blood = "AB";
					obj.put("bloodType", blood);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});
		btnOther.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
//					blood = Integer.parseInt("4");
					blood = "OTHER";
					obj.put("id", userId);
					obj.put("bloodType", blood);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	private int mYear, mMonth, mDay;
	public void DatePicker() {
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final DatePicker datepicker = new DatePicker(_this);
		String sBirth = "";
		Integer year = 1995;
		Integer month = 0;
		Integer day = 1;
		if (txtBirthPersonalProfile.getText() != null) {
			sBirth = txtBirthPersonalProfile.getText().toString().trim();
			if (sBirth.length() >= 10) {
				year = Integer.parseInt(sBirth.substring(0, 4));
				month = Integer.parseInt(sBirth.substring(5, 7)) - 1;
				day = Integer.parseInt(sBirth.substring(8, 10));
			}
		};
		datepicker.init(year, month, day, null);

		AlertDialog.Builder builder = new AlertDialog.Builder(_this);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener(){
			@Override
			public void onCancel(DialogInterface dialog) {
				pickerExist = false;
			}
		});
		builder.setView(datepicker);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mYear = datepicker.getYear();
				mMonth = datepicker.getMonth() + 1;
				mDay = datepicker.getDayOfMonth();
				// Toast.makeText(_this, mYear+"-"+mMonth+"-"+mDay,
				// Toast.LENGTH_SHORT).show();
//				new LoadDataTask().execute("birthday", mYear + "-" + mMonth
//						+ "-" + mDay);
				birthDay = mYear + "-" + mMonth + "-" + mDay;
				JSONObject obj = new JSONObject();
				long userId = CurrentUserHelper.getCurrentUserId();
				try {
					obj.put("birthday", birthDay);
					obj.put("id", userId);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_INFO_UPDATE);
				new LoadDataTask().execute(2,requestUrl,obj,HttpClient.TYPE_PUT_JSON);
				pickerExist = false;
			}

		});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						pickerExist = false;
					}
				});
		builder.show();
	}		
	
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		long userId = CurrentUserHelper.getCurrentUserId();
		return new SqlCursorLoader(this,
				"select * from user_info where username='" + userId
						+ "' limit 1;", UserInfo.class);
	}
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 1) {
			data.moveToFirst();
			Integer sex = data.getInt(data.getColumnIndex("sex"));
			Integer bloodtype = data.getInt(data.getColumnIndex("bloodtype"));
			String email = data.getString(data.getColumnIndex("email"));
			String birthday = data.getString(data.getColumnIndex("birthday"));
			String address = data.getString(data.getColumnIndex("address"));
			// set sex value
			if (sex == 1) {
				txtSexPersonalProfile.setText(_this.getString(R.string.dialog_pick_sex_female));
			} else if (sex == 0) {
				txtSexPersonalProfile.setText(_this
						.getString(R.string.dialog_pick_sex_male));
			} else {
				txtSexPersonalProfile.setText(_this
						.getString(R.string.dialog_pick_sex_null));
			}

			// set birthday value
			if (birthday == null || birthday.equals("")) {
				txtBirthPersonalProfile.setText("");
			} else {
				if (birthday.length() >= 10)
					birthday = birthday.substring(0, 10);
				txtBirthPersonalProfile.setText(birthday);
			}

			// set blood type value
			if (bloodtype == 0) {
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_A));
			} else if (bloodtype == 1) {
				txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_B));
			} else if (bloodtype == 2) {
				txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_O));
			} else if (bloodtype == 3) {
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_AB));
			} else if (bloodtype == 4) {
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_Other));
			} else {
				txtBloodTypePersonalProfile.setText("");
			}

			// set email value
			if (email == null || email.equals("")) {
				txtEmailPersonalProfile.setText("");
			} else {
				this.email = email;
				txtEmailPersonalProfile.setText(email);
			}

			if (address != null && !address.equals("")) {
				txtAddressPersonalProfile.setText(address);
			}

		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
	private void showToast(String content) {
		Toast.makeText(this, content, 3).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(_this);
	}

}

package com.qubaopen.activity;

import java.lang.reflect.Field;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.domain.UserInfo;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-20.
 */
public class UserInfoActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private UserInfoActivity _this;
	private Boolean pickerExist;
	private Boolean yearCanPick = false;
	/** 性别 */
	private LinearLayout layoutSexPersonalProfile;
	/** 血型 */
	private LinearLayout layoutBloodTypePersonalProfile;
	/** 身份认证 */
	private LinearLayout layoutAuthenticationPersonalProfile;
	/** 收货地址 */
	private LinearLayout layoutAddressPersonalProfile;
	/** 出生日期 */
	private LinearLayout layoutBirthdayPersonalProfile;
	/** 电子邮箱 */
	private LinearLayout layoutEmailPersonalProfile;
	/** 性别显示框 */
	private TextView txtSexPersonalProfile;
	/** 出生日期显示框 */
	private TextView txtBirthPersonalProfile;
	/** 血型显示框 */
	private TextView txtBloodTypePersonalProfile;
	/** 地址显示框 */
	private TextView txtAddressPersonalProfile;
	/** 邮箱显示框 */
	private TextView txtEmailPersonalProfile;
	/** 身份认证显示框 */
	private TextView txtAuthenticationPersonalProfile;
	/** 邮箱 */
	private long userId;
	private int mYear, mMonth, mDay;
	private int selectYear, selectMonth, selectDay;
	// private QubaopenProgressDialog progressDialog;
	private Integer sex, bloodType;
	private Integer localSex, localBloodType;
	// private String birthDay,defaultAddress,IdCard,email;
	private String currentSex, currentBirthDay, currentIdCard,
			currentBloodType, currentDefaultAddress, currentEmail;
	private String localSexStr, localBloodTypeStr, localBirthDay, localIdCard,
			localDefaultAddress, localEmail;

	// private UserInfo user;
	private UserInfoDao userInfoDao;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case 0:
				if (currentSex != null) {
					if (sex != localSex) {
						if (currentSex.equals("MALE")) {
							txtSexPersonalProfile.setText("男");
						} else if (currentSex.equals("FEMALE")) {
							txtSexPersonalProfile.setText("女");
						} else {
							txtSexPersonalProfile.setText("无");
						}
					}
				} else {
					txtSexPersonalProfile.setText(localSexStr);
				}
				if (currentBloodType != null) {
					if (!bloodType.equals(localBloodType)) {
						if (currentBloodType.equals("A")) {
							txtBloodTypePersonalProfile.setText("A");
						} else if (currentBloodType.equals("B")) {
							txtBloodTypePersonalProfile.setText("B");
						} else if (currentBloodType.equals("O")) {
							txtBloodTypePersonalProfile.setText("O");
						} else if (currentBloodType.equals("AB")) {
							txtBloodTypePersonalProfile.setText("AB");
						} else {
							txtBloodTypePersonalProfile.setText("其他");
						}
					}

				} else {
					txtBloodTypePersonalProfile.setText(localBloodTypeStr);
				}

				if (currentBirthDay != null) {
					if (!currentBirthDay.equals(localBirthDay)) {
						txtBirthPersonalProfile.setText(currentBirthDay);
					}
				} else {
					txtBirthPersonalProfile.setText(localBirthDay);
				}

				if (currentIdCard != null) {
					if (!currentIdCard.equals(localIdCard)) {
						txtAuthenticationPersonalProfile.setText(currentIdCard);
					}
				} else {
					txtAuthenticationPersonalProfile.setText(localIdCard);
				}

				if (currentEmail != null) {
					if (!currentEmail.equals(localEmail)) {
						txtEmailPersonalProfile.setText(currentEmail);
					}

				} else {
					txtEmailPersonalProfile.setText(localEmail);
				}

				if (currentDefaultAddress != null) {
					if (!currentDefaultAddress.equals(localDefaultAddress)) {
						txtAddressPersonalProfile
								.setText(currentDefaultAddress);
					}
				} else {
					txtAddressPersonalProfile.setText(localDefaultAddress);
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
		initView();
		userInfoDao = new UserInfoDao();
		userId = CurrentUserHelper.getCurrentUserId();

	}

	private void initView() {
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

		// progressDialog = new QubaopenProgressDialog(this);
		pickerExist = false;

	}

	@Override
	protected void onStart() {
		super.onStart();
		getSupportLoaderManager().restartLoader(0, null, _this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
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
		case R.id.layoutAuthenticationPersonalProfile:
			if (!pickerExist) {
				intent = new Intent(_this, UserInfoAuthenticationActivity.class);
				startActivity(intent);
				v.setEnabled(false);
			}
			break;
		case R.id.layoutBirthdayPersonalProfile:
			if (!pickerExist) {
				pickerExist = true;
				this.DatePicker();
			}
			break;
		case R.id.layoutAddressPersonalProfile:
			if (!pickerExist) {
				// SharedPreferences sharedPref = this.getSharedPreferences(
				// SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				// Boolean isAddressSaved = sharedPref.getBoolean(
				// SettingValues.KEY_CURRENT_ADDRESS_SAVED, false);
				intent = new Intent(_this, UserInfoAddressActivity.class);
				startActivity(intent);
				v.setEnabled(false);
			}
			break;
		case R.id.layoutEmailPersonalProfile:
			if (!pickerExist) {
				intent = new Intent(_this, UserInfoEmailActivity.class);
				if (currentEmail != null) {
					intent.putExtra(UserInfoEmailActivity.INTENT_EMAIL,
							currentEmail);
				} else {
					intent.putExtra(UserInfoEmailActivity.INTENT_EMAIL,
							localEmail);
				}

				startActivity(intent);
				v.setEnabled(false);
			}
			break;
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
		// currentDialog = dialog;
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnMale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				currentSex = "MALE";
				sex = 0;
				txtSexPersonalProfile.setText("男");
				JSONObject obj = new JSONObject();
				if (sex != localSex) {
					try {

						obj.put("sex", sex);
						obj.put("id", userId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(0, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});
		btnFemale.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				currentSex = "FEMALE";
				sex = 1;
				txtSexPersonalProfile.setText("女");
				JSONObject obj = new JSONObject();
				if (sex != localSex) {
					try {

						obj.put("sex", sex);
						obj.put("id", userId);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(0, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}

				dialog.dismiss();
			}
		});
		if (currentSex == null || currentSex.equals("")) {
			currentSex = localSexStr;
			sex = localSex;
		}

		dialog.show();
	}

	public void DatePicker() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		final DatePicker datepicker = new DatePicker(_this);
		String sBirth = "";
		// Integer year = 1995;
		// Integer month = 01;
		// Integer day = 01;
		if (txtBirthPersonalProfile.getText() != null) {
			sBirth = txtBirthPersonalProfile.getText().toString().trim();
			if (sBirth.length() >= 10) {
				mYear = Integer.parseInt(sBirth.substring(0, 4));
				mMonth = Integer.parseInt(sBirth.substring(5, 7)) - 1;
				mDay = Integer.parseInt(sBirth.substring(8, 10));
			}
			// datepicker.init(year, month, day, null);
		}
		OnDateChangedListener onDateChangedListener = new OnDateChangedListener() {

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				Log.i("year", "select......" + year);
				if (year >= 1970 && year <= c.get(Calendar.YEAR)) {
					yearCanPick = true;
				} else {
					yearCanPick = false;
				}
				mYear = year;
			}
		};
		datepicker.init(mYear, mMonth, mDay, onDateChangedListener);

		AlertDialog.Builder builder = new AlertDialog.Builder(_this);
		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				pickerExist = false;
			}
		});

		builder.setView(datepicker);

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				datepicker.clearFocus();
				
				Log.i("yearCanPick", "......" + yearCanPick);
				selectYear = datepicker.getYear();
				Log.i("year", "年份" + selectYear);
					if (yearCanPick) {
						selectMonth = datepicker.getMonth() + 1;
						selectDay = datepicker.getDayOfMonth();
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(selectYear);

						if (selectMonth < 10) {
							stringBuilder.append("-0").append(selectMonth);
						} else {
							stringBuilder.append("-").append(selectMonth);
						}
						if (selectDay < 10) {
							stringBuilder.append("-0").append(selectDay);
						} else {
							stringBuilder.append("-").append(selectDay);
						}
						currentBirthDay = stringBuilder.toString();
						Log.i("datepicker", "日期......" + currentBirthDay);
						if (currentBirthDay != localBirthDay) {
							JSONObject obj = new JSONObject();
							try {
								obj.put("birthday", currentBirthDay);
								obj.put("id", userId);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							String requestUrl = SettingValues.URL_PREFIX
									+ getString(R.string.URL_USER_INFO_UPDATE);
							new LoadDataTask().execute(1, requestUrl, obj,
									HttpClient.TYPE_PUT_JSON);
						}
					} else {
						showToast("年份错误");
						try {
							Field field = arg0.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(arg0, false); // false - 使之不能关闭(此为机关所在，其它语句相同)
						} catch (Exception e) {
							e.printStackTrace();
						}
						return;
					}
				
				try {
					Field field = arg0.getClass().getSuperclass()
							.getDeclaredField("mShowing");
					field.setAccessible(true);
					field.set(arg0, true); // true - 使之可以关闭(此为机关所在，其它语句相同)
				} catch (Exception e) {
					e.printStackTrace();
				}
				pickerExist = false;

			}

		});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							Field field = dialog.getClass().getSuperclass()
									.getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true); // true - 使之可以关闭(此为机关所在，其它语句相同)
						} catch (Exception e) {
							e.printStackTrace();
						}
						pickerExist = false;
					}
				});
		if (currentBirthDay != null && !currentBirthDay.equals("")) {
			txtBirthPersonalProfile.setText(currentBirthDay);

		} else {
			currentBirthDay = localBirthDay;
		}

		builder.show();
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
				currentBloodType = "A";
				bloodType = 0;
				if (bloodType != localBloodType) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", userId);
						obj.put("bloodType", bloodType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(2, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});

		btnB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentBloodType = "B";
				bloodType = 1;
				if (bloodType != localBloodType) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", userId);
						obj.put("bloodType", bloodType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(2, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});
		btnO.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentBloodType = "O";
				bloodType = 2;
				if (bloodType != localBloodType) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", userId);
						obj.put("bloodType", bloodType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(2, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});

		btnAB.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentBloodType = "AB";
				bloodType = 3;
				if (bloodType != localBloodType) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", userId);
						obj.put("bloodType", bloodType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(2, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});
		btnOther.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				currentBloodType = "OTHER";
				bloodType = 4;
				if (bloodType != localBloodType) {
					JSONObject obj = new JSONObject();
					try {
						obj.put("id", userId);
						obj.put("bloodType", bloodType);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_USER_INFO_UPDATE);
					new LoadDataTask().execute(2, requestUrl, obj,
							HttpClient.TYPE_PUT_JSON);

				}
				dialog.dismiss();
			}
		});

		if (currentBloodType != null && !currentBloodType.equals("")) {
			txtBloodTypePersonalProfile.setText(currentBloodType);
		} else {
			bloodType = localBloodType;
		}

		dialog.show();
	}

	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 0:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					break;
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					break;
				case 2:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					break;
				default:
					break;
				}
				// Log.i("請求結果", result + "");
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
				case 0:
					if (result != null && result.getInt("success") == 1) {
						userInfoDao.saveUserInfoSexById(userId, sex);
						getSupportLoaderManager().restartLoader(0, null, _this);

						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);

						showToast("修改性别成功！");
					} else {
						showToast("修改性别失败！");
					}
					break;
				case 1:
					if (result != null && result.getInt("success") == 1) {
						userInfoDao.saveUserInfoBirthDayById(userId,
								currentBirthDay);
						getSupportLoaderManager().restartLoader(0, null, _this);

						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);
						showToast("修改生日成功！");
					} else {
						showToast("修改生日失败！");
					}
					break;
				case 2:
					if (result != null && result.getInt("success") == 1) {
						userInfoDao
								.saveUserInfoBloodTypeById(userId, bloodType);
						getSupportLoaderManager().restartLoader(0, null, _this);

						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);
						showToast("修改血型成功！");
					} else {
						showToast("修改血型失败！");
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
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SqlCursorLoader(this,
				"select * from user_info where userId='" + userId
						+ "' limit 1;", UserInfo.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.getCount() == 1) {
			data.moveToFirst();
			localSex = data.getInt(data.getColumnIndex("sex"));
			localBirthDay = data.getString(data.getColumnIndex("birthDay"));
			localIdCard = data.getString(data.getColumnIndex("identityNumber"));
			localBloodType = data.getInt(data.getColumnIndex("bloodType"));
			localDefaultAddress = data
					.getString(data.getColumnIndex("address"));
			localEmail = data.getString(data.getColumnIndex("email"));
			// set sex value
			if (localSex == 1) {
				localSexStr = "女";
				txtSexPersonalProfile.setText(_this
						.getString(R.string.dialog_pick_sex_female));
			} else if (localSex == 0) {
				localSexStr = "男";
				txtSexPersonalProfile.setText(_this
						.getString(R.string.dialog_pick_sex_male));
			} else {
				localSexStr = "无";
				txtSexPersonalProfile.setText(_this
						.getString(R.string.dialog_pick_sex_null));
			}

			// set birthday value
			if (localBirthDay == null || localBirthDay.equals("")) {
				localBirthDay = "";
			} else {
				if (localBirthDay.length() >= 10)
					localBirthDay = localBirthDay.substring(0, 10);
			}
			txtBirthPersonalProfile.setText(localBirthDay);

			// set idCard value
			if (localIdCard == null || localIdCard.equals("")) {
				localIdCard = "";
			}
			txtAuthenticationPersonalProfile.setText(localIdCard);

			// set blood type value
			if (localBloodType == 0) {
				localBloodTypeStr = "A";
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_A));
			} else if (localBloodType == 1) {
				localBloodTypeStr = "B";
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_B));
			} else if (localBloodType == 2) {
				localBloodTypeStr = "O";
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_O));
			} else if (localBloodType == 3) {
				localBloodTypeStr = "AB";
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_AB));
			} else {
				localBloodTypeStr = "其他";
				txtBloodTypePersonalProfile.setText(_this
						.getString(R.string.dialog_pick_blood_type_Other));
			}

			// set email value
			if (localEmail == null || localEmail.equals("")) {
				localEmail = "";
			}
			txtEmailPersonalProfile.setText(localEmail);

			if (localDefaultAddress == null || localDefaultAddress.equals("")) {
				localDefaultAddress = "";
			}
			txtAddressPersonalProfile.setText(localDefaultAddress);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	private void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(_this);
	}

}

package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.MatcherUtil;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoEmailActivity extends FragmentActivity implements
		View.OnClickListener {

	public static final String INTENT_EMAIL = "currentEmail";

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private Button submitBtn;
	private ImageButton clearEmail;

	private Activity _this;

	private String currentEmail;
	private EditText txtInputEmailPersonalProfile;
	private long userId;
	private String sEmail;

	private UserInfoDao userInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_email);

		_this = this;

		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_user_email));
		submitBtn = (Button) this.findViewById(R.id.addressSubmit);
		submitBtn.setOnClickListener(this);
		clearEmail = (ImageButton) this.findViewById(R.id.clearEmailBtn);
		clearEmail.setOnClickListener(this);

		txtInputEmailPersonalProfile = (EditText) this
				.findViewById(R.id.txtInputEmailPersonalProfile);
		currentEmail = getIntent().getStringExtra(INTENT_EMAIL);
		if (currentEmail != null && !currentEmail.equals("")) {
			txtInputEmailPersonalProfile.setText(currentEmail);
			txtInputEmailPersonalProfile.setSelection(currentEmail.length());

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.addressSubmit:
			// sendRequest();
			if (txtInputEmailPersonalProfile.getText() != null) {
				sEmail = txtInputEmailPersonalProfile.getText().toString();
				if (!sEmail.equals("")) {
					if (MatcherUtil.validateEmail(sEmail)) {
						if (sEmail != currentEmail) {
							String requestUrl = SettingValues.URL_PREFIX
									+ UserInfoEmailActivity.this
											.getString(R.string.URL_USER_UPDATE);
							JSONObject jsonParams = new JSONObject();
							userId = CurrentUserHelper.getCurrentUserId();
							try {
								jsonParams.put("email", sEmail);
								jsonParams.put("id", userId);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							new LoadDataTask1().execute(1, requestUrl,
									jsonParams, HttpClient.TYPE_PUT_JSON);
						} else {
							showToast("请填写新邮箱！");
						}

					} else {
						showToast(_this
								.getString(R.string.toast_user_info_wrong_email));
					}
				} else {
					showToast(_this
							.getString(R.string.toast_input_cant_be_null));
				}
			} else {
				showToast(_this.getString(R.string.toast_input_cant_be_null));
			}

			v.setEnabled(true);
			break;
		case R.id.clearEmailBtn:
			txtInputEmailPersonalProfile.setText("");
			v.setEnabled(true);
			break;
		default:
			break;
		}
	}

	private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
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
						txtInputEmailPersonalProfile.setText("");
						userInfoDao = new UserInfoDao();
						userInfoDao.saveUserInfoEmailById(userId, sEmail);
						Toast.makeText(_this, "修改邮箱成功！", Toast.LENGTH_SHORT)
								.show();
						_this.onBackPressed();
					} else {
						Toast.makeText(_this, "修改邮箱失败！", Toast.LENGTH_SHORT)
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

	private void showToast(String content) {
		Toast.makeText(_this, content, Toast.LENGTH_SHORT).show();
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
}

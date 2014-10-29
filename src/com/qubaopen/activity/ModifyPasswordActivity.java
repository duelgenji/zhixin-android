package com.qubaopen.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class ModifyPasswordActivity extends Activity implements OnClickListener {
	private UserInfoDao userInfoDao;
	private ModifyPasswordActivity _this;
	private ImageButton iBtnPageBack;
	private TextView titleOfThePage;
	private TextView submit;

	private EditText oldPasswordEditText;
	private EditText newPasswordOneEditText;
	private EditText newPasswordTwoEditText;

	private String oldPassword, localPassword;
	private String newPasswordOne;
	private String newPasswordTwo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		userInfoDao = new UserInfoDao();
		setContentView(R.layout.activity_more_reset_pwd);
		initView();
	}

	private void initView() {
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		titleOfThePage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfThePage.setText("修改密码");
		submit = (TextView) this.findViewById(R.id.regist_submit);
		submit.setOnClickListener(this);
		oldPasswordEditText = (EditText) this.findViewById(R.id.txtOldPwdReset);
		newPasswordOneEditText = (EditText) this
				.findViewById(R.id.txtNewPwdReset1);
		newPasswordTwoEditText = (EditText) this
				.findViewById(R.id.txtNewPwdReset2);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		case R.id.regist_submit:
			oldPassword = oldPasswordEditText.getText().toString();
			localPassword = userInfoDao
					.getUserPasswordByPhone(CurrentUserHelper.getCurrentPhone());

			newPasswordOne = newPasswordOneEditText.getText().toString();
			newPasswordTwo = newPasswordTwoEditText.getText().toString();

			String requestUrl = SettingValues.URL_PREFIX
					+ this.getString(R.string.URL_MODIFY_PWD);
			JSONObject params = new JSONObject();
			if (!oldPassword.equals(localPassword)) {
				showToast(getString(R.string.toast_tow_line_password_not_match));
			} else {
				if (!newPasswordOne.equals(newPasswordTwo)) {
					showToast(getString(R.string.toast_more_reset_not_same));
				} else {
					if (oldPassword.equals(newPasswordOne)) {
						showToast(getString(R.string.toast_more_reset_same));
					} else {
						try {
							params.put("oldPwd", oldPassword);
							params.put("newPwd", newPasswordOne);
							new ModifyPwdTask().execute(1, requestUrl, params,
									HttpClient.TYPE_POST_FORM);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				}

			}

			break;
		default:
			break;
		}
	}

	private class ModifyPwdTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			Integer syncType = (Integer) params[0];
			JSONObject result = null;
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					// Log.i("modifypwd", "修改密码" + result);
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
						showToast(getString(R.string.toast_more_reset_request_success));
						userInfoDao.saveUserInfoPassword(
								CurrentUserHelper.getCurrentPhone(),
								newPasswordOne);
						finish();
					} else {
						showToast(getString(R.string.toast_more_reset_request_failed));
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
}

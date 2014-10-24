package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

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
				Toast.makeText(_this, "原密码错误！", Toast.LENGTH_SHORT).show();
			} else {
				if (!newPasswordOne.equals(newPasswordTwo)) {
					Toast.makeText(_this, "俩次输入的密码不一致！", Toast.LENGTH_SHORT)
							.show();
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
					Log.i("modifypwd", "修改密码" + result);
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
						Toast.makeText(_this, "修改密码成功！", Toast.LENGTH_SHORT)
								.show();
						userInfoDao.saveUserInfoPassword(
								CurrentUserHelper.getCurrentPhone(),
								newPasswordOne);
						finish();
					} else {
						Toast.makeText(_this, "修改密码失败！", Toast.LENGTH_SHORT)
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
}

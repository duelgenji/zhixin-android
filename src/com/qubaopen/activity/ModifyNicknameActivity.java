package com.qubaopen.activity;

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

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.StringFormatUtil;

public class ModifyNicknameActivity extends Activity implements OnClickListener {

	public static final String INTENT_NICKNAME = "localNickName";

	private TextView titleOfThePage;
	private EditText nicknameTextView;

	// private Context context;
	private ModifyNicknameActivity _this;
	private TextView submit;
	private ImageButton clearTextviewBtn;
	private ImageButton iBtnPageBack;
	private String nickName;
	private String localNickName;

	private long userId;
	private UserInfoDao userInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.activity_modify_nickname);

		// context = this.getApplicationContext();
		initView();
	}

	private void initView() {
		titleOfThePage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfThePage.setText(getString(R.string.head_modify_nickname));

		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		submit = (TextView) this.findViewById(R.id.submit);
		submit.setOnClickListener(this);

		clearTextviewBtn = (ImageButton) this
				.findViewById(R.id.clearTextviewBtn);
		clearTextviewBtn.setOnClickListener(new ClearTextView());

		nicknameTextView = (EditText) this.findViewById(R.id.nicknameTextView);
		localNickName = getIntent().getStringExtra(INTENT_NICKNAME);
		if (localNickName != null && !localNickName.equals("")) {
			nicknameTextView.setText(localNickName);
			nicknameTextView.setSelection(localNickName.length());
		}
	}

	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

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
					if (result != null
							&& result.getString("success").equals("1")) {
						// 。。。。。。。。。
						nicknameTextView.setText("");

						userInfoDao = new UserInfoDao();
						userInfoDao.saveUserInfoNickNameById(userId, nickName);

						showToast(getString(R.string.toast_modify_nickname_success));
						finish();
					} else {
						showToast(getString(R.string.toast_modify_nickname_failed));
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

	private class ClearTextView implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			nicknameTextView.setText("");

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.clearTextviewBtn:
			nicknameTextView.setText("");
			v.setEnabled(true);
			break;
		case R.id.submit:
			if (nicknameTextView.getText() != null) {
				nickName = nicknameTextView.getText().toString();
				if (!nickName.equals("")) {
					if (StringFormatUtil.caculateStringLength(nickName) <= 14) {
						if (!nickName.equals(localNickName)) {
							String requestUrl = SettingValues.URL_PREFIX
									+ ModifyNicknameActivity.this
											.getString(R.string.URL_USER_INFO_UPDATE);
							JSONObject jsonParams = new JSONObject();
							userId = CurrentUserHelper.getCurrentUserId();
							try {
								jsonParams.put("nickName", nickName);
								jsonParams.put("id", userId);

							} catch (JSONException e) {
								e.printStackTrace();
							}
							new LoadDataTask().execute(1, requestUrl,
									jsonParams, HttpClient.TYPE_PUT_JSON);
						} else {
							showToast(getString(R.string.toast_input_new_nickname));
						}

					} else {
						showToast(_this
								.getString(R.string.toast_nickname_length_too_long));
					}
				} else {
					showToast(_this.getString(R.string.nickname_empty));
				}

			} else {
				showToast(_this.getString(R.string.nickname_empty));
			}

			submit.setEnabled(true);

			break;
		default:
			break;

		}
		v.setEnabled(true);
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

	private void showToast(String content) {
		Toast.makeText(_this, content, Toast.LENGTH_SHORT).show();
	}

}
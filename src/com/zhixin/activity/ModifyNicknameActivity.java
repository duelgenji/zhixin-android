package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.common.RequestLogic;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class ModifyNicknameActivity extends Activity implements
		OnClickListener {

	public static final String INTENT_NICKNAME = "nickName";

	private TextView titleOfThePage;
	private EditText nicknameTextView;

	private ModifyNicknameActivity _this;
	private TextView submit;
	private ImageButton clearTextviewBtn;
	private ImageButton iBtnPageBack;

	private Toast nicknameTextViewEmptyToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.activity_modify_nickname);

		titleOfThePage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfThePage.setText(getString(R.string.head_modify_nickname));

		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		submit = (TextView) this.findViewById(R.id.submit);
		submit.setOnClickListener(new SubmitAction());

		clearTextviewBtn = (ImageButton) this
				.findViewById(R.id.clearTextviewBtn);
		clearTextviewBtn.setOnClickListener(new ClearTextView());

		nicknameTextView = (EditText) this.findViewById(R.id.nicknameTextView);
		String nickName = getIntent().getStringExtra(INTENT_NICKNAME);
		if (nickName != null && !nickName.equals("")) {
			nicknameTextView.setText(nickName);
			nicknameTextView.setSelection(nickName.length());
		}
	}

	private class SubmitAction implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			String nickname = nicknameTextView.getText().toString();
			if (nickname.equals(getIntent().getStringExtra(INTENT_NICKNAME))) {
				onBackPressed();
				v.setEnabled(true);
			}
			if (!StringUtils.isBlank(nickname)) {

				if (nickname.getBytes().length <= 21) {

					// do the things
					submit.setEnabled(false);
					//得到后台接口
					String requestUrl = SettingValues.URL_PREFIX
							+ ModifyNicknameActivity.this
									.getString(R.string.URL_MODIFY_NICKNAME);
					JSONObject jsonParams = new JSONObject();
					try {
						jsonParams.put("nickname", nickname);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					HttpClient.request(requestUrl, jsonParams,
							new RequestLogic() {

								@Override
								public void onLoading(long count, long current) {
								}

								@Override
								public void whenSuccess(JSONObject result) {
									ModifyNicknameActivity.this.onBackPressed();
									submit.setEnabled(true);
								}

								@Override
								public void whenFail(JSONObject message) {

									try {
										String errMessage = message
												.getString("message");
										if (StringUtils
												.isAsciiPrintable(errMessage)) {
											errMessage = ErrHashMap.getErrMessage(message
													.getString("message"));
										}

										Toast.makeText(_this, errMessage, 3)
												.show();
									} catch (JSONException e) {
										e.printStackTrace();
									} finally {
										submit.setEnabled(true);
									}
								}

								@Override
								public void whenRequestFail(String errcode) {
									submit.setEnabled(true);

								}

							});

					submit.setEnabled(true);
				} else {
					showToast(_this
							.getString(R.string.toast_nickname_length_too_long));
				}
			} else {
				if (nicknameTextViewEmptyToast == null) {
					nicknameTextViewEmptyToast = Toast.makeText(
							ModifyNicknameActivity.this,
							ModifyNicknameActivity.this
									.getString(R.string.nickname_empty), 3);
				}
				nicknameTextViewEmptyToast.show();

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

		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.clearTextviewBtn:
			nicknameTextView.setText("");
			v.setEnabled(true);
			break;
		default:
			break;

		}
		v.setEnabled(true);
	}

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

	private void showToast(String content) {
		Toast.makeText(_this, content, 3).show();
	}

}
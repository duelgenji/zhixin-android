package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
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

public class ModifySignatureActivity extends Activity implements
		OnClickListener {

	public static final String INTENT_SIGNATURE = "localSignature";

	private TextView titleOfThePage;
	private EditText signatureTextView;

	private Context context;
	private ModifySignatureActivity _this;
	private TextView submit;
	private ImageButton clearTextviewBtn;
	private ImageButton iBtnPageBack;
	private Toast signatureTextViewEmptyToast;
	private String localSignature;
	private String signature;
	private long userId;

	private UserInfoDao userInfoDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_this = this;
		setContentView(R.layout.activity_modify_signature);

		context = this.getApplicationContext();
		initView();

	}

	private void initView() {
		titleOfThePage = (TextView) this.findViewById(R.id.title_of_the_page);
		titleOfThePage.setText(getString(R.string.head_modify_signature));

		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		submit = (TextView) this.findViewById(R.id.submit);
		submit.setOnClickListener(this);

		clearTextviewBtn = (ImageButton) this
				.findViewById(R.id.clearTextviewBtn);
		clearTextviewBtn.setOnClickListener(new ClearTextView());

		signatureTextView = (EditText) this
				.findViewById(R.id.signatureTextView);
		localSignature = getIntent().getStringExtra(INTENT_SIGNATURE);
		if (localSignature != null && !localSignature.equals("")) {
			signatureTextView.setText(localSignature);
			signatureTextView.setSelection(localSignature.length());
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
					if (result != null
							&& result.getString("success").equals("1")) {
						// 。。。。。。。。。
						signatureTextView.setText("");

						userInfoDao = new UserInfoDao();
						userInfoDao
								.saveUserInfoSignatureById(userId, signature);

						Toast.makeText(_this, "修改签名成功！", Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(_this, "修改失败！", Toast.LENGTH_SHORT)
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

	private class ClearTextView implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			signatureTextView.setText("");

		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.clearTextviewBtn:
			signatureTextView.setText("");
			v.setEnabled(true);
			break;
		case R.id.submit:
			signature = signatureTextView.getText().toString();
			if (signature.equals(localSignature)) {
				onBackPressed();
				v.setEnabled(true);
			}
			if (!StringUtils.isBlank(signature)) {
				if (!(signature == null && signature.equals(""))) {
					if (signature.getBytes().length <= 21) {
						// do the things
						// submit.setEnabled(false);
						// 得到后台接口
						String requestUrl = SettingValues.URL_PREFIX
								+ ModifySignatureActivity.this
										.getString(R.string.URL_USER_INFO_UPDATE);
						JSONObject jsonParams = new JSONObject();
						userId = CurrentUserHelper.getCurrentUserId();
						try {
							jsonParams.put("signature", signature);
							jsonParams.put("id", userId);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						new LoadDataTask().execute(1, requestUrl, jsonParams,
								HttpClient.TYPE_PUT_JSON);

						submit.setEnabled(true);
					} else {
						showToast(_this
								.getString(R.string.toast_signature_length_too_long));
					}
				} else {
					showToast(_this
							.getString(R.string.toast_validate_code_not_empty));
				}
			} else {
				if (signatureTextViewEmptyToast == null) {
					signatureTextViewEmptyToast = Toast.makeText(
							ModifySignatureActivity.this,
							ModifySignatureActivity.this
									.getString(R.string.signature_empty), 3);
				}
				signatureTextViewEmptyToast.show();
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
		Toast.makeText(_this, content, 3).show();
	}

}
package com.qubaopen.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.datasynservice.SubmitOnlyService;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

/**
 * Created by duel on 14-3-20.
 */
public class MoreSuggestionActivity extends FragmentActivity implements
		View.OnClickListener {

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;

	private MoreSuggestionActivity _this;
	private SubmitOnlyService submitOnlyService;

	private EditText txtContentSuggestion;
	private EditText txtContactSuggestion;
	private Button btnSubmitSuggestion;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_suggestion);

		_this = this;
		context = this.getApplicationContext();
		submitOnlyService = new SubmitOnlyService(this);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_more_suggestion));
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);

		txtContentSuggestion = (EditText) this
				.findViewById(R.id.txtContentSuggestion);
		txtContactSuggestion = (EditText) this
				.findViewById(R.id.txtContactSuggestion);

		btnSubmitSuggestion = (Button) this
				.findViewById(R.id.btnSubmitSuggestion);
		btnSubmitSuggestion.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.btnSubmitSuggestion:
			String content = txtContentSuggestion.getText().toString().trim();
			String contactMethod = txtContactSuggestion.getText() != null ? txtContactSuggestion
					.getText().toString() : "";
			if (!content.equals("")) {
				if (!contactMethod.equals("")) {
					try {
						sendSuggestion(content, contactMethod);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else {
					showToast(this
							.getString(R.string.toast_input_contact_cant_be_null));
					btnSubmitSuggestion.setEnabled(true);
				}
			} else {
				showToast(this.getString(R.string.toast_input_cant_be_null));
				btnSubmitSuggestion.setEnabled(true);
			}
			btnSubmitSuggestion.setEnabled(true);
			break;
		default:
			break;
		}
	}

	private void sendSuggestion(String content, String contactMethod)
			throws ParseException {
		JSONObject params = new JSONObject();
		try {
			params.put("content", content);
			params.put("contactMethod", contactMethod);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_MORE_SUGGESTION);

//		Log.i("suggestion", "提交内容:......" + obj);
		new LoadDataTask1().execute(1, requestUrl, params,
				HttpClient.TYPE_POST_FORM);
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
					Log.i("suggestion", "提交结果:......" + result);
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
					if (result.getString("success").equals("1")) {
						txtContentSuggestion.setText("");
						txtContactSuggestion.setText("");
						Toast.makeText(_this, "提交成功！", Toast.LENGTH_SHORT)
								.show();
						finish();
					} else {
						Toast.makeText(_this, "提交失败!", Toast.LENGTH_SHORT)
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

	// private void sendSuggestion(){
	// String sContent=txtContentSuggestion.getText().toString().trim();
	// if(sContent.equals("")){
	// showToast(this.getString(R.string.toast_input_cant_be_null));
	// btnSubmitSuggestion.setEnabled(true);
	// return;
	// }
	// String sContact= txtContactSuggestion.getText() != null ?
	// txtContactSuggestion.getText().toString() : "";
	//
	// if(sContact.equals("")){
	// showToast(this.getString(R.string.toast_input_contact_cant_be_null));
	// btnSubmitSuggestion.setEnabled(true);
	// return;
	// }
	// try {
	// JSONObject jsonParams = new JSONObject();
	// jsonParams.put("sContent", sContent);
	// jsonParams.put("sBack", sContact);
	// jsonParams.put("iLx", "0");
	// new LoadDataTask().execute(jsonParams);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// private class LoadDataTask extends AsyncTask<JSONObject, Void,
	// JSONObject> {
	// @Override
	// protected JSONObject doInBackground(JSONObject... params) {
	// try {
	// return submitOnlyService.moreSuggestion(params[0]);
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONObject result) {
	// try {
	// if (result != null && result.getString("success").equals("1")) {
	// showToast(_this.getString(R.string.toast_thanks_for_your_support));
	// _this.onBackPressed();
	// }else if(result.getString("success").equals("0")){
	// String context= ErrHashMap.getErrMessage(result.getString("message"));
	// context= context==null? _this.getString(R.string.toast_unknown):context;
	// showToast(context);
	// }
	// btnSubmitSuggestion.setEnabled(true);
	//
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	private void showToast(String content) {
		Toast.makeText(this, content, 3).show();
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

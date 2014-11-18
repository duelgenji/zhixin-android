package com.qubaopen.dialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.daos.UserMoodInfoDao;
import com.qubaopen.datasynservice.UserService;
import com.qubaopen.domain.UserMoodInfo;

/**
 * Created by duel on 14-3-27.
 */
public class SetMoodDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private EditText moodMessage;
	private TextView btnOk;
	private TextView btnCancel;
	private int type;
	private String message;

	private UserService userService;
	private UserInfoDao userInfoDao;
	private UserMoodInfo userMoodInfo;
	private UserMoodInfoDao userMoodInfoDao;

	public SetMoodDialog(Context context, int type) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.context = context;
		this.type = type;
		userInfoDao = new UserInfoDao();
		userMoodInfo = new UserMoodInfo();
		userMoodInfoDao = new UserMoodInfoDao();
		init();
	}

	public void init() {

		this.setContentView(R.layout.dialog_set_mood);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		Window window = this.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.dimAmount = 0.7f;
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		window.setAttributes(lp);
		userService = new UserService(context);
		moodMessage = (EditText) this
				.findViewById(R.id.dialog_set_mood_message);
		btnOk = (TextView) this.findViewById(R.id.dialog_set_mood_confirm);
		btnCancel = (TextView) this.findViewById(R.id.dialog_set_mood_cancel);

		btnCancel.setOnClickListener(this);
		btnOk.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_set_mood_confirm:
			new LoadDataTask().execute(0,type);
			break;
		case R.id.dialog_set_mood_cancel:

			break;
		default:
			break;
		}
		this.dismiss();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	// 用户心情 获取以及设置
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			int type = (Integer) params[1];
			try {
				switch (syncType) {
				case 0:
					// setMood 设置用户心情
					message = moodMessage.getText().toString();
					result = userService.setMood(type, message);
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
				Log.i("getIndexInfo", "mood+others..." + result);
				Integer syncType = result.getInt("syncType");
				if (result != null && result.getInt("success") == 1) {

					switch (syncType) {
					case 0:
						userInfoDao.saveUserIndexInfo(result);
						userMoodInfoDao.saveTodayMood(result);
						showToast(context
								.getString(R.string.toast_set_mood_success));
						break;
					default:
						break;
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	private void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
}

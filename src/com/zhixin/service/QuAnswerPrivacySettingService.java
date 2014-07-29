package com.zhixin.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;

import com.zhixin.R;
import com.zhixin.activity.QuceshiAnswerActivity;
import com.zhixin.daos.QuWjAnswerDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class QuAnswerPrivacySettingService extends IntentService {

	private QuWjAnswerDao quWjAnswerDao;

	public QuAnswerPrivacySettingService() {
		super("QuAnswerPrivacySettingService");
		quWjAnswerDao = new QuWjAnswerDao();
	}

	@Override
	protected void onHandleIntent(Intent data) {

		int wjId = data.getIntExtra(QuceshiAnswerActivity.INTENT_WJ_ID, 0);
		final int isShow = data.getIntExtra(
				QuceshiAnswerActivity.INTENT_IS_SHOW, 0);

		String requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_ANSWER_PRIVACY);
		// JSONObject jsonParams = new JSONObject();
		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("iWjId", wjId);
			jsonParams.put("isShow", isShow);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonParams);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}

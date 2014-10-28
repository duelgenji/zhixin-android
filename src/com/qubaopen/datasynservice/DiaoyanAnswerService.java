package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.daos.DiaoyanDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class DiaoyanAnswerService {

	private Context context;

	private DiaoyanDao diaoyanDao;

	public DiaoyanAnswerService(Context context) {
		this.context = context;
		diaoyanDao = new DiaoyanDao();
	}

	public Boolean getAnswer(JSONObject requestParams) throws JSONException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_SUBMIT_DIAOYAN_ANSWER);

		int wjId = requestParams.getInt("iWjId");
		JSONObject result = HttpClient.requestSync(requestUrl, requestParams);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			if (result.getString("isSuc").equals("1")) {
				return true;
			}
		} else if (result != null && result.getString("success").equals("0")) {
			return false;
		}
		return null;
	}

	public Boolean getAllHistroy(boolean refresh) throws JSONException,
			ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_HISTORY_DIAOYAN_ANSWER);
		JSONObject reuqsetParams = new JSONObject();
		if (refresh) {
			reuqsetParams.put("flag", 1);
		}

		JSONObject result = HttpClient.requestSync(requestUrl, reuqsetParams);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			diaoyanDao.saveHistoryWj(result);
			if (!refresh) {
				JSONObject resultOfCallback = HttpClient
						.requestSync(
								SettingValues.URL_PREFIX
										+ context
												.getString(R.string.URL_CONFIRM_DIAOYAN_HISTORY),
								null);
			}
			return true;

		} else if (result != null && result.getString("success").equals("0")) {
			return false;
		}
		return null;
	}

}

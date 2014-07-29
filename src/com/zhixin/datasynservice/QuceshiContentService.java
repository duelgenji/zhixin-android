package com.zhixin.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.QuContentDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class QuceshiContentService {

	private Context context;

	private QuContentDao quContentDao;

	public QuceshiContentService(Context context) {
		this.context = context;
		quContentDao = new QuContentDao();
	}

	public String saveData(int questionnareId) throws JSONException {
		if (!quContentDao.isWjExists(questionnareId)) {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_GET_XQWJ);

			JSONObject requestParams = new JSONObject();
			requestParams.put("iWjId", questionnareId);
			JSONObject result = HttpClient.requestSync(requestUrl,
					requestParams);
			if (result != null
					&& (!result.has("success") || result.getString("success")
							.equals("1"))) {
				quContentDao.saveAQuestionnare(result, questionnareId);
			} else if (result != null
					&& result.getString("success").equals("0")) {
				return result.getString("message");
			}
		}
		return null;
	}

	
	
	
}

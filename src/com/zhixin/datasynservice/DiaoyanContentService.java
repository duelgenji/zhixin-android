package com.zhixin.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.DiaoyanContentDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class DiaoyanContentService {


	private Context context;

	private DiaoyanContentDao diaoyanContentDao;

	public DiaoyanContentService(Context context) {
		this.context = context;
		diaoyanContentDao = new DiaoyanContentDao();
	}

	public String saveData(int questionnareId) throws JSONException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_DIAOYAN_WJ);

		JSONObject requestParams = new JSONObject();
		requestParams.put("wjId", questionnareId);
		JSONObject result = HttpClient.requestSync(requestUrl, requestParams);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			diaoyanContentDao.saveAQuestionnare(result, questionnareId);
		} else if (result != null && result.getString("success").equals("0")) {
			return result.getString("message");
		}
		return null;
	}
}

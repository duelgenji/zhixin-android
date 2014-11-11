package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class InterestHistoryService {

	private Context context;

	public InterestHistoryService(Context context) {
		this.context = context;
	}

	public JSONObject getInterestList(Integer type,Integer historyId) throws JSONException,
			ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_INTEREST_HISTORY);
		Log.i("interestHistory", "requestUrl>>>>>>" + requestUrl);
		JSONObject requestParams = new JSONObject();
		JSONObject result = new JSONObject();
		if (type != null && type != 0) {
			requestParams.put("typeId", type);
		}
		if (historyId != null && historyId != 0) {
			requestParams.put("historyId", historyId);
		}
		result = HttpClient.requestSync(requestUrl, requestParams,
				HttpClient.TYPE_POST_FORM);

		return result;
	}

}

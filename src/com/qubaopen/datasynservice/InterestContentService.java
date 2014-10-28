package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.daos.InterestContentDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class InterestContentService {

	private Context context;

	private InterestContentDao interestContentDao;

	public InterestContentService(Context context) {
		this.context = context;
		interestContentDao = new InterestContentDao();
	}

	public String getInterestContent(int interestId) throws JSONException {
		if (!interestContentDao.isInterestExists(interestId)) {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_INTEREST_GET_QUESTION);

			requestUrl+="/"+interestId;
			JSONObject result = HttpClient.requestSync(requestUrl,null,HttpClient.TYPE_GET);
			
			if (result != null
					&& (!result.has("success") || result.getString("success")
							.equals("1"))) {
				interestContentDao.saveInterestContent(result, interestId);
			} else if (result != null
					&& result.getString("success").equals("0")) {
				return result.getString("message");
			}
		}
		return null;
	}

	
	
	
}

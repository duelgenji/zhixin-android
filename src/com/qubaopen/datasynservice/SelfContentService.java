package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.daos.SelfContentDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class SelfContentService {
	
	private Context context;

	private SelfContentDao selfContentDao;
	
	public SelfContentService(Context context) {
		this.context = context;
		selfContentDao = new SelfContentDao();
	}
	
	public String getSelfContent(int selfId) throws JSONException {
		if (!selfContentDao.isSelfExists(selfId)) {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_SELF_GET_QUESTION);

			requestUrl+="/"+selfId;
			JSONObject result = HttpClient.requestSync(requestUrl,null,HttpClient.TYPE_GET);
			
			if (result != null
					&& (!result.has("success") || result.getString("success")
							.equals("1"))) {
				selfContentDao.saveSelfContent(result, selfId);
			} else if (result != null
					&& result.getString("success").equals("0")) {
				return result.getString("message");
			}
		}
		return null;
	}
	
}

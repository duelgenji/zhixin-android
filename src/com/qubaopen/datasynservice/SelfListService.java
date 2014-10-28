package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.daos.SelfListDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class SelfListService {
	public static class SelfListSqlMaker {

		public static String makeSql(String ids) {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from self_list");
			sqlBuffer.append(" where selfId in ( ");
			sqlBuffer.append(ids);
			sqlBuffer.append(" ) ");
			return sqlBuffer.toString();

		}
	}

	private Context context;
	private SelfListDao selfListDao;

	public SelfListService(Context context) {
		this.context = context;
		this.selfListDao = new SelfListDao();
	}

	public String requestSelfList(boolean refresh) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_SELF_GET_LIST);

			if (refresh) {
				requestUrl += "/?refresh=true";
			}
			JSONObject result;
			result = HttpClient.requestSync(requestUrl, null,
					HttpClient.TYPE_GET);

			if (result != null && result.getString("success").equals("1")) {
				selfListDao = new SelfListDao();
				return selfListDao.saveSelfList(result);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public JSONObject requestSelfListInfo(int selfId) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_SELF_GET_LIST_INFO);
		
			requestUrl+="/"+selfId;
			JSONObject result;
			result = HttpClient.requestSync(requestUrl, null,
					HttpClient.TYPE_GET);

			if (result != null && result.getString("success").equals("1")) {
				selfListDao = new SelfListDao();
				result.put("selfId",selfId);
				selfListDao.addSelfListInfo(result);
				return result;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

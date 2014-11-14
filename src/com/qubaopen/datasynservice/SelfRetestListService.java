package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class SelfRetestListService {
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

	public SelfRetestListService(Context context) {
		this.context = context;
	}
	//新接口
	public JSONObject requestSelfList(int groupId) {
		JSONObject result = null;
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_RETEST) + "/" +groupId;
			
			result = HttpClient.requestSync(requestUrl, null,
					HttpClient.TYPE_GET);
			Log.i("retest", "retest......" + result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

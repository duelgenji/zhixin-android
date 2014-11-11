package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.daos.SelfListDao;
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
	private SelfListDao selfListDao;

	public SelfRetestListService(Context context) {
		this.context = context;
		this.selfListDao = new SelfListDao();
	}
	//新接口
	public String requestSelfList(int groupId) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_RETEST) + "/" +groupId;
			JSONObject result;
			result = HttpClient.requestSync(requestUrl, null,
					HttpClient.TYPE_GET);
			Log.i("retest", "retest......" + result);
			if (result != null && result.getString("success").equals("1")) {
				selfListDao = new SelfListDao();
				return selfListDao.saveSelfList(result);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.QuListDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class QuListService {
	public static class QuceshiSqlMaker {
		public static final int ALL_TEST = 0;
		public static final int XINGE_TEST = 1;
		public static final int QINGAN_TEST = 2;
		public static final int ZHICHANG_TEST = 3;
		public static final int XINGZUO_TEST = 4;
		public static final int ZHILI_TEST = 5;
		public static final int QITA_TEST = 6;

		public static final int DEFAULT_ORDER = 0;
		public static final int TUIJIAN_ORDER = 0;
		public static final int POP_ORDER = 1;
		public static final int TIME_ORDER = 2;
		public static final int CREDIT_ORDER = 3;

		public static String makeSql(int order, int type) {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from qu_list");
			if (type != ALL_TEST) {
				sqlBuffer.append(" where type=");
				sqlBuffer.append(type);
				sqlBuffer.append(" and wjorder=");
				sqlBuffer.append(order);
				sqlBuffer.append(" and controlFlag=0");
			} else {
				sqlBuffer.append(" where wjorder=");
				sqlBuffer.append(order);
				sqlBuffer.append(" and controlFlag=0");
			}
			sqlBuffer.append(" order by _id asc");
			return sqlBuffer.toString();

		}
	}

	private Context context;
	private QuListDao quListDao;

	public QuListService(Context context) {
		this.context = context;
		this.quListDao = new QuListDao();
	}

	public String refreshData(int order, int type, boolean refresh)
			throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_QUCESHI_LIST);
		JSONObject requestParams;
		if (refresh) {
			requestParams = new JSONObject();
			if (type != 0) {
				requestParams.put("iType", String.valueOf(type));
			}
			requestParams.put("iOrderBy", String.valueOf(order));
		} else {
			requestParams = quListDao.getOldData(order, type);
		}
		JSONObject result = HttpClient.requestSync(url, requestParams);
		if (result != null && result.getString("success").equals("1")) {
			quListDao.saveDataByJson(result, order, type, refresh);

		} else if (result != null) {
			return result.getString("message");
		}
		return null;

	}

	public Boolean getHistoryWjList() throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_HISTORY_WJ);
		JSONObject requestParams = new JSONObject();
		requestParams.put("flag", "1");

		JSONObject result = HttpClient.requestSync(url, requestParams);
		if (result != null && result.getString("success").equals("1")) {
			quListDao.saveHistoryWjList(result);

			return true;
		} else if (result != null) {

		}
		return null;

	}

}

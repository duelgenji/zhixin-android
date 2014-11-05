package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.daos.InterestListDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class InterestListService {
	public static class QuceshiSqlMaker {
		// public static final int ALL_TEST = 0;
		// public static final int XINGE_TEST = 1;
		// public static final int QINGAN_TEST = 2;
		// public static final int ZHICHANG_TEST = 3;
		// public static final int XINGZUO_TEST = 4;
		// public static final int ZHILI_TEST = 5;
		// public static final int QITA_TEST = 6;

		public static final int DEFAULT_ORDER = 0;
		public static final int TUIJIAN_ORDER = 0;
		public static final int POP_ORDER = 1;
		public static final int TIME_ORDER = 2;
		public static final int CREDIT_ORDER = 3;

		public static String makeSql() {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from interest_list");
			sqlBuffer.append(" order by _id asc");
			return sqlBuffer.toString();

		}

		public static String makeSql(String ids) {
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from interest_list");
			sqlBuffer.append(" where selfId in ( ");
			sqlBuffer.append(ids);
			sqlBuffer.append(" ) ");
			return sqlBuffer.toString();

		}
	}

	private Context context;
	private InterestListDao interestListDao;

	public InterestListService(Context context) {
		this.context = context;
		this.interestListDao = new InterestListDao();
	}

	public JSONObject getInterestList(Integer type,Integer page)
			throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_INTEREST_GET_LIST);
		JSONObject requestParams = new JSONObject();
		if (type != null && type != 0) {
			requestParams.put("typeId", type);
		}
		
		if (page != null) {
			requestParams.put("page", page);
		}

		requestParams.put("size", 10);
		JSONObject result = HttpClient.requestSync(url, requestParams,
				HttpClient.TYPE_POST_FORM);
		
		Log.i("interest", "interesr......" + result);
		if (result != null && result.getString("success").equals("1")) {
			result.put("page", page);
			interestListDao.saveInterestList(result);
		}
		return result;
	}

	public JSONObject getInterestList(Integer order, Integer type, Integer page)
			throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_INTEREST_GET_LIST);
		JSONObject requestParams = new JSONObject();

		if (order != null && order != 0) {
			requestParams.put("sortTypeId", order);
		}
		if (type != null && type != 0) {
			requestParams.put("interestTypeId", type);
		}

		if (page != null) {
			requestParams.put("page", page);
		}

		requestParams.put("size", 10);

		JSONObject result = HttpClient.requestSync(url, requestParams,
				HttpClient.TYPE_POST_FORM);
		Log.i("interest", "interesr......" + result);
		if (result != null && result.getString("success").equals("1")) {

			result.put("page", page);
			interestListDao.saveInterestList(result);

		}
		return result;

	}

}

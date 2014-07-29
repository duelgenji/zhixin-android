package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zhixin.R;
import com.zhixin.daos.XinliziceListDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class XinliziceListService {

	private Context context;
	private XinliziceListDao xinliziceListDao;

	public XinliziceListService(Context context) {
		this.context = context;
		this.xinliziceListDao = new XinliziceListDao();
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
			requestParams = xinliziceListDao.getOldData(order, type);
		}
		JSONObject result = HttpClient.requestSync(url, requestParams);
		if (result != null && result.getString("success").equals("1")) {
			xinliziceListDao.saveDataByJson(result, order, type, refresh);

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
			xinliziceListDao.saveHistoryWjList(result);

			return true;
		} else if (result != null) {

		}
		return null;

	}

}

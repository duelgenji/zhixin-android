package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.HistoryDuijiangDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class HistoryDuijiangService {

	private Context context;
	private HistoryDuijiangDao historyDuijiangDao;

	public HistoryDuijiangService(Context context) {
		this.context = context;
		historyDuijiangDao = new HistoryDuijiangDao();

	}

	public Boolean refreshData() throws JSONException, ParseException {
		String url = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_HISTORY_DUIJIANG);

		JSONObject result = HttpClient.requestSync(url, null);
		if (result != null) {
			historyDuijiangDao.saveHistoryDuijiang(result);
			return true;
		}
		return false;

	}

}

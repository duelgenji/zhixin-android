package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class UserService {

	private Context context;
	private UserInfoDao userInfoDao;

	public UserService(Context context) {
		this.context = context;
		userInfoDao = new UserInfoDao();
	}

	public JSONObject qiandao() throws JSONException, ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_QIANDAO);
		JSONObject jsonParams = new JSONObject();
		JSONObject result = HttpClient.requestSync(requestUrl, null);
		if (result != null && result.getInt("success") == 1) {


		}
		return result;
	}

}

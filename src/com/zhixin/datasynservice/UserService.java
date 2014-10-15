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

	//提交性别年龄    0男1女
	public JSONObject SubmitAgeAndSex(int age,int sex) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_SUBMIT_AGE_SEX);
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("age", age);
			jsonParams.put("sex", sex);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,HttpClient.TYPE_POST_FORM);

			if (result != null && result.getInt("success") == 1) {

			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//获取每日心情
	public JSONObject getMood() {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_GET_MOOD);
			
			JSONObject result = HttpClient.requestSync(requestUrl, null,HttpClient.TYPE_GET);

			if (result != null && result.getInt("success") == 1) {

			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//提交每日心情
	public JSONObject setMood(int type) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_SET_MOOD);
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("type", type);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,HttpClient.TYPE_POST_FORM);

			if (result != null && result.getInt("success") == 1) {

			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}

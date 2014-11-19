package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class UserService {

	private Context context;

	private UserInfoDao userInfoDao;
	

	public UserService(Context context) {
		this.context = context;
		this.userInfoDao = new UserInfoDao();
		
	}

	public JSONObject qiandao() throws JSONException, ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_QIANDAO);
//		JSONObject jsonParams = new JSONObject();
		JSONObject result = HttpClient.requestSync(requestUrl, null);
		if (result != null && result.getInt("success") == 1) {

		}
		return result;
	}

	// 提交性别年龄 0男1女
	public JSONObject SubmitAgeAndSex(int age, int sex) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_SUBMIT_AGE_SEX);
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("age", age);
			jsonParams.put("sex", sex);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,
					HttpClient.TYPE_POST_FORM);

			if (result != null && result.getInt("success") == 1) {

			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 获取每日心情
	public JSONObject getIndexInfo() {
		JSONObject result = new JSONObject();
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_GET_MOOD);

			result = HttpClient.requestSync(requestUrl, null,
					HttpClient.TYPE_GET);
			// if (result != null && result.getInt("success") == 1) {
			//
			// }
			// return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 提交每日心情
	public JSONObject setMood(int type, String message) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_SET_MOOD);
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("type", type);
			jsonParams.put("message", message);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,
					HttpClient.TYPE_POST_FORM);

			if (result != null && result.getInt("success") == 1) {

			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	

	//第三方登录
	public JSONObject thirdLogin(JSONObject jsonObject) {
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_USER_THIRD_LOGON);
			JSONObject result = HttpClient.requestSync(requestUrl, jsonObject,
					HttpClient.TYPE_POST_FORM);

			if (result != null && result.getInt("success") == 1) {
				result.put("thirdToken", jsonObject.getString("token"));
				result.put("thirdType", jsonObject.getString("type"));
				userInfoDao.saveUserForFirsttime(result, "");
			}
			return result;
		} catch (JSONException e) {
			e.printStackTrace();
		}catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}

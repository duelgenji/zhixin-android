package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class RegistService {

	private Context context;
	private UserInfoDao userInfoDao;

	public RegistService(Context context) {
		this.context = context;
		userInfoDao = new UserInfoDao();
	}

	public void saveUserPassword(String password) {

		userInfoDao.saveUserInfoPassword(CurrentUserHelper.getCurrentPhone(),
				password);

	}

	public boolean logOnAction() throws JSONException, ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_USER_LOGON);
		String phone = CurrentUserHelper.getCurrentPhone();

		String password = userInfoDao.getUserPasswordByPhone(phone);
		if (password == null) {
			return false;
		}
		JSONObject jsonParams = new JSONObject();
		jsonParams.put("phone", phone);
		jsonParams.put("password", password);
//		if (CurrentUserHelper.getCurrentUserId() != 0) {
//			jsonParams.put("uuid", SettingValues.JPUSH_ALIAS_PREFIX
//					+ CurrentUserHelper.getCurrentUserId());
//		}
		JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,HttpClient.TYPE_PUT_JSON);

		if (result != null && result.getInt("success") == 1) {
			userInfoDao.saveUserForFirsttime(result, password);
		} else {
			return false;
		}
		return true;
	}

}

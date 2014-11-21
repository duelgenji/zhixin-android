package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

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
	public boolean logOnActionByThird() throws JSONException, ParseException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_USER_THIRD_LOGON);
		Log.i("RegistService", "params" + CurrentUserHelper.getCurrentThird());
		JSONObject result = HttpClient.requestSync(requestUrl, CurrentUserHelper.getCurrentThird(),
				HttpClient.TYPE_POST_FORM);

		if (result != null && result.getInt("success") == 1) {
			userInfoDao.saveUserForFirsttime(result, "");
		
		} else {
			return false;
		}
		return true;
	}
	
	public boolean logOnAction(){
		boolean result = false;
		try {
			if (CurrentUserHelper.getCurrentPhone() != null) {
				result = logOnActionByPhone();
				
			}else {
				result = logOnActionByThird();
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;

	}
	public boolean logOnActionByPhone() throws JSONException, ParseException {
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
		JSONObject result = HttpClient.requestSync(requestUrl, jsonParams,HttpClient.TYPE_POST_FORM);

		if (result != null && result.getInt("success") == 1) {
			userInfoDao.saveUserForFirsttime(result, password);
		} else {
			return false;
		}
		return true;
	}

}

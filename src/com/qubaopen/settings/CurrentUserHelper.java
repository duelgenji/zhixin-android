package com.qubaopen.settings;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.qubaopen.database.DbManager;

public class CurrentUserHelper {

	private static String CURRENT_USER_PHONE;
	private static Bitmap CURRENT_USER_BITMAP;
	private static long CURRENT_USER_ID;
	private static String CURRENT_NICKNAME;
	private static JSONObject CURRENT_THIRD;

	public static void saveCurrentThird(JSONObject params) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		try {
			editor.putString(SettingValues.KEY_CURRENT_ACTIVE_THIRD_TOKEN,
					params.getString("token"));
			editor.putString(SettingValues.KEY_CURRENT_ACTIVE_USER_NICKNAME,
					params.getString("nickName"));
			editor.putString(SettingValues.KEY_CURRENT_ACTIVE_THIRD_ICON,
					params.getString("icon"));
			editor.putInt(SettingValues.KEY_CURRENT_ACTIVE_THIRD_TYPE,
					params.getInt("type"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		editor.commit();
		CURRENT_THIRD = params;

		DbManager.initPrivateDatabase();
	}

	public static JSONObject getCurrentThird() {
		if (CURRENT_THIRD == null) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			JSONObject params = new JSONObject();
			try {
				params.put("token", sharedPref.getString(
						SettingValues.KEY_CURRENT_ACTIVE_THIRD_TOKEN, null));
				params.put("nickName", sharedPref.getString(
						SettingValues.KEY_CURRENT_ACTIVE_USER_NICKNAME, null));
				params.put("icon", sharedPref.getString(
						SettingValues.KEY_CURRENT_ACTIVE_THIRD_ICON, null));
				params.put("type", sharedPref.getInt(
						SettingValues.KEY_CURRENT_ACTIVE_THIRD_TYPE, 0));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			CURRENT_THIRD = params;
			return CURRENT_THIRD;
		} else {
			return CURRENT_THIRD;
		}
	}

	public static void saveCurrentUserId(Long userId) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(SettingValues.KEY_CURRENT_ACTIVE_USER_ID, userId);
		editor.commit();
		CURRENT_USER_ID = userId;

		DbManager.initPrivateDatabase();
	}

	public static long getCurrentUserId() {
		if (CURRENT_USER_ID == 0) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			CURRENT_USER_ID = sharedPref.getLong(
					SettingValues.KEY_CURRENT_ACTIVE_USER_ID, 0);
			return CURRENT_USER_ID;
		} else {
			return CURRENT_USER_ID;
		}
	}

	public static void saveCurrentNickName(String nickName) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_USER_NICKNAME,
				nickName);
		editor.commit();
		CURRENT_NICKNAME = nickName;

		DbManager.initPrivateDatabase();
	}

	public static String getCurrentNickName() {
		if (CURRENT_NICKNAME == null) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			CURRENT_NICKNAME = sharedPref.getString(
					SettingValues.KEY_CURRENT_ACTIVE_USER_NICKNAME, null);
			return CURRENT_NICKNAME;
		} else {
			return CURRENT_NICKNAME;
		}
	}

	public static void saveCurrentPhone(String phone) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_USER_PHONE, phone);
		editor.commit();
		CURRENT_USER_PHONE = phone;

		DbManager.initPrivateDatabase();
	}

	public static void saveBitmap(Bitmap bp) {
		CURRENT_USER_BITMAP = bp;
	}

	public static String getCurrentPhone() {
		if (CURRENT_USER_PHONE == null) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			CURRENT_USER_PHONE = sharedPref.getString(
					SettingValues.KEY_CURRENT_ACTIVE_USER_PHONE, null);
		}
		return CURRENT_USER_PHONE;
	}

	public static Bitmap getBitmap() {
		return CURRENT_USER_BITMAP;
	}

	public static void clearCurrentPhone() {
		CURRENT_USER_PHONE = null;
		CURRENT_USER_ID = 0;
		CURRENT_USER_BITMAP = null;
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_USER_PHONE, null);
		editor.commit();
		DbManager.releseDatabase();
		JPushInterface.stopPush(MyApplication.getAppContext());
	}
	public static void clearCurrentUserId() {
		CURRENT_USER_PHONE = null;
		CURRENT_USER_ID = 0;
		CURRENT_USER_BITMAP = null;
		CURRENT_THIRD = null;
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong(SettingValues.KEY_CURRENT_ACTIVE_USER_ID, 0);
		editor.commit();
		DbManager.releseDatabase();
		JPushInterface.stopPush(MyApplication.getAppContext());
	}
}

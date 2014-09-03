package com.zhixin.settings;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import cn.jpush.android.api.JPushInterface;

public class CurrentUserHelper {

	private static String CURRENT_USER_PHONE;

	private static Bitmap CURRENT_USER_BITMAP;

	private static long CURRENT_USER_ID;
	private static String CURRENT_NICKNAME;

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
			// String phone = getCurrentPhone();
			// String sql = "select * from user_info where username = '" + phone
			// + "'";
			// UserInfo user = DbManager.getDatabase().findUniqueBySql(
			// UserInfo.class, sql);
			// if (user != null) {
			// CURRENT_USER_ID = user.getUserId();
			// }
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
			// String phone = getCurrentPhone();
			// String sql = "select * from user_info where username = '" + phone
			// + "'";
			// UserInfo user = DbManager.getDatabase().findUniqueBySql(
			// UserInfo.class, sql);
			// if (user != null) {
			// CURRENT_NICKNAME = user.getNickName();
			// }
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

	// public static int getCurrentMemberId() {
	// if (CURRENT_MEMBER_ID == 0) {
	// String phone = getCurrentPhone();
	// String sql = "select * from user_info where username = '" + phone
	// + "'";
	// UserInfo user = DbManager.getDatabase().findUniqueBySql(
	// UserInfo.class, sql);
	// if (user != null) {
	// CURRENT_MEMBER_ID = user.getUserId();
	// }
	// return CURRENT_MEMBER_ID;
	// } else {
	// return CURRENT_MEMBER_ID;
	// }
	// }

	public static void clearCurrentPhone() {
		CURRENT_USER_PHONE = null;
		CURRENT_USER_ID = 0;
		CURRENT_USER_BITMAP = null;
		// Program exploit when we change the user and database,some ongoing
		// thread that use the previous user database will definitely have
		// errors
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_USER_PHONE, null);
		editor.commit();
		DbManager.releseDatabase();
		JPushInterface.stopPush(MyApplication.getAppContext());
	}
}

package com.zhixin.settings;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import cn.jpush.android.api.JPushInterface;

public class CurrentUserHelper {

	private static String CURRENT_MEMBER_PHONE;

	private static Bitmap CURRENT_MEMBER_BITMAP;

	private static int CURRENT_MEMBER_ID = 0;
	
	private static long CURRENT_USER_ID ;
	
	
	public static void saveCurrentUserId(Long userId) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong("userId", userId);
		editor.commit();
		CURRENT_USER_ID = userId;

		DbManager.initPrivateDatabase();
	}
	
	public static long getCurrentUserId() {
		if (CURRENT_USER_ID == 0) {
			String phone = getCurrentPhone();
			String sql = "select * from user_info where username = '" + phone
					+ "'";
			UserInfo user = DbManager.getDatabase().findUniqueBySql(
					UserInfo.class, sql);
			if (user != null) {
				CURRENT_USER_ID = user.getUserId();
			}
			return CURRENT_USER_ID;
		} else {
			return CURRENT_USER_ID;
		}
	}
	
	
	public static void saveCurrentPhone(String phone) {
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_MEMBER_PHONE, phone);
		editor.commit();
		CURRENT_MEMBER_PHONE = phone;

		DbManager.initPrivateDatabase();
	}

	public static void saveBitmap(Bitmap bp) {
		CURRENT_MEMBER_BITMAP = bp;
	}

	public static String getCurrentPhone() {
		if (CURRENT_MEMBER_PHONE == null) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			CurrentUserHelper.CURRENT_MEMBER_PHONE = sharedPref.getString(
					SettingValues.KEY_CURRENT_ACTIVE_MEMBER_PHONE, null);
		}
		return CURRENT_MEMBER_PHONE;
	}

	public static Bitmap getBitmap() {
		return CURRENT_MEMBER_BITMAP;
	}

//	public static int getCurrentMemberId() {
//		if (CURRENT_MEMBER_ID == 0) {
//			String phone = getCurrentPhone();
//			String sql = "select * from user_info where username = '" + phone
//					+ "'";
//			UserInfo user = DbManager.getDatabase().findUniqueBySql(
//					UserInfo.class, sql);
//			if (user != null) {
//				CURRENT_MEMBER_ID = user.getUserId();
//			}
//			return CURRENT_MEMBER_ID;
//		} else {
//			return CURRENT_MEMBER_ID;
//		}
//	}

	public static void clearCurrentPhone() {
		CURRENT_MEMBER_PHONE = null;
		CURRENT_MEMBER_ID = 0;
		CURRENT_MEMBER_BITMAP = null;
		// Program exploit when we change the user and database,some ongoing
		// thread that use the previous user database will definitely have
		// errors
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(SettingValues.KEY_CURRENT_ACTIVE_MEMBER_PHONE, null);
		editor.commit();
		DbManager.releseDatabase();
		JPushInterface.stopPush(MyApplication.getAppContext());
	}
}

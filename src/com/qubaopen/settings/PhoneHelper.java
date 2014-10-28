package com.qubaopen.settings;


import android.content.Context;
import android.content.SharedPreferences;

public class PhoneHelper {

	private static int PHONE_HEIGHT=0;
	private static int PHONE_WIDTH=0;
	private static float PHONE_DENSITY=0;
	
	public static void savePhoneHeight(int height){
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(SettingValues.KEY_PHONE_HEIGHT, height);
		editor.commit();
		PHONE_HEIGHT = height;

	}

	public static int getPhoneHeight() {
		if (PHONE_HEIGHT == 0) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			PHONE_HEIGHT = sharedPref.getInt(
					SettingValues.KEY_PHONE_HEIGHT,0);
		}
		return PHONE_HEIGHT;
	}

	public static void savePhoneWidth(int width){
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(SettingValues.KEY_PHONE_WIDTH, width);
		editor.commit();
		PHONE_WIDTH = width;

	}

	public static int getPhoneWIDTH() {
		if (PHONE_WIDTH == 0) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			PHONE_WIDTH = sharedPref.getInt(
					SettingValues.KEY_PHONE_WIDTH,0);
		}
		return PHONE_WIDTH;
	}

	public static void savePhoneDensity(float density){
		SharedPreferences sharedPref = MyApplication.getAppContext()
				.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putFloat(SettingValues.KEY_PHONE_DENSITY, density);
		editor.commit();
		PHONE_DENSITY = density;

	}

	public static float getPhoneDensity() {
		if (PHONE_DENSITY == 0) {
			SharedPreferences sharedPref = MyApplication.getAppContext()
					.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
							Context.MODE_PRIVATE);
			PHONE_DENSITY = sharedPref.getInt(
					SettingValues.KEY_PHONE_DENSITY,0);
		}
		return PHONE_DENSITY;
	}

}

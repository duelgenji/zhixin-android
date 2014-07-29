package com.zhixin.settings;

import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

	private static Context context;

	public void onCreate() {
		super.onCreate();
		MyApplication.context = getApplicationContext();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())

		.build();
		ImageLoader.getInstance().init(config);

		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

		JPushInterface.setSilenceTime(context, 22, 0, 9, 0);

	}

	public static Context getAppContext() {
		return MyApplication.context;
	}
}
package com.zhixin.service;

import java.util.Set;

import com.zhixin.daos.UserSettingsDao;
import com.zhixin.domain.UserSettings;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.MyApplication;
import com.zhixin.settings.SettingValues;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class SettingJpushAliasService extends IntentService {

	private boolean onGoing;
	private boolean shouldStop;

	public SettingJpushAliasService() {
		super("SettingJpushAliasService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final String memberId = CurrentUserHelper.getCurrentMemberId() + "";
		UserSettings userSettings = new UserSettingsDao().getUserSettings();

		if (userSettings != null) {
			int isTs = userSettings.getIsTs();

			if (isTs == 1) {
				if (JPushInterface.isPushStopped(MyApplication.getAppContext())) {

					JPushInterface.resumePush(MyApplication.getAppContext());
				}
			} else {
				JPushInterface.stopPush(MyApplication.getAppContext());
			}
		} else {

			if (JPushInterface.isPushStopped(MyApplication.getAppContext())) {
				JPushInterface.resumePush(MyApplication.getAppContext());
			}
		}

		onGoing = false;
		shouldStop = false;
		while (!shouldStop) {
			if (!onGoing) {
				onGoing = true;
				JPushInterface.setAliasAndTags(this,
						SettingValues.JPUSH_ALIAS_PREFIX + memberId, null,
						new TagAliasCallback() {

							@Override
							public void gotResult(int statusCode, String arg1,
									Set<String> arg2) {
								Log.i("setting alias status code", statusCode
										+ "");
								if (statusCode == 0) {
									shouldStop = true;
								} else {
									onGoing = false;
									try {
										Thread.sleep(10000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						});

			}
		}

	}

}

package com.qubaopen.service;

import java.util.Set;

import com.qubaopen.daos.UserSettingsDao;
import com.qubaopen.domain.UserSettings;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;

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
//		final String memberId = CurrentUserHelper.getCurrentMemberId() + "";
		final String userId = CurrentUserHelper.getCurrentUserId() + "";
		UserSettings userSettings = new UserSettingsDao().getUserSettings(CurrentUserHelper.getCurrentUserId());

		if (userSettings != null) {
			Boolean isTs = userSettings.isPush();

			if (isTs) {
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
						SettingValues.JPUSH_ALIAS_PREFIX + userId, null,
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

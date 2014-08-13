package com.zhixin.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import cn.jpush.android.api.JPushInterface;

import com.zhixin.R;
import com.zhixin.daos.UserSettingsDao;
import com.zhixin.settings.MyApplication;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

/**
 * Created by duel on 14-3-19.
 */
public class UserSettingIntentService extends IntentService {

	private UserSettingsDao userSettingsDao;

	public UserSettingIntentService() {
		super("UserSettingIntentService");
		userSettingsDao = new UserSettingsDao();
	}

	@Override
	protected void onHandleIntent(Intent data) {

		data.getComponent();
		String requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_MORE_SETTINGS);
		// JSONObject jsonParams = new JSONObject();
		try {
			JSONObject jbo = new JSONObject(data.getStringExtra("json"));
			jbo.remove("sStartTime");
			jbo.remove("sEndTime");
			jbo.remove("isTs");
			jbo.remove("isHygkwj");
			jbo.remove("isSllms");
			JSONObject orjbo = new JSONObject(data.getStringExtra("json"));

			JSONObject result = HttpClient.requestSync(requestUrl, jbo);
			if (result != null && result.getString("success").equals("1")) {
				userSettingsDao.updateSettings(orjbo);
			}

			String[] startTime = orjbo.getString("sStartTime").split(":");
			String[] endTime = orjbo.getString("sEndTime").split(":");
			
			JPushInterface.setSilenceTime(MyApplication.getAppContext(),
					Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]),
					Integer.parseInt(startTime[0]),
					Integer.parseInt(startTime[1]));
			
			int isTsValue = orjbo.getInt("isTs");
			if (isTsValue == 1){
				if (JPushInterface.isPushStopped(MyApplication.getAppContext())){
					
					JPushInterface.resumePush(MyApplication.getAppContext());
				}
			}else{
				JPushInterface.stopPush(MyApplication.getAppContext());
			}
			
			

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}

package com.zhixin.daos;

import java.text.ParseException;

import net.tsz.afinal.FinalDb;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;
import com.zhixin.domain.UserSettings;

public class UserSettingsDao {
	private final FinalDb db = DbManager.getDatabase();

	public void saveUserSettings(JSONObject jbo, long userId)
			throws JSONException, ParseException {
		// Log.i("要存储的设置", jbo + "");
		UserSettings us = null;
		if (db.tableExists(UserInfo.class)) {
			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
		}
		if (us == null) {
			us = new UserSettings();
			setContent(jbo, us);
			us.setUserId(userId);
			db.save(us);
		} else {
			setContent(jbo, us);
			db.update(us);
		}
		// Log.i("更新本地设置", us + "");
	}

	public void saveUserSettingsFront(JSONObject jbo, long userId)
			throws JSONException, ParseException {
		// Log.i("要存储的前二设置", jbo + "");
		UserSettings us = null;

		if (db.tableExists(UserInfo.class)) {
			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
		}
		if (us == null) {
			us = new UserSettings();
			setContent(jbo, us);
			us.setUserId(userId);
			db.save(us);
		} else {
			setContent(jbo, us);
			db.update(us);
		}
		// Log.i("更新本地设置前二", us + "");

	}

	public void saveUserSettingsBehind(JSONObject jbo, long userId)
			throws JSONException, ParseException {
		// Log.i("要存储的后二设置", jbo + "");
		UserSettings us = null;

		if (db.tableExists(UserInfo.class)) {
			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
		}
		if (us == null) {
			us = new UserSettings();
			us.setUserId(userId);
			setContent(jbo, us);
			db.save(us);
		} else {
			setContent(jbo, us);
			db.update(us);
		}
		// Log.i("更新本地设置后二", us + "");

	}

	private void setContent(JSONObject jbo, UserSettings us)
			throws JSONException, ParseException {
		// 是否推送
		if (jbo.has("push") && StringUtils.isNotEmpty(jbo.getString("push"))) {
			us.setPush(jbo.getBoolean("push"));
		}
		// 开始时间
		if (jbo.has("startTime")
				&& StringUtils.isNotEmpty(jbo.getString("startTime"))) {
			us.setStatTime(jbo.getString("startTime"));
		}
		// 结束时间
		if (jbo.has("endTime")
				&& StringUtils.isNotEmpty(jbo.getString("endTime"))) {
			us.setEndTime(jbo.getString("endTime"));
		}
		// 是否向好友公开问卷
		if (jbo.has("publicAnswersToFriend")
				&& StringUtils.isNotEmpty(jbo
						.getString("publicAnswersToFriend"))) {
			us.setPublicAnswersToFriend(jbo.getBoolean("publicAnswersToFriend"));
		} else

		// 是否开启省流量模式
		if (jbo.has("saveFlow")
				&& StringUtils.isNotEmpty(jbo.getString("saveFlow"))) {
			us.setSaveFlow(jbo.getBoolean("saveFlow"));
		}
	}

	public UserSettings getUserSettings(long userId) {
		return db.findUniqueByWhere(UserSettings.class, "userId='" + userId
				+ "'");
	}

}

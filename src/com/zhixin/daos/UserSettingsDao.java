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

	public void saveUserSettings(JSONObject jbo)  {
		try {
			Log.i("设置信息json", jbo + "");
			Long userId;

			userId = jbo.getLong("id");

			UserSettings us = null;

			if (db.tableExists(UserInfo.class)) {

				us = db.findUniqueByWhere(UserSettings.class, "userId='"
						+ userId + "'");

			}
			if (us == null) {
				us = new UserSettings();
				setContent(jbo, us);
				db.save(us);
			} else {
				setContent(jbo, us);
				db.update(us);
			}
			Log.i("更新本地设置", us + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setContent(JSONObject jbo, UserSettings us)
			throws JSONException, ParseException {
		// 是否推送
		if (StringUtils.isNotEmpty(jbo.getString("push"))) {
			us.setPush(jbo.getBoolean("push"));
		}

		// 是否向好友公开问卷
		if (StringUtils.isNotEmpty(jbo.getString("publicAnswersToFriend"))) {
			us.setPublicAnswersToFriend(jbo.getBoolean("publicAnswersToFriend"));
		}

		// 是否开启省流量模式
		if (StringUtils.isNotEmpty(jbo.getString("saveFlow"))) {
			us.setSaveFlow(jbo.getBoolean("saveFlow"));
		}

		// 开始时间
		if (StringUtils.isNotEmpty(jbo.getString("startTime"))) {
			us.setStatTime(jbo.getString("startTime"));
		}
		// 结束时间
		if (StringUtils.isNotEmpty(jbo.getString("endTime"))) {
			us.setEndTime(jbo.getString("endTime"));
		}
	}

	public UserSettings getUserSettings(long userId) {

		return db.findUniqueByWhere(UserSettings.class, "userId='" + userId
				+ "'");

	}

}

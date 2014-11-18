package com.qubaopen.daos;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.UserMoodInfo;
import com.qubaopen.settings.CurrentUserHelper;

/**
 * Created by duel on 14-3-26.
 */
public class UserMoodInfoDao {
	private final FinalDb db = DbManager.getDatabase();
	long userId = CurrentUserHelper.getCurrentUserId();

	public void saveUserMoodInfo(JSONObject jbo) throws JSONException {

		JSONArray data = jbo.getJSONArray("moodList");
		JSONObject tempJson;

		UserMoodInfo userMoodInfo = null;

		for (int i = 0; i < data.length(); i++) {
			tempJson = data.getJSONObject(i);
			String moodDate = tempJson.getString("date");
			if (db.tableExists(UserMoodInfo.class)) {
				userMoodInfo = db.findUniqueByWhere(UserMoodInfo.class,
						"userId='" + userId + "'" + "and moodDate='" + moodDate
								+ "'");
				if (userMoodInfo == null) {
					userMoodInfo = new UserMoodInfo();
					userMoodInfo.setUserId(userId);
					userMoodInfo.setMoodDate(tempJson.getString("date"));
					userMoodInfo.setMoodId(tempJson.getInt("mood"));
					if (tempJson.has("message")
							&& !tempJson.getString("message").equals("")) {
						userMoodInfo.setMoodMessage(tempJson
								.getString("message"));
					}

					DbManager.getDatabase().save(userMoodInfo);
				} else {
					userMoodInfo.setMoodDate(tempJson.getString("date"));
					userMoodInfo.setMoodId(tempJson.getInt("mood"));
					if (tempJson.has("message")
							&& !tempJson.getString("message").equals("")) {
						userMoodInfo.setMoodMessage(tempJson
								.getString("message"));
					}
					DbManager.getDatabase().update(userMoodInfo);
				}
			}

		}

	}

	@SuppressLint("SimpleDateFormat")
	public void saveTodayMood(JSONObject jbo) throws JSONException {
		UserMoodInfo userMoodInfo = null;
		String moodDate = (new SimpleDateFormat("yyyy-MM-dd"))
				.format(new Date());
		if (db.tableExists(UserMoodInfo.class)) {
			userMoodInfo = db.findUniqueByWhere(UserMoodInfo.class, "userId='"
					+ userId + "'" + "and moodDate='" + moodDate + "'");
			if (userMoodInfo == null) {
				userMoodInfo = new UserMoodInfo();
				userMoodInfo.setUserId(userId);
				userMoodInfo.setMoodDate(moodDate);
				userMoodInfo.setMoodId(jbo.getInt("moodType"));
				userMoodInfo.setMoodMessage(jbo.getString("message"));

				DbManager.getDatabase().save(userMoodInfo);
			} else {
				userMoodInfo.setMoodId(jbo.getInt("moodType"));
				userMoodInfo.setMoodMessage(jbo.getString("message"));
				DbManager.getDatabase().update(userMoodInfo);
			}
		}
	}

	public UserMoodInfo getUserMoodInfo(String date) {
		UserMoodInfo userMoodInfo = null;
		if (db.tableExists(UserMoodInfo.class)) {
			userMoodInfo = db.findUniqueByWhere(UserMoodInfo.class, "userId='"
					+ userId + "'" + "and moodDate='" + date + "'");
		}
		return userMoodInfo;

	}
}

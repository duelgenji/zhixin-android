package com.qubaopen.daos;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalDb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.InterestList;
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
		deleteByUserId(userId);

		for (int i = 0; i < data.length(); i++) {
			tempJson = data.getJSONObject(i);
			if (db.tableExists(UserMoodInfo.class)) {

				userMoodInfo = new UserMoodInfo();
				userMoodInfo.setUserId(userId);
				userMoodInfo.setMoodDate(tempJson.getString("date"));
				userMoodInfo.setMoodId(tempJson.getInt("mood"));
				if (tempJson.has("message")
						&& !tempJson.getString("message").equals("")) {
					userMoodInfo.setMoodMessage(tempJson.getString("message"));
				}

				DbManager.getDatabase().save(userMoodInfo);

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
	
	public List<UserMoodInfo> getUserMoodInfoList(String date) {
		date=date.substring(0,7);
		List<UserMoodInfo> list = null;
		if (db.tableExists(UserMoodInfo.class)) {
			list = db.findAllByWhere(UserMoodInfo.class, "userId='"
					+ userId + "'" + "and moodDate like '" + date + "%' ");
		}
		return list;

	}

	private void deleteByUserId(long userId) {
		if (DbManager.getDatabase().tableExists(UserMoodInfo.class)) {
			String sql = "delete from user_mood_info where userId=" + userId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}
}

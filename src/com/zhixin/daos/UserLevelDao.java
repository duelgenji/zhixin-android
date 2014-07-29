package com.zhixin.daos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;
import com.zhixin.domain.UserLevel;
import com.zhixin.settings.CurrentUserHelper;


public class UserLevelDao {

	public LevelAndName getLevel(int historyCredit) {
		String sql = "select * from user_level where zdjf >= " + historyCredit
				+ " order by zdjf asc limit 1 offset 0";
		UserLevel ul = DbManager.getPublicDatabase().findUniqueBySql(
				UserLevel.class, sql);
		if ((ul.getZdjf() - historyCredit) == 0) {
			return new LevelAndName(ul.get_id(), ul.getMc());
		} else {
			sql = "select * from user_level where _id =" + (ul.get_id() - 1);

			UserLevel ulMinus = DbManager.getPublicDatabase().findUniqueBySql(
					UserLevel.class, sql);
			return new LevelAndName(ulMinus.get_id(), ulMinus.getMc());
		}

	}

	public float getPercentagePoints(int historyCredit) {
		UserInfo user = DbManager.getDatabase().findUniqueByWhere(
				UserInfo.class,
				"username='" + CurrentUserHelper.getCurrentPhone() + "'");

		String sql = "select * from user_level " + " where _id="
				+ user.getLevel() + " order by zdjf asc limit 1 offset 0";
		UserLevel ulMin = DbManager.getPublicDatabase().findUniqueBySql(
				UserLevel.class, sql);
		sql = "select * from " + " user_level where  _id="
				+ (ulMin.get_id() + 1) + " order by zdjf asc limit 1 offset 0";
		UserLevel ulMax = DbManager.getPublicDatabase().findUniqueBySql(
				UserLevel.class, sql);

		float rate = (float) (historyCredit - ulMin.getZdjf())
				/ (float) (ulMax.getZdjf() - ulMin.getZdjf());

		return (rate * 100);
	}

	public void saveAllInfo(String sql) {
		try {
			JSONObject jbo = new JSONObject(sql);
			JSONArray a = jbo.getJSONArray("a");
			JSONObject jboInA;
			UserLevel ul;
			for (int i = 0; i < a.length(); i++) {
				jboInA = a.getJSONObject(i);
				ul = new UserLevel();
				ul.setZdjf(jboInA.getInt("zdjf"));
				ul.setBbslXz(jboInA.getInt("bbslXz"));
				ul.setMc(jboInA.getString("mc"));
				DbManager.getPublicDatabase().save(ul);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public class LevelAndName {
		private int level;

		private String name;

		public LevelAndName(int level, String name) {
			super();
			this.level = level;
			this.name = name;
		}

		public int getLevel() {
			return level;
		}

		public String getName() {
			return name;
		}

	}

}

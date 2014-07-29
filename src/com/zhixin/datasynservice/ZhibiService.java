package com.zhixin.datasynservice;

import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.domain.UserInfo;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;


public class ZhibiService {
	public static class QushejiaoSqlMaker {

		public static String makeSql(String phone) {
			return "select distinct * from user_info where username='" + phone
					+ "'";
		}

	}

	private Context context;

	private UserInfoDao userInfoDao;

	public ZhibiService(Context context) {
		this.context = context;
		userInfoDao = new UserInfoDao();
	}

	public Void saveQushejiaoInfo() {

		UserInfo user = userInfoDao.getUserByphone(CurrentUserHelper
				.getCurrentPhone());
		try {
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_QUSHEJIAO_SHOUYE);
			JSONObject info = HttpClient.requestSync(requestUrl, null);
			if (info != null && info.getString("success").equals("1")) {
				user = userInfoDao.saveUserZhibiInfo(
						CurrentUserHelper.getCurrentPhone(), info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

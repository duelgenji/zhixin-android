package com.zhixin.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.QuWjAnswerDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;

public class QuceshiAnswerService {
	private Context context;
	private QuWjAnswerDao quWjAnswerDao;

	public static class QuceshiAnswerSqlMaker {

		public static String makeSql(int memberId, int wjId) {
			String sql = "select * from qu_user_wj_answer " + "where memberId="
					+ memberId + " and wjId=" + wjId;
			return sql;
		}

		public static String makeSql(int wjId) {
			String sql = "select * from qu_wj_answer_list " + "where wjId="
					+ wjId + " order by _id asc";
			return sql;
		}

		public static String makeFriendSql(int wjId, String choiceNo) {
			String sql = "select * from qu_friend_answer" + " where wjId="
					+ wjId + " and choiceNo='" + choiceNo + "'";
			return sql;
		}

	}

	public QuceshiAnswerService(Context context) {
		this.context = context;
		quWjAnswerDao = new QuWjAnswerDao();

	}

	public String getAnswer(JSONObject requestParams) throws JSONException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_SUBMIT_ANSWER);

		int wjId = requestParams.getInt("questionnaireId");
		JSONObject result = HttpClient.requestSync(requestUrl, requestParams);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			quWjAnswerDao.saveMyAnswer(result, wjId);
		} else if (result != null && result.getString("success").equals("0")) {
			return result.getString("message");
		}
		return null;
	}

	public String getFriendAnswer(int wjId) throws JSONException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_FRIEND_ANSWER);

		JSONObject requestParams = new JSONObject();
		requestParams.put("iWjId", wjId);

		JSONObject result = HttpClient.requestSync(requestUrl, requestParams);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			quWjAnswerDao.saveFriendAnswers(result, wjId);
		} else if (result != null && result.getString("success").equals("0")) {
			return result.getString("message");
		}

		return null;
	}
}

package com.zhixin.daos;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.QuFriendAnswer;
import com.zhixin.domain.QuList;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.QuUserWjAnswer;
import com.zhixin.domain.QuWjAnswerList;
import com.zhixin.settings.CurrentUserHelper;

public class QuWjAnswerDao {

	public void saveMyAnswer(JSONObject jbo, int wjId) throws JSONException {
		deleteAll(wjId);
		QuUserWjAnswer ans = new QuUserWjAnswer();
		QuList wj = DbManager.getDatabase().findUniqueBySql(
				QuList.class,
				"select * from qu_list where questionnarieId=" + wjId
						+ " limit 1");

		ans.setWjTitle(wj.getTitle());

		if (jbo.has("mainTitle")
				&& StringUtils.isNotEmpty(jbo.getString("mainTitle"))) {
			ans.setAnswerTitle(jbo.getString("mainTitle"));
		}
		if (jbo.has("answerContent")
				&& StringUtils.isNotEmpty(jbo.getString("answerContent"))) {
			ans.setAnswerChoiceContent(jbo.getString("answerContent"));

		}
		if (jbo.has("answerTitle")
				&& StringUtils.isNotEmpty(jbo.getString("answerTitle"))) {
			ans.setAnswerChoiceTitle(jbo.getString("answerTitle"));
		}
		if (jbo.has("answerNo")
				&& StringUtils.isNotEmpty(jbo.getString("answerNo"))) {
			ans.setAnswerChoiceNo(jbo.getString("answerNo"));
		}
		ans.setUserId(CurrentUserHelper.getCurrentUserId());
		ans.setWjId(wjId);
		ans.setAnswerTime(new Date());
		ans.setControlFlag(0);

		DbManager.getDatabase().save(ans);

	}

	private void deleteAll(int wjId) {
		if (DbManager.getDatabase().tableExists(QuUserWjAnswer.class)) {
			String sql = "delete from qu_user_wj_answer where wjId=" + wjId
					+ " and memberId=" + CurrentUserHelper.getCurrentUserId();
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

	public QuUserWjAnswer getTheAnswer(int wjId) {

		return DbManager.getDatabase().findUniqueByWhere(QuUserWjAnswer.class,
				"wjId=" + wjId);

	}

	public void saveFriendAnswers(JSONObject jbo, int wjId)
			throws JSONException {

		JSONArray answerArray = jbo.getJSONArray("aAnswer");
		JSONObject jboInA;
		QuWjAnswerList ansList;
		if (answerArray.length() > 0) {
			deleteAllFriendAnswer(wjId);

		}
		for (int i = 0; i < answerArray.length(); i++) {
			jboInA = answerArray.getJSONObject(i);
			ansList = new QuWjAnswerList();
			ansList.setWjId(wjId);
			if (jboInA.has("sNo")
					&& !StringUtils.isBlank(jboInA.getString("sNo"))) {
				ansList.setAnswerNo(jboInA.getString("sNo"));
			}
			if (jboInA.has("sTitle")
					&& !StringUtils.isBlank(jboInA.getString("sTitle"))) {
				ansList.setAnswerTitle(jboInA.getString("sTitle"));
			}
			if (jboInA.has("sContent")
					&& !StringUtils.isBlank(jboInA.getString("sContent"))) {
				ansList.setAnswerContent(jboInA.getString("sContent"));
			}
			DbManager.getDatabase().save(ansList);

			JSONArray friendArray = jboInA.getJSONArray("aFriend");
			JSONObject friendJbo;
			QuFriendAnswer quFriendAnswer;
			for (int j = 0; j < friendArray.length(); j++) {
				friendJbo = friendArray.getJSONObject(j);
				quFriendAnswer = new QuFriendAnswer();
				quFriendAnswer.setChoiceNo(ansList.getAnswerNo());
				quFriendAnswer.setWjId(wjId);
				quFriendAnswer.setFriendId(friendJbo.getInt("iYhId"));
				quFriendAnswer.setNickname(friendJbo.getString("sNickName"));
				if (friendJbo.has("sTpUrl")
						&& StringUtils
								.isNotEmpty(friendJbo.getString("sTpUrl"))) {
					quFriendAnswer.setPicUrl(friendJbo.getString("sTpUrl"));
				}
				DbManager.getDatabase().save(quFriendAnswer);

			}

		}

	}

	private void deleteAllFriendAnswer(int wjId) {
		if (DbManager.getDatabase().tableExists(QuWjAnswerList.class)
				&& DbManager.getDatabase().tableExists(QuFriendAnswer.class)) {
			String sql1 = "delete from qu_friend_answer where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql1);
			String sql2 = "delete from qu_wj_answer_list where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql2);
		}
	}

	public void deletAllAnsweredQuesitons(int wjId) {
		if (DbManager.getDatabase().tableExists(QuUserQuestionAnswer.class)) {
			String sql = "delete from qu_user_question_answer where wjId="
					+ wjId;
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

	public void setPublicValue(int wjId, int publicValue) {
		QuUserWjAnswer wjAnswer = DbManager.getDatabase().findUniqueByWhere(
				QuUserWjAnswer.class, "wjId=" + wjId);
		wjAnswer.setIsPublicAnwer(publicValue);
		DbManager.getDatabase().update(wjAnswer);

	}

}

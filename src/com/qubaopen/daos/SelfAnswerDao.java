package com.qubaopen.daos;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.database.DbManager;
import com.qubaopen.domain.SelfList;
import com.qubaopen.domain.SelfUserAnswer;
import com.qubaopen.domain.SelfUserQuestionAnswer;
import com.qubaopen.settings.CurrentUserHelper;

public class SelfAnswerDao {

	public SelfUserAnswer saveMyAnswer(JSONObject jbo, int selfId) throws JSONException {
		deleteAll(selfId);
		SelfUserAnswer ans = new SelfUserAnswer();
		SelfList wj = DbManager.getDatabase().findUniqueBySql(
				SelfList.class,
				"select * from self_list where selfId=" + selfId
						+ " limit 1");

		ans.setSelfId(selfId);

		if (jbo.has("resultTitle")
				&& StringUtils.isNotEmpty(jbo.getString("resultTitle"))) {
			ans.setResultTitle(jbo.getString("resultTitle"));
		}
		if (jbo.has("content")
				&& StringUtils.isNotEmpty(jbo.getString("content"))) {
			ans.setContent(jbo.getString("content"));

		}
		if (jbo.has("optionTitle")
				&& StringUtils.isNotEmpty(jbo.getString("optionTitle"))) {
			ans.setOptionTitle(jbo.getString("optionTitle"));
		}
		if (jbo.has("resultNum")
				&& StringUtils.isNotEmpty(jbo.getString("resultNum"))) {
			ans.setResultNum(jbo.getString("resultNum"));
		}
		ans.setUserId(CurrentUserHelper.getCurrentUserId());
		DbManager.getDatabase().save(ans);
		return ans;

	}

	private void deleteAll(int selfId) {
		if (DbManager.getDatabase().tableExists(SelfUserAnswer.class)) {
			String sql = "delete from self_user_answer where selfId=" + selfId
					+ " and userId=" + CurrentUserHelper.getCurrentUserId();
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

	public SelfUserAnswer getTheAnswer(int selfId) {
		return DbManager.getDatabase().findUniqueByWhere(SelfUserAnswer.class,
				"selfId=" + selfId);

	}

	public void deletAllAnsweredQuesitons(int selfId) {
		if (DbManager.getDatabase().tableExists(SelfUserQuestionAnswer.class)) {
			String sql = "delete from self_user_question_answer where selfId="
					+ selfId;
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

}

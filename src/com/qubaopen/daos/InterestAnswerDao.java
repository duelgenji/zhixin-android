package com.qubaopen.daos;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.database.DbManager;
import com.qubaopen.domain.InterestList;
import com.qubaopen.domain.InterestUserAnswer;
import com.qubaopen.domain.QuUserQuestionAnswer;
import com.qubaopen.settings.CurrentUserHelper;

public class InterestAnswerDao {

	public InterestUserAnswer saveMyAnswer(JSONObject jbo, int interestId) throws JSONException {
		deleteAll(interestId);
		InterestUserAnswer ans = new InterestUserAnswer();
		InterestList wj = DbManager.getDatabase().findUniqueBySql(
				InterestList.class,
				"select * from interest_list where interestId=" + interestId
						+ " limit 1");

		ans.setInterestId(interestId);

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

	private void deleteAll(int interestId) {
		if (DbManager.getDatabase().tableExists(InterestUserAnswer.class)) {
			String sql = "delete from interest_user_answer where interestId=" + interestId
					+ " and userId=" + CurrentUserHelper.getCurrentUserId();
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

	public InterestUserAnswer getTheAnswer(int interestId) {
		return DbManager.getDatabase().findUniqueByWhere(InterestUserAnswer.class,
				"interestId=" + interestId);

	}

	public void deletAllAnsweredQuesitons(int interestId) {
		if (DbManager.getDatabase().tableExists(QuUserQuestionAnswer.class)) {
			String sql = "delete from qu_user_question_answer where interestId="
					+ interestId;
			DbManager.getDatabase().exeCustomerSql(sql);

		}
	}

}

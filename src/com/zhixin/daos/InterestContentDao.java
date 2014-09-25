package com.zhixin.daos;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.InterestOption;
import com.zhixin.domain.InterestQuestion;
import com.zhixin.domain.QuQuestion;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.logic.DatiLogicObject;
import com.zhixin.logic.DoLogicObject;

public class InterestContentDao {

	public void saveInterestContent(JSONObject jbo, int interestId)
			throws JSONException {
		JSONArray questionsArray = jbo.getJSONArray("questions");
		if (questionsArray.length() > 0) {
			delete(interestId);
		}
		InterestQuestion interestQuestion;
		JSONObject jboInArray;
		for (int i = 0; i < questionsArray.length(); i++) {
			jboInArray = questionsArray.getJSONObject(i);
			interestQuestion = new InterestQuestion();
			interestQuestion.setQuestionId(jboInArray.getInt("questionId"));
			interestQuestion.setQuestionnaireId(interestId);

			interestQuestion.setQuestionContent(jboInArray.getString("questionContent"));

			if (jboInArray.has("limitTime")
					&& StringUtils
							.isNotEmpty(jboInArray.getString("limitTime"))) {
				interestQuestion.setLimitTime(jboInArray.getInt("limitTime"));
			} else {
				interestQuestion.setLimitTime(0);
			}

			if (jboInArray.has("questionType")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionType"))) {
				if(jboInArray.getString("questionType").equals("SINGLE"))
					interestQuestion.setQuestionType(1);
				if(jboInArray.getString("questionType").equals("MULTIPLE"))
					interestQuestion.setQuestionType(2);
				if(jboInArray.getString("questionType").equals("QA"))
					interestQuestion.setQuestionType(3);
				if(jboInArray.getString("questionType").equals("SORT"))
					interestQuestion.setQuestionType(4);
				if(jboInArray.getString("questionType").equals("SCORE"))
					interestQuestion.setQuestionType(5);
			}

			if (jboInArray.has("optionCount")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("optionCount"))) {
				interestQuestion.setOptionCount(jboInArray.getInt("optionCount"));
			}

			if (jboInArray.has("special")
					&& StringUtils.isNotEmpty(jboInArray.getString("special"))) {
				if (jboInArray.getBoolean("special")) {
					interestQuestion.setSpecial(true);
				} else {
					interestQuestion.setSpecial(false);

				}
			}
			
			if (jboInArray.has("questionNum")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionNum"))) {
				interestQuestion.setQuestionNum(jboInArray.getString("questionNum"));
			}
			

			if (jboInArray.has("matrix")
					&& StringUtils.isNotEmpty(jboInArray.getString("matrix"))) {
				if (jboInArray.getBoolean("matrix")) {
					interestQuestion.setMatrix(true);
					interestQuestion.setMatrixTitle(jboInArray.getString("matrixTitle"));
					interestQuestion.setMatrixNo(jboInArray.getString("matrixNo"));
				} else {
					interestQuestion.setMatrix(false);

				}
			}
			
			if (jboInArray.has("order")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("order"))) {
				interestQuestion.setQuestionOrder(jboInArray.getString("order"));
			}
			
			if (jboInArray.has("options")) {
				JSONArray choiceArray = jboInArray.getJSONArray("options");
				interestQuestion.setOptionCount(choiceArray.length());
				JSONObject choiceJbo;
				InterestOption interestOption;
				for (int k = 0; k < choiceArray.length(); k++) {
					choiceJbo = choiceArray.getJSONObject(k);
					interestOption = new InterestOption();
					interestOption.setOptionId(choiceJbo.getInt("optionId"));
					if (choiceJbo.has("optionNum")) {
						interestOption.setOptionNum(choiceJbo.getString("optionNum"));
					}
					interestOption
							.setOptionContent(choiceJbo.getString("optionContent"));
					interestOption.setQuestionId(interestQuestion.getQuestionId());

					DbManager.getDatabase().save(interestOption);
				}

			}

			
			DbManager.getDatabase().save(interestQuestion);

		}

	}

	private void delete(int interestId) {
		if (DbManager.getDatabase().tableExists(InterestQuestion.class)
				&& DbManager.getDatabase().tableExists(InterestOption.class)) {
			String sql = "delete from interest_option where questionId "
					+ "in (select distinct questionId "
					+ "from interest_question where questionnaireId=" + interestId + ")";
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from interest_question where questionnaireId=" + interestId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public void saveAnswer(List<? extends UserQuestionAnswer> currentAnswer,
			DoLogicObject logic) {
		deleteCurrentQuesitonAnswer(currentAnswer);
		QuUserQuestionAnswer ans;
		switch (currentAnswer.get(0).getQuestionType()) {
		case 1:
			ans = new QuUserQuestionAnswer();
			ans.setQuestionId(currentAnswer.get(0).getQuestionId());
			ans.setOptionNum(currentAnswer.get(0).getOptionNum());
			ans.setOptionId(currentAnswer.get(0).getOptionId());
			ans.setQuestionType(currentAnswer.get(0).getQuestionType());
			ans.setWjId(logic.getWjId());
			DbManager.getDatabase().save(ans);
			break;
		case 2:

			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new QuUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setOptionNum(aAns.getOptionNum());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setWjId(logic.getWjId());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 3:
			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new QuUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setContent(aAns.getContent());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setOptionNum(aAns.getOptionNum());
				ans.setWjId(logic.getWjId());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 4:
			int shunOrder = 0;
			for (UserQuestionAnswer aAns : currentAnswer) {
				shunOrder++;
				ans = new QuUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setWjId(logic.getWjId());
				ans.setTurn(shunOrder);
				ans.setOptionNum(aAns.getOptionNum());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 5:
			break;
		default:
			break;
		}

	}

	private void deleteCurrentQuesitonAnswer(
			List<? extends UserQuestionAnswer> currentAnswer) {
		if (DbManager.getDatabase().tableExists(QuUserQuestionAnswer.class)) {

			for (UserQuestionAnswer ans : currentAnswer) {

				String sql = "delete from qu_user_question_answer where questionId="
						+ ans.getQuestionId();
				DbManager.getDatabase().exeCustomerSql(sql);
			}
		}
	}

	public boolean quesitonExisted(int questionId) {
		return DbManager.getDatabase().findUniqueByWhere(QuQuestion.class,
				"questionId=" + questionId) == null ? false : true;

	}

	public boolean isInterestExists(int interestId) {
		String sql = "questionnaireId=" + interestId;
		List<InterestQuestion> list = DbManager.getDatabase().findAllByWhere(
				InterestQuestion.class, sql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}

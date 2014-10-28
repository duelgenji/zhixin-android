package com.qubaopen.daos;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.database.DbManager;
import com.qubaopen.domain.SelfOption;
import com.qubaopen.domain.SelfQuestion;
import com.qubaopen.domain.SelfUserQuestionAnswer;
import com.qubaopen.domain.UserQuestionAnswer;
import com.qubaopen.logic.DoLogicObject;

public class SelfContentDao {
	public void saveSelfContent(JSONObject jbo, int selfId)
			throws JSONException {
		JSONArray questionsArray = jbo.getJSONArray("questions");
		if (questionsArray.length() > 0) {
			deleteSelfOption(selfId);
		}
		
		SelfQuestion selfQuestion;
		JSONObject jboInArray;
		for (int i = 0; i < questionsArray.length(); i++) {
			jboInArray = questionsArray.getJSONObject(i);
			selfQuestion = new SelfQuestion();
			selfQuestion.setQuestionId(jboInArray.getInt("questionId"));
			selfQuestion.setQuestionnaireId(selfId);

			selfQuestion.setQuestionContent(jboInArray.getString("questionContent"));

			if (jboInArray.has("limitTime")
					&& StringUtils
							.isNotEmpty(jboInArray.getString("limitTime"))) {
				selfQuestion.setLimitTime(jboInArray.getInt("limitTime"));
			} else {
				selfQuestion.setLimitTime(0);
			}

			if (jboInArray.has("questionType")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionType"))) {
				if(jboInArray.getString("questionType").equals("SINGLE"))
					selfQuestion.setQuestionType(1);
				if(jboInArray.getString("questionType").equals("MULTIPLE"))
					selfQuestion.setQuestionType(2);
				if(jboInArray.getString("questionType").equals("QA"))
					selfQuestion.setQuestionType(3);
				if(jboInArray.getString("questionType").equals("SORT"))
					selfQuestion.setQuestionType(4);
				if(jboInArray.getString("questionType").equals("SCORE"))
					selfQuestion.setQuestionType(5);
			}

			if (jboInArray.has("optionCount")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("optionCount"))) {
				selfQuestion.setOptionCount(jboInArray.getInt("optionCount"));
			}

			if (jboInArray.has("special")
					&& StringUtils.isNotEmpty(jboInArray.getString("special"))) {
				if (jboInArray.getBoolean("special")) {
					selfQuestion.setSpecial(true);
				} else {
					selfQuestion.setSpecial(false);

				}
			}
			
			if (jboInArray.has("questionNum")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionNum"))) {
				selfQuestion.setQuestionNum(jboInArray.getString("questionNum"));
			}
			

			if (jboInArray.has("matrix")
					&& StringUtils.isNotEmpty(jboInArray.getString("matrix"))) {
				if (jboInArray.getBoolean("matrix")) {
					selfQuestion.setMatrix(true);
					selfQuestion.setMatrixTitle(jboInArray.getString("matrixTitle"));
					selfQuestion.setMatrixNo(jboInArray.getString("matrixNo"));
				} else {
					selfQuestion.setMatrix(false);

				}
			}
			
			if (jboInArray.has("order")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("order"))) {
				selfQuestion.setQuestionOrder(jboInArray.getString("order"));
			}
			
			if (jboInArray.has("options")) {
				JSONArray choiceArray = jboInArray.getJSONArray("options");
				selfQuestion.setOptionCount(choiceArray.length());
				JSONObject choiceJbo;
				SelfOption selfOption;
				for (int k = 0; k < choiceArray.length(); k++) {
					choiceJbo = choiceArray.getJSONObject(k);
					selfOption = new SelfOption();
					selfOption.setOptionId(choiceJbo.getInt("optionId"));
					if (choiceJbo.has("optionNum")) {
						selfOption.setOptionNum(choiceJbo.getString("optionNum"));
					}
					selfOption
							.setOptionContent(choiceJbo.getString("optionContent"));
					selfOption.setQuestionId(selfQuestion.getQuestionId());

					DbManager.getDatabase().save(selfOption);
				}

			}

			
			DbManager.getDatabase().save(selfQuestion);

		}

	}

	private void deleteSelfOption(int interestId) {
		if (DbManager.getDatabase().tableExists(SelfQuestion.class)
				&& DbManager.getDatabase().tableExists(SelfOption.class)) {
			String sql = "delete from self_option where questionId "
					+ "in (select distinct questionId "
					+ "from self_question where questionnaireId=" + interestId + ")";
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from self_question where questionnaireId=" + interestId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}
	
	
	public boolean isSelfExists(int selfId) {
		String sql = "questionnaireId=" + selfId;
		List<SelfQuestion> list = DbManager.getDatabase().findAllByWhere(
				SelfQuestion.class, sql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public void saveAnswer(List<? extends UserQuestionAnswer> currentAnswer,
			DoLogicObject logic) {
		deleteCurrentQuesitonAnswer(currentAnswer);
		SelfUserQuestionAnswer ans;
		switch (currentAnswer.get(0).getQuestionType()) {
		case 1:
			ans = new SelfUserQuestionAnswer();
			ans.setQuestionId(currentAnswer.get(0).getQuestionId());
			ans.setOptionNum(currentAnswer.get(0).getOptionNum());
			ans.setOptionId(currentAnswer.get(0).getOptionId());
			ans.setQuestionType(currentAnswer.get(0).getQuestionType());
			ans.setSelfId(logic.getWjId());
			DbManager.getDatabase().save(ans);
			break;
		case 2:

			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new SelfUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setOptionNum(aAns.getOptionNum());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setSelfId(logic.getWjId());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 3:
			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new SelfUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setContent(aAns.getContent());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setOptionNum(aAns.getOptionNum());
				ans.setSelfId(logic.getWjId());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 4:
			int shunOrder = 0;
			for (UserQuestionAnswer aAns : currentAnswer) {
				shunOrder++;
				ans = new SelfUserQuestionAnswer();
				ans.setOptionId(aAns.getOptionId());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setSelfId(logic.getWjId());
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
		if (DbManager.getDatabase().tableExists(SelfUserQuestionAnswer.class)) {

			for (UserQuestionAnswer ans : currentAnswer) {

				String sql = "delete from self_user_question_answer where questionId="
						+ ans.getQuestionId();
				DbManager.getDatabase().exeCustomerSql(sql);
			}
		}
	}
}

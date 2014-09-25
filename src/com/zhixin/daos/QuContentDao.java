package com.zhixin.daos;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.QuChoices;
import com.zhixin.domain.QuQuestion;
import com.zhixin.domain.QuQuestionOrder;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.logic.DatiLogicObject;

public class QuContentDao {

	public void saveAQuestionnare(JSONObject jbo, int wjId)
			throws JSONException {
		JSONArray questionsArray = jbo.getJSONArray("questions");
		if (questionsArray.length() > 0) {
			delete(wjId);
		}
		QuQuestion quQuestion;
		JSONObject jboInArray;
		for (int i = 0; i < questionsArray.length(); i++) {
			jboInArray = questionsArray.getJSONObject(i);
			quQuestion = new QuQuestion();
			quQuestion.setQuestionId(jboInArray.getInt("questionId"));
			quQuestion.setWjId(wjId);

			quQuestion.setQuestionTitle(jboInArray.getString("questionTitle"));

			if (jboInArray.has("limitTime")
					&& StringUtils
							.isNotEmpty(jboInArray.getString("limitTime"))) {
				quQuestion.setLimitTime(jboInArray.getInt("limitTime"));
			} else {
				quQuestion.setLimitTime(0);
			}

			if (jboInArray.has("questionType")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionType"))) {
				quQuestion.setQuestionType(jboInArray.getInt("questionType"));
			} else {
				quQuestion.setQuestionType(0);
			}

			if (jboInArray.has("choiceNumber")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("choiceNumber"))) {
				quQuestion.setChoiceNumber(jboInArray.getInt("choiceNumber"));
			}

			if (jboInArray.has("special")
					&& StringUtils.isNotEmpty(jboInArray.getString("special"))) {
				if (jboInArray.getInt("special") == 1) {
					quQuestion.setSpecial(true);
				} else if (jboInArray.getInt("special") == 0) {
					quQuestion.setSpecial(false);

				}
			}

			if (jboInArray.has("choices")) {
				JSONArray choiceArray = jboInArray.getJSONArray("choices");
				quQuestion.setChoiceNumber(choiceArray.length());
				JSONObject choiceJbo;
				QuChoices quChoices;
				for (int k = 0; k < choiceArray.length(); k++) {
					choiceJbo = choiceArray.getJSONObject(k);
					quChoices = new QuChoices();
					quChoices.setChoiceId(choiceJbo.getInt("choiceId"));
					if (choiceJbo.has("choiceNo")) {
						quChoices.setChoiceNo(choiceJbo.getString("choiceNo"));
					}
					quChoices
							.setChoiceTitle(choiceJbo.getString("choiceTitle"));
					quChoices.setQuestionId(quQuestion.getQuestionId());

					DbManager.getDatabase().save(quChoices);
				}

			}

			if (jboInArray.has("nextQuestionId")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("nextQuestionId"))) {
				String orderStr = jboInArray.getString("nextQuestionId");
				StringTokenizer st = new StringTokenizer(orderStr, "|");

				QuQuestionOrder quQuestionOrder;
				while (st.hasMoreTokens()) {
					String anOrder = st.nextToken();
					quQuestionOrder = new QuQuestionOrder();

					StringTokenizer st2 = new StringTokenizer(anOrder, ":");

					quQuestionOrder.setWjId(wjId);
					quQuestionOrder.setQuestionId(quQuestion.getQuestionId());

					int u = 0;
					while (st2.hasMoreTokens()) {
						String innerOrder = st2.nextToken();

						switch (u) {
						case 0:
							quQuestionOrder.setCurrentQuestionId(Integer
									.parseInt(innerOrder));
							break;
						case 1:
							if (!innerOrder.equals("0")) {
								quQuestionOrder.setChoiceNo(innerOrder);
							}
							break;
						case 2:
							if (innerOrder.equals("0")) {
								quQuestionOrder.setNextOneEnd(true);
							} else {
								quQuestionOrder.setNextQuestionId(Integer
										.parseInt(innerOrder));
								quQuestionOrder.setNextOneEnd(false);
							}
							break;
						default:
							break;
						}
						u++;
					}
					DbManager.getDatabase().save(quQuestionOrder);
				}
			}
			quQuestion.setQuesitonNo(String.valueOf(i + 1));
			quQuestion.setMatrix(false);
			DbManager.getDatabase().save(quQuestion);

		}

	}

	private void delete(int wjId) {
		if (DbManager.getDatabase().tableExists(QuQuestion.class)
				&& DbManager.getDatabase().tableExists(QuChoices.class)
				&& DbManager.getDatabase().tableExists(QuQuestionOrder.class)) {
			String sql = "delete from qu_choices " + "where questionId "
					+ "in (select distinct questionId "
					+ "from qu_question where wjId=" + wjId + ")";
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from qu_question_order where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from qu_question where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public void saveAnswer(List<? extends UserQuestionAnswer> currentAnswer,
			DatiLogicObject logic) {
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
				ans.setContent(aAns.getContent());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
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

	public boolean isWjExists(int wjId) {
		String sql = "wjId=" + wjId;
		List<QuQuestion> list = DbManager.getDatabase().findAllByWhere(
				QuQuestion.class, sql);
		if (list.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

}

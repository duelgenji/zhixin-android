package com.zhixin.daos;

import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.DiaoyanChoices;
import com.zhixin.domain.DiaoyanQuestion;
import com.zhixin.domain.DiaoyanQuestionOrder;
import com.zhixin.domain.DiaoyanUserQuestionAnswer;
import com.zhixin.domain.UserQuestionAnswer;
import com.zhixin.logic.DatiLogicObject;

public class DiaoyanContentDao {

	public void saveAQuestionnare(JSONObject jbo, int wjId)
			throws JSONException {
		JSONArray questionsArray = jbo.getJSONArray("questions");
		if (questionsArray.length() > 0) {
			delete(wjId);
		}
		DiaoyanQuestion diaoyanQuestion;
		JSONObject jboInArray;
		for (int i = 0; i < questionsArray.length(); i++) {
			jboInArray = questionsArray.getJSONObject(i);
			diaoyanQuestion = new DiaoyanQuestion();

			diaoyanQuestion.setQuesitonNo(String.valueOf(i + 1));

			diaoyanQuestion.setQuestionId(jboInArray.getInt("questionId"));
			diaoyanQuestion.setWjId(wjId);

			diaoyanQuestion.setQuestionTitle(jboInArray
					.getString("questionTitle"));

			if (jboInArray.has("limitTime")
					&& StringUtils
							.isNotEmpty(jboInArray.getString("limitTime"))) {
				diaoyanQuestion.setLimitTime(jboInArray.getInt("limitTime"));
			} else {
				diaoyanQuestion.setLimitTime(0);
			}

			if (jboInArray.has("questionType")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("questionType"))) {
				diaoyanQuestion.setQuestionType(jboInArray
						.getInt("questionType"));
			} else {
				diaoyanQuestion.setQuestionType(0);
			}

			if (jboInArray.has("special")
					&& StringUtils.isNotBlank(jboInArray.getString("special"))) {
				if (jboInArray.getInt("special") == 1) {
					diaoyanQuestion.setSpecial(true);
				} else if (jboInArray.getInt("special") == 0) {
					diaoyanQuestion.setSpecial(false);

				}
			}

			if (jboInArray.has("isMatrix")
					&& StringUtils.isNotBlank(jboInArray.getString("isMatrix"))) {
				if (jboInArray.getInt("isMatrix") == 1) {
					diaoyanQuestion.setMatrix(true);
				} else if (jboInArray.getInt("isMatrix") == 0) {
					diaoyanQuestion.setMatrix(false);
				}
			}

			if (jboInArray.has("mainTitle")
					&& StringUtils
							.isNotBlank(jboInArray.getString("mainTitle"))) {
				diaoyanQuestion.setMainTitle(jboInArray.getString("mainTitle"));
			}

			if (jboInArray.has("mainNo")
					&& StringUtils.isNotBlank(jboInArray.getString("mainNo"))) {
				diaoyanQuestion.setMainNo(jboInArray.getString("mainNo"));
			}

			if (jboInArray.has("choices")) {
				JSONArray choiceArray = jboInArray.getJSONArray("choices");
				diaoyanQuestion.setChoiceNumber(choiceArray.length());
				JSONObject choiceJbo;
				DiaoyanChoices quChoices;
				for (int k = 0; k < choiceArray.length(); k++) {
					choiceJbo = choiceArray.getJSONObject(k);
					quChoices = new DiaoyanChoices();

					quChoices.setChoiceId(choiceJbo.getInt("choiceId"));
					if (choiceJbo.has("choiceTitle")
							&& StringUtils.isNotBlank(choiceJbo
									.getString("choiceTitle"))) {
						quChoices.setChoiceNo(choiceJbo
								.getString("choiceTitle"));
					}
					quChoices.setChoiceTitle(choiceJbo
							.getString("choiceContent"));
					quChoices.setQuestionId(diaoyanQuestion.getQuestionId());

					DbManager.getDatabase().save(quChoices);
				}

			}

			if (jboInArray.has("direction")
					&& StringUtils
							.isNotEmpty(jboInArray.getString("direction"))) {
				String orderStr = jboInArray.getString("direction");
				StringTokenizer st = new StringTokenizer(orderStr, "|");

				DiaoyanQuestionOrder dianyanOrder;
				while (st.hasMoreTokens()) {
					String anOrder = st.nextToken();
					dianyanOrder = new DiaoyanQuestionOrder();

					StringTokenizer st2 = new StringTokenizer(anOrder, ":");

					dianyanOrder.setWjId(wjId);
					dianyanOrder.setQuestionId(diaoyanQuestion.getQuestionId());

					int u = 0;
					while (st2.hasMoreTokens()) {
						String innerOrder = st2.nextToken();

						switch (u) {
						case 0:
							dianyanOrder.setCurrentQuestionId(Integer
									.parseInt(innerOrder));
							break;
						case 1:

							dianyanOrder.setChoiceId(Integer
									.parseInt(innerOrder));

							break;
						case 2:
							if (innerOrder.equals("0")) {
								dianyanOrder.setNextOneEnd(true);
							} else {
								dianyanOrder.setNextQuestionId(Integer
										.parseInt(innerOrder));
								dianyanOrder.setNextOneEnd(false);
							}
							break;
						default:
							break;
						}
						u++;
					}
					DbManager.getDatabase().save(dianyanOrder);
				}
			}

			if (jboInArray.has("choiceNumber")
					&& StringUtils.isNotEmpty(jboInArray
							.getString("choiceNumber"))) {
				diaoyanQuestion.setChoiceNumber(jboInArray
						.getInt("choiceNumber"));
			}

			if (jboInArray.has("questionNo")
					&& StringUtils.isNotBlank(jboInArray
							.getString("questionNo"))) {
				diaoyanQuestion.setQuesitonNo(jboInArray
						.getString("questionNo"));

			}

			DbManager.getDatabase().save(diaoyanQuestion);

		}

	}

	private void delete(int wjId) {
		if (DbManager.getDatabase().tableExists(DiaoyanQuestion.class)
				&& DbManager.getDatabase().tableExists(DiaoyanChoices.class)
				&& DbManager.getDatabase().tableExists(
						DiaoyanQuestionOrder.class)) {
			String sql = "delete from diaoyan_choices " + "where questionId "
					+ "in (select distinct questionId "
					+ "from diaoyan_question where wjId=" + wjId + ")";
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from diaoyan_question_order where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
			sql = "delete from diaoyan_question where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public void saveAnswer(List<? extends UserQuestionAnswer> currentAnswer,
			DatiLogicObject logic) {
		deleteCurrentQuesitonAnswer(currentAnswer);
		DiaoyanUserQuestionAnswer ans;
		switch (currentAnswer.get(0).getQuestionType()) {
		case 1:
			ans = new DiaoyanUserQuestionAnswer();
			ans.setQuestionId(currentAnswer.get(0).getQuestionId());
			ans.setChoiceId(currentAnswer.get(0).getChoiceId());
			ans.setQuestionType(currentAnswer.get(0).getQuestionType());
			ans.setWjId(logic.getWjId());
			DbManager.getDatabase().save(ans);
			break;
		case 2:
			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new DiaoyanUserQuestionAnswer();
				ans.setChoiceId(aAns.getChoiceId());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setWjId(logic.getWjId());
				DbManager.getDatabase().save(ans);
			}
			break;
		case 3:

			for (UserQuestionAnswer aAns : currentAnswer) {
				ans = new DiaoyanUserQuestionAnswer();
				ans.setAnswer(aAns.getAnswer());
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
				ans = new DiaoyanUserQuestionAnswer();
				ans.setChoiceId(aAns.getChoiceId());
				ans.setQuestionId(aAns.getQuestionId());
				ans.setQuestionType(aAns.getQuestionType());
				ans.setWjId(logic.getWjId());
				ans.setShunxuorder(shunOrder);
				ans.setChoiceNo(aAns.getChoiceNo());
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
		if (DbManager.getDatabase()
				.tableExists(DiaoyanUserQuestionAnswer.class)) {
			for (UserQuestionAnswer ans : currentAnswer) {
				String sql = "delete from diaoyan_user_question_answer where questionId="
						+ ans.getQuestionId();
				DbManager.getDatabase().exeCustomerSql(sql);
			}
		}
	}

	public boolean quesitonExisted(int questionId) {
		return DbManager.getDatabase().findUniqueByWhere(DiaoyanQuestion.class,
				"questionId=" + questionId) == null ? false : true;

	}

	public void saveMatrixAnswer(
			List<? extends UserQuestionAnswer> currentAnswer,
			DatiLogicObject logic) {
		deleteCurrentQuesitonAnswer(currentAnswer);
		DiaoyanUserQuestionAnswer ans;
		for (UserQuestionAnswer question : currentAnswer) {
			ans = new DiaoyanUserQuestionAnswer();
			ans.setQuestionId(question.getQuestionId());
			ans.setQuestionType(question.getQuestionType());
			ans.setScore(question.getScore());
			ans.setWjId(logic.getWjId());
			ans.setMainNo(question.getMainNo());
			DbManager.getDatabase().save(ans);

		}
	}
	
	public void deleteAllAnsweredQuesitons(int wjId){
		if (DbManager.getDatabase().tableExists(DiaoyanUserQuestionAnswer.class)){
			String sql = "delete from diaoyan_user_question_answer where wjId=" + wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}
		
		
	}

}

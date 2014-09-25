package com.zhixin.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.DiaoyanUserQuestionAnswer;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.Question;

import android.database.Cursor;

public class DatiQuestionAnswer {

	protected int questionId;
	protected int choiceId;
	protected String choiceNo;

	protected List<String> wendaAnswers;
	protected List<Integer> choiceIdArray;
	protected List<String> choiceNoArray;
	protected List<Integer> questionIdArray;

	protected List<Question> questionArray;

	protected int questionType;

	public DatiQuestionAnswer(int questionType) {
		this.questionType = questionType;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

	public List<String> getWendaAnswers() {
		return wendaAnswers;
	}

	public List<Integer> getChoiceIdArray() {
		return choiceIdArray;
	}

	public List<String> getChoiceNoArray() {
		return choiceNoArray;
	}

	public void addChoiceId(int value) {
		if (choiceIdArray == null) {
			choiceIdArray = new ArrayList<Integer>();
		}
		choiceIdArray.add(value);
	}

	public void addChoiceNo(String value) {
		if (choiceNoArray == null) {
			choiceNoArray = new ArrayList<String>();
		}
		choiceNoArray.add(value);
	}

	public void addWendaAnswer(String value) {
		if (wendaAnswers == null) {
			wendaAnswers = new ArrayList<String>();
		}
		wendaAnswers.add(value);
	}

	public void addMatrixQuestion(Question question) {
		if (questionArray == null) {
			questionArray = new ArrayList<Question>();
		}
		questionArray.add(question);

	}

	public List<Question> getQuestionArray() {
		return questionArray;
	}

	public static JSONObject loadDiaoyanAnswers(int wjId) {
		JSONObject answer = new JSONObject();
		try {
			answer.put("iWjId", wjId);

			String sql = "select questionId,Max(wjId),questionType,score "
					+ "from diaoyan_user_question_answer " + "where wjId="
					+ wjId + " GROUP BY questionId order by questionId asc";

			Cursor questionCursor = DbManager.getDatabase()
					.findAllBySqlReturnCursor(DiaoyanUserQuestionAnswer.class,
							sql);
			JSONArray questionArray = null;
			if (questionCursor.getCount() >= 1) {
				questionArray = new JSONArray();
			}
			questionCursor.moveToFirst();
			int questionId;
			int questionType;
			List<DiaoyanUserQuestionAnswer> choiceList;
			JSONObject questionObj;
			do {

				questionId = questionCursor.getInt(questionCursor
						.getColumnIndex("questionId"));
				questionType = questionCursor.getInt(questionCursor
						.getColumnIndex("questionType"));

				questionObj = new JSONObject();
				questionObj.put("iWtId", questionId);
				JSONArray choiceArray = new JSONArray();

				choiceList = DbManager.getDatabase().findAllByWhere(
						DiaoyanUserQuestionAnswer.class,
						"questionId=" + questionId, "_id asc");
				switch (questionType) {
				case 1:
				case 2:
				case 4:
				case 5:

					for (DiaoyanUserQuestionAnswer aChoice : choiceList) {
						if (questionType != 5) {

							choiceArray.put(aChoice.getOptionId());

						} else {
							choiceArray.put(aChoice.getScore());

						}
					}
					questionObj.put("aAnswers", choiceArray);
					break;
				case 3:
					for (DiaoyanUserQuestionAnswer aChoice : choiceList) {
						choiceArray.put(aChoice.getContent());
					}
					questionObj.put("aAnswers", choiceArray);
					break;

				default:

					break;

				}

				questionArray.put(questionObj);

			} while (questionCursor.moveToNext());
			answer.put("aData", questionArray);
			questionCursor.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return answer;
	}

	public static JSONObject loadQuAnswers(int wjId) {
		JSONObject answer = new JSONObject();
		try {
			answer.put("questionnaireId", wjId);

			String sql = "select questionId,Max(wjId),questionType "
					+ "from qu_user_question_answer " + "where wjId=" + wjId
					+ " GROUP BY questionId order by questionId asc";

			Cursor questionCursor = DbManager.getDatabase()
					.findAllBySqlReturnCursor(QuUserQuestionAnswer.class, sql);
			JSONArray questionArray = null;
			if (questionCursor.getCount() >= 1) {
				questionArray = new JSONArray();
			}
			questionCursor.moveToFirst();
			int questionId;
			int questionType;
			List<QuUserQuestionAnswer> choiceList;
			JSONObject questionObj;
			do {

				questionId = questionCursor.getInt(questionCursor
						.getColumnIndex("questionId"));
				questionType = questionCursor.getInt(questionCursor
						.getColumnIndex("questionType"));

				questionObj = new JSONObject();
				questionObj.put("questionId", questionId);
				JSONArray choiceArray = new JSONArray();

				choiceList = DbManager.getDatabase().findAllByWhere(
						QuUserQuestionAnswer.class, "questionId=" + questionId);
				switch (questionType) {
				case 1:
				case 2:
				case 4:
					for (QuUserQuestionAnswer aChoice : choiceList) {
						choiceArray.put(aChoice.getOptionId());
					}
					questionObj.put("choiceId", choiceArray);
					break;
				case 3:
					for (QuUserQuestionAnswer aChoice : choiceList) {
						choiceArray.put(aChoice.getContent());
					}
					questionObj.put("content", choiceArray);
					break;
				default:

					break;

				}

				questionArray.put(questionObj);

			} while (questionCursor.moveToNext());
			answer.put("questions", questionArray);
			questionCursor.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return answer;
	}

}

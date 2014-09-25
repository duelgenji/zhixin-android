package com.zhixin.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.DiaoyanUserQuestionAnswer;
import com.zhixin.domain.QuUserQuestionAnswer;
import com.zhixin.domain.Question2;
import com.zhixin.domain.SelfUserQuestionAnswer;

import android.database.Cursor;
import android.util.Log;

public class DoQuestionAnswer {

	protected int questionId;
	protected int optionId;
	protected String optionNum;

	protected List<String> contents;
	protected List<Integer> optionIdList;
	protected List<String> optionNumList;
	protected List<Integer> questionIdList;

	protected List<Question2> questionList;

	protected int questionType;

	public DoQuestionAnswer(int questionType) {
		this.questionType = questionType;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public String getOptionNum() {
		return optionNum;
	}

	public void setChoiceNo(String optionNum) {
		this.optionNum = optionNum;
	}

	public List<String> getContents() {
		return contents;
	}

	public List<Integer> getOptionIdList() {
		return optionIdList;
	}

	public List<String> getOptionNumList() {
		return optionNumList;
	}

	public void addOptionId(int value) {
		if (optionIdList == null) {
			optionIdList = new ArrayList<Integer>();
		}
		optionIdList.add(value);
	}

	public void addOptionNum(String value) {
		if (optionNumList == null) {
			optionNumList = new ArrayList<String>();
		}
		optionNumList.add(value);
	}

	public void addContent(String value) {
		if (contents == null) {
			contents = new ArrayList<String>();
		}
		contents.add(value);
	}

	public void addMatrixQuestion(Question2 question) {
		if (questionList == null) {
			questionList = new ArrayList<Question2>();
		}
		questionList.add(question);

	}

	public List<Question2> getQuestionArray() {
		return questionList;
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
	
	
	public static JSONObject loadSelfAnswers(int selfId) {
		JSONObject answer = new JSONObject();
		try {
			answer.put("selfId", selfId);

			String sql = "select questionId,Max(selfId),questionType,score "
					+ "from self_user_question_answer " + "where selfId="
					+ selfId + " GROUP BY questionId order by questionId asc";

			Cursor questionCursor = DbManager.getDatabase()
					.findAllBySqlReturnCursor(SelfUserQuestionAnswer.class,
							sql);
			JSONArray questionArray = null;
			if (questionCursor.getCount() >= 1) {
				questionArray = new JSONArray();
			}
			questionCursor.moveToFirst();
			int questionId;
			int questionType;
			List<SelfUserQuestionAnswer> optionList;
			JSONObject questionObj;
			do {

				questionId = questionCursor.getInt(questionCursor
						.getColumnIndex("questionId"));
				questionType = questionCursor.getInt(questionCursor
						.getColumnIndex("questionType"));

				questionObj = new JSONObject();
				questionObj.put("questionId", questionId);
				JSONArray choiceArray = new JSONArray();

				optionList = DbManager.getDatabase().findAllByWhere(
						SelfUserQuestionAnswer.class,
						"questionId=" + questionId, "_id asc");
				for (SelfUserQuestionAnswer aChoice : optionList) {
					JSONObject inner=new JSONObject();
					inner.put("id", aChoice.getOptionId());
					
					if (questionType == 3) {
						inner.put("cnt", aChoice.getContent());

					} else if(questionType == 4) {
						inner.put("order", aChoice.getTurn());

					}
					choiceArray.put(inner);
				}
				questionObj.put("contents", choiceArray);

				questionArray.put(questionObj);

			} while (questionCursor.moveToNext());
			answer.put("questionJson", questionArray);
			questionCursor.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return answer;
	}

}

package com.qubaopen.logic;

import java.util.List;

import com.qubaopen.database.DbManager;
import com.qubaopen.domain.DiaoyanChoices;
import com.qubaopen.domain.DiaoyanQuestion;
import com.qubaopen.domain.InterestOption;
import com.qubaopen.domain.InterestQuestion;
import com.qubaopen.domain.Options;
import com.qubaopen.domain.Question2;
import com.qubaopen.domain.SelfOption;
import com.qubaopen.domain.SelfQuestion;

import android.database.Cursor;
import android.os.AsyncTask;

public class DoDataObject extends AsyncTask<Void, Void, Void> {

	public interface DiaoyanDatiLoadFinished {
		public void displayThings();
	}

	private List<? extends Question2> diaoyanQuestionMatrixList;

	private boolean isMatrix;

	private int questionId;

	private int wjId;

	private Question2 question;

	private int score;

	private int questionType;

	private DiaoyanDatiLoadFinished mQuDatiLoadFinished;

	private List<? extends Options> choices;

	private int questionSumInWj;

	private String questionNo;

	private String questionTitle;

	/**
	 * cate 0 for interest  兴趣测试
	 * cate 1 for survey    调研测试
	 * cate 2 for self      自测  
	 * */
	private int cate;

	private int limitTime;

	public DoDataObject(int questionId, int cate) {
		this.questionId = questionId;
		this.cate = cate;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Question2 question = null;
		switch (cate) {
		case 0:
			question = DbManager.getDatabase().findUniqueBySql(
					InterestQuestion.class,
					"select * from interest_question where questionId=" + questionId);

			break;
		case 1:
//			diaoyanQuestion = DbManager.getDatabase().findUniqueBySql(
//					DiaoyanQuestion.class,
//					"select * from diaoyan_question where questionId="
//							+ questionId);
			break;
		case 2:
			question = DbManager.getDatabase().findUniqueBySql(
					SelfQuestion.class,
					"select * from self_question where questionId=" + questionId);
			break;
		}

		questionType = question.getQuestionType();
		isMatrix = question.isMatrix();
		if (isMatrix) {
//			diaoyanQuestionMatrixList = DbManager.getDatabase().findAllByWhere(
//					DiaoyanQuestion.class,
//					"mainNo='" + diaoyanQuestion.getMainNo() + "' and wjId="
//							+ diaoyanQuestion.getWjId(), "_id");
//			this.questionNo = diaoyanQuestion.getMainNo();
//			this.questionTitle = diaoyanQuestion.getMainTitle();
//			this.score = diaoyanQuestion.getChoiceNumber();
//			limitTime = 0;
//			for (Question question : diaoyanQuestionMatrixList) {
//				limitTime = limitTime + question.getLimitTime();
//
//			}

		} else {
			this.question = question;

			switch (cate) {
			case 0:
				choices = DbManager.getDatabase().findAllByWhere(
						InterestOption.class, "questionId=" + questionId);
				break;
			case 1:
//				choices = DbManager.getDatabase().findAllByWhere(
//						DiaoyanChoices.class, "questionId=" + questionId);
				break;
			case 2:
				choices = DbManager.getDatabase().findAllByWhere(
						SelfOption.class, "questionId=" + questionId);
				break;
			}

			this.questionNo = question.getQuestionNum();
			this.questionTitle = question.getQuestionContent();
			this.limitTime = question.getLimitTime();

		}
		this.wjId = question.getQuestionnaireId();

		switch (cate) {
		case 0:
			questionSumInWj = DbManager.getDatabase()
					.findAllByWhere(InterestQuestion.class, "questionnaireId=" + wjId).size();

			break;
		case 1:
			questionSumInWj = DbManager
					.getDatabase()
					.findAllByWhere(DiaoyanQuestion.class,
							"wjId=" + wjId + " and isMatrix=0").size();
			Cursor cursor = DbManager.getDatabase().findAllBySqlReturnCursor(
					DiaoyanQuestion.class,
					"select max(mainNo) from diaoyan_question where wjId="
							+ wjId + " and mainNo"
							+ " is not null group by mainNo");
			questionSumInWj = questionSumInWj + cursor.getCount();
			cursor.close();

			break;
		case 2:
			questionSumInWj = DbManager.getDatabase()
			.findAllByWhere(SelfQuestion.class, "questionnaireId=" + wjId).size();
			break;
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		mQuDatiLoadFinished.displayThings();
	}

	public int getQuestionType() {
		return questionType;
	}

	public int getQuestionId() {
		return questionId;
	}

	public List<? extends Options> getChoices() {
		return choices;
	}

	public void setmQuDatiLoadFinished(
			DiaoyanDatiLoadFinished mQuDatiLoadFinished) {
		this.mQuDatiLoadFinished = mQuDatiLoadFinished;
	}

	public int getQuestionSumInWj() {
		return questionSumInWj;
	}

	public int getWjId() {
		return wjId;
	}

	public List<? extends Question2> getDiaoyanQuestionMatrixList() {
		return diaoyanQuestionMatrixList;
	}

	public boolean isMatrix() {
		return isMatrix;
	}

	public Question2 getDiaoyanQuestion() {
		return question;
	}

	public int getScore() {
		return score;
	}

	public String getQuestionNo() {
		return questionNo;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public int getLimitTime() {
		return limitTime;
	}

}

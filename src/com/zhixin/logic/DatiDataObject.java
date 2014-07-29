package com.zhixin.logic;

import java.util.List;

import com.zhixin.database.DbManager;
import com.zhixin.domain.Choices;
import com.zhixin.domain.DiaoyanChoices;
import com.zhixin.domain.DiaoyanQuestion;
import com.zhixin.domain.QuChoices;
import com.zhixin.domain.QuQuestion;
import com.zhixin.domain.Question;

import android.database.Cursor;
import android.os.AsyncTask;

public class DatiDataObject extends AsyncTask<Void, Void, Void> {

	public interface DiaoyanDatiLoadFinished {
		public void displayThings();
	}

	private List<? extends Question> diaoyanQuestionMatrixList;

	private boolean isMatrix;

	private int questionId;

	private int wjId;

	private Question diaoyanQuestion;

	private int score;

	private int questionType;

	private DiaoyanDatiLoadFinished mQuDatiLoadFinished;

	private List<? extends Choices> choices;

	private int questionSumInWj;

	private String questionNo;

	private String questionTitle;

	// cate 0 for quceshi
	// cate 1 for qudiaoyan
	private int cate;

	private int limitTime;

	public DatiDataObject(int questionId, int cate) {
		this.questionId = questionId;
		this.cate = cate;
	}

	@Override
	protected Void doInBackground(Void... params) {
		Question diaoyanQuestion = null;
		switch (cate) {
		case 0:
			diaoyanQuestion = DbManager.getDatabase().findUniqueBySql(
					QuQuestion.class,
					"select * from qu_question where questionId=" + questionId);

			break;
		case 1:
			diaoyanQuestion = DbManager.getDatabase().findUniqueBySql(
					DiaoyanQuestion.class,
					"select * from diaoyan_question where questionId="
							+ questionId);

			break;
		}

		questionType = diaoyanQuestion.getQuestionType();
		isMatrix = diaoyanQuestion.isMatrix();
		if (isMatrix) {
			diaoyanQuestionMatrixList = DbManager.getDatabase().findAllByWhere(
					DiaoyanQuestion.class,
					"mainNo='" + diaoyanQuestion.getMainNo() + "' and wjId="
							+ diaoyanQuestion.getWjId(), "_id");
			this.questionNo = diaoyanQuestion.getMainNo();
			this.questionTitle = diaoyanQuestion.getMainTitle();
			this.score = diaoyanQuestion.getChoiceNumber();
			limitTime = 0;
			for (Question question : diaoyanQuestionMatrixList) {
				limitTime = limitTime + question.getLimitTime();

			}

		} else {
			this.diaoyanQuestion = diaoyanQuestion;

			switch (cate) {
			case 0:
				choices = DbManager.getDatabase().findAllByWhere(
						QuChoices.class, "questionId=" + questionId);
				break;
			case 1:
				choices = DbManager.getDatabase().findAllByWhere(
						DiaoyanChoices.class, "questionId=" + questionId);
				break;
			}

			this.questionNo = diaoyanQuestion.getQuesitonNo();
			this.questionTitle = diaoyanQuestion.getQuestionTitle();
			this.limitTime = diaoyanQuestion.getLimitTime();

		}
		this.wjId = diaoyanQuestion.getWjId();

		switch (cate) {
		case 0:
			questionSumInWj = DbManager.getDatabase()
					.findAllByWhere(QuQuestion.class, "wjId=" + wjId).size();

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

	public List<? extends Choices> getChoices() {
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

	public List<? extends Question> getDiaoyanQuestionMatrixList() {
		return diaoyanQuestionMatrixList;
	}

	public boolean isMatrix() {
		return isMatrix;
	}

	public Question getDiaoyanQuestion() {
		return diaoyanQuestion;
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

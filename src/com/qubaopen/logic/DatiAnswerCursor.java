package com.qubaopen.logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.database.Cursor;

import com.qubaopen.cache.PreviousUserQuestionCache;
import com.qubaopen.database.DbManager;
import com.qubaopen.domain.DiaoyanUserQuestionAnswer;
import com.qubaopen.domain.QuUserQuestionAnswer;
import com.qubaopen.domain.SelfUserQuestionAnswer;
import com.qubaopen.domain.UserQuestionAnswer;

public class DatiAnswerCursor {
	private int wjId;

	// 0 quceshi
	// 1 qudiaoyan
	private int cate;

	private int questionId;

	private List<ComplexQuesionObject> questionList;

	private StringBuffer sqlStrBuff;

	public DatiAnswerCursor(int wjId, int cate) {
		this.wjId = wjId;
		this.cate = cate;
		String sql = "";
		Cursor cursor = null;
		switch (cate) {
		case 0:
			sql = "select max(questionType),questionId from qu_user_question_answer"
					+ " where wjId="
					+ wjId
					+ " group by questionId order by _id asc";
			cursor = DbManager.getDatabase().findAllBySqlReturnCursor(
					QuUserQuestionAnswer.class, sql);
			break;
		case 1:
			sql = "select max(questionType),questionId,mainNo from diaoyan_user_question_answer"
					+ " where wjId="
					+ wjId
					+ " group by questionId order by _id asc";
			cursor = DbManager.getDatabase().findAllBySqlReturnCursor(
					DiaoyanUserQuestionAnswer.class, sql);

			break;
		case 2:
			sql = "select max(questionType),questionId from self_user_question_answer"
					+ " where selfId="
					+ wjId
					+ " group by questionId order by _id asc";
			cursor = DbManager.getDatabase().findAllBySqlReturnCursor(
					SelfUserQuestionAnswer.class, sql);

			break;
		default:
			break;
		}
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			questionList = new ArrayList<ComplexQuesionObject>();
			HashSet<String> mainNoSet = new HashSet<String>();

			do {
				if (cursorHasColumn("mainNo", cursor)
						&& !StringUtils.isBlank(cursor.getString(cursor
								.getColumnIndex("mainNo")))) {
					String mainNo = cursor.getString(cursor
							.getColumnIndex("mainNo"));

					if (!mainNoSet.contains(mainNo)) {
						questionList.add(new ComplexQuesionObject(cursor
								.getInt(cursor.getColumnIndex("questionId")),
								true, mainNo));
						mainNoSet.add(mainNo);
					}
				} else {
					questionList.add(new ComplexQuesionObject(cursor
							.getInt(cursor.getColumnIndex("questionId")),
							false, null));
				}
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (questionList != null && questionList.size() > 0) {
			this.questionId = questionList.get(questionList.size() - 1)
					.getQuestionId();
		}
	}

	public DatiAnswerCursor(int wjId, int cate, int quesitonId) {
		this(wjId, cate);
		this.questionId = quesitonId;

	}

	public Integer previousQuestion() {
		if (this.questionId != 0) {
			if (questionList != null && questionList.size() > 0) {
				saveHistroyToCache(questionList.get(questionList.size() - 1));
				deleteQuestionAnswer(questionList.get(questionList.size() - 1));
				return questionList.get(questionList.size() - 1)
						.getQuestionId();
			}
		}
		return null;
	}

	public boolean historyAnswered() {
		if (questionList == null) {
			return false;
		} else {
			return true;
		}
	}

	private void deleteQuestionAnswer(ComplexQuesionObject question) {
		switch (cate) {
		case 0:
			DbManager.getDatabase().deleteByWhere(QuUserQuestionAnswer.class,
					"questionId=" + question.getQuestionId());
			break;
		case 1:
			if (!question.isMatrix) {
				DbManager.getDatabase().deleteByWhere(
						DiaoyanUserQuestionAnswer.class,
						"questionId=" + question.getQuestionId());
			} else {

				DbManager.getDatabase().deleteByWhere(
						DiaoyanUserQuestionAnswer.class, sqlStrBuff.toString());

			}
		case 2:
			DbManager.getDatabase().deleteByWhere(SelfUserQuestionAnswer.class,
					"questionId=" + question.getQuestionId());
			break;
		default:
			break;
		}
	}

	private void saveHistroyToCache(ComplexQuesionObject question) {
		switch (cate) {
		case 0:
			PreviousUserQuestionCache.saveCache(DbManager.getDatabase()
					.findAllByWhere(QuUserQuestionAnswer.class,
							"questionId=" + question.getQuestionId()));
			break;
		case 1:

			if (!question.isMatrix()) {
				PreviousUserQuestionCache.saveCache(DbManager.getDatabase()
						.findAllByWhere(
								DiaoyanUserQuestionAnswer.class,
								"questionId=" + question.getQuestionId()
										+ " order by shunxuorder asc"));
			} else {
				sqlStrBuff = new StringBuffer();
				sqlStrBuff
						.append("questionId in (select questionId from diaoyan_user_question_answer ");
				sqlStrBuff.append("where mainNo='" + question.getMainNo());
				sqlStrBuff.append("' and wjId=(select distinct wjId from ");
				sqlStrBuff.append("diaoyan_user_question_answer where ");
				sqlStrBuff.append("questionId=" + question.getQuestionId());
				sqlStrBuff.append("))");
				PreviousUserQuestionCache.saveCache(DbManager.getDatabase()
						.findAllByWhere(DiaoyanUserQuestionAnswer.class,
								sqlStrBuff.toString()));
			}
			break;
		case 2:
			PreviousUserQuestionCache.saveCache(DbManager.getDatabase()
					.findAllByWhere(SelfUserQuestionAnswer.class,
							"questionId=" + question.getQuestionId()));
			break;
		default:
			break;
		}

	}

	public static List<? extends UserQuestionAnswer> getAnswer(int questionId,
			int cate) {
		switch (cate) {
		case 0:
			return DbManager.getDatabase().findAllByWhere(
					QuUserQuestionAnswer.class, "questionId=" + questionId);
		case 1:
			return DbManager.getDatabase()
					.findAllByWhere(DiaoyanUserQuestionAnswer.class,
							"questionId=" + questionId);
		case 2:
			return DbManager.getDatabase()
					.findAllByWhere(SelfUserQuestionAnswer.class,
							"questionId=" + questionId);
		}
		return null;
	}

	public List<? extends UserQuestionAnswer> getCurrentTopQuestionAnswer(
			int questionId) {
		switch (cate) {
		case 0:
			return DbManager.getDatabase().findAllByWhere(
					QuUserQuestionAnswer.class, "questionId=" + questionId);
		case 1:
			return DbManager.getDatabase()
					.findAllByWhere(DiaoyanUserQuestionAnswer.class,
							"questionId=" + questionId);
		case 2:
			return DbManager.getDatabase()
					.findAllByWhere(SelfUserQuestionAnswer.class,
							"questionId=" + questionId);
		default:
			return null;
		}

	}

	public Integer getCurrentTopQuestion() {
		if (questionList != null && questionList.size() > 0) {
			return questionList.get(questionList.size() - 1).getQuestionId();

		}

		return null;
	}

	public class ComplexQuesionObject {
		private Integer questionId;

		private boolean isMatrix;

		private String mainNo;

		public ComplexQuesionObject(Integer questionId, boolean isMatrix,
				String mainNo) {
			super();
			this.questionId = questionId;
			this.isMatrix = isMatrix;
			this.mainNo = mainNo;
		}

		public Integer getQuestionId() {
			return questionId;
		}

		public void setQuestionId(Integer questionId) {
			this.questionId = questionId;
		}

		public boolean isMatrix() {
			return isMatrix;
		}

		public void setMatrix(boolean isMatrix) {
			this.isMatrix = isMatrix;
		}

		public String getMainNo() {
			return mainNo;
		}

		public void setMainNo(String mainNo) {
			this.mainNo = mainNo;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + (isMatrix ? 1231 : 1237);
			result = prime * result
					+ ((mainNo == null) ? 0 : mainNo.hashCode());
			result = prime * result
					+ ((questionId == null) ? 0 : questionId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ComplexQuesionObject other = (ComplexQuesionObject) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (isMatrix != other.isMatrix)
				return false;
			if (mainNo == null) {
				if (other.mainNo != null)
					return false;
			} else if (!mainNo.equals(other.mainNo))
				return false;
			if (questionId == null) {
				if (other.questionId != null)
					return false;
			} else if (!questionId.equals(other.questionId))
				return false;
			return true;
		}

		private DatiAnswerCursor getOuterType() {
			return DatiAnswerCursor.this;
		}

	}

	private boolean cursorHasColumn(String str, Cursor cursor) {
		if (cursor != null) {
			for (String strInColumn : cursor.getColumnNames()) {
				if (str.equals(strInColumn)) {
					return true;
				}

			}

		}
		return false;
	}

}

package com.zhixin.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "qu_question_order")
public class QuQuestionOrder extends QuestionOrder{

	@Id(column = "_id")
	private int _id;

	private int currentQuestionId;

	private int nextQuestionId;

	private boolean nextOneEnd;

	private int choiceId;

	private String choiceNo;

	private int wjId;

	private int questionId;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getCurrentQuestionId() {
		return currentQuestionId;
	}

	public void setCurrentQuestionId(int currentQuestionId) {
		this.currentQuestionId = currentQuestionId;
	}

	public int getNextQuestionId() {
		return nextQuestionId;
	}

	public void setNextQuestionId(int nextQuestionId) {
		this.nextQuestionId = nextQuestionId;
	}

	public boolean isNextOneEnd() {
		return nextOneEnd;
	}

	public void setNextOneEnd(boolean nextOneEnd) {
		this.nextOneEnd = nextOneEnd;
	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

	public int getWjId() {
		return wjId;
	}

	public void setWjId(int wjId) {
		this.wjId = wjId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

}

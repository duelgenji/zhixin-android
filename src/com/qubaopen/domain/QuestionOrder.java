package com.qubaopen.domain;

public class QuestionOrder {

	protected int currentQuestionId;

	protected int nextQuestionId;

	protected boolean nextOneEnd;

	protected int choiceId;

	protected String choiceNo;

	protected int wjId;

	protected int questionId;

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

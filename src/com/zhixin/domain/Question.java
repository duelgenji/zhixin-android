package com.zhixin.domain;

public class Question {

	protected int wjId;

	protected String questionTitle;

	protected int questionId;

	protected int questionType;

	protected int choiceNumber;

	protected int limitTime;

	protected boolean isSpecial;

	protected String quesitonNo;

	protected boolean isMatrix;

	protected String mainTitle;

	protected String mainNo;
	
	protected int score;

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getWjId() {
		return wjId;
	}

	public void setWjId(int wjId) {
		this.wjId = wjId;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public int getChoiceNumber() {
		return choiceNumber;
	}

	public void setChoiceNumber(int choiceNumber) {
		this.choiceNumber = choiceNumber;
	}

	public int getLimitTime() {
		return limitTime;
	}

	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public String getQuesitonNo() {
		return quesitonNo;
	}

	public void setQuesitonNo(String quesitonNo) {
		this.quesitonNo = quesitonNo;
	}

	public boolean isMatrix() {
		return isMatrix;
	}

	public void setMatrix(boolean isMatrix) {
		this.isMatrix = isMatrix;
	}

	public String getMainTitle() {
		return mainTitle;
	}

	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}

	public String getMainNo() {
		return mainNo;
	}

	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}

}

package com.zhixin.domain;

public class UserQuestionAnswer {

	protected int questionId;

	protected int choiceId;

	protected String choiceNo;

	protected int score;

	protected int shunxuorder;

	protected int questionType;

	protected int wjId;

	protected String Answer;
	
	protected String mainNo;

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

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getShunxuorder() {
		return shunxuorder;
	}

	public void setShunxuorder(int shunxuorder) {
		this.shunxuorder = shunxuorder;
	}

	public int getQuestionType() {
		return questionType;
	}

	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}

	public int getWjId() {
		return wjId;
	}

	public void setWjId(int wjId) {
		this.wjId = wjId;
	}

	public String getAnswer() {
		return Answer;
	}

	public void setAnswer(String answer) {
		Answer = answer;
	}

	public String getMainNo() {
		return mainNo;
	}

	public void setMainNo(String mainNo) {
		this.mainNo = mainNo;
	}

}

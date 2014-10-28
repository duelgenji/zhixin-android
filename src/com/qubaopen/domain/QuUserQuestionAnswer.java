package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "qu_user_question_answer")
public class QuUserQuestionAnswer extends UserQuestionAnswer{
	@Id(column = "_id")
	private int _id;

	private int questionId;

	private int optionId;

	private String optionNum;

	private int score;

	private int shunxuorder;

	private int questionType;

	private int wjId;

	private String Answer;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
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

	public void setOptionId(int choiceId) {
		this.optionId = choiceId;
	}

	public String getOptionNum() {
		return optionNum;
	}

	public void setOptionNum(String choiceNo) {
		this.optionNum = choiceNo;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTurn() {
		return shunxuorder;
	}

	public void setTurn(int shunxuorder) {
		this.shunxuorder = shunxuorder;
	}

	public String getContent() {
		return Answer;
	}

	public void setContent(String answer) {
		Answer = answer;
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

}

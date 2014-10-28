package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "diaoyan_choices")
public class DiaoyanChoices extends Choices {

	@Id(column = "_id")
	private int _id;

	private boolean isMatrix;

	private String choiceNo;

	private String choiceTitle;

	private int choiceId;

	private int questionId;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public boolean isMatrix() {
		return isMatrix;
	}

	public void setMatrix(boolean isMatrix) {
		this.isMatrix = isMatrix;
	}

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

	public String getChoiceTitle() {
		return choiceTitle;
	}

	public void setChoiceTitle(String choiceTitle) {
		this.choiceTitle = choiceTitle;
	}

	public int getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(int choiceId) {
		this.choiceId = choiceId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

}

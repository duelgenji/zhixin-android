package com.zhixin.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "diaoyan_question")
public class DiaoyanQuestion extends Question{

	@Id(column = "_id")
	private int _id;

	private int wjId;

	private String questionTitle;

	private int questionId;

	private int questionType;

	private int choiceNumber;

	private int limitTime;

	private boolean isSpecial;

	private String quesitonNo;

	private boolean isMatrix;

	private String mainTitle;

	private String mainNo;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
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

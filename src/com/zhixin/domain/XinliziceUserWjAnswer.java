package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;

public class XinliziceUserWjAnswer {

	@Id(column = "_id")
	private int _id;

	private int memberId;

	private int wjId;

	private String wjTitle;

	private String answerTitle;

	private String answerChoiceNo;

	private String answerChoiceTitle;

	private String answerChoiceContent;

	private Date answerTime;

	private int controlFlag;

	private Integer isPublicAnwer;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getWjId() {
		return wjId;
	}

	public void setWjId(int wjId) {
		this.wjId = wjId;
	}

	public String getAnswerTitle() {
		return answerTitle;
	}

	public void setAnswerTitle(String answerTitle) {
		this.answerTitle = answerTitle;
	}

	public String getAnswerChoiceNo() {
		return answerChoiceNo;
	}

	public void setAnswerChoiceNo(String answerChoiceNo) {
		this.answerChoiceNo = answerChoiceNo;
	}

	public String getAnswerChoiceTitle() {
		return answerChoiceTitle;
	}

	public void setAnswerChoiceTitle(String answerChoiceTitle) {
		this.answerChoiceTitle = answerChoiceTitle;
	}

	public String getAnswerChoiceContent() {
		return answerChoiceContent;
	}

	public void setAnswerChoiceContent(String answerChoiceContent) {
		this.answerChoiceContent = answerChoiceContent;
	}

	public String getWjTitle() {
		return wjTitle;
	}

	public void setWjTitle(String wjTitle) {
		this.wjTitle = wjTitle;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}

	public Integer getIsPublicAnwer() {
		return isPublicAnwer;
	}

	public void setIsPublicAnwer(Integer isPublicAnwer) {
		this.isPublicAnwer = isPublicAnwer;
	}



}

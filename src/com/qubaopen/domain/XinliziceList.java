package com.qubaopen.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;

public class XinliziceList {

	@Id(column = "_id")
	private int _id;

	private long selfId;
	
	public long getSelfId() {
		return selfId;
	}

	public void setSelfId(long selfId) {
		this.selfId = selfId;
	}

	public String getManagementType() {
		return managementType;
	}

	public void setManagementType(String managementType) {
		this.managementType = managementType;
	}

	private int questionnarieId;

	private String title;

	private int credit;

	private int popularity;

	private String managementType;

	private int recommend;

	private Date time;

	private int friends;

	private int coin;

	private int failCredit;

	private String tags;

	private int wjorder;

	private int controlFlag; // 0 normal 1 questionnaire has been taken

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getQuestionnarieId() {
		return questionnarieId;
	}

	public void setQuestionnarieId(int questionnarieId) {
		this.questionnarieId = questionnarieId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getRecommend() {
		return recommend;
	}

	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getFriends() {
		return friends;
	}

	public void setFriends(int friends) {
		this.friends = friends;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public int getWjorder() {
		return wjorder;
	}

	public void setWjorder(int wjorder) {
		this.wjorder = wjorder;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getFailCredit() {
		return failCredit;
	}

	public void setFailCredit(int failCredit) {
		this.failCredit = failCredit;
	}


}

package com.qubaopen.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "diaoyan_list")
public class DiaoyanList {

	// "iWjId":"value",
	// "sTitle":"value", //问卷标题
	// "iCoin":"value", //完成获取金币
	// "iCredit":"value", //完成获取积分
	// "iFailCredit":"value", //失败获取积分
	// "iCompleted":"value", //当前完成数量
	// "iRequired":"value", //问卷需求数量
	// "iType":"value", //问卷类型
	// "iRecommend":"value",
	// "iRemainTime":"value" //剩余时间 单位：秒
	// "iFriends":"value" //好友参与人数
	// "aTags": [{ "iTag":"value" }] //问卷标签 1推荐 2热门 3最新 4限时 5少量 6金币

	@Id(column = "_id")
	private int _id;

	private int questionnarieId;

	private String title;

	private int credit;

	private int popularity;

	private int type;

	private int recommend;

	private Date time;

	private int friends;

	private int coin;

	private int failCredit;

	private int required;

	private String tags;

	private int wjorder;

	private int remainTime;

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public int getRequired() {
		return required;
	}

	public void setRequired(int required) {
		this.required = required;
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

	public int getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}
	
	

}

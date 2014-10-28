package com.qubaopen.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "qu_list")
public class QuList {
	@Id(column = "_id")
	private int _id;
	/**问题题目id*/
	private int questionnarieId;
	/**题目*/
	private String title;
	/**图片*/
	private String picUrl;
	/***/
	private int credit;
	/***/
	private int popularity;
	/**类型*/
	private int type;
	/***/
	private int recommend;
	/**时间*/
	private Date time;
	/**好友*/
	private int friends;
	/**金币*/
	private int coin;
	/***/
	private int failCredit;
	/**标签*/
	private String tags;
	/**问卷顺序*/
	private int wjorder;
	/***/
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
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
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

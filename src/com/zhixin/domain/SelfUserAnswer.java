package com.zhixin.domain;


import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "self_user_answer")
public class SelfUserAnswer {
	@Id(column = "_id")
	private int _id;

	private long userId;

	private int selfId;

	private String resultTitle;

	private String resultNum;

	private String optionTitle;

	private String content;


	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getSelfId() {
		return selfId;
	}

	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}

	public String getResultTitle() {
		return resultTitle;
	}

	public void setResultTitle(String resultTitle) {
		this.resultTitle = resultTitle;
	}

	public String getResultNum() {
		return resultNum;
	}

	public void setResultNum(String resultNum) {
		this.resultNum = resultNum;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


}

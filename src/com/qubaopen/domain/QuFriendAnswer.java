package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "qu_friend_answer")
public class QuFriendAnswer {
	@Id(column = "_id")
	private int _id;

	private int friendId;

	private String nickname;

	private String picUrl;

	private int wjId;

	private String choiceNo;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getWjId() {
		return wjId;
	}

	public void setWjId(int wjId) {
		this.wjId = wjId;
	}

	public String getChoiceNo() {
		return choiceNo;
	}

	public void setChoiceNo(String choiceNo) {
		this.choiceNo = choiceNo;
	}

}

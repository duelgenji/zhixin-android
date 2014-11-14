package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "user_mood_info")
public class UserMoodInfo {

	@Id(column = "_id")
	private int _id;
	
	private long userId;

	private int moodId;

	private String moodDate;

	private String moodMessage;

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

	public int getMoodId() {
		return moodId;
	}

	public void setMoodId(int moodId) {
		this.moodId = moodId;
	}

	public String getMoodDate() {
		return moodDate;
	}

	public void setMoodDate(String moodDate) {
		this.moodDate = moodDate;
	}

	public String getMoodMessage() {
		return moodMessage;
	}

	public void setMoodMessage(String moodMessage) {
		this.moodMessage = moodMessage;
	}

}

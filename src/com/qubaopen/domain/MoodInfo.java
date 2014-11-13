package com.qubaopen.domain;

import java.util.Date;

public class MoodInfo {
	
	private int moodId;
	
	private Date moodDate;
	
	private String moodMessage;

	public int getMoodId() {
		return moodId;
	}

	public void setMoodId(int moodId) {
		this.moodId = moodId;
	}

	public Date getMoodDate() {
		return moodDate;
	}

	public void setMoodDate(Date moodDate) {
		this.moodDate = moodDate;
	}

	public String getMoodMessage() {
		return moodMessage;
	}

	public void setMoodMessage(String moodMessage) {
		this.moodMessage = moodMessage;
	}
	
	
}

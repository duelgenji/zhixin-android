package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "qu_huati")
public class QuHuati {
	@Id(column = "_id")
	private int _id;

	private int hutatiId;

	private int participateNumber;

	private Date time;

	private String title;

	private int type;

	private String picUrl;

	private String usernickname;

	private int friendNumber;

	private int controlFlag;

	private int answeredOrNot;
	
	private Integer creditGain;

	private Integer status;

	// 0 for current huati
	// 1 for my participate
	// 2 for my publish
	private int uiCate;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getHutatiId() {
		return hutatiId;
	}

	public void setHutatiId(int hutatiId) {
		this.hutatiId = hutatiId;
	}

	public int getParticipateNumber() {
		return participateNumber;
	}

	public void setParticipateNumber(int participateNumber) {
		this.participateNumber = participateNumber;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUsernickname() {
		return usernickname;
	}

	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}

	public int getFriendNumber() {
		return friendNumber;
	}

	public void setFriendNumber(int friendNumber) {
		this.friendNumber = friendNumber;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}

	public int getUiCate() {
		return uiCate;
	}

	public void setUiCate(int uiCate) {
		this.uiCate = uiCate;
	}

	public int getAnsweredOrNot() {
		return answeredOrNot;
	}

	public void setAnsweredOrNot(int answeredOrNot) {
		this.answeredOrNot = answeredOrNot;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCreditGain() {
		return creditGain;
	}

	public void setCreditGain(Integer creditGain) {
		this.creditGain = creditGain;
	}
	
	
	

}

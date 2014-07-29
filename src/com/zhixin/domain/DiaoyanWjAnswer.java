package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "diaoyan_wj_answer")
public class DiaoyanWjAnswer {
	@Id(column = "_id")
	private int _id;

	private int wjId;

	private Date answerTime;

	private String wjTitle;

	private int status;

	private Integer coin;

	private Integer credit;

	private int controlFlag;

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

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public String getWjTitle() {
		return wjTitle;
	}

	public void setWjTitle(String wjTitle) {
		this.wjTitle = wjTitle;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}

	public Integer getCoin() {
		return coin;
	}

	public void setCoin(Integer coin) {
		this.coin = coin;
	}

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

}

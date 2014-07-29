package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "history_duijiang")
public class HistoryDuijiang {

	@Id(column = "_id")
	private int _id;

	// "type":"value" //0抽奖 1兑奖
	// "id":"value", //用户抽奖或兑奖ID
	// "title":"value", //兑抽奖标题
	// "date":"value", //兑抽奖时间
	// "credit":"value", //所消耗的积分数量
	// "coin":"value", //所消耗的金币数量 抽奖活动固定为0
	// "status":"value" //0:抽奖中 1:未中奖 2:领奖 3:发货中 4:待确认 5:已确认 6:处理中

	private int type;
	private int duijiangId;
	private String title;
	private Date date;
	private int credit;
	private int coin;
	private int status;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getDuijiangId() {
		return duijiangId;
	}

	public void setDuijiangId(int duijiangId) {
		this.duijiangId = duijiangId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}

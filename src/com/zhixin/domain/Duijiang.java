package com.zhixin.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "quduijiang")
public class Duijiang {
	@Id(column = "_id")
	private int _id;

	private int lotteryId;

	private String picUrl;

    private int creditConsume;

    private int coinConsume;

	private String title;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(int lotteryId) {
		this.lotteryId = lotteryId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


    public int getCreditConsume() {
        return creditConsume;
    }

    public void setCreditConsume(int creditConsume) {
        this.creditConsume = creditConsume;
    }

    public int getCoinConsume() {
        return coinConsume;
    }

    public void setCoinConsume(int coinConsume) {
        this.coinConsume = coinConsume;
    }
}

package com.zhixin.domain;

import android.R.integer;
import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "quduijiang")
public class Duijiang {
	@Id(column = "_id")
	private int _id;

	private int lotteryId;

	private String picUrl;
	
	private String title;
	
	private String content;
	
	//每人参与次数,0为不限
    private int creditConsume;
    
    //金币消耗量
    private int coinConsume;

    //奖品总数
    private int totalCountLimit;
    
    //剩余数量
    private int currnetCount;
    
    private Boolean isNew;
    
    private String rewardType;
    
    //奖品状态
    private String status;
    
	
    

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

	public int getTotalCountLimit() {
		return totalCountLimit;
	}

	public void setTotalCountLimit(int totalCountLimit) {
		this.totalCountLimit = totalCountLimit;
	}

	public int getCurrnetCount() {
		return currnetCount;
	}

	public void setCurrnetCount(int currnetCount) {
		this.currnetCount = currnetCount;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}

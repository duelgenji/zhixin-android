package com.qubaopen.domain;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by duel on 14-3-7.
 */

@Table(name = "user_settings")
public class UserSettings {
    @Id(column = "_id")
   
    /**id*/
    private int _id;
    
    /**用户id*/
    private long userId;
  
    /**是否提示*/
    private boolean isPush;
   
    /**是否对好友公开问卷*/
    private boolean isPublicAnswersToFriend;
   
    /**是否对门主公开问卷*/
//    private Integer isMzgkwj;
  
    /**是否公开答题*/
//    private Integer isGkdt;
   
    /**是否开启省流量模式*/
    private boolean isSaveFlow;
   
    /**开始时间*/
    private String statTime;
   
    /**结束时间*/
    private String endTime;

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


	public boolean isPush() {
		return isPush;
	}

	public void setPush(boolean isPush) {
		this.isPush = isPush;
	}

	public boolean isPublicAnswersToFriend() {
		return isPublicAnswersToFriend;
	}

	public void setPublicAnswersToFriend(boolean isPublicAnswersToFriend) {
		this.isPublicAnswersToFriend = isPublicAnswersToFriend;
	}

	public boolean isSaveFlow() {
		return isSaveFlow;
	}

	public void setSaveFlow(boolean isSaveFlow) {
		this.isSaveFlow = isSaveFlow;
	}

	public String getStatTime() {
		return statTime;
	}

	public void setStatTime(String statTime) {
		this.statTime = statTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


   
}

package com.zhixin.domain;


import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "self_list")
public class SelfList {
	
	@Id(column = "_id")
	private int _id;
	
	/**问题题目id*/
	private int selfId;
	
	/**标题*/
	private String title;
	
	/**自测问卷类型  1性格分析, 2 情绪管理, 3个人发展*/
	private int managementType;

	/**指导语*/
	private String guidanceSentence;
	
	/**简介*/
	private String remark;
	
	/**提示语*/
	private String tips;
	
	/**最后一次获取时间*/
	private Date lastGetTime;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getSelfId() {
		return selfId;
	}

	public void setSelfId(int selfId) {
		this.selfId = selfId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getManagementType() {
		return managementType;
	}

	public void setManagementType(int managementType) {
		this.managementType = managementType;
	}

	public String getGuidanceSentence() {
		return guidanceSentence;
	}

	public void setGuidanceSentence(String guidanceSentence) {
		this.guidanceSentence = guidanceSentence;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Date getLastGetTime() {
		return lastGetTime;
	}

	public void setLastGetTime(Date lastGetTime) {
		this.lastGetTime = lastGetTime;
	}
	
	

}

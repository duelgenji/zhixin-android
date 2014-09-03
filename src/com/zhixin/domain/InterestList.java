package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "interest_list")
public class InterestList {
	@Id(column = "_id")
	private int _id;
	/**问题题目id*/
	private int interestId;
	/**题目*/
	private String title;
	/**图片*/
	private String picPath;
	/**兴趣问卷类型*/
	private String interestType;
	/**推荐值*/
	private int recommendedValue;
	/**时间*/
	private Date time;
	/**好友*/
	private int friendCount;
	/**金币*/
	private int golds;
	/**标签   ; 分割*/
	private String questionnaireTagType;
	/**备注*/
	private String remark;
	/**总共参与人数*/
	private int totalRespondentsCount;
	/**已完成标志  后续可以通过其他方式 不用这个字段*/
	private int controlFlag; // 0 normal 1 questionnaire has been taken

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getInterestId() {
		return interestId;
	}

	public void setInterestId(int interestId) {
		this.interestId = interestId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getInterestType() {
		return interestType;
	}

	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}

	public int getRecommendedValue() {
		return recommendedValue;
	}

	public void setRecommendedValue(int recommendedValue) {
		this.recommendedValue = recommendedValue;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getFriendCount() {
		return friendCount;
	}

	public void setFriendCount(int friendCount) {
		this.friendCount = friendCount;
	}

	public int getGolds() {
		return golds;
	}

	public void setGolds(int golds) {
		this.golds = golds;
	}

	public String getQuestionnaireTagType() {
		return questionnaireTagType;
	}

	public void setQuestionnaireTagType(String questionnaireTagType) {
		this.questionnaireTagType = questionnaireTagType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getControlFlag() {
		return controlFlag;
	}

	public void setControlFlag(int controlFlag) {
		this.controlFlag = controlFlag;
	}

	public int getTotalRespondentsCount() {
		return totalRespondentsCount;
	}

	public void setTotalRespondentsCount(int totalRespondentsCount) {
		this.totalRespondentsCount = totalRespondentsCount;
	}

	
}

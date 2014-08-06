package com.zhixin.domain;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "user_info")
public class UserInfo {

	@Id(column = "_id")
	private int _id;

	private String username;

	private String password;

	// member's real name,it is supposed to be stored with the validated id.
	private String name;

	private int memberId;

	// sex 0 for male 1 for female
	private int sex;

	private String nickname;

	private Date birthday;

	private String identityNumber;

	// '0'=A型 '1'=B型 '2'=O型 '3'=AB型 '4'=其他
	private int bloodtype;

	private String district;

	private String address;

	private String email;

	private String picUrl;

	private int coin;

	private int credit;

	private int historyCredit;

	private int historyCoin;

	private int rank;

	private int rankMonth;

	private int rankWeek;

	private int unsend;

	private int unread;

	private int qiandaoId;

	private int fameValue;

	private int petNumber;

	private int level;

	private String levelName;

	private String levelPromotionTips;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}

	public int getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(int bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}

	public int getHistoryCredit() {
		return historyCredit;
	}

	public void setHistoryCredit(int historyCredit) {
		this.historyCredit = historyCredit;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public int getUnsend() {
		return unsend;
	}

	public void setUnsend(int unsend) {
		this.unsend = unsend;
	}

	public int getUnread() {
		return unread;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public int getQiandaoId() {
		return qiandaoId;
	}

	public void setQiandaoId(int qiandaoId) {
		this.qiandaoId = qiandaoId;
	}

	public int getHistoryCoin() {
		return historyCoin;
	}

	public void setHistoryCoin(int historyCoin) {
		this.historyCoin = historyCoin;
	}

	public int getFameValue() {
		return fameValue;
	}

	public void setFameValue(int fameValue) {
		this.fameValue = fameValue;
	}

	public int getPetNumber() {
		return petNumber;
	}

	public void setPetNumber(int petNumber) {
		this.petNumber = petNumber;
	}

	public String getLevelPromotionTips() {
		return levelPromotionTips;
	}

	public void setLevelPromotionTips(String levelPromotionTips) {
		this.levelPromotionTips = levelPromotionTips;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRankMonth() {
		return rankMonth;
	}

	public void setRankMonth(int rankMonth) {
		this.rankMonth = rankMonth;
	}

	public int getRankWeek() {
		return rankWeek;
	}

	public void setRankWeek(int rankWeek) {
		this.rankWeek = rankWeek;
	}

	

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

}

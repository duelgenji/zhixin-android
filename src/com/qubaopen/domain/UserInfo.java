package com.qubaopen.domain;

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

//	private int memberId;
	private long userId;

	
	// sex 0 for male 1 for female
	private Integer sex;

	private String nickName;
	
	private String signature;

	private String birthDay;

	private String identityNumber;

	// '0'=A型 '1'=B型 '2'=O型 '3'=AB型 '4'=其他
	private Integer bloodType;

	private String district;

	private String address;

	private String email;

	private String picUrl;

	private int coin;

	private int historyCoin;

	private int unsend;

	private int unread;

	private int qiandaoId;

	private int fameValue;

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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}



	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public String getIdentityNumber() {
		return identityNumber;
	}

	public void setIdentityNumber(String identityNumber) {
		this.identityNumber = identityNumber;
	}


	public Integer getBloodType() {
		return bloodType;
	}

	public void setBloodType(Integer bloodType) {
		this.bloodType = bloodType;
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

}

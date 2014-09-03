package com.zhixin.daos;

import java.text.ParseException;

import net.tsz.afinal.FinalDb;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;
import com.zhixin.domain.UserSettings;

public class UserSettingsDao {
	private final FinalDb db = DbManager.getDatabase();
	public void saveUserSettings(JSONObject jbo,long userId) throws JSONException, ParseException {
		Log.i("获取到的设置", jbo+"");
		UserSettings us = null;

		if (db.tableExists(UserInfo.class)) {

			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
			
		}
		if (us == null) {
			us = new UserSettings();
			setContent(jbo, us);
			us.setUserId(userId);
			db.save(us);
		}else {
			setContent(jbo, us);
			db.update(us);
		}
		Log.i("更新本地设置", us + "");
		
	}

	public void saveUserSettingsFront(JSONObject jbo,long userId) throws JSONException, ParseException {
		Log.i("获取到的前二设置", jbo+"");
		UserSettings us = null;

		if (db.tableExists(UserInfo.class)) {

			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
			
		}
		if (us == null) {
			us = new UserSettings();
			setContentFront(jbo, us);
			us.setUserId(userId);
			db.save(us);
		}else {
			setContentFront(jbo, us);
			db.update(us);
		}
		Log.i("更新本地设置前二", us + "");
		
	}
	
	public void saveUserSettingsBehind(JSONObject jbo,long userId) throws JSONException, ParseException {
		Log.i("获取到的后二设置", jbo+"");
		UserSettings us = null;

		if (db.tableExists(UserInfo.class)) {

			us = db.findUniqueByWhere(UserSettings.class, "userId='" + userId
					+ "'");
			
		}
		if (us == null) {
			us = new UserSettings();
			setContentBehind(jbo, us);
			db.save(us);
		}else {
			setContentBehind(jbo, us);
			db.update(us);
		}
		Log.i("更新本地设置后二", us + "");
		
	}
	private void setContent(JSONObject jbo,UserSettings us)throws JSONException, ParseException {
		//是否推送
		if (StringUtils.isNotEmpty(jbo.getString("push"))) {
			us.setPush(jbo.getBoolean("push"));
		}else {
			us.setPush(true);
		}
		
		// 开始时间
		if (StringUtils.isNotEmpty(jbo.getString("startTime"))){
			us.setStatTime(jbo.getString("startTime"));
		}else {
			us.setStatTime("09:00");
		}
		// 结束时间
		if (StringUtils.isNotEmpty(jbo.getString("endTime"))){
			us.setEndTime(jbo.getString("endTime"));
		}else {
			us.setEndTime("22:00");
		}
		
		// 是否向好友公开问卷
		if (StringUtils.isNotEmpty(jbo.getString("publicAnswersToFriend"))) {
			us.setPublicAnswersToFriend(jbo.getBoolean("publicAnswersToFriend"));
		}else {
			us.setPush(false);
		}
				
		// 是否开启省流量模式
		if (StringUtils.isNotEmpty(jbo.getString("saveFlow"))) {
			us.setSaveFlow(jbo.getBoolean("saveFlow"));
		}else {
			us.setSaveFlow(true);
		}
	}
	private void setContentFront(JSONObject jbo,UserSettings us)throws JSONException, ParseException {
		//是否推送
		if (StringUtils.isNotEmpty(jbo.getString("isPush"))) {
			us.setPush(jbo.getBoolean("isPush"));
		}else {
			us.setPush(true);
		}
		
		// 开始时间
		if (StringUtils.isNotEmpty(jbo.getString("startTime"))){
			us.setStatTime(jbo.getString("startTime"));
		}else {
			us.setStatTime("09:00");
		}
		// 结束时间
		if (StringUtils.isNotEmpty(jbo.getString("endTime"))){
			us.setEndTime(jbo.getString("endTime"));
		}else {
			us.setEndTime("22:00");
		}
	}
	private void setContentBehind(JSONObject jbo,UserSettings us)throws JSONException, ParseException {
		
		// 是否向好友公开问卷
		if (StringUtils.isNotEmpty(jbo.getString("publicAnswersToFriend"))) {
			us.setPublicAnswersToFriend(jbo.getBoolean("publicAnswersToFriend"));
		}else {
			us.setPush(false);
		}
		
		// 是否开启省流量模式
		if (StringUtils.isNotEmpty(jbo.getString("saveFlow"))) {
			us.setSaveFlow(jbo.getBoolean("saveFlow"));
		}else {
			us.setSaveFlow(true);
		}
		
	}
	public UserSettings getUserSettings(long userId) {

		return db.findUniqueByWhere(UserSettings.class, "userId='" + userId
				+ "'");

	}

}

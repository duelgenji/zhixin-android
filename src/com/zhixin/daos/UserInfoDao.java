package com.zhixin.daos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.tsz.afinal.FinalDb;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserInfo;

public class UserInfoDao {
//保存用户第一次登陆的信息
	public void saveUserForFirsttime(JSONObject jbo, String password)
			throws JSONException, ParseException {
//		用户名默认是手机号码
		String username = jbo.getString("phone");
		
		UserInfo user = null;
		final FinalDb db = DbManager.getDatabase();
//		检查用户信息是否存在
		if (db.tableExists(UserInfo.class)) {			
			user = db.findUniqueByWhere(UserInfo.class, "username='"+username+"'");
		
		}
		if (user == null) {
			user = new UserInfo();
			user.setUsername(username);
			user.setPassword(password);
		   //user = saveUserForFirsttime_particial(jbo, user);
			db.save(user);
		} else {
			user = saveUserForFirsttime_particial(jbo, user);
			user.setPassword(password);
			db.update(user);
		}

	}

	private UserInfo saveUserForFirsttime_particial(JSONObject jbo,
			UserInfo user) throws JSONException, ParseException {

		if (StringUtils.isNotEmpty(jbo.getString("memberId"))) {
			user.setMemberId(jbo.getInt("memberId"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("name"))) {
			user.setName(jbo.getString("name"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("sex"))) {
			user.setSex(jbo.getInt("sex"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("nickname"))) {
			user.setNickname(jbo.getString("nickname"));
		}

		if (StringUtils.isNotEmpty(jbo.getString("birthdate"))) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date = formatter.parse(jbo.getString("birthdate"));
			user.setBirthday(date);
		}
		if (StringUtils.isNotEmpty(jbo.getString("id"))) {
			user.setIdentityNumber(jbo.getString("id"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("bloodtype"))) {
			user.setBloodtype(jbo.getInt("bloodtype"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("district"))) {
			user.setDistrict(jbo.getString("district"));
		}

		if (StringUtils.isNotEmpty(jbo.getString("address"))) {
			user.setAddress(jbo.getString("address"));
		}
		if (StringUtils.isNotEmpty(jbo.getString("email"))) {
			user.setEmail(jbo.getString("email"));
		}


		return user;

	}

	public void saveUserInfoPassword(String phone, String password) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user == null) {
			user = new UserInfo();
			user.setUsername(phone);
			user.setPassword(password);
			DbManager.getDatabase().save(user);
		} else {
			user.setPassword(password);
			DbManager.getDatabase().update(user);
		}
	}

	public void saveUserInfoEmail(String phone, String email) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user == null) {
			user = new UserInfo();
			user.setUsername(phone);
			user.setEmail(email);
			DbManager.getDatabase().save(user);
		} else {
			user.setEmail(email);
			DbManager.getDatabase().update(user);
		}
	}

	public void saveUserInfoSex(String phone, Integer sex) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user == null) {
			user = new UserInfo();
			user.setUsername(phone);
			user.setSex(sex);
			DbManager.getDatabase().save(user);
		} else {
			user.setSex(sex);
			DbManager.getDatabase().update(user);
		}
	}

	public void saveUserInfoBloodType(String phone, Integer type) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user == null) {
			user = new UserInfo();
			user.setUsername(phone);
			user.setBloodtype(type);
			DbManager.getDatabase().save(user);
		} else {
			user.setBloodtype(type);
			DbManager.getDatabase().update(user);
		}
	}

	public void saveUserInfoBirthDay(String phone, Date date) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user == null) {
			user = new UserInfo();
			user.setUsername(phone);
			user.setBirthday(date);
			DbManager.getDatabase().save(user);
		} else {
			user.setBirthday(date);
			DbManager.getDatabase().update(user);
		}
	}

	public String getUserPasswordByPhone(String phone) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user != null) {
			return user.getPassword();
		} else {
			return null;
		}
	}

	public UserInfo saveUserZhibiInfo(String phone, JSONObject result)
			throws JSONException {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		if (user != null) {
			if (!StringUtils.isEmpty(result.getString("picUrl"))) {
				user.setPicUrl(result.getString("picUrl"));
			}

			if (!StringUtils.isEmpty(result.getString("nickName"))) {
				user.setNickname(result.getString("nickName"));
			}

			if (!StringUtils.isEmpty(result.getString("coin"))) {
				user.setCoin(result.getInt("coin"));
			}

			if (!StringUtils.isEmpty(result.getString("unsend"))) {
				user.setUnsend(result.getInt("unsend"));
			}

			if (!StringUtils.isEmpty(result.getString("qiandaoId"))) {
				user.setQiandaoId(result.getInt("qiandaoId"));
			}
			DbManager.getDatabase().update(user);
		}
		return user;

	}

	public UserInfo getUserByphone(String phone) {
		String sql = "select * from user_info where username='" + phone + "'";
		UserInfo user = DbManager.getDatabase().findUniqueBySql(UserInfo.class,
				sql);
		return user;
	}

}

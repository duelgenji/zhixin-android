package com.zhixin.daos;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.UserSettings;

public class UserSettingsDao {

	public void updateSettings(JSONObject jbo) throws JSONException{
		 UserSettings us=DbManager.getDatabase().findById(1, UserSettings.class);
//			是否公开答题
         us.setIsGkdt(jbo.getInt("iGkdt"));
//         是否向好友公开问卷
         us.setIsHygkwj(jbo.getInt("iHygkwj"));
//         是否向萌主公开问卷
         us.setIsMzgkwj(jbo.getInt("iMzgkwj"));
//         是否开启省流量模式
         us.setIsSllms(jbo.getInt("iSllms"));
         //开始时间
         us.setKssj(jbo.getString("sStartTime"));
         //结束时间
         us.setJssj(jbo.getString("sEndTime"));
        
         us.setIsTs(jbo.getInt("isTs"));
         
         DbManager.getDatabase().update(us);
	}
	
	public UserSettings getUserSettings(){
		
		return DbManager.getDatabase().findById(1, UserSettings.class);
		
	}
	
}

package com.zhixin.daos;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.SelfList;

public class SelfListDao {

	/**保存自测问卷列表  返回问卷组id 用来做数据库查询 格式如: “15,5,6”*/
	public String saveSelfList(JSONObject jbo) {

		String ids="";
		try {
			JSONArray array;
			array = jbo.getJSONArray("data");
			boolean isNew=false;

			SelfList selfList;
			JSONObject jboInA;
			for (int i = 0; i < array.length(); i++) {
				jboInA = array.getJSONObject(i);

				if (jboInA.has("selfId")
						&& StringUtils.isNotEmpty(jboInA.getString("selfId"))) {
					int selfId = jboInA.getInt("selfId");
					selfList = getSelfList(selfId);
					if (selfList == null) {
						isNew=true;
						selfList = new SelfList();
						selfList.setSelfId(selfId);
					}

					if (jboInA.has("title")
							&& StringUtils.isNotEmpty(jboInA
									.getString("title"))) {
						selfList.setTitle(jboInA.getString("title"));
					}

					if (jboInA.has("managementType")
							&& StringUtils.isNotEmpty(jboInA
									.getString("managementType"))) {
						selfList.setManagementType(jboInA
								.getInt("managementType"));
					}
					
					selfList.setLastGetTime(new Date());
					
					if(isNew)
						DbManager.getDatabase().save(selfList);
					else 
						DbManager.getDatabase().update(selfList);
					
					ids+= (i==array.length()-1)?selfId:selfId+",";
					
					
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;

	}

	/**
	 * 添加用户自测问卷的其他信息 提示语 简介 指导语
	 * */
	public void addSelfListInfo(JSONObject jbo) {
		try {

			SelfList selfList;

			if (jbo.has("selfId")
					&& StringUtils.isNotEmpty(jbo.getString("selfId"))) {
				int selfId = jbo.getInt("selfId");
				selfList = getSelfList(selfId);
				if (selfList == null) {
					return;
				}

				if (jbo.has("guidanceSentence")
						&& StringUtils.isNotEmpty(jbo.getString("guidanceSentence"))) {
					selfList.setGuidanceSentence(jbo.getString("guidanceSentence"));
				}

				if (jbo.has("tips")
						&& StringUtils.isNotEmpty(jbo.getString("tips"))) {
					selfList.setTips(jbo.getString("tips"));
				}
				
				if (jbo.has("remark")
						&& StringUtils.isNotEmpty(jbo.getString("remark"))) {
					selfList.setRemark(jbo.getString("remark"));
				}

				selfList.setLastGetTime(new Date());
				DbManager.getDatabase().update(selfList);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/** 判断自测问卷是否存在 存在的话返回之前的对象 用来作修改 */
	private SelfList getSelfList(int selfId) {
		if (DbManager.getDatabase().tableExists(SelfList.class)) {
			SelfList selfList = DbManager.getDatabase().findUniqueByWhere(
					SelfList.class, "selfId=" + selfId);
			return selfList;
		}
		return null;
	}

}

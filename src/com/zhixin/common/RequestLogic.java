package com.zhixin.common;

import org.json.JSONObject;

/**
 * @author Administrator
 *请求逻辑
 */
public interface RequestLogic {
	/** 加载*/
	public void onLoading(long count, long current);
	/** 如果成功*/
	public void whenSuccess(JSONObject result);
	/** 如果失败*/
	public void whenFail(JSONObject message);
	/** 如果请求失败*/
	public void whenRequestFail(String errcode);
}

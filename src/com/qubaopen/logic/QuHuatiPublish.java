package com.qubaopen.logic;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuHuatiPublish {

	private String huatiTitle;

	private int type;

	private List<String> content;

	public QuHuatiPublish(String huatiTitle, int type, List<String> content) {
		super();
		this.huatiTitle = huatiTitle;
		this.type = type;
		this.content = content;
	}

	public String getHuatiTitle() {
		return huatiTitle;
	}

	public int getType() {
		return type;
	}

	public JSONObject generateAnswerJSON() throws JSONException {
		JSONObject jbo = new JSONObject();
		jbo.put("sTitle", huatiTitle);
		jbo.put("iType", type);

		JSONArray array = new JSONArray();
		JSONObject jboInA;
		for (String one : content) {
			jboInA = new JSONObject();
			jboInA.put("sTitle", one);
			array.put(jboInA);
		}

		jbo.put("aChoice", array);
		
		return jbo;
	}

}

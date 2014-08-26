package com.zhixin.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConvertData4HighCharts {

	public JSONObject getSpiderData(JSONObject obj) {
		JSONObject returnObj = new JSONObject();
		try {

			if (obj.has("title")) {
				returnObj.put("title", obj.get("title"));

			}
			

			if (obj.has("max")) {
				returnObj.put("max", obj.get("max"));

			}

			if (obj.has("chart")) {
				JSONArray aParent = obj.getJSONArray("chart");
				JSONArray aCate = new JSONArray();
				JSONArray aData = new JSONArray();
				JSONObject jChild;
				for (int i = 0; i < aParent.length(); i++) {
					jChild = (JSONObject) aParent.get(i);
					aCate.put(jChild.get("name"));
					aData.put(jChild.get("value"));
				}
				returnObj.put("categories", aCate);
				returnObj.put("data", aData);

				
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return returnObj;
	}

}

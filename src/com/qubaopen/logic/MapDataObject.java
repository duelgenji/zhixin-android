package com.qubaopen.logic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.domain.MapData;

public class MapDataObject {

	public static List<MapData> manageDataFromJson(JSONObject jbo)
			throws JSONException {

		JSONArray array = new JSONArray();
		MapData mapData = null;
		JSONObject tempJson = new JSONObject();

		if (jbo.has("data") && StringUtils.isNotEmpty(jbo.getString("data"))) {
			array = jbo.getJSONArray("data");
		}

		List<MapData> dataList = new ArrayList<MapData>();

		for (int i = 0; i < array.length(); i++) {

			tempJson = array.getJSONObject(i);
			mapData = new MapData();

			if (tempJson.has("lock")
					&& StringUtils.isNotEmpty(tempJson.getString("lock"))) {
				mapData.setMapDataIsLock(tempJson.getBoolean("lock"));
			}

			if (tempJson.has("graphicsType")
					&& StringUtils.isNotEmpty(tempJson
							.getString("graphicsType"))) {

				mapData.setMapDataGraphicsType(tempJson
						.getString("graphicsType"));
			}

			if (tempJson.has("tips")
					&& StringUtils.isNotEmpty(tempJson.getString("tips"))) {
				mapData.setMapDataTips(tempJson.getString("tips"));
			}

			if (tempJson.has("point")
					&& StringUtils.isNotEmpty(tempJson.getString("point"))) {
				mapData.setMapDataPoint(tempJson.getJSONObject("point"));
			}

			if (tempJson.has("level")
					&& StringUtils.isNotEmpty(tempJson.getString("level"))) {
				mapData.setMapDataLevel(tempJson.getInt("level"));
			}

			if (tempJson.has("picPath")
					&& StringUtils.isNotEmpty(tempJson.getString("picPath"))) {
				mapData.setMapDataPicPath(tempJson.getString("picPath"));
			}

			if (tempJson.has("chart")
					&& StringUtils.isNotEmpty(tempJson.getString("chart"))) {
				mapData.setMapDataChat(tempJson.getJSONArray("chart"));
			}

			if (tempJson.has("mapMax")
					&& StringUtils.isNotEmpty(tempJson.getString("mapMax"))) {
				mapData.setMapDataMax(tempJson.getInt("mapMax"));
			}

			if (tempJson.has("resultScore")
					&& StringUtils
							.isNotEmpty(tempJson.getString("resultScore"))) {
				mapData.setMapDataResultScore(tempJson.getString("resultScore"));
			}

			if (tempJson.has("resultName")
					&& StringUtils.isNotEmpty(tempJson.getString("resultName"))) {
				mapData.setMapDataName(tempJson.getString("resultName"));
			}

			if (tempJson.has("mapTitle")
					&& StringUtils.isNotEmpty(tempJson.getString("mapTitle"))) {
				mapData.setMapDataTitle(tempJson.getString("mapTitle"));
			}

			if (tempJson.has("resultContent")
					&& StringUtils.isNotEmpty(tempJson
							.getString("resultContent"))) {
				mapData.setMapDataContent(tempJson.getString("resultContent"));

				// singData.put("resultContent",
				// tempJson.getString("resultContent"));
			}

			if (tempJson.has("recommendedValue")
					&& StringUtils.isNotEmpty(tempJson
							.getString("recommendedValue"))) {
				mapData.setMapDataRecommededValue(tempJson
						.getInt("recommendedValue"));
			}

			if (tempJson.has("special")
					&& StringUtils.isNotEmpty(tempJson.getString("special"))) {
				mapData.setMapDataIsSpecial(tempJson.getBoolean("special"));
			}

			if (tempJson.has("managementType")
					&& StringUtils.isNotEmpty(tempJson
							.getString("managementType"))) {
				mapData.setMapDataMamagementType(tempJson
						.getInt("managementType"));
			}
			dataList.add(mapData);
		}

		List<MapData> sortedData = new ArrayList<MapData>();
		int size = dataList.size();

		for (int i = 0; i < size; i++) {
			MapData tempData = new MapData();
			int index = 0;
			tempData = dataList.get(0);

			if (dataList.size() > 1) {
				for (int j = 1; j < dataList.size(); j++) {
					if (tempData.getMapDataRecommededValue() <= dataList.get(j)
							.getMapDataRecommededValue()) {
						tempData = dataList.get(j);
						index = j;
					}
				}
			}

			sortedData.add(tempData);
			dataList.remove(index);
		}
		return sortedData;
	}
}

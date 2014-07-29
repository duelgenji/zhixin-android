package com.zhixin.daos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.DiaoyanList;
import com.zhixin.domain.DiaoyanWjAnswer;

public class DiaoyanDao {

	public boolean saveDataByJson(JSONObject jbo, int order, int type,
			boolean refreshFlag) throws JSONException, ParseException {
		JSONArray array = jbo.getJSONArray("aData");

		if (refreshFlag) {
			deleteAll(type, order);
		}

		DiaoyanList diaoyanList;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			jboInA = array.getJSONObject(i);
			diaoyanList = new DiaoyanList();

			diaoyanList.setControlFlag(0);
			if (jboInA.has("sTitle")
					&& StringUtils.isNotEmpty(jboInA.getString("sTitle"))) {
				diaoyanList.setTitle(jboInA.getString("sTitle"));
			}

			if (jboInA.has("sTime")
					&& StringUtils.isNotEmpty(jboInA.getString("sTime"))) {
				DateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");

				Date date = formatter.parse(jboInA.getString("sTime"));
				diaoyanList.setTime(date);
			}

			if (jboInA.has("iWjId")
					&& StringUtils.isNotEmpty(jboInA.getString("iWjId"))) {
				diaoyanList.setQuestionnarieId(jboInA.getInt("iWjId"));
			}

			if (jboInA.has("iCompleted")
					&& StringUtils.isNotEmpty(jboInA.getString("iCompleted"))) {
				diaoyanList.setPopularity(jboInA.getInt("iCompleted"));
			}

			if (jboInA.has("iType")
					&& StringUtils.isNotEmpty(jboInA.getString("iType"))) {
				diaoyanList.setType(jboInA.getInt("iType"));
			}

			if (jboInA.has("iCredit")
					&& StringUtils.isNotEmpty(jboInA.getString("iCredit"))) {
				diaoyanList.setCredit(jboInA.getInt("iCredit"));
			}

			if (jboInA.has("iRecommend")
					&& StringUtils.isNotEmpty(jboInA.getString("iRecommend"))) {
				diaoyanList.setRecommend(jboInA.getInt("iRecommend"));
			}

			if (jboInA.has("iFriends")
					&& StringUtils.isNotEmpty(jboInA.getString("iFriends"))) {
				diaoyanList.setFriends(jboInA.getInt("iFriends"));
			}

			if (jboInA.has("aTags")) {
				JSONArray aTag = jboInA.getJSONArray("aTags");
				if (aTag.length() != 0) {
					String tags = "";
					for (int j = 0; j < (aTag.length() > 5 ? 5 : aTag.length()); j++) {
						tags = tags + aTag.getJSONObject(j).getInt("iTag")
								+ ";";
					}
					tags.substring(0, tags.length() - 1);
					diaoyanList.setTags(tags);

				}

			}

			if (jboInA.has("iCoin")
					&& StringUtils.isNotEmpty(jboInA.getString("iCoin"))) {
				diaoyanList.setCoin(jboInA.getInt("iCoin"));
			}

			if (jboInA.has("iFailCredit")
					&& StringUtils.isNotEmpty(jboInA.getString("iFailCredit"))) {
				diaoyanList.setFailCredit(jboInA.getInt("iFailCredit"));
			}

			if (jboInA.has("iRemainTime")
					&& StringUtils.isNotEmpty(jboInA.getString("iRemainTime"))) {
				diaoyanList.setRemainTime(jboInA.getInt("iRemainTime"));
			}

			if (jboInA.has("iRequired")
					&& StringUtils.isNotEmpty(jboInA.getString("iRequired"))) {
				diaoyanList.setRequired(jboInA.getInt("iRequired"));
			}

			diaoyanList.setWjorder(order);

			DbManager.getDatabase().save(diaoyanList);
		}
		if (array.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void deleteAll(int type, int order) {
		if (DbManager.getDatabase().tableExists(DiaoyanList.class)) {
			String sql = "delete from diaoyan_list where wjorder=" + order;
			if (type != 0) {
				sql = sql + " and type=" + type;
			}
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public JSONObject getOldData(int order, int type) throws JSONException {
		JSONObject jbo = new JSONObject();
		if (type != 0) {
			jbo.put("iType", String.valueOf(type));
		}
		jbo.put("iOrderBy", String.valueOf(order));
		String sql;
		switch (order) {
		case 0:
		case 1:
			if (type != 0) {
				sql = "type=" + type + " and wjorder=" + order
						+ " and controlFlag=0";
			} else {
				sql = "wjorder=" + order + " and controlFlag=0";
			}
			List<DiaoyanList> quList = DbManager.getDatabase().findAllByWhere(
					DiaoyanList.class, sql);
			JSONArray array = new JSONArray();
			JSONObject jboInA;
			for (DiaoyanList quObj : quList) {
				jboInA = new JSONObject();
				jboInA.put("iWjId", quObj.getQuestionnarieId());
				array.put(jboInA);
			}
			jbo.put("aWjId", array);
			break;
		case 2:
		case 3:
			if (type != 0) {
				sql = "select * from diaoyan_list where type=" + type
						+ " and wjorder=" + order + " and controlFlag=0"
						+ " order by _id desc limit 1";
			} else {
				sql = "select * from diaoyan_list where wjorder=" + order
						+ " and controlFlag=0" + " order by _id desc limit 1";
			}

			DiaoyanList lastOne = DbManager.getDatabase().findUniqueBySql(
					DiaoyanList.class, sql);

			jbo.put("iLastId", lastOne.getQuestionnarieId());

			break;

		default:
			break;
		}

		return jbo;
	}

	public void deleteWjInList(int wjId) {
		if (DbManager.getDatabase().tableExists(DiaoyanList.class)) {
			String sql = "update diaoyan_list set controlFlag=1 where questionnarieId="
					+ wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	public DiaoyanList getWjByWjId(int wjId) {

		return DbManager.getDatabase().findUniqueBySql(
				DiaoyanList.class,
				"select * from diaoyan_list " + "where questionnarieId=" + wjId
						+ " limit 1");
	}

	public void saveHistoryWj(JSONObject jbo) throws JSONException,
			ParseException {
		JSONArray array = jbo.getJSONArray("data");
		DiaoyanWjAnswer wjAnswer;
		JSONObject jInA;
		for (int i = 0; i < array.length(); i++) {
			jInA = array.getJSONObject(i);
			wjAnswer = DbManager.getDatabase().findUniqueByWhere(
					DiaoyanWjAnswer.class, "wjId=" + jInA.getInt("id"));
			boolean shouldUpdate = (wjAnswer == null ? false : true);
			if (wjAnswer == null) {
				wjAnswer = new DiaoyanWjAnswer();
			}

			wjAnswer.setControlFlag(0);
			wjAnswer.setWjId(jInA.getInt("id"));
			wjAnswer.setWjTitle(jInA.getString("title"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(jInA.getString("date"));
			wjAnswer.setAnswerTime(date);
			wjAnswer.setStatus(jInA.getInt("status"));
			if (!StringUtils.isBlank(jInA.getString("coin"))) {
				wjAnswer.setCoin(jInA.getInt("coin"));
			}
			if (!StringUtils.isBlank(jInA.getString("credit"))) {

				wjAnswer.setCredit(jInA.getInt("credit"));
			}

			if (shouldUpdate) {
				DbManager.getDatabase().update(wjAnswer);

			} else {
				DbManager.getDatabase().save(wjAnswer);
			}

		}

	}

}

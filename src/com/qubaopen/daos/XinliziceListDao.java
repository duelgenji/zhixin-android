package com.qubaopen.daos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.database.DbManager;
import com.qubaopen.domain.XinliziceList;
import com.qubaopen.domain.XinliziceUserWjAnswer;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class XinliziceListDao {


	public boolean saveDataByJson(JSONObject jbo, int order, 
			boolean refreshFlag) throws JSONException, ParseException {
		JSONArray array = jbo.getJSONArray("aData");

		if (refreshFlag) {
			deleteAll(order);
		}

		XinliziceList xinliziceList;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			jboInA = array.getJSONObject(i);
			xinliziceList = new XinliziceList();

			xinliziceList.setControlFlag(0);
			if (jboInA.has("sTitle")
					&& StringUtils.isNotEmpty(jboInA.getString("sTitle"))) {
				xinliziceList.setTitle(jboInA.getString("sTitle"));
			}

			if (jboInA.has("sTime")
					&& StringUtils.isNotEmpty(jboInA.getString("sTime"))) {
				DateFormat formatter = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss.S");

				Date date = formatter.parse(jboInA.getString("sTime"));
				xinliziceList.setTime(date);
			}

			if (jboInA.has("iWjId")
					&& StringUtils.isNotEmpty(jboInA.getString("iWjId"))) {
				xinliziceList.setQuestionnarieId(jboInA.getInt("iWjId"));
			}

			if (jboInA.has("iCompleted")
					&& StringUtils.isNotEmpty(jboInA.getString("iCompleted"))) {
				xinliziceList.setPopularity(jboInA.getInt("iCompleted"));
			}

			if (jboInA.has("iType")
					&& StringUtils.isNotEmpty(jboInA.getString("iType"))) {
				xinliziceList.setManagementType(jboInA.getString("iType"));
			}

			if (jboInA.has("iCredit")
					&& StringUtils.isNotEmpty(jboInA.getString("iCredit"))) {
				xinliziceList.setCredit(jboInA.getInt("iCredit"));
			}

			if (jboInA.has("iRecommend")
					&& StringUtils.isNotEmpty(jboInA.getString("iRecommend"))) {
				xinliziceList.setRecommend(jboInA.getInt("iRecommend"));
			}

			if (jboInA.has("iFriends")
					&& StringUtils.isNotEmpty(jboInA.getString("iFriends"))) {
				xinliziceList.setFriends(jboInA.getInt("iFriends"));
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
					xinliziceList.setTags(tags);

				}

			}

			if (jboInA.has("iCoin")
					&& StringUtils.isNotEmpty(jboInA.getString("iCoin"))) {
				xinliziceList.setCoin(jboInA.getInt("iCoin"));
			}

			xinliziceList.setWjorder(order);
			DbManager.getDatabase().save(xinliziceList);
		}
		if (array.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private void deleteAll(int order) {
		if (DbManager.getDatabase().tableExists(XinliziceList.class)) {
			String sql = "delete from qu_list where wjorder=" + order;
//			if (type != 0) {
//				sql = sql + " and type=" + type;
//			}
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	public JSONObject getOldData(int order) throws JSONException {
		JSONObject jbo = new JSONObject();
//		if (type != 0) {
//			jbo.put("iType", String.valueOf(type));
//		}
		jbo.put("iOrderBy", String.valueOf(order));
		String sql;
		switch (order) {
		case 0:
		case 1:
//			if (type != 0) {
//				sql = "type=" + type + " and wjorder=" + order
//						+ " and controlFlag=0";
//			} else {
				sql = "wjorder=" + order + " and controlFlag=0";
//			}
			List<XinliziceList> quList = DbManager.getDatabase().findAllByWhere(
					XinliziceList.class, sql);
			JSONArray array = new JSONArray();
			JSONObject jboInA;
			for (XinliziceList quObj : quList) {
				jboInA = new JSONObject();
				jboInA.put("iWjId", quObj.getQuestionnarieId());
				array.put(jboInA);
			}
			jbo.put("aWjId", array);
			break;
		case 2:
		case 3:
//			if (type != 0) {
//				sql = "select * from qu_list where type=" + type
//						+ " and wjorder=" + order + " and controlFlag=0"
//						+ " order by _id desc limit 1";
//			} else {
				sql = "select * from qu_list where wjorder=" + order
						+ " and controlFlag=0" + " order by _id desc limit 1";
//			}

			XinliziceList lastOne = DbManager.getDatabase().findUniqueBySql(
					XinliziceList.class, sql);

			jbo.put("iLastId", lastOne.getQuestionnarieId());

			break;

		default:
			break;
		}

		return jbo;
	}

	public void deleteWjInList(int wjId) {
		if (DbManager.getDatabase().tableExists(XinliziceList.class)) {
			String sql = "update qu_list set controlFlag=1 where questionnarieId="
					+ wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	public void saveHistoryWjList(JSONObject jbo) throws JSONException,
			ParseException {
		JSONArray array = jbo.getJSONArray("data");
		JSONObject jInA;
		XinliziceUserWjAnswer wjAnswer;
		for (int i = 0; i < array.length(); i++) {
			jInA = array.getJSONObject(i);
			wjAnswer = DbManager.getDatabase().findUniqueByWhere(
					XinliziceUserWjAnswer.class, "wjId=" + jInA.getInt("id"));
			if (wjAnswer == null) {
				wjAnswer = new XinliziceUserWjAnswer();
				wjAnswer = saveOrUpdateWjAnswer(wjAnswer, jInA);
				DbManager.getDatabase().save(wjAnswer);

			} else {
				wjAnswer.setControlFlag(0);
				wjAnswer = saveOrUpdateWjAnswer(wjAnswer, jInA);
				DbManager.getDatabase().update(wjAnswer);
			}

		}

	}

	private XinliziceUserWjAnswer saveOrUpdateWjAnswer(XinliziceUserWjAnswer wjAnswer,
			JSONObject jInA) throws JSONException, ParseException {

		wjAnswer.setControlFlag(0);
		wjAnswer.setWjId(jInA.getInt("id"));
		wjAnswer.setWjTitle(jInA.getString("title"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = sdf.parse(jInA.getString("date"));
		wjAnswer.setAnswerTime(date);

		JSONObject requestParams = new JSONObject();
		requestParams.put("iWjId", wjAnswer.getWjId());
		JSONObject wjAnswerDetail = HttpClient.requestSync(
				SettingValues.URL_PREFIX + "xqwj/getHistoryWj.htm",
				requestParams);
		wjAnswer.setAnswerTitle(wjAnswerDetail.getString("mainTitle"));
		if (!StringUtils.isBlank(wjAnswerDetail.getString("answerNo"))) {
			wjAnswer.setAnswerChoiceNo(wjAnswerDetail.getString("answerNo"));
		}
		if (!StringUtils.isBlank(wjAnswerDetail.getString("answerTitle"))) {
			wjAnswer.setAnswerChoiceTitle(wjAnswerDetail
					.getString("answerTitle"));
		}
		wjAnswer.setAnswerChoiceContent(wjAnswerDetail
				.getString("answerContent"));
		wjAnswer.setIsPublicAnwer(wjAnswerDetail.getInt("isShow"));
		return wjAnswer;
	}


}

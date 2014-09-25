package com.zhixin.daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.database.DbManager;
import com.zhixin.domain.InterestList;
import com.zhixin.domain.QuList;
import com.zhixin.domain.QuUserWjAnswer;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class InterestListDao {

	public boolean saveInterestList(JSONObject jbo) throws JSONException, ParseException {
		JSONArray array = jbo.getJSONArray("data");
		InterestList interestList;
		JSONObject jboInA;
		Integer page = jbo.getInt("page");
		if(page==null || page.equals(0)){
			deleteAll();
		}
		for (int i = 0; i < array.length(); i++) {
			jboInA = array.getJSONObject(i);
			interestList = new InterestList();
			interestList.setControlFlag(0);
			if (jboInA.has("interestId")
					&& StringUtils.isNotEmpty(jboInA.getString("interestId"))) {
				interestList.setInterestId(jboInA.getInt("interestId"));
				//deleteById(jboInA.getInt("interestId"));
			}
			if (jboInA.has("title")
					&& StringUtils.isNotEmpty(jboInA.getString("title"))) {
				interestList.setTitle(jboInA.getString("title"));
			}
			if (jboInA.has("interestType")
					&& StringUtils.isNotEmpty(jboInA.getString("interestType"))) {
				interestList.setInterestType(jboInA.getString("interestType"));
			}
			if (jboInA.has("remark")
					&& StringUtils.isNotEmpty(jboInA.getString("remark"))) {
				interestList.setRemark(jboInA.getString("remark"));
			}
			if (jboInA.has("picPath")
					&& StringUtils.isNotEmpty(jboInA.getString("picPath"))) {
				interestList.setPicPath(jboInA.getString("picPath"));
			}
			if (jboInA.has("golds")	
					&& StringUtils.isNotEmpty(jboInA.getString("golds"))) {
				interestList.setGolds(jboInA.getInt("golds"));
			}
			if (jboInA.has("friendCount")
					&& StringUtils.isNotEmpty(jboInA.getString("friendCount"))) {
				interestList.setFriendCount(jboInA.getInt("friendCount"));
			}
			if (jboInA.has("recommendedValue")
					&& StringUtils.isNotEmpty(jboInA.getString("recommendedValue"))) {
				interestList.setRecommendedValue(jboInA.getInt("recommendedValue"));
			}
			if (jboInA.has("totalRespondentsCount")
					&& StringUtils.isNotEmpty(jboInA.getString("totalRespondentsCount"))) {
				interestList.setTotalRespondentsCount(jboInA.getInt("totalRespondentsCount"));
			}
			interestList.setTime(new Date());
			
			
			
			if (jboInA.has("questionnaireTagType")) {
				JSONArray aTag = jboInA.getJSONArray("questionnaireTagType");
				if (aTag.length() != 0) {
					String tags = "";
					for (int j = 0; j < (aTag.length() > 5 ? 5 : aTag.length()); j++) {
						tags = tags + aTag.getJSONObject(j).getInt("tagId")
								+ ";";
					}
					tags.substring(0, tags.length() - 1);
					interestList.setQuestionnaireTagType(tags);

				}

			}

			DbManager.getDatabase().save(interestList);
		}
		
		if (jbo.has("lastPage")
				&& StringUtils.isNotEmpty(jbo.getString("lastPage"))) {
			return jbo.getBoolean("lastPage");
		}
		return false;
	}

	private void deleteAll() {
		if (DbManager.getDatabase().tableExists(InterestList.class)) {
			String sql = "delete from interest_list ";
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}
	
	private void deleteById(int id) {
		if (DbManager.getDatabase().tableExists(InterestList.class)) {
			String sql = "delete from interest_list where interestId=" + id;
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
			List<QuList> quList = DbManager.getDatabase().findAllByWhere(
					QuList.class, sql);
			JSONArray array = new JSONArray();
			JSONObject jboInA;
			for (QuList quObj : quList) {
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

			QuList lastOne = DbManager.getDatabase().findUniqueBySql(
					QuList.class, sql);

			jbo.put("iLastId", lastOne.getQuestionnarieId());

			break;

		default:
			break;
		}

		return jbo;
	}

	public void deleteWjInList(int wjId) {
		if (DbManager.getDatabase().tableExists(QuList.class)) {
			String sql = "update qu_list set controlFlag=1 where questionnarieId="
					+ wjId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	public void saveHistoryWjList(JSONObject jbo) throws JSONException,
			ParseException {
		JSONArray array = jbo.getJSONArray("data");
		JSONObject jInA;
		QuUserWjAnswer wjAnswer;
		for (int i = 0; i < array.length(); i++) {
			jInA = array.getJSONObject(i);
			wjAnswer = DbManager.getDatabase().findUniqueByWhere(
					QuUserWjAnswer.class, "wjId=" + jInA.getInt("id"));
			if (wjAnswer == null) {
				wjAnswer = new QuUserWjAnswer();
				wjAnswer = saveOrUpdateWjAnswer(wjAnswer, jInA);
				DbManager.getDatabase().save(wjAnswer);

			} else {
				wjAnswer.setControlFlag(0);
				wjAnswer = saveOrUpdateWjAnswer(wjAnswer, jInA);
				DbManager.getDatabase().update(wjAnswer);
			}

		}

	}

	private QuUserWjAnswer saveOrUpdateWjAnswer(QuUserWjAnswer wjAnswer,
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

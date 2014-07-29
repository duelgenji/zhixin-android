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
import com.zhixin.domain.HuatiChoices;
import com.zhixin.domain.QuHuati;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class QuhuatiDao {

	private static String[] CHOICE_NO_ARRAY = { "A", "B", "C", "D", "E", "F",
			"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z" };

	public void saveData(JSONObject jbo) throws JSONException, ParseException {
		JSONArray array = jbo.getJSONArray("aData");
		if (array.length() > 0) {
			deleteAll();
		}
		QuHuati huati;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			huati = new QuHuati();
			huati.setUiCate(0);
			jboInA = array.getJSONObject(i);
			huati.setFriendNumber(jboInA.getInt("iFriends"));
			huati.setHutatiId(jboInA.getInt("iHtId"));
			huati.setParticipateNumber(jboInA.getInt("iCompleted"));
			if (jboInA.has("sPicUrl")
					&& StringUtils.isNotEmpty(jboInA.getString("sPicUrl"))) {
				huati.setPicUrl(jboInA.getString("sPicUrl"));
			}
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

			Date date = formatter.parse(jboInA.getString("sTime"));

			huati.setTime(date);

			huati.setTitle(jboInA.getString("sTitle"));
			huati.setType(jboInA.getInt("iType"));
			if (jboInA.has("sUser")
					&& StringUtils.isNotEmpty(jboInA.getString("sUser"))) {
				huati.setUsernickname(jboInA.getString("sUser"));
			}

			JSONObject jsonParams = new JSONObject();
			jsonParams.put("iHtId", huati.getHutatiId());
			JSONObject result = HttpClient.requestSync(SettingValues.URL_PREFIX
					+ "yhht/isDone.htm", jsonParams);
			if (result != null
					&& !StringUtils.isBlank(result.getString("iFlag"))) {
				huati.setAnsweredOrNot(result.getInt("iFlag"));
			}

			DbManager.getDatabase().save(huati);

		}

	}

	public void saveHistoryMyPublishHuati(JSONObject jbo) throws JSONException {
		JSONArray array = jbo.getJSONArray("aData");
		if (array.length() > 0) {
			deleteAllHistoryMyPublishHuati();
		}
		QuHuati huati;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			huati = new QuHuati();
			huati.setUiCate(2);
			jboInA = array.getJSONObject(i);
			huati.setFriendNumber(jboInA.getInt("iFriends"));
			huati.setHutatiId(jboInA.getInt("iHtId"));
			huati.setParticipateNumber(jboInA.getInt("iCompleted"));
			huati.setTitle(jboInA.getString("sTitle"));
			if (jboInA.has("iStatus")) {
				huati.setStatus(jboInA.getInt("iStatus"));
			}

			if (jboInA.has("iTotalCredit")) {
				huati.setCreditGain(jboInA.getInt("iTotalCredit"));

			}

			JSONObject jsonParams = new JSONObject();
			jsonParams.put("iHtId", huati.getHutatiId());
			JSONObject result = HttpClient.requestSync(SettingValues.URL_PREFIX
					+ "yhht/isDone.htm", jsonParams);
			if (result != null
					&& !StringUtils.isBlank(result.getString("iFlag"))) {
				huati.setAnsweredOrNot(result.getInt("iFlag"));
			}

			DbManager.getDatabase().save(huati);
		}

	}

	public void saveHistroyParticipateHuati(JSONObject jbo)
			throws JSONException {
		JSONArray array = jbo.getJSONArray("aData");
		if (array.length() > 0) {
			deleteAllHistoryParticipateHuati();
		}
		QuHuati huati;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			huati = new QuHuati();
			huati.setUiCate(1);
			jboInA = array.getJSONObject(i);
			huati.setFriendNumber(jboInA.getInt("iFriends"));
			huati.setHutatiId(jboInA.getInt("iHtId"));
			huati.setParticipateNumber(jboInA.getInt("iCompleted"));
			huati.setTitle(jboInA.getString("sTitle"));

			JSONObject jsonParams = new JSONObject();
			jsonParams.put("iHtId", huati.getHutatiId());
			JSONObject result = HttpClient.requestSync(SettingValues.URL_PREFIX
					+ "yhht/isDone.htm", jsonParams);
			if (result != null
					&& !StringUtils.isBlank(result.getString("iFlag"))) {
				huati.setAnsweredOrNot(result.getInt("iFlag"));
			}

			DbManager.getDatabase().save(huati);
		}

	}

	public void saveChoices(JSONObject jbo, int questionId)
			throws JSONException {
		JSONArray array = jbo.getJSONArray("aChoice");
		if (array.length() > 0) {
			deleteAllChoices(questionId);
		}
		HuatiChoices choice;
		JSONObject jboInA;
		for (int i = 0; i < array.length(); i++) {
			choice = new HuatiChoices();
			jboInA = array.getJSONObject(i);
			choice.setChoiceId(jboInA.getInt("iChoiceId"));
			if (i < CHOICE_NO_ARRAY.length) {
				choice.setChoiceNo(CHOICE_NO_ARRAY[i]);
			} else {
				choice.setChoiceNo(i + "");
			}
			choice.setChoiceTitle(jboInA.getString("sTitle"));
			choice.setQuestionId(questionId);
			DbManager.getDatabase().save(choice);
		}

	}

	private void deleteAll() {

		if (DbManager.getDatabase().tableExists(QuHuati.class)) {
			String sql = "delete from qu_huati where uiCate=0";
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	private void deleteAllHistoryMyPublishHuati() {
		if (DbManager.getDatabase().tableExists(QuHuati.class)) {
			String sql = "delete from qu_huati where uiCate=2";
			DbManager.getDatabase().exeCustomerSql(sql);
		}
	}

	private void deleteAllHistoryParticipateHuati() {
		if (DbManager.getDatabase().tableExists(QuHuati.class)) {
			String sql = "delete from qu_huati where uiCate=1";
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	private void deleteAllChoices(int questionId) {

		if (DbManager.getDatabase().tableExists(HuatiChoices.class)) {
			String sql = "delete from huati_choices where questionId="
					+ questionId;
			DbManager.getDatabase().exeCustomerSql(sql);
		}

	}

	public QuHuati getQuhuati(int huatiId) {
		QuHuati huati = DbManager.getDatabase().findUniqueByWhere(
				QuHuati.class, "hutatiId =" + huatiId);
		return huati;
	}

	public List<HuatiChoices> getHuatiChoices(int huatiId) {
		return DbManager.getDatabase().findAllByWhere(HuatiChoices.class,
				"questionId=" + huatiId);

	}

	public void huatiTaken(int huatiId) {
		List<QuHuati> huati = DbManager.getDatabase().findAllByWhere(
				QuHuati.class, "hutatiId=" + huatiId);
		for (QuHuati one : huati) {
			one.setAnsweredOrNot(1);
			DbManager.getDatabase().update(one);
		}
	}

}

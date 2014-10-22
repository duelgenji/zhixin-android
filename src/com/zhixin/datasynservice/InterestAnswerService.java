package com.zhixin.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zhixin.R;
import com.zhixin.daos.InterestAnswerDao;
import com.zhixin.domain.InterestUserAnswer;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class InterestAnswerService {
	private Context context;
	private InterestAnswerDao interestAnswerDao;


	public InterestAnswerService(Context context) {
		this.context = context;
		interestAnswerDao = new InterestAnswerDao();

	}

	
	//提交自测答案， 返回一个答案对象 
	public InterestUserAnswer getAnswer(JSONObject requestParams) throws JSONException {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_INTEREST_SUBMIT_QUESTION);

		int wjId = requestParams.getInt("interestId");

		JSONObject result = HttpClient.requestSync(requestUrl, requestParams,HttpClient.TYPE_POST_FORM);
		if (result != null
				&& (!result.has("success") || result.getString("success")
						.equals("1"))) {
			return interestAnswerDao.saveMyAnswer(result, wjId);
		} else if (result != null && result.getString("success").equals("0")) {
			return null;
		}
		return null;
	}


}

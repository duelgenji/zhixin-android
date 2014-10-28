package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class SubmitOnlyService {
	private Context context;



	public SubmitOnlyService(Context context) {
		this.context = context;
	}

	public JSONObject moreSuggestion(JSONObject obj) throws ParseException {
        JSONObject result=new JSONObject();
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_MORE_SUGGESTION);
		try {
			result = HttpClient.requestSync(requestUrl, obj);
			if (result != null && result.getInt("success") == 1) {
                return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}




    public JSONObject moreCompany(JSONObject obj) throws ParseException {
        JSONObject result=new JSONObject();
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_MORE_SUGGESTION);
        try {
            result = HttpClient.requestSync(requestUrl, obj);
            if (result != null && result.getInt("success") == 1) {
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


}

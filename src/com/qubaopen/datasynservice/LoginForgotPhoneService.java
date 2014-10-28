package com.qubaopen.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

/**
 * Created by duel on 14-3-31.
 */
public class LoginForgotPhoneService {
    private Context context;



    public LoginForgotPhoneService(Context context) {
        this.context = context;
    }

    public JSONObject getForgotToken(String phone) throws ParseException {
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LOGIN_FORGOT_TOKEN);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("phone",phone);

            return HttpClient.requestSync(requestUrl, jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

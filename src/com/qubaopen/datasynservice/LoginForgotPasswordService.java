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
public class LoginForgotPasswordService {
    private Context context;



    public LoginForgotPasswordService(Context context) {
        this.context = context;
    }

    public JSONObject setNewPwd(String phone,String pwd,String token) throws ParseException {
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LOGIN_FORGOT_SET_NEW_PWD);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("phone",phone);
            jsonParams.put("token",token);
            jsonParams.put("pwd",pwd);

            return HttpClient.requestSync(requestUrl, jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

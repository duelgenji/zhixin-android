package com.zhixin.datasynservice;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import java.text.ParseException;

/**
 * Created by duel on 14-3-31.
 */
public class QuduijiangConfirmService {
    private Context context;



    public QuduijiangConfirmService(Context context) {
        this.context = context;
    }

    public JSONObject sendRequest(String lotteryId,String dzId) throws ParseException {
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_DUIJIANG_DRAW);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("duijiangId",lotteryId);
            jsonParams.put("addressId",dzId);
            jsonParams.put("sl",1);
            return HttpClient.requestSync(requestUrl, jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}

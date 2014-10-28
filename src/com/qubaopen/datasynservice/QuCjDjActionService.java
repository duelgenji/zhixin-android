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
public class QuCjDjActionService {
    private Context context;



    public QuCjDjActionService(Context context) {
        this.context = context;
    }

    public JSONObject lingjiang(String lotteryId,String dzId) throws ParseException {
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LING_JIANG);
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("lotteryNumber",lotteryId);
            jsonParams.put("addressId",dzId);
            return HttpClient.requestSync(requestUrl, jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject confirmLottery(int type,int lotteryId) throws ParseException {
        JSONObject result=null;
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LING_JIANG_CONFIRM);
        try {

            JSONObject params=new JSONObject();
            if(type==0){
                params.put("lotteryNumber",lotteryId);
                params.put("duijiangNumber","");
            }else if(type==1){
                params.put("lotteryNumber","");
                params.put("duijiangNumber",lotteryId);
            }
            result = HttpClient.requestSync(requestUrl, params);
            if (result != null && result.getInt("success") == 1) {
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public JSONObject denyLottery(int type,int lotteryId) throws ParseException {
        JSONObject result=null;
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LING_JIANG_DENY);
        try {

            JSONObject params=new JSONObject();
            if(type==0){
                params.put("lotteryNumber",lotteryId);
                params.put("duijiangNumber","");
            }else if(type==1){
                params.put("lotteryNumber","");
                params.put("duijiangNumber",lotteryId);
            }
            result = HttpClient.requestSync(requestUrl, params);
            if (result != null && result.getInt("success") == 1) {
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public JSONObject checkLottery(int type,int lotteryId) throws ParseException {
        JSONObject result=null;
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_LING_JIANG_CHECK);
        try {

            JSONObject params=new JSONObject();
                params.put("iType",type);
                params.put("id",lotteryId);
            result = HttpClient.requestSync(requestUrl, params);
            if (result != null && result.getInt("success") == 1) {
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

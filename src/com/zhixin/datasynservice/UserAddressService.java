package com.zhixin.datasynservice;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.zhixin.R;
import com.zhixin.daos.UserAddressDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressService {
    private Context context;
    private UserAddressDao userAddressDao;

    public static class UserAddressSqlMaker {

        public static String makeSql() {
            String sql = "select _id,memberId,dzId,name,sfId,csId,dqId," +
                    "address,postCode,isDefault,phone,sfmc,csmc,dqmc" +
                    " from user_address " +
                    "where memberId="
                    + CurrentUserHelper.getCurrentMemberId();
            return sql;
        }

        public static String makeDefaultAddressSql() {
            String sql = "select _id,memberId,dzId,name," +
                    "address,isDefault,phone from user_address " +
                    "where isDefault='1' and memberId="
                    + CurrentUserHelper.getCurrentMemberId();
            return sql;
        }
    }

    public UserAddressService(Context context) {
        this.context = context;
        userAddressDao = new UserAddressDao();
    }

    public Void saveUserAddress() throws ParseException {
        String requestUrl = SettingValues.URL_PREFIX
                + context.getString(R.string.URL_USER_ADDRESS);
        try {
            JSONObject result = HttpClient.requestSync(requestUrl, null);
            if (result != null && result.getInt("success") == 1) {
                userAddressDao.saveUserAddress(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}

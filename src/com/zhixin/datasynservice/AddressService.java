package com.zhixin.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.daos.AddressDao;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

import android.content.Context;
import android.util.Log;

/**
 * Created by duel on 14-3-26.
 */
public class AddressService {
	private Context context;
	private AddressDao addressDao;

	public static String makeSql(int sqlCate, String code) {
		String sql = null;
		switch (sqlCate) {
		case 1:
			sql = "select * from address_sf ";
			return sql;
		case 2:
			sql = "select * from address_cs where sfdm='" + code + "'";
			return sql;
		case 3:
			sql = "select * from address_dq where csdm='" + code + "'";
			return sql;
		default:
			return null;
		}

	}

	public AddressService(Context context) {
		this.context = context;
		addressDao = new AddressDao();
	}

	public Void getAddress() {
		Log.i("Thread id in intent service get address","" + Thread.currentThread().getId());
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_GET_ADDRESS);
		try {
			JSONObject result = HttpClient.requestSync(requestUrl, null);
			if (result != null && result.getString("success").equals("1")) {
				addressDao.saveAddress(result, context);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}

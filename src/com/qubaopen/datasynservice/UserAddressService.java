package com.qubaopen.datasynservice;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.daos.UserAddressDao;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

/**
 * Created by duel on 14-3-26.
 */
public class UserAddressService {
	private Context context;
	private UserAddressDao userAddressDao;

	public static class UserAddressSqlMaker {

		public static String makeSql() {
			String sql = "select _id,userId,dzId,name,sfId,csId,dqId,"
					+ "address,postCode,isDefault,phone,sfmc,csmc,dqmc"
					+ " from user_address " + "where userId="
					+ CurrentUserHelper.getCurrentUserId();
			return sql;
		}

		public static String makeDefaultAddressSql() {
			String sql = "select _id,userId,dzId,name,"
					+ "address,isDefault,phone from user_address "
					+ "where isDefault='1' and userId="
					+ CurrentUserHelper.getCurrentUserId();
			return sql;
		}
	}

	public UserAddressService(Context context) {
		this.context = context;
		userAddressDao = new UserAddressDao();
	}

	public void saveUserAddress() {
		String requestUrl = SettingValues.URL_PREFIX
				+ context.getString(R.string.URL_USER_GET_ADDRESS_LIST);
//		Log.i("address", requestUrl);
		new LoadDataTask1().execute(1, requestUrl, null, HttpClient.TYPE_GET);
	}

	private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer)params[3]);
					result.put("syncType", syncType);
//					Log.i("address", "获取到的地址：......" + result);
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result != null
							&& result.getString("success").equals("1")) {
						
						Toast.makeText(context, "获取地址成功！", Toast.LENGTH_SHORT)
						.show();
						// 保存到本地数据库
						userAddressDao.saveUserAddress(result);
						
					} else {
						Toast.makeText(context, "获取地址失败！", Toast.LENGTH_SHORT)
								.show();
					}
					break;

				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}

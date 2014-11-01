package com.qubaopen.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.UserAddressAdapter;
import com.qubaopen.daos.UserAddressDao;
import com.qubaopen.datasynservice.UserAddressService;
import com.qubaopen.domain.UserAddress;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAddressActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private ImageView btnAdd;
	private RelativeLayout imgNoAddress;

	private ListView userAddressList;

	public ListView getUserAddressList() {
		return userAddressList;
	}

	private UserAddressDao userAddressDao;
	private UserAddressAdapter adapter;
	private Activity _this;
	public static UserInfoAddressActivity userInfoAddressActivity;
	private String requestUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_address);

		_this = this;
		userInfoAddressActivity = this;
		userAddressDao = new UserAddressDao();
		imgNoAddress = (RelativeLayout) this
				.findViewById(R.id.layout_no_address);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this
				.getString(R.string.title_user_delivery_address));
		btnAdd = (ImageView) this.findViewById(R.id.add_address);
		btnAdd.setOnClickListener(this);
		userAddressList = (ListView) this.findViewById(R.id.userAddressList);
		requestUrl = SettingValues.URL_PREFIX
				+ this.getString(R.string.URL_USER_GET_ADDRESS_LIST);
		// Log.i("address", requestUrl);
		new LoadAddressDataTask().execute(1, requestUrl, null,
				HttpClient.TYPE_GET);
	}

	@Override
	protected void onStart() {
		super.onStart();
		getSupportLoaderManager().restartLoader(0, null, this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (adapter != null) {
			adapter.changeCursor(null);
		}
	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.add_address:
			Intent intent = new Intent(_this, UserInfoAddressAddActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;

		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SqlCursorLoader(this,
				UserAddressService.UserAddressSqlMaker.makeSql(),
				UserAddress.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.getCount() != 0) {
			if (adapter == null) {
				if (cursor.getCount() != 0) {
					imgNoAddress.setVisibility(View.GONE);
					adapter = new UserAddressAdapter(_this, cursor);
					userAddressList.setAdapter(adapter);
				} else {
					imgNoAddress.setVisibility(View.VISIBLE);
				}

			} else {
				adapter.changeCursor(cursor);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (adapter != null) {
			adapter.changeCursor(null);
		}
	}

	private class LoadAddressDataTask extends
			AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					result.put("syncType", syncType);
					// Log.i("address", "获取到的地址：......" + result);
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
					if (result.has("success")
							&& result.getString("success").equals("1")) {
						// 保存到本地数据库
						 Log.i("address", "获取到的地址：......" + result);
						userAddressDao.saveUserAddress(result);
					} else {
						Toast.makeText(_this, "获取地址失败！", Toast.LENGTH_SHORT)
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


	public void reSetAddress() {

		getSupportLoaderManager().restartLoader(0, null,
				UserInfoAddressActivity.this);
	}


}

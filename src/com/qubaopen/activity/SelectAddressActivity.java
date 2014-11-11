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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.SelectAddressAdapter;
import com.qubaopen.datasynservice.UserAddressService;
import com.qubaopen.domain.UserAddress;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.SqlCursorLoader;

public class SelectAddressActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private TextView btnAddressManager;
	private RelativeLayout imgNoAddress;
	private ListView userAddressList;
	private TextView btnSumbmit;

	private int selectedId;

	public ListView getUserAddressList() {
		return userAddressList;
	}

	private SelectAddressAdapter adapter;
	private Activity _this;
	public static SelectAddressActivity selectAddressActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_address);
		_this = this;
		selectAddressActivity = this;

		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this
				.getString(R.string.title_user_delivery_address));
		btnAddressManager = (TextView) this
				.findViewById(R.id.btn_address_manager);
		btnAddressManager.setOnClickListener(this);

		imgNoAddress = (RelativeLayout) this
				.findViewById(R.id.layout_analysis_no_address);
		imgNoAddress.setVisibility(View.GONE);

		userAddressList = (ListView) this
				.findViewById(R.id.user_analysis_address_list);
		btnSumbmit = (TextView) this
				.findViewById(R.id.btn_sumbmit_selected_address);
		btnSumbmit.setOnClickListener(this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		getSupportLoaderManager().restartLoader(0, null, this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getSupportLoaderManager().restartLoader(0, null,
				SelectAddressActivity.this);
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
		case R.id.btn_address_manager:
			Intent intent = new Intent(_this, UserInfoAddressActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.btn_sumbmit_selected_address:
			selectedId = adapter.getSelectedId();
			Log.i("SelectAddressActivity", "selectedId..." + selectedId);
			if (selectedId >= 0) {
				try {
					String requestUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_JOIN_HEART_REPORT);
					JSONObject params = new JSONObject();
					params.put("addressId", selectedId);
					new LoadSelectAddressTask().execute(1, requestUrl, params,
							HttpClient.TYPE_POST_FORM);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				showToast("请选择地址！");
			}
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
					adapter = new SelectAddressAdapter(_this, cursor, -1);
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

	private class LoadSelectAddressTask extends
			AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					Log.i("SelectAddressActivity", "参与活动结果：......" + result);
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
						showToast("地址已提交至活动！");
					} else {
						showToast("参与活动失败！");
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

	private void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
}

package com.zhixin.activity;

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
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.UserAddressAdapter;
import com.zhixin.datasynservice.UserAddressService;
import com.zhixin.domain.UserAddress;
import com.zhixin.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAddressActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private ImageView btnAdd;

//	private AddressService addressService;

	private ListView userAddressList;

	public ListView getUserAddressList() {
		return userAddressList;
	}

	private UserAddressService userAddressService;
	private UserAddressAdapter adapter;
	private Activity _this;
	public static UserInfoAddressActivity userInfoAddressActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_address);

		_this = this;
		userInfoAddressActivity = this;
		userAddressService = new UserAddressService(this);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this
				.getString(R.string.title_user_delivery_address));
		btnAdd = (ImageView) this.findViewById(R.id.add_address);
		btnAdd.setOnClickListener(this);
		userAddressList = (ListView) this.findViewById(R.id.userAddressList);
		new LoadAddressDataTask().execute();
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
			this.onBackPressed();
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
				adapter = new UserAddressAdapter(_this, cursor);
				userAddressList.setAdapter(adapter);
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

	private class LoadAddressDataTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
				userAddressService.saveUserAddress();
				Log.i("address", "======保存到本地完成======" );
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			getSupportLoaderManager().restartLoader(0, null,
					UserInfoAddressActivity.this);
		}
		
	}
	public void reSetAddress(){
		new LoadAddressDataTask().execute();
	}
	

}

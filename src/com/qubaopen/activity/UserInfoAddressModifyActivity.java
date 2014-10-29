package com.qubaopen.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.UserAddressDao;
import com.qubaopen.dialog.AddressDialog;
import com.qubaopen.domain.UserAddress;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;
import com.qubaopen.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAddressModifyActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static final String INTENT_ADDRESS_ID = "addressId";

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private Button btnSubmit;

	private EditText txtAddressModifyName;
	private EditText txtAddressModifyPhone;
	private TextView txtAddressModifyArea;
	private EditText txtAddressModifyCode;
	private EditText txtAddressModifyAddress;

	private AddressDialog ad;

	private Button btnAddressModifyDelete;
	private Button btnAddressModifyDefault;

	private UserInfoAddressModifyActivity _this;
	private UserAddressDao userAddressDao;
	private JSONObject modifyParams;
	private JSONObject defaultParams;

	private int dzId;
	private String sfdm;
	private String csdm;
	private String dqdm;

	private Boolean pickerExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_address_modify);

		_this = this;
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle
				.setText(this.getString(R.string.title_user_modify_address));
		btnSubmit = (Button) this.findViewById(R.id.addressSubmit);
		btnSubmit.setOnClickListener(this);

		txtAddressModifyName = (EditText) this
				.findViewById(R.id.txtAddressModifyName);
		txtAddressModifyPhone = (EditText) this
				.findViewById(R.id.txtAddressModifyPhone);
		txtAddressModifyArea = (TextView) this
				.findViewById(R.id.txtAddressModifyArea);
		txtAddressModifyCode = (EditText) this
				.findViewById(R.id.txtAddressModifyCode);
		txtAddressModifyAddress = (EditText) this
				.findViewById(R.id.txtAddressModifyAddress);

		btnAddressModifyDelete = (Button) this
				.findViewById(R.id.btnAddressModifyDelete);
		btnAddressModifyDelete.setOnClickListener(this);
		btnAddressModifyDefault = (Button) this
				.findViewById(R.id.btnAddressModifyDefault);
		btnAddressModifyDefault.setOnClickListener(this);
		txtAddressModifyArea.setOnClickListener(this);

		dzId = this.getIntent().getIntExtra(
				UserInfoAddressModifyActivity.INTENT_ADDRESS_ID, 0);
		pickerExist = false;

	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		modifyParams = sendRequest();
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			v.setEnabled(true);
			break;
		case R.id.addressSubmit:
			String modifyUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_MODIFY_USER_ADDRESS);
			modifyUrl += "?id=" + dzId;
			new LoadDataTask().execute(1, modifyUrl, modifyParams,
					HttpClient.TYPE_POST_FORM);
			v.setEnabled(true);
			break;
		case R.id.btnAddressModifyDelete:
			final AlertDialog dlg = new AlertDialog.Builder(_this).create();
			dlg.show();
			Window window = dlg.getWindow();
			window.setContentView(R.layout.dialog_alert_dialog);
			// 为确认按钮添加事件,执行退出应用操作
			TextView txtAlertContent = (TextView) window
					.findViewById(R.id.txtAlertContent);
			txtAlertContent.setText("确认删除地址？");
			Button ok = (Button) window.findViewById(R.id.btnConfirm);
			ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {

					String deleteUrl = SettingValues.URL_PREFIX
							+ getString(R.string.URL_DELETE_USER_ADDRESS);
					deleteUrl += "?id=" + dzId;
					new LoadDataTask().execute(3, deleteUrl, dzId,
							HttpClient.TYPE_DELETE);

					dlg.cancel();
					_this.finish();
				}
			});
			// 关闭alert对话框架
			Button cancel = (Button) window.findViewById(R.id.btnCancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					dlg.cancel();// 对话框关闭。
				}
			});
			v.setEnabled(true);
			break;
		case R.id.btnAddressModifyDefault:
			String defaultUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_MODIFY_USER_ADDRESS);
			defaultUrl += "?id=" + dzId;
			try {
				defaultParams = new JSONObject();
				defaultParams.put("id", dzId);
				defaultParams.put("defaultAddress", true);
				new LoadDataTask().execute(2, defaultUrl, defaultParams,
						HttpClient.TYPE_POST_FORM);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			v.setEnabled(true);
			break;
		case R.id.txtAddressModifyArea:
			if (!pickerExist) {
				AreaPicker();
			}
			break;
		default:
			break;
		}
	}

	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
//					Log.i("modify", result + "");
					result.put("syncType", syncType);
					break;
				case 2:
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
//					Log.i("modify", result + "");
					result.put("syncType", syncType);
					break;
				case 3:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
//					Log.i("delete", result + "");
					result.put("syncType", syncType);
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
				userAddressDao = new UserAddressDao();
				switch (syncType) {
				case 1:
					if (result != null
							&& result.getString("success").equals("1")) {

						Toast.makeText(_this, "修改地址成功！", Toast.LENGTH_SHORT)
								.show();
						userAddressDao.updateSingleUserAddress(modifyParams);
						UserInfoAddressActivity.userInfoAddressActivity.reSetAddress();
						_this.onBackPressed();
					} else {
						Toast.makeText(_this, "修改地址失败！", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case 2:
					if (result != null
							&& result.getString("success").equals("1")) {

						Toast.makeText(_this, "设置默认成功！", Toast.LENGTH_SHORT)
								.show();
						userAddressDao.updateSingleUserAddress(defaultParams);
						UserInfoAddressActivity.userInfoAddressActivity.reSetAddress();
					} else {
						Toast.makeText(_this, "设置默认失败！", Toast.LENGTH_SHORT)
								.show();
					}
					break;
				case 3:
					if (result != null
							&& result.getString("success").equals("1")) {

						Toast.makeText(_this, "删除地址成功！", Toast.LENGTH_SHORT)
								.show();
						userAddressDao.deleteAddressById(dzId);
						UserInfoAddressActivity.userInfoAddressActivity.reSetAddress();
					} else {
						Toast.makeText(_this, "删除地址失败！", Toast.LENGTH_SHORT)
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

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
		if (dzId != 0) {
			Bundle bundle = new Bundle();
			bundle.putInt("dzId", dzId);
			getSupportLoaderManager().restartLoader(1, bundle, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == 1) {
			String sql = "select _id,userId,dzId,name,a.sfId,a.csId,a.dqId,"
					+ "address,postCode,isDefault,phone,sfmc,csmc,dqmc"
					+ " from user_address as a " + "where dzId='"
					+ args.getInt("dzId") + "' limit 1;";
			return new SqlCursorLoader(this, sql, UserAddress.class);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == 1) {
			if (data.getCount() == 1) {
				data.moveToFirst();
				String name = data.getString(data.getColumnIndex("name"));
				String phone = data.getString(data.getColumnIndex("phone"));
				String address = data.getString(data.getColumnIndex("address"));
				String postCode = data.getString(data
						.getColumnIndex("postCode"));
				String sfmc = data.getString(data.getColumnIndex("sfmc"));
				String csmc = data.getString(data.getColumnIndex("csmc"));
				String dqmc = data.getString(data.getColumnIndex("dqmc"));
				sfdm = data.getString(data.getColumnIndex("sfId"));
				csdm = data.getString(data.getColumnIndex("csId"));
				dqdm = data.getString(data.getColumnIndex("dqId"));

//				Integer isDefault = data.getInt(data
//						.getColumnIndex("isDefault"));
//
//				if (isDefault == 1) {
//					defaultAddress = true;
//				} else {
//					defaultAddress = false;
//				}
				String detail = "";

				if (sfmc != null && !sfmc.equals("")) {
					detail += sfmc;
					if (csmc != null && !csmc.equals("")) {
						detail += " " + csmc;
						if (dqmc != null && !dqmc.equals("")) {
							detail += " " + dqmc;
						}
					}
				}

				if (name != null && !name.equals(""))
					txtAddressModifyName.setText(name);

				if (phone != null && !phone.equals(""))
					txtAddressModifyPhone.setText(phone);

				if (address != null && !address.equals(""))
					txtAddressModifyAddress.setText(address);

				if (postCode != null && !postCode.equals(""))
					txtAddressModifyCode.setText(postCode);

				if (!detail.equals(""))
					txtAddressModifyArea.setText(detail);

			}
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	public void AreaPicker() {

		final String sf = _this.sfdm;
		final String cs = _this.csdm;
		final String dq = _this.dqdm;

		if (ad == null) {
			ad = new AddressDialog(this);

			ad.setOnDismissListener(new DialogInterface.OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					if (ad.isValueGet()) {
						ad.getCsObj();
						ad.getDqObj();
						String areaText = "";
						if (ad.getSfObj() != null) {
							UserInfoAddressModifyActivity.this.sfdm = ad
									.getSfObj().getSfdm();
							areaText = ad.getSfObj().getMc();
							if (ad.getCsObj() != null) {
								UserInfoAddressModifyActivity.this.csdm = ad
										.getCsObj().getCsdm();
								areaText = areaText + " "
										+ ad.getCsObj().getMc();
								if (ad.getDqObj() != null) {
									UserInfoAddressModifyActivity.this.dqdm = ad
											.getDqObj().getDqdm();
									areaText = areaText + " "
											+ ad.getDqObj().getMc();

								} else {
									UserInfoAddressModifyActivity.this.dqdm = null;
								}
							} else {
								UserInfoAddressModifyActivity.this.csdm = null;
							}

						} else {
							UserInfoAddressModifyActivity.this.sfdm = null;
						}

						UserInfoAddressModifyActivity.this.txtAddressModifyArea
								.setText(areaText);

					}

				}

			});

		}
		ad.setmSfSelectedListener(new AddressDialog.OnSfSelectedListener() {
			@Override
			public void onAddressSelected() {
				ad.setDefaultSf(sf);
			}
		});
		ad.setmCsSelectedListener(new AddressDialog.OnCsSelectedListener() {

			@Override
			public void onAddressSelected() {
				ad.setDefaultCs(cs);

			}
		});
		ad.setmOnDqSelectedListener(new AddressDialog.OnDqSelectedListener() {
			@Override
			public void onAddressSelected() {
				ad.setDefaultDq(dq);

			}
		});
		ad.init();

		ad.show();

		txtAddressModifyArea.setEnabled(true);

	}

	private JSONObject sendRequest() {

		try {
			JSONObject jsonParams = new JSONObject();
			jsonParams.put("id", dzId);
			String name = "";
			String phone = "";
			String postCode = "";
			String address = "";
			String iSfId = "";
			String iCsId = "";
			String iDqId = "";
			String areaCode = "";

			name = txtAddressModifyName.getText().toString();
			phone = txtAddressModifyPhone.getText().toString();
			postCode = txtAddressModifyCode.getText().toString();
			address = txtAddressModifyAddress.getText().toString();
			if (StringUtils.isBlank(name)) {
				showToast("收货人不能为空");
			}
			if (StringUtils.isBlank(phone)) {
				showToast("手机号码不能为空");
			}
			if (StringUtils.isBlank(address)) {
				showToast("详细地址不能为空");
			}
			if (this.sfdm != null && !this.sfdm.equals("")
					&& !this.sfdm.equals("0")) {
				iSfId = this.sfdm;
				areaCode = iSfId;
				if (this.csdm != null && !this.csdm.equals("")
						&& !this.csdm.equals("0")) {
					iCsId = this.csdm;
					areaCode = iCsId;
					if (this.dqdm != null && !this.dqdm.equals("")
							&& !this.dqdm.equals("0")) {
						iDqId = this.dqdm;
						areaCode = iDqId;
					}
				}
			}

			if (ad != null) {
				if (ad.getSfObj() != null) {
					String sSfmc = ad.getSfObj().getMc();
					jsonParams.put("sSfmc", sSfmc);
				}
				if (ad.getCsObj() != null) {
					String sCsmc = ad.getCsObj().getMc();
					jsonParams.put("sCsmc", sCsmc);
				}
				if (ad.getDqObj() != null) {
					String sDqmc = ad.getDqObj().getMc();
					jsonParams.put("sDqmc", sDqmc);
				}
			}

			jsonParams.put("consignee", name);
			jsonParams.put("phone", phone);
			jsonParams.put("detialAddress", address);
			jsonParams.put("areaCode", areaCode);
			jsonParams.put("postCode", postCode);
//			jsonParams.put("defaultAddress", defaultAddress);

			return jsonParams;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void showToast(String content) {
		Toast.makeText(_this, content, Toast.LENGTH_SHORT).show();
	}

}

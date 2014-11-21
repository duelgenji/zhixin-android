package com.qubaopen.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.daos.UserAddressDao;
import com.qubaopen.dialog.AddressDialog;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAddressAddActivity extends FragmentActivity implements
		View.OnClickListener {

	public static final String INTENT_ADDRESS_ID = "addressId";

	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;
	private Button btnSubmit;

	private EditText txtAddressAddName;
	private EditText txtAddressAddPhone;
	private TextView txtAddressAddArea;
	private EditText txtAddressAddCode;
	private EditText txtAddressAddAddress;

	private JSONObject jsonParams;
	 private UserAddressDao userAddressDao;

	private AddressDialog areaDialog;
	private UserInfoAddressAddActivity _this;

	private String sfdm;
	private String csdm;
	private String dqdm;

	private Boolean pickerExist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info_address_add);

		_this = this;
		 userAddressDao = new UserAddressDao();

		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_user_add_address));
		btnSubmit = (Button) this.findViewById(R.id.addressSubmit);
		btnSubmit.setOnClickListener(this);

		txtAddressAddName = (EditText) this
				.findViewById(R.id.txtAddressAddName);
		txtAddressAddPhone = (EditText) this
				.findViewById(R.id.txtAddressAddPhone);
		String addressAddPhone =  CurrentUserHelper.getCurrentPhone();
		if ( addressAddPhone != null) {
			txtAddressAddPhone.setText(addressAddPhone);
		}else {
			txtAddressAddPhone.setText("");
		}
		
		txtAddressAddArea = (TextView) this
				.findViewById(R.id.txtAddressAddArea);
		txtAddressAddCode = (EditText) this
				.findViewById(R.id.txtAddressAddCode);
		txtAddressAddAddress = (EditText) this
				.findViewById(R.id.txtAddressAddAddress);

		txtAddressAddArea.setOnClickListener(this);

		pickerExist = false;

	}

	@Override
	public void onClick(View v) {
		// v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			v.setEnabled(false);
			finish();
			v.setEnabled(true);
			break;
		case R.id.addressSubmit:
			v.setEnabled(false);
			try {
				jsonParams = new JSONObject();
				String name = "";
				String phone = "";
				String areaCode = "";
				String postCode = "";
				String address = "";
				String iSfId = "";
				String iCsId = "";
				String iDqId = "";

				name = txtAddressAddName.getText().toString();
				phone = txtAddressAddPhone.getText().toString();
				postCode = txtAddressAddCode.getText().toString();
				address = txtAddressAddAddress.getText().toString();
				if (StringUtils.isBlank(name)) {
					showToast("收货人不能为空");
					return;
				}
				if (StringUtils.isBlank(phone)) {
					showToast("手机号码不能为空");
					return;
				}
				if (StringUtils.isBlank(address)) {
					showToast("详细地址不能为空");
					return;
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
				jsonParams.put("consignee", name);
				jsonParams.put("areaCode", areaCode);
				jsonParams.put("detialAddress", address);
				jsonParams.put("phone", phone);
				jsonParams.put("postCode", postCode);
				jsonParams.put("defaultAddress", false);
				// Log.i("增加地址", jsonParams + "");
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_USER_ADD_ADDRESS);

				new LoadDataTask1().execute(1, requestUrl, jsonParams,
						HttpClient.TYPE_POST_FORM);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			v.setEnabled(true);
			break;
		case R.id.txtAddressAddArea:
			if (!pickerExist) {
				pickerExist = true;
				AreaPicker();
			}
			v.setEnabled(true);
			break;
		default:
			break;

		}
	}

	private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							(JSONObject) params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					Log.i("增加地址=====", result + "");
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
						// 。。。。。。。。。
						Toast.makeText(_this, "提交地址成功！", Toast.LENGTH_SHORT)
								.show();
						// jsonParams.put("userId",
						// CurrentUserHelper.getCurrentUserId());
						String requestUrl = SettingValues.URL_PREFIX
								+ getString(R.string.URL_USER_GET_ADDRESS_LIST);
						new updateAddressAddedDataTask().execute(1, requestUrl, null,
								HttpClient.TYPE_GET);
						
						_this.finish();
					} else {
						Toast.makeText(_this, "提交地址失败！", Toast.LENGTH_SHORT)
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

	private class updateAddressAddedDataTask extends
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
					Log.i("address", "获取到的地址：......" + result);
					// 保存到本地数据库
					
					userAddressDao.saveUserAddress(result);
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
						UserInfoAddressActivity.userInfoAddressActivity
						.reSetAddress();
					} else {
						Toast.makeText(_this, "刷新地址失败！", Toast.LENGTH_SHORT)
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

	public void AreaPicker() {

		if (areaDialog == null) {
			areaDialog = new AddressDialog(this);

			areaDialog
					.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							if (areaDialog.isValueGet()) {
								areaDialog.getCsObj();
								areaDialog.getDqObj();
								String areaText = "";
								if (areaDialog.getSfObj() != null) {
									_this.sfdm = areaDialog.getSfObj()
											.getSfdm();
									areaText = areaDialog.getSfObj().getMc();
									if (areaDialog.getCsObj() != null) {
										_this.csdm = areaDialog.getCsObj()
												.getCsdm();
										areaText = areaText + " "
												+ areaDialog.getCsObj().getMc();
										if (areaDialog.getDqObj() != null) {
											_this.dqdm = areaDialog.getDqObj()
													.getDqdm();
											areaText = areaText
													+ " "
													+ areaDialog.getDqObj()
															.getMc();

										} else {
											_this.dqdm = null;
										}
									} else {
										_this.csdm = null;
									}

								} else {
									_this.sfdm = null;
								}

								_this.txtAddressAddArea.setText(areaText);

							}
							pickerExist = false;
						}

					});
		}

		areaDialog.init();

		areaDialog.show();

		txtAddressAddArea.setEnabled(true);

	}

	private void showToast(String content) {
		Toast.makeText(_this, content, Toast.LENGTH_SHORT).show();
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
}
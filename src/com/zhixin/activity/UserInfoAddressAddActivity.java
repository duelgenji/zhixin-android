package com.zhixin.activity;

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
import com.zhixin.R;
import com.zhixin.dialog.AddressDialog;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

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
		txtAddressAddPhone.setText(CurrentUserHelper.getCurrentPhone());
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
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.addressSubmit:

			try {
				JSONObject jsonParams = new JSONObject();
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
				Log.i("增加地址", jsonParams + "");
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
					if (result != null
							&& result.getString("success").equals("1")) {
						// 。。。。。。。。。
						Toast.makeText(_this, "提交地址成功！", Toast.LENGTH_SHORT)
								.show();
						UserInfoAddressActivity.userInfoAddressActivity.reSetAddress();
						_this.onBackPressed();
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
package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.daos.UserAddressDao;
import com.zhixin.dialog.AddressDialog;
import com.zhixin.domain.UserAddress;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.SqlCursorLoader;

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

	private Dialog currentDialog;
	private UserInfoAddressModifyActivity _this;
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
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;
		case R.id.addressSubmit:
            sendRequest("0");
			v.setEnabled(true);
			break;
		case R.id.btnAddressModifyDelete:
            //sendRequest("2");

            final AlertDialog dlg = new AlertDialog.Builder(_this).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setContentView(R.layout.dialog_alert_dialog);
            // 为确认按钮添加事件,执行退出应用操作
            TextView txtAlertContent=(TextView) window.findViewById(R.id.txtAlertContent);
            txtAlertContent.setText("确认删除地址？");
            Button ok = (Button) window.findViewById(R.id.btnConfirm);
            ok.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendRequest("2");
                    dlg.cancel();
                }
            });
            // 关闭alert对话框架
            Button cancel = (Button) window.findViewById(R.id.btnCancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dlg.cancel();//对话框关闭。
                }
            });



			v.setEnabled(true);
			break;
		case R.id.btnAddressModifyDefault:
            sendRequest("1");
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

	private class LoadDataTask extends AsyncTask<JSONObject, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(JSONObject... jbo) {
			JSONObject result = new JSONObject();

			try {
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_ADDRESS_MODIFY);
				JSONObject jsonParams = jbo[0];
                if(jsonParams.getString("requestType").equals("2")){
                    requestUrl=SettingValues.URL_PREFIX
                            + getString(R.string.URL_USER_ADDRESS_DELETE);
                }
				result = HttpClient.requestSync(requestUrl, jsonParams);
                if (result != null && result.has("success")
                        && result.getString("success").equals("1")) {
                    new UserAddressDao().saveUserAddressOne(jsonParams);
                }
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(JSONObject jbo) {
			if (jbo != null) {
				try {
					if (jbo.has("success")
							&& jbo.getString("success").equals("1")) {
						showToast(_this
                                .getString(R.string.toast_action_success));
						_this.onBackPressed();

					} else if (jbo.getString("success").equals("0")) {
						String context = ErrHashMap.getErrMessage(jbo
								.getString("message"));
						context = context == null ? _this
								.getString(R.string.toast_unknown) : context;
						Toast.makeText(_this, context, 5).show();
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			btnSubmit.setEnabled(true);
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == 1) {
			String sql = "select _id,memberId,dzId,name,a.sfId,a.csId,a.dqId,"
					+ "address,postCode,isDefault,phone,sfmc,csmc,dqmc"
					+ " from user_address as a "
					+ "where dzId='" + args.getInt("dzId") + "' limit 1;";
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

				Integer isDefault = data.getInt(data
						.getColumnIndex("isDefault"));

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

	private void sendRequest(String requestType) {
        //0修改 1设置默认并修改 //2删除

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("iDzId",dzId);
            jsonParams.put("requestType",requestType);
            if(!requestType.equals("2")){
                String name ="";
                String phone="";
                String code="";
                String address="";
                String iSfId="";
                String iCsId="";
                String iDqId="";

                name=txtAddressModifyName.getText().toString();
                phone=txtAddressModifyPhone.getText().toString();
                code=txtAddressModifyCode.getText().toString();
                address=txtAddressModifyAddress.getText().toString();
                if(StringUtils.isBlank(name)){
                    showToast("收货人不能为空");
                    return;
                }
                if(StringUtils.isBlank(phone)){
                    showToast("手机号码不能为空");
                    return;
                }
                if(StringUtils.isBlank(address)){
                    showToast("详细地址不能为空");
                    return;
                }
                if(this.sfdm!=null && !this.sfdm.equals("") && !this.sfdm.equals("0")){
                    iSfId=this.sfdm;
                    if(this.csdm!=null && !this.csdm.equals("") && !this.csdm.equals("0")){
                        iCsId=this.csdm;
                        if(this.dqdm!=null && !this.dqdm.equals("") && !this.dqdm.equals("0")){
                            iDqId=this.dqdm;
                        }
                    }
                }

                //showToast(name+phone+code+address+iSfId+iCsId+iDqId+requestType);

                if(ad!=null){
                    if(ad.getSfObj()!=null){
                        String sSfmc=ad.getSfObj().getMc();
                        jsonParams.put("sSfmc",sSfmc);
                    }
                    if(ad.getCsObj()!=null){
                        String sCsmc=ad.getCsObj().getMc();
                        jsonParams.put("sCsmc",sCsmc);
                    }
                    if(ad.getDqObj()!=null){
                        String sDqmc=ad.getDqObj().getMc();
                        jsonParams.put("sDqmc",sDqmc);
                    }
                }

                jsonParams.put("sName",name);
                jsonParams.put("iSfId",iSfId);
                jsonParams.put("iCsId",iCsId);
                jsonParams.put("iDqId",iDqId);
                jsonParams.put("sAddress",address);
                jsonParams.put("sPhone",phone);
                jsonParams.put("sPostCode",code);
                if(requestType.equals("1")){
                    jsonParams.put("isDefault","1");
                }
            }

            new LoadDataTask().execute(jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }


	}

	private void showToast(String content) {
		Toast.makeText(_this, content, 3).show();
	}

}

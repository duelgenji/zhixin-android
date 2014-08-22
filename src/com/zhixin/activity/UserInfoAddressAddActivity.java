package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.daos.UserAddressDao;
import com.zhixin.dialog.AddressDialog;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAddressAddActivity extends FragmentActivity implements View.OnClickListener {

    public static final String INTENT_ADDRESS_ID = "addressId";

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;
    private Button btnSubmit;

    private EditText txtAddressAddName;
    private EditText txtAddressAddPhone;
    private TextView txtAddressAddArea;
    private EditText txtAddressAddCode;
    private EditText txtAddressAddAddress;

    private AddressDialog ad;


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
        txtPageTitle
                .setText(this.getString(R.string.title_user_add_address));
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
//        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.backup_btn:
                this.onBackPressed();
                v.setEnabled(true);
                break;
            case R.id.addressSubmit:
//                sendRequest();
            	
            	 try {
                     JSONObject jsonParams = new JSONObject();
                     String name ="";
                     String phone="";
                     String code="";
                     String address="";
                     String iSfId="";
                     String iCsId="";
                     String iDqId="";

                     name=txtAddressAddName.getText().toString();
                     phone=txtAddressAddPhone.getText().toString();
                     code=txtAddressAddCode.getText().toString();
                     address=txtAddressAddAddress.getText().toString();
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

                     //showToast(name+phone+code+address+iSfId+iCsId+iDqId);

                     jsonParams.put("sName",name);
                     jsonParams.put("iSfId",iSfId);
                     jsonParams.put("iCsId",iCsId);
                     jsonParams.put("iDqId",iDqId);
                     jsonParams.put("sAddress",address);
                     jsonParams.put("sPhone",phone);
                     jsonParams.put("sPostCode",code);
                     String requestUrl = SettingValues.URL_PREFIX
     						+ getString(R.string.URL_USER_ADDRESS);
                     new LoadDataTask1().execute(1,requestUrl,jsonParams,HttpClient.TYPE_POST);
                     
//                     new LoadDataTask().execute(jsonParams);
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
                break;
            default:
                break;

        }
    }

    private class LoadDataTask1 extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result=null;
			Integer syncType=(Integer)params[0];
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST
					result = HttpClient.requestSync(params[1].toString(), (JSONObject)params[2],(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				default :
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
				Integer syncType=result.getInt("syncType");
				switch(syncType){
				case 1:
					if (result != null && result.getInt("success") == 1) {
		                //。。。。。。。。。
						Intent intent = new Intent(_this, UserInfoAddressActivity.class);
						startActivity(intent);
						Toast.makeText(_this, "提交地址成功！", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(_this, "提交数据失败！", Toast.LENGTH_SHORT).show();
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
    
    private class LoadDataTask extends AsyncTask<JSONObject, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... jbo) {
            JSONObject result = new JSONObject();

            try {
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_ADDRESS);
                JSONObject jsonParams = jbo[0];
                result = HttpClient.requestSync(requestUrl, jsonParams);
                if (result != null && result.has("success")
                        && result.getString("success").equals("1")) {
                    new UserAddressDao().clearData();
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
                        showToast(_this.getString(R.string.toast_add_success));
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



    public void AreaPicker() {

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
                            _this.sfdm = ad
                                    .getSfObj().getSfdm();
                            areaText = ad.getSfObj().getMc();
                            if (ad.getCsObj() != null) {
                                _this.csdm = ad
                                        .getCsObj().getCsdm();
                                areaText = areaText + " "
                                        + ad.getCsObj().getMc();
                                if (ad.getDqObj() != null) {
                                    _this.dqdm = ad
                                            .getDqObj().getDqdm();
                                    areaText = areaText + " "
                                            + ad.getDqObj().getMc();

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

                }

            });
        }

        ad.init();

        ad.show();

        txtAddressAddArea.setEnabled(true);

    }

    private void sendRequest() {

        try {
            JSONObject jsonParams = new JSONObject();
            String name ="";
            String phone="";
            String code="";
            String address="";
            String iSfId="";
            String iCsId="";
            String iDqId="";

            name=txtAddressAddName.getText().toString();
            phone=txtAddressAddPhone.getText().toString();
            code=txtAddressAddCode.getText().toString();
            address=txtAddressAddAddress.getText().toString();
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

            //showToast(name+phone+code+address+iSfId+iCsId+iDqId);
            jsonParams.put("sName",name);
            jsonParams.put("iSfId",iSfId);
            jsonParams.put("iCsId",iCsId);
            jsonParams.put("iDqId",iDqId);
            jsonParams.put("sAddress",address);
            jsonParams.put("sPhone",phone);
            jsonParams.put("sPostCode",code);

            new LoadDataTask().execute(jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void showToast(String content) {
        Toast.makeText(_this, content, 3).show();
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
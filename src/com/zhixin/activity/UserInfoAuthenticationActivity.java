package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.MatcherUtil;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoAuthenticationActivity extends FragmentActivity implements View.OnClickListener {

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;
    private Button submitBtn;

    private Activity _this;

    private TextView txtInputNamePersonalProfile;
    private TextView txtInputIdentityPersonalProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_authentication);
        _this=this;
        txtPageTitle= (TextView)this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton)this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_user_authentication));
        submitBtn= (Button) this.findViewById(R.id.addressSubmit); 
        submitBtn.setOnClickListener(this);

        txtInputNamePersonalProfile=(TextView)  this.findViewById(R.id.txtInputNamePersonalProfile);
        txtInputIdentityPersonalProfile=(TextView)  this.findViewById(R.id.txtInputIdentityPersonalProfile);
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
                sendRequest();
                break;
            default:
                break;
        }
    }

    private class LoadDataTask extends AsyncTask<JSONObject, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... jbo) {
            JSONObject result=new JSONObject();
            String requestUrl = SettingValues.URL_PREFIX
                    + getString(R.string.URL_USER_INFO_ID);

            try {
                JSONObject jsonParams = jbo[0];
                result = HttpClient.requestSync(
                        requestUrl, jsonParams);
                if(result!=null && result.has("success") && result.getString("success").equals("1")){
               
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jbo) {
            if(jbo!=null){
                try {
                    if(jbo.has("success") && jbo.getString("success").equals("1")){
                        showToast(_this.getString(R.string.toast_modify_success));
                        _this.onBackPressed();


                    }else if(jbo.getString("success").equals("0")){
                        String context= ErrHashMap.getErrMessage(jbo.getString("message"));
                        context= context==null? _this.getString(R.string.toast_unknown):context;
                        Toast.makeText(_this, context, 5).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            submitBtn.setEnabled(true);
        }

    }


    private void sendRequest(){
        if(txtInputNamePersonalProfile.getText()==null ||
                txtInputIdentityPersonalProfile.getText()==null ){
            showToast(_this.getString(R.string.toast_input_cant_be_null));
            submitBtn.setEnabled(true);
            return;
        }
        String sName=txtInputNamePersonalProfile.getText().toString().trim();
        String sId=txtInputIdentityPersonalProfile.getText().toString().trim();


        if(sName.equals("")|| sId.equals("")){
            showToast(_this.getString(R.string.toast_input_cant_be_null));
            submitBtn.setEnabled(true);
            return;
        }
        if(!MatcherUtil.validateId(sId)){
            showToast(_this.getString(R.string.toast_user_info_wrong_id));
            submitBtn.setEnabled(true);
            return;
        }
        if(!MatcherUtil.isIdCard(sId)){
            showToast(_this.getString(R.string.toast_user_info_wrong_id));
            submitBtn.setEnabled(true);
            return;
        }
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("id",sId);
            jsonParams.put("name",sName);
            new LoadDataTask().execute(jsonParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showToast(String content){
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
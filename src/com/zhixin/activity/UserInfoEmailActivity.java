package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import com.zhixin.daos.UserInfoDao;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.MatcherUtil;

/**
 * Created by duel on 14-3-24.
 */
public class UserInfoEmailActivity  extends FragmentActivity implements View.OnClickListener {

    public static final String INTENT_EMAIL = "email";

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;
    private Button submitBtn;

        private Activity _this;

    private String sNewEmail;
    private EditText txtInputEmailPersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_email);

        _this=this;

        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_user_email));
        submitBtn= (Button) this.findViewById(R.id.addressSubmit);
        submitBtn.setOnClickListener(this);

        txtInputEmailPersonalProfile=(EditText)  this.findViewById(R.id.txtInputEmailPersonalProfile);
        String email=getIntent().getStringExtra(INTENT_EMAIL);
        if(email!=null && !email.equals("")){
            txtInputEmailPersonalProfile.setText(email);
            txtInputEmailPersonalProfile.setSelection(email.length());

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
                    + getString(R.string.URL_USER_INFO_EMAIL);

            try {
                JSONObject jsonParams = jbo[0];
                result = HttpClient.requestSync(
                        requestUrl, jsonParams);
                if(result!=null && result.has("success") && result.getString("success").equals("1")){
                    new UserInfoDao().saveUserInfoEmail(CurrentUserHelper.getCurrentPhone(),
                            sNewEmail);
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
        if(txtInputEmailPersonalProfile.getText()==null){
            showToast(_this.getString(R.string.toast_input_cant_be_null));
            submitBtn.setEnabled(true);
            return;
        }
        String sEmail=txtInputEmailPersonalProfile.getText().toString().trim();

        if(sEmail.equals("")){
            showToast(_this.getString(R.string.toast_input_cant_be_null));
            submitBtn.setEnabled(true);
            return;
        }
        if(!MatcherUtil.validateEmail(sEmail)){
            showToast(_this.getString(R.string.toast_user_info_wrong_email));
            submitBtn.setEnabled(true);
            return;
        }
        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("email",sEmail);
            sNewEmail=sEmail;
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
        // TODO Auto-generated method stub
        super.onResume();
        StatService.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}

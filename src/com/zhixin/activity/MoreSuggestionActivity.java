package com.zhixin.activity;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.zhixin.datasynservice.SubmitOnlyService;
import com.zhixin.settings.ErrHashMap;

/**
 * Created by duel on 14-3-20.
 */
public class MoreSuggestionActivity  extends FragmentActivity implements View.OnClickListener {

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;

    private MoreSuggestionActivity _this;
    private SubmitOnlyService submitOnlyService;

    private EditText txtContentSuggestion;
    private EditText txtContactSuggestion;
    private Button btnSubmitSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_suggestion);

        _this=this;
        submitOnlyService=new SubmitOnlyService(this);
        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_more_suggestion));

        txtContentSuggestion=(EditText) this.findViewById(R.id.txtContentSuggestion);
        txtContactSuggestion=(EditText) this.findViewById(R.id.txtContactSuggestion);
        btnSubmitSuggestion=(Button) this.findViewById(R.id.btnSubmitSuggestion);

        btnSubmitSuggestion.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.backup_btn:
                this.onBackPressed();
                v.setEnabled(true);
                break;
            case R.id.btnSubmitSuggestion:
                sendSuggestion();
                break;
            default:
                break;

        }
    }


    private void sendSuggestion(){
        String sContent=txtContentSuggestion.getText().toString().trim();
        if(sContent.equals("")){
            showToast(this.getString(R.string.toast_input_cant_be_null));
            btnSubmitSuggestion.setEnabled(true);
            return;
        }
        String sContact= txtContactSuggestion.getText() != null ? txtContactSuggestion.getText().toString() : "";

        if(sContact.equals("")){
            showToast(this.getString(R.string.toast_input_contact_cant_be_null));
            btnSubmitSuggestion.setEnabled(true);
            return;
        }

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("sContent", sContent);
            jsonParams.put("sBack", sContact);
            jsonParams.put("iLx", "0");
            new LoadDataTask().execute(jsonParams);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    private class LoadDataTask extends AsyncTask<JSONObject, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(JSONObject... params) {
            try {
                return submitOnlyService.moreSuggestion(params[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result != null && result.getString("success").equals("1")) {
                    showToast(_this.getString(R.string.toast_thanks_for_your_support));
                    _this.onBackPressed();
                }else if(result.getString("success").equals("0")){
                    String context= ErrHashMap.getErrMessage(result.getString("message"));
                    context= context==null? _this.getString(R.string.toast_unknown):context;
                    showToast(context);
                }
                btnSubmitSuggestion.setEnabled(true);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void showToast(String content){
        Toast.makeText(this,content,3).show();
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

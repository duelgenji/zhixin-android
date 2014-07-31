package com.zhixin.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.UserInfo;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.ErrHashMap;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
import com.zhixin.utils.SqlCursorLoader;

/**
 * Created by duel on 14-3-20.
 */
public class UserInfoActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>,View.OnClickListener {

    private TextView txtPageTitle;
    private ImageButton iBtnPageBack;

    private Activity _this;

    private Boolean pickerExist;
    private Dialog currentDialog;

    private LinearLayout layoutSexPersonalProfile;
    private LinearLayout layoutBloodTypePersonalProfile;
    private LinearLayout layoutAuthenticationPersonalProfile;
    private LinearLayout layoutAddressPersonalProfile;
    private LinearLayout layoutBirthdayPersonalProfile;
    private LinearLayout layoutEmailPersonalProfile;

    private TextView txtSexPersonalProfile;
    private TextView txtBirthPersonalProfile;
    private TextView txtBloodTypePersonalProfile;
    private TextView txtAddressPersonalProfile;
    private TextView txtEmailPersonalProfile;
    private TextView txtAuthenticationPersonalProfile;

    private String email;

    private QubaopenProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        _this=this;
        txtPageTitle= (TextView)
                this.findViewById(R.id.title_of_the_page);
        iBtnPageBack =(ImageButton) this.findViewById(R.id.backup_btn);
        iBtnPageBack.setOnClickListener(this);
        txtPageTitle.setText(this.getString(R.string.title_user_personal_profile));

        txtSexPersonalProfile= (TextView) this.findViewById(R.id.txtSexPersonalProfile);
        txtBirthPersonalProfile= (TextView) this.findViewById(R.id.txtBirthPersonalProfile);
        txtBloodTypePersonalProfile= (TextView) this.findViewById(R.id.txtBloodTypePersonalProfile);
        txtAddressPersonalProfile= (TextView) this.findViewById(R.id.txtAddressPersonalProfile);
        txtEmailPersonalProfile= (TextView) this.findViewById(R.id.txtEmailPersonalProfile);
        txtAuthenticationPersonalProfile= (TextView) this.findViewById(R.id.txtAuthenticationPersonalProfile);


        layoutSexPersonalProfile=(LinearLayout) this.findViewById(R.id.layoutSexPersonalProfile);
        layoutSexPersonalProfile.setOnClickListener(this);
        layoutBloodTypePersonalProfile=(LinearLayout) this.findViewById(R.id.layoutBloodTypePersonalProfile);
        layoutBloodTypePersonalProfile.setOnClickListener(this);
        layoutBirthdayPersonalProfile=(LinearLayout) this.findViewById(R.id.layoutBirthdayPersonalProfile);
        layoutBirthdayPersonalProfile.setOnClickListener(this);
        layoutAuthenticationPersonalProfile=(LinearLayout) this.findViewById(R.id.layoutAuthenticationPersonalProfile);
        layoutAuthenticationPersonalProfile.setOnClickListener(this);
        layoutAddressPersonalProfile=(LinearLayout) this.findViewById(R.id.layoutAddressPersonalProfile);
        layoutAddressPersonalProfile.setOnClickListener(this);
        layoutEmailPersonalProfile=(LinearLayout) this.findViewById(R.id.layoutEmailPersonalProfile);
        layoutEmailPersonalProfile.setOnClickListener(this);
        progressDialog = new QubaopenProgressDialog(this);


        pickerExist=false;


    }


    private class LoadDataTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            //modify sex
            JSONObject result=new JSONObject();
            if(params[0]!=null && params[0].equals("sex")){
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_INFO_SEX);
                JSONObject jsonParams = new JSONObject();
                try {

                    Integer iSex=Integer.parseInt(params[1]);
                    jsonParams.put("sex",iSex);
                    result = HttpClient.requestSync(
                            requestUrl, jsonParams);
                    if (result != null && result.getString("success").equals("1")) {
                        result.put("sex",iSex);
                        new UserInfoDao().saveUserInfoSex(CurrentUserHelper.getCurrentPhone(),
                                iSex);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //modify blood type
            if(params[0]!=null && params[0].equals("bloodType")){
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_INFO_BLOOD_TYPE);
                JSONObject jsonParams = new JSONObject();
                try {

                    Integer iType=Integer.parseInt(params[1]);
                    jsonParams.put("bloodtype",iType);
                    result = HttpClient.requestSync(
                            requestUrl, jsonParams);
                    if (result != null && result.getString("success").equals("1")) {
                        result.put("bloodtype",iType);
                        new UserInfoDao().saveUserInfoBloodType(CurrentUserHelper.getCurrentPhone(),
                                iType);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //modify birthday
            if(params[0]!=null && params[0].equals("birthday")){
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_INFO_BIRTHDAY);
                JSONObject jsonParams = new JSONObject();
                try {

                    String sBirth=params[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    Date date = sdf.parse(sBirth);

                    jsonParams.put("birthdate",sBirth);
                    result = HttpClient.requestSync(
                            requestUrl, jsonParams);
                    if (result != null && result.getString("success").equals("1")) {
                        result.put("birthdate",sBirth);
                        new UserInfoDao().saveUserInfoBirthDay(CurrentUserHelper.getCurrentPhone(),
                                date);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            //load validate info
            if(params[0]!=null && params[0].equals("validate")){
                String requestUrl = SettingValues.URL_PREFIX
                        + getString(R.string.URL_USER_INFO_GET_ID);
                JSONObject jsonParams = new JSONObject();
                try {
                    result = HttpClient.requestSync(
                            requestUrl, null);
                    result.put("validate","1");
                    if (result != null && result.getString("success").equals("1")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(JSONObject jbo) {
            if(jbo!=null){
                try {
                    if(jbo.has("validate")){
                        if(jbo.has("isExist") && jbo.getString("isExist").equals("1")){
                            txtAuthenticationPersonalProfile.setText(_this.getString(R.string.user_info_authentication_true));
                        }else if(jbo.getString("isExist").equals("0")){
                            txtAuthenticationPersonalProfile.setText(_this.getString(R.string.user_info_authentication_false));

                        }
                        progressDialog.dismiss();

                        return;

                    }
                    if(jbo.has("success") && jbo.getString("success").equals("1")){

                        showToast(_this.getString(R.string.toast_modify_success));
                        getSupportLoaderManager().restartLoader(0, null, UserInfoActivity.this);

                    }else if(jbo.getString("success").equals("0")){
                        String context= ErrHashMap.getErrMessage(jbo.getString("message"));
                        context= context==null? _this.getString(R.string.toast_unknown):context;
                        Toast.makeText(_this,context,5).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        StatService.onResume(this);
        //加载数据
        progressDialog.show();
        getSupportLoaderManager().restartLoader(0, null, this);
        new LoadDataTask().execute("validate");
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        v.setEnabled(false);
        switch (v.getId()) {
            case R.id.backup_btn:
                this.onBackPressed();
                v.setEnabled(true);
                break;
            case R.id.layoutSexPersonalProfile:
                if(!pickerExist){
                    pickerExist=true;
                    this.SexPicker();
                }
                break;
            case R.id.layoutBloodTypePersonalProfile:
                if(!pickerExist){
                    pickerExist=true;
                    this.BloodTypePicker();
                }
                break;
            case R.id.layoutBirthdayPersonalProfile:
                if(!pickerExist){
                    pickerExist=true;
                    this.DatePicker();
                }
                break;
            case R.id.layoutEmailPersonalProfile:
                if(!pickerExist){
                    intent = new Intent(_this, UserInfoEmailActivity.class);
                    if(email!=null && !email.equals("")){
                        intent.putExtra(UserInfoEmailActivity.INTENT_EMAIL,email);
                    }
                    startActivity(intent);
                    v.setEnabled(false);
                }
                break;
            case R.id.layoutAuthenticationPersonalProfile:
                if(!pickerExist){
                    intent = new Intent(_this, UserInfoAuthenticationActivity.class);
                    startActivity(intent);
                    v.setEnabled(false);
                }
                break;
            case R.id.layoutAddressPersonalProfile:
                if(!pickerExist){
                    SharedPreferences sharedPref = this.getSharedPreferences(
                            SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
                    Boolean isAddressSaved = sharedPref.getBoolean(
                            SettingValues.KEY_CURRENT_ADDRESS_SAVED, false);
                    if(!isAddressSaved){
                        showToast("正在加载数据中");
                        v.setEnabled(true);
                        return;
                    }

                    intent =new Intent(_this,UserInfoAddressActivity.class);
                    startActivity(intent);
                    v.setEnabled(false);
                }
            default:
                break;

        }
        v.setEnabled(true);
    }

    public void SexPicker(){
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar){
            @Override
            public void dismiss() {
                super.dismiss();
                pickerExist=false;
            }
        };
        dialog.setContentView(R.layout.dialog_pick_sex);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        Button btnMale = (Button) dialog
                .findViewById(R.id.btnMaleSexPicker);
        Button btnFemale = (Button) dialog
                .findViewById(R.id.btnFemaleSexPicker);
        Button btnCancel = (Button) dialog
                .findViewById(R.id.btnCancelSexPicker);

        currentDialog=dialog;

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnMale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("sex","0");
                dialog.dismiss();

            }
        });

        btnFemale.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("sex","1");
                dialog.dismiss();

            }
        });

        dialog.show();

    }


    public void BloodTypePicker(){
        final Dialog dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar){
            @Override
            public void dismiss() {
                super.dismiss();
                pickerExist=false;
            }
        };;
        dialog.setContentView(R.layout.dialog_pick_blood_type);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        dialog.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(lp);
        Button btnA = (Button) dialog
                .findViewById(R.id.btnABloodTypePicker);
        Button btnB = (Button) dialog
                .findViewById(R.id.btnBBloodTypePicker);
        Button btnO = (Button) dialog
                .findViewById(R.id.btnOBloodTypePicker);
        Button btnAB = (Button) dialog
                .findViewById(R.id.btnABBloodTypePicker);
        Button btnOther = (Button) dialog
                .findViewById(R.id.btnOtherBloodTypePicker);
        Button btnCancel = (Button) dialog
                .findViewById(R.id.btnCancelBloodTypePicker);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("bloodType","0");
                dialog.dismiss();
            }
        });

        btnB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("bloodType","1");
                dialog.dismiss();
            }
        });
        btnO.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("bloodType", "2");
                dialog.dismiss();
            }
        });

        btnAB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               new LoadDataTask().execute("bloodType","3");
              dialog.dismiss();
            }
        });
        btnOther.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new LoadDataTask().execute("bloodType","4");
              dialog.dismiss();
            }
        });

        dialog.show();

    }

    private int mYear,mMonth,mDay;

    public void DatePicker(){
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        final DatePicker datepicker = new DatePicker(_this);
        String sBirth="";
        Integer year=1995;
        Integer month=0;
        Integer day=1;
        if(txtBirthPersonalProfile.getText()!=null){
            sBirth=txtBirthPersonalProfile.getText().toString().trim();
            if(sBirth.length()>=10){
                year=Integer.parseInt(sBirth.substring(0,4));
                month=Integer.parseInt(sBirth.substring(5,7))-1;
                day=Integer.parseInt(sBirth.substring(8,10));
            }
        };

        datepicker.init(year, month, day, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(_this);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                pickerExist=false;
            }
        });
        builder.setView(datepicker);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                mYear=datepicker.getYear();
                mMonth=datepicker.getMonth()+1;
                mDay=datepicker.getDayOfMonth();
                //Toast.makeText(_this, mYear+"-"+mMonth+"-"+mDay, Toast.LENGTH_SHORT).show();
                new LoadDataTask().execute("birthday", mYear+"-"+mMonth+"-"+mDay);
                pickerExist=false;
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                pickerExist=false;
            }

        });
        builder.show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String name=CurrentUserHelper.getCurrentPhone();
        return new SqlCursorLoader(this,"select * from user_info where username='"+name+"' limit 1;",
                UserInfo.class);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if(data.getCount()==1){
            data.moveToFirst();
            Integer sex=data.getInt(data.getColumnIndex("sex"));
            Integer bloodtype=data.getInt(data.getColumnIndex("bloodtype"));
            String email=data.getString(data.getColumnIndex("email"));
            String birthday=data.getString(data.getColumnIndex("birthday"));
            String address=data.getString(data.getColumnIndex("address"));

            //set sex value
            if(sex==1){
                txtSexPersonalProfile.setText(_this.getString(R.string.dialog_pick_sex_female));
            }else if(sex==0){
                txtSexPersonalProfile.setText(_this.getString(R.string.dialog_pick_sex_male));
            }else{
                txtSexPersonalProfile.setText(_this.getString(R.string.dialog_pick_sex_null));
            }

            //set birthday value
            if(birthday==null || birthday.equals("")){
                txtBirthPersonalProfile.setText("");
            }else{
                if(birthday.length()>=10)
                    birthday=birthday.substring(0,10);
                txtBirthPersonalProfile.setText(birthday);
            }

            //set blood type value
            if(bloodtype==0){
                txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_A));
            }else if(bloodtype==1){
                txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_B));
            }else if(bloodtype==2){
                txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_O));
            }else if(bloodtype==3){
                txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_AB));
            }else if(bloodtype==4){
                txtBloodTypePersonalProfile.setText(_this.getString(R.string.dialog_pick_blood_type_Other));
            }else{
                txtBloodTypePersonalProfile.setText("");
            }

            //set email value
            if(email==null || email.equals("")){
                txtEmailPersonalProfile.setText("");
            }else{
                this.email=email;
                txtEmailPersonalProfile.setText(email);
            }

            if(address!=null && !address.equals("")){
                txtAddressPersonalProfile.setText(address);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void showToast(String content){
        Toast.makeText(this, content, 3).show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

}

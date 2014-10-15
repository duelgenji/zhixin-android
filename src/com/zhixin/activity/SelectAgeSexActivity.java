package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.datasynservice.UserService;
import com.zhixin.service.UploadUserJpushAliasService;
import com.zhixin.utils.HttpClient;

public class SelectAgeSexActivity extends Activity implements
		View.OnClickListener {

	private ImageButton btnSubmit, btnBoy, btnGirl;
	
	private EditText txtAge;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_age_sex);
		
		txtAge=(EditText)findViewById(R.id.txtAge);

		btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		btnBoy = (ImageButton) findViewById(R.id.btnBoy);
		btnBoy.setOnClickListener(this);
		btnGirl = (ImageButton) findViewById(R.id.btnGirl);
		btnGirl.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSubmit:
			String sAge=txtAge.getText().toString();
			Integer age=null; 
			if(StringUtils.isNotEmpty(sAge)){
				age=Integer.parseInt(sAge);
			}else{
				Toast.makeText(getApplicationContext(), "请输入年龄", Toast.LENGTH_SHORT).show();
				return;
			}
			
			
			Integer sex=2;
			if(!btnBoy.isEnabled()){
				sex=0;
			}else if(!btnGirl.isEnabled()){
				sex=1;
			}else{
				Toast.makeText(getApplicationContext(), "请选性别", Toast.LENGTH_SHORT).show();
				return;
			}
			//Toast.makeText(getApplicationContext(), "年龄"+age+"性别"+sex, Toast.LENGTH_SHORT).show();
			new LoadDataTask().execute(age,sex);
			break;
		case R.id.btnBoy:
			btnBoy.setEnabled(false);
			btnGirl.setEnabled(true);
			break;
		case R.id.btnGirl:
			btnGirl.setEnabled(false);
			btnBoy.setEnabled(true);
			break;
		default:
			break;
		}
	}
	
	
	
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			UserService userService=new UserService(SelectAgeSexActivity.this);
			int age=(Integer) params[0];
			int sex=(Integer) params[1];
			result=userService.SubmitAgeAndSex(age, sex);
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				if (result != null && result.getInt("success") == 1) {
					Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(SelectAgeSexActivity.this, UploadUserJpushAliasService.class);
					startService(intent);
					SelectAgeSexActivity.this.onBackPressed();
				}else{
					Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}

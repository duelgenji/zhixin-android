package com.qubaopen.asynctask;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;

import com.qubaopen.datasynservice.RegistService;
import com.qubaopen.settings.MyApplication;

import android.os.AsyncTask;
import android.widget.Toast;

public class loginAsyncTask extends AsyncTask<Object, Object, Boolean> {

	RegistService service;
	
	
	@Override
	protected Boolean doInBackground(Object... params) {
			service=new RegistService(MyApplication.getAppContext());
			service.logOnAction();
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		super.onProgressUpdate(values);
	}

}

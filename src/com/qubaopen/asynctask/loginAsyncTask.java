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
		// TODO Auto-generated method stub
		try {
			service=new RegistService(MyApplication.getAppContext());
			service.logOnAction();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Object... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}

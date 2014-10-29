package com.qubaopen.service;

import java.io.File;
import java.io.FileNotFoundException;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.qubaopen.R;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

import eu.janmuller.android.simplecropimage.CropImage;

public class CropImageIntentService extends IntentService {
	public static final String IMAGE_UPLOAD_DONE_RECEIVER = "MAGE_UPLOAD_DONE_RECEIVER";

	public CropImageIntentService() {
		super("CropImageIntentService");
	}

	@Override
	protected void onHandleIntent(Intent data) {

		String path = data.getStringExtra(CropImage.IMAGE_PATH);
		if (path == null) {
			return;
		}
		String requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_UPLOAD_AVATAR);
		AjaxParams params = new AjaxParams();
		try {
			File fileToSend = new File(SettingValues.TEMP_PHOTO_FILE_PATH);
			params.put("avatar", fileToSend);
//			Log.i("upload","上传头像：......" + fileToSend);
			JSONObject result = HttpClient.requestSyncForUnchangedParams(
					requestUrl, params);
			if (result != null) {

				if (result.getString("success").equals("1")) {
					
						Intent i = new Intent(IMAGE_UPLOAD_DONE_RECEIVER)
								.putExtra("success", "1");
						this.sendBroadcast(i);

				
				}

			} else {
				Intent i = new Intent(IMAGE_UPLOAD_DONE_RECEIVER).putExtra(
						"success", "0");
				this.sendBroadcast(i);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	private String getFileName(String fullname) {
		String[] filenameArray = fullname.split(File.separator);
		return filenameArray[filenameArray.length - 1];
	}

}

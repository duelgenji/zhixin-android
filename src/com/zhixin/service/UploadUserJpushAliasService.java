package com.zhixin.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;







import com.zhixin.database.DbManager;
import com.zhixin.datasynservice.RegistService;
import com.zhixin.domain.UserSettings;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.MyApplication;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import eu.janmuller.android.simplecropimage.CropImage;

public class UploadUserJpushAliasService extends IntentService {

	private RegistService service;

	public UploadUserJpushAliasService() {
		super("UploadUserJpushAliasService");
	}

	@Override
	protected void onHandleIntent(Intent data) {
		if (!DbManager.getDatabase().tableExists(UserSettings.class)) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new InputStreamReader(MyApplication
						.getAppContext().getAssets()
						.open("userSettingsSql.txt")));
				String sql = reader.readLine();
				DbManager.getDatabase().exeCustomerSql(sql);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
		service = new RegistService(MyApplication.getAppContext());
		if (CurrentUserHelper.getCurrentUserId() != 0
				&& StringUtils.isNotBlank(CurrentUserHelper.getCurrentPhone())) {

			try {
				service.logOnAction();

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

}

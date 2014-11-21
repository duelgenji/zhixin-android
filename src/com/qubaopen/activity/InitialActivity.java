package com.qubaopen.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import cn.jpush.android.api.InstrumentedActivity;
import cn.sharesdk.framework.ShareSDK;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.service.GetAddressIntentService;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.PhoneHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.ShareUtil;

/**
 * @author Administrator 标志Logo
 */
public class InitialActivity extends InstrumentedActivity {

	boolean isFirstIn = false;

//	private RegistService registService;

	private Long currentUserId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_initial);

		// share sdk inital
		ShareSDK.initSDK(this);
		ShareUtil.addSharePic();

//		registService = new RegistService(this);

		// determine screen size in dp
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		float density = getResources().getDisplayMetrics().density;
//		float dpHeight = outMetrics.heightPixels / density;
//		float dpWidth = outMetrics.widthPixels / density;

		PhoneHelper.savePhoneDensity(density);
		PhoneHelper.savePhoneHeight(outMetrics.heightPixels);
		PhoneHelper.savePhoneWidth(outMetrics.widthPixels);

//		Log.i("phone", "density" + String.valueOf(density));
//		Log.i("phone", "height" + String.valueOf(dpHeight));
//		Log.i("phone", "width" + String.valueOf(dpWidth));
//		Log.i("phone", "sdk version" + android.os.Build.VERSION.SDK_INT);

		new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				Boolean isAddressSaved = false;
				SharedPreferences sharedPref = InitialActivity.this
						.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
								Context.MODE_PRIVATE);
				isAddressSaved = sharedPref.getBoolean(
						SettingValues.KEY_CURRENT_ADDRESS_SAVED, false);

				return isAddressSaved;
			}

			@Override
			protected void onPostExecute(Boolean params) {
				if (!params) {
					Intent intent = new Intent(InitialActivity.this,
							GetAddressIntentService.class);
					//InitialActivity.this.startService(intent);
				}

				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						SharedPreferences sharedPref = InitialActivity.this
								.getSharedPreferences(
										SettingValues.FILE_NAME_SETTINGS,
										Context.MODE_PRIVATE);
						isFirstIn = sharedPref.getBoolean(
								SettingValues.APP_FIRSTTIME_IN, true);
						// isFirstIn=true;
						currentUserId = CurrentUserHelper.getCurrentUserId();
					
						return null;
					}

					@Override
					protected void onPostExecute(Void params) {
						new Thread() {
							public void run() {
								try {
									Thread.sleep(1000);

									judgeWhichToStart();
									finish();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

						}.start();
					}
				}.execute();

			}
		}.execute();

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

	@Override
	public void onStart() {
		super.onStart();

	}

	private void judgeWhichToStart() {

		if (isFirstIn) {
			Intent intent = new Intent(InitialActivity.this,
					FristInIntroduceActivity.class);
			startActivity(intent);
			return;
		} else {
			if (currentUserId != 0) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			} else {
				startLoginActivity();
			}
		}
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, MainLoginActivity.class);
		startActivity(intent);
	}

}

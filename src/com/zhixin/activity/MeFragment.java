package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhixin.R;
import com.zhixin.database.DbManager;
import com.zhixin.domain.UserSettings;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

public class MeFragment extends Fragment implements View.OnClickListener{
	private Activity mainActivity;
	/***/
	private ImageLoader imageLoader;
	/***/
	private DisplayImageOptions imageOptions;
	private ImageView shezhi;
	/**
	 * 标题
	 */
	private TextView txtPageTitle;
	
	private LinearLayout layoutMyprofile,layoutDuijiang,layoutSuggestion,layoutAbout,layoutShareAppComp;
	
	private View scoreTheApp;
	
	private TextView nickNameTextView;
	private ImageButton editNickNameBtn;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.mainActivity = activity;
	}
	private class NicknameIconClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			editNickNameBtn.setEnabled(false);
			Intent intent = new Intent(mainActivity,
					ModifyNicknameActivity.class);
			String nickName = nickNameTextView.getText().toString();
			if (nickName != null && !nickName.equals("")) {
				intent.putExtra(ModifyNicknameActivity.INTENT_NICKNAME,
						nickName);
			}
			startActivity(intent);
			editNickNameBtn.setEnabled(true);
		}

	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.me_main, container,
				false);
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText(this.getString(R.string.title_me));
		
		nickNameTextView = (TextView) view.findViewById(R.id.nickNameTextView);
		
		
		editNickNameBtn = (ImageButton) view.findViewById(R.id.editNickNameBtn);
		editNickNameBtn.setOnClickListener(new NicknameIconClickListener());
		
		layoutMyprofile = (LinearLayout) view.findViewById(R.id.layoutMyprofile);
		layoutMyprofile.setOnClickListener(this);
		
		layoutDuijiang = (LinearLayout) view.findViewById(R.id.layoutDuijiang);
		layoutDuijiang.setOnClickListener(this);
		
//		layoutDafen = (LinearLayout) view.findViewById(R.id.layoutDafen);
//		layoutDafen.setOnClickListener(this);
		layoutShareAppComp = (LinearLayout) view.findViewById(R.id.layoutShareAppComp);
		layoutShareAppComp.setOnClickListener(this);
		
		layoutSuggestion = (LinearLayout) view.findViewById(R.id.layoutSuggestion);
		layoutSuggestion.setOnClickListener(this);
		
		layoutAbout = (LinearLayout) view.findViewById(R.id.layoutAbout);
		layoutAbout.setOnClickListener(this);
		
		shezhi = (ImageView) view.findViewById(R.id.shezhi);
		shezhi.setOnClickListener(this);
		
		scoreTheApp = view.findViewById(R.id.scoreTheApp);
		scoreTheApp.setOnClickListener(this);
		return view;
		
	}
	private class LoadDataTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			if (params[0] != null && params[0].equals("userSetting")) {
				String requestUrl = SettingValues.URL_PREFIX
						+ getString(R.string.URL_GET_OPTION);
				// JSONObject jsonParams = new JSONObject();
				try {

					JSONObject result = HttpClient
							.requestSync(requestUrl, null);
					if (result != null
							&& result.getString("success").equals("1")) {

						UserSettings us = DbManager.getDatabase().findById(1,
								UserSettings.class);

						us.setIsGkdt(result.getInt("iGkdt"));
						us.setIsHygkwj(result.getInt("iHygkwj"));
						us.setIsMzgkwj(result.getInt("iMzgkwj"));
						DbManager.getDatabase().update(us);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			} 
//			else if (params[0] != null && params[0].equals("newVersion")) {
//				service = new MainMenuService(mainActivity);
//				try {
//					return service.newVersion();
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//			}

			return params[0];
		}
//版本更新
//		@Override
//		protected void onPostExecute(String params) {
//			if (params.equals("userSetting")) {
//				Intent intent = new Intent(mainActivity,
//						MoreSetting.class);
//				startActivity(intent);
//
//				layoutSetting.setEnabled(true);
//			} else if (params.equals("isNew")) {
//				Toast.makeText(mainActivity, "已经是最新版本", 3).show();
//				layoutUpdateCanClick = true;
//			} else {
//				if (params != null && !params.equals("")) {
//					updateManager = new UpdateManager(mainActivity);
//					updateManager.checkUpdateInfo(params);
//				}
//				layoutUpdateCanClick = true;
//			}
//		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		v.setEnabled(false);
		Intent intent;
		switch (v.getId()) {
		case R.id.shezhi:
			new LoadDataTask().execute("userSetting");
			break;
		case R.id.layoutMyprofile:
			intent = new Intent(mainActivity, UserInfoActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layoutDuijiang:
			intent = new Intent(mainActivity, DuijiangActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
//		case R.id.layoutResetPwd:
//			intent = new Intent(mainActivity, MoreResetPwdActivity.class);
//			startActivity(intent);
//			v.setEnabled(true);
//			break;
//		case R.id.layoutUpdate:
//			v.setEnabled(true);
//			if (layoutUpdateCanClick) {
//				layoutUpdateCanClick = false;
//				new LoadDataTask().execute("newVersion");
//			}
//			break;
		case R.id.layoutAbout:
			intent = new Intent(mainActivity, MoreAboutusActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
//		case R.id.logOutBtn:
//			logOut();
//			break;
			
			
//			打分，
//		case R.id.scoreTheApp:
//
//			intent = new Intent(Intent.ACTION_VIEW);
//			intent.setData(Uri.parse("market://details?id=com.qubaopen"));
//
//			if (intent.resolveActivity(mainActivity.getPackageManager()) != null) {
//				startActivity(intent);
//			} else {
//
//				Toast.makeText(mainActivity, "您没有安装任何市场，无法打分",
//						Toast.LENGTH_SHORT).show();
//			}
//
//			v.setEnabled(true);
//			break;
		case R.id.layoutSuggestion:
			intent = new Intent(mainActivity, MoreSuggestionActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layoutShareAppComp:
			intent = new Intent(mainActivity, ShareActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;

		}
	}
}

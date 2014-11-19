package com.qubaopen.activity;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.datasynservice.UserService;
import com.qubaopen.logic.DoDataObject;
import com.qubaopen.logic.DoLogicObject;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class MainLoginActivity extends Activity implements View.OnClickListener {
	/** 电话账号 */
	/***/

	private MainLoginActivity _this;
	private Context context;

	private LinearLayout tencentLogin;
	private LinearLayout wxLogin;
	private LinearLayout sinaLogin;
	private LinearLayout phoneLogin;
	private TextView register;

	private UserService userService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_login);
		_this = this;
		userService = new UserService(this);
		initView();
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		context = this;
		tencentLogin = (LinearLayout) findViewById(R.id.layout_tencent_login);
		tencentLogin.setOnClickListener(this);
		wxLogin = (LinearLayout) findViewById(R.id.layout_wx_login);
		wxLogin.setOnClickListener(this);
		sinaLogin = (LinearLayout) findViewById(R.id.layout_sina_login);
		sinaLogin.setOnClickListener(this);
		phoneLogin = (LinearLayout) findViewById(R.id.layout_phone_login);
		phoneLogin.setOnClickListener(this);
		register = (TextView) findViewById(R.id.txt_regiset);
		register.setOnClickListener(this);
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
	public void onClick(View v) {
		Intent intent;
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.layout_tencent_login:
			qqLogin();
			v.setEnabled(true);
			break;
		case R.id.layout_wx_login:
			WechatLogin();
			v.setEnabled(true);
			break;
		case R.id.layout_sina_login:
			sinaWeiboLogin();
			v.setEnabled(true);
			break;
		case R.id.layout_phone_login:
			intent = new Intent(_this, PhoneLoginActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.txt_regiset:
			intent = new Intent(_this, RegistPhoneActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;

		}

	}

	private void sinaWeiboLogin() {

		Platform sinaWeibo = ShareSDK.getPlatform(
				MyApplication.getAppContext(), SinaWeibo.NAME);
		if (sinaWeibo.isValid()) {
			sinaWeibo.removeAccount();
		}
		sinaWeibo.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				Log.i("thirdLogin", "e");
			}

			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				String nickName = platform.getDb().getUserName(); // 等等来获取用户信息
				String id = platform.getDb().getUserId();
				String avatarUrl = platform.getDb().getUserIcon();

				try {
					JSONObject jsonObject = new JSONObject();

					jsonObject.put("token", id);
					jsonObject.put("nickName", nickName);
					jsonObject.put("icon", avatarUrl);
					jsonObject.put("type", 0);

					new LoadDataTask().execute(jsonObject);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Override
			public void onCancel(Platform platform, int action) {
			}
		});
		sinaWeibo.authorize();

	}

	private void qqLogin() {

		Platform qq = ShareSDK.getPlatform(MyApplication.getAppContext(),
				QQ.NAME);

		qq.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				Log.i("thirdLogin", "e");
			}

			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
			}

			@Override
			public void onCancel(Platform platform, int action) {
			}
		});
		qq.authorize();
	}

	private void WechatLogin() {

		Platform wechat = ShareSDK.getPlatform(MyApplication.getAppContext(),
				Wechat.NAME);
		wechat.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				Log.i("thirdLogin", "e");
			}

			@Override
			public void onComplete(Platform platform, int action,
					HashMap<String, Object> res) {
				// 通过platform.getDb().getUserId(
				String nickName = platform.getDb().getUserName(); // 等等来获取用户信息
				String token = platform.getDb().getToken();
				String id = platform.getDb().getUserId();
				String avatarUrl = platform.getDb().getUserIcon();
//				Log.i("ssssss", nickName + "," + token + "," + id + ","
//						+ avatarUrl);

			}

			@Override
			public void onCancel(Platform platform, int action) {
			
			}
		});

		wechat.authorize();
	}

	private class LoadDataTask extends AsyncTask<JSONObject, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(JSONObject... params) {
			return userService.thirdLogin(params[0]);

		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				if (result != null  && result.getInt("success") == 1) {
					Long userId = result.getLong("userId");
					CurrentUserHelper.saveCurrentUserId(userId);
					Intent intent = new Intent(_this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

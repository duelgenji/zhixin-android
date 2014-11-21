package com.qubaopen.utils;

import java.text.ParseException;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.activity.MainLoginActivity;
import com.qubaopen.activity.PhoneLoginActivity;
import com.qubaopen.common.RequestLogic;
import com.qubaopen.datasynservice.RegistService;
import com.qubaopen.settings.ErrHashMap;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.SettingValues;

/**
 * @author Administrator 服务器请求
 */
public class HttpClient {

	static private FinalHttp fh = new FinalHttp();
	static {
		fh.configTimeout(SettingValues.MAX_TIME_OUT * 1000);
	}

	public static void request(String requestUrl, JSONObject jsonParams,
			final RequestLogic rl) {

		AjaxParams params = new AjaxParams();
		if (jsonParams != null) {
			params.put("json", jsonParams.toString());
		}
		AjaxCallBack<String> ajaxCallBack = new AjaxCallBack<String>() {
			public void onSuccess(String response) {
				if (!StringUtils.isEmpty(response)) {
					JSONObject jbo;
					try {
						jbo = new JSONObject(response);
						if (jbo.getInt("success") == 1) {
							rl.whenSuccess(jbo);
						} else if (jbo.getInt("success") == 0) {
							rl.whenFail(jbo);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					Log.w("request fail", "nothing return");
				}
			}

			public void onFailure(Throwable t, int errorNo, String strMsg) {
				rl.whenRequestFail(String.valueOf(errorNo));
			}

			public void onLoading(long count, long current) {
				rl.onLoading(count, current);
			}

		};
		fh.post(requestUrl, params, ajaxCallBack);
	}

	public static JSONObject requestSync(String requestUrl,
			JSONObject jsonParams) throws JSONException {
		AjaxParams params = new AjaxParams();
		if (jsonParams != null) {
			params.put("json", jsonParams.toString());
		}
		Object resultObj = null;
		if (NetworkUtils.isNetworkAvailable(MyApplication.getAppContext())) {
			int count = 0;
			while (resultObj == null) {
				try {
					resultObj = fh.postSync(requestUrl, params);
					count++;
					if (resultObj == null) {
						if (count == SettingValues.MAX_REQUEST_TIME) {
							break;
						}
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (!(e instanceof java.io.InterruptedIOException)) {
						continue;
					} else {
						break;
					}

				}

			}
		}
		if (resultObj != null) {
			JSONObject result = new JSONObject(resultObj.toString());
			if (result.has("success")
					&& result.getString("success").equals("0")
					&& result.getString("message").equals("err000")) {
				if (new RegistService(MyApplication.getAppContext())
						.logOnAction()) {
					resultObj = fh.postSync(requestUrl, params);
				} else {
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent(MyApplication
									.getAppContext(), MainLoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);

						}
					});
				}

				return new JSONObject(resultObj.toString());
			} else if (result.has("success")
					&& result.getString("success").equals("0")
					&& !result.getString("message").equals("err000")) {
				Handler handler = new Handler(Looper.getMainLooper());
				final JSONObject _result = result;
				handler.post(new Runnable() {
					@Override
					public void run() {
						String context = null;
						try {
							String temp = _result.getString("message");
							if (temp.contains("err")) {
								context = ErrHashMap.getErrMessage(_result
										.getString("message"));
							} else if (temp != null && !temp.equals("")) {
								context = "";
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						context = context == null ? MyApplication
								.getAppContext().getString(
										R.string.toast_unknown) : context;
						if (!context.equals("")) {
							Toast.makeText(MyApplication.getAppContext(),
									context, 3).show();
						}
					}
				});
				return result;

			}

			else {
				return result;
			}
		} else {
			if (!NetworkUtils.isNetworkAvailable(MyApplication.getAppContext())) {
				Handler handler = new Handler(Looper.getMainLooper());

				handler.post(new Runnable() {
					@Override
					public void run() {

						Toast.makeText(MyApplication.getAppContext(),
								ErrHashMap.getErrMessage("errFFF"), 5).show();

					}
				});
			}
			JSONObject errObj = new JSONObject();
			errObj.put("success", "0");
			errObj.put("message", "errFFF");
			return errObj;
		}

	}

	public final static int TYPE_GET = 1;
	public final static int TYPE_POST_JSON = 2;
	public final static int TYPE_PUT_JSON = 3;
	public final static int TYPE_DELETE = 4;
	public final static int TYPE_POST_NORMAL = 5;
	public final static int TYPE_PUT_NORMAL = 6;
	public final static int TYPE_POST_FORM = 7;
	public final static int TYPE_PUT_FORM = 8;

	// **根据类型来发送请求
	public static JSONObject requestSync(String requestUrl, Object params,
			int type) throws JSONException {

		JSONObject jsonParams = null;
		AjaxParams ajaxParams = null;

		if (params instanceof JSONObject) {
			jsonParams = (JSONObject) params;
		} else if (params instanceof AjaxParams) {
			ajaxParams = (AjaxParams) params;
		}

		Object resultObj = null;
		if (NetworkUtils.isNetworkAvailable(MyApplication.getAppContext())) {
			int count = 0;
			while (resultObj == null) {
				try {
					switch (type) {
					case TYPE_GET:
						resultObj = fh.getSync(requestUrl);
						break;
					case TYPE_POST_JSON:
						resultObj = fh.postSyncJSON2(requestUrl, jsonParams);
						break;
					case TYPE_PUT_JSON:
						resultObj = fh.putSyncJSON2(requestUrl, jsonParams);
						break;
					case TYPE_DELETE:
						resultObj = fh.deleteSync(requestUrl);
						break;
					case TYPE_POST_NORMAL:
						resultObj = fh.postSync(requestUrl, ajaxParams);
						break;
					case TYPE_PUT_NORMAL:
						resultObj = fh.putSync(requestUrl, ajaxParams);
						break;
					case TYPE_POST_FORM:
						resultObj = fh.postSyncJSONForm(requestUrl, jsonParams);
						break;
					case TYPE_PUT_FORM:
						resultObj = fh.putSync(requestUrl);
						break;
					default:
						break;
					}

					count++;
					if (resultObj == null) {
						if (count == SettingValues.MAX_REQUEST_TIME) {
							break;
						}
						Thread.sleep(1000);
					}
				} catch (Exception e) {
					e.printStackTrace();
					if (!(e instanceof java.io.InterruptedIOException)) {
						continue;
					} else {
						break;
					}

				}

			}
		}
		if (resultObj != null) {
			JSONObject result = new JSONObject(resultObj.toString());
			if (result.has("success")
					&& result.getString("success").equals("0")
					&& result.getString("message").equals("err000")) {
				// 登陆接口 返回未登录的异常处理
				if (requestUrl.equals(SettingValues.URL_PREFIX
						+ MyApplication.getAppContext().getString(
								R.string.URL_USER_LOGON))
						|| requestUrl.equals(SettingValues.URL_PREFIX
								+ MyApplication.getAppContext().getString(
										R.string.URL_USER_THIRD_LOGON))) {

					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(MyApplication.getAppContext(),
									"服务器故障", 3).show();
						}
					});
					return null;
				}
				if (new RegistService(MyApplication.getAppContext())
						.logOnAction()) {
					switch (type) {
					case TYPE_GET:
						resultObj = fh.getSync(requestUrl);
						break;
					case TYPE_POST_JSON:
						resultObj = fh.postSyncJSON(requestUrl, jsonParams);
						break;
					case TYPE_PUT_JSON:
						resultObj = fh.putSyncJSON(requestUrl, jsonParams);
						break;
					case TYPE_DELETE:
						resultObj = fh.deleteSync(requestUrl);
						break;
					case TYPE_POST_NORMAL:
						resultObj = fh.postSync(requestUrl, ajaxParams);
						break;
					case TYPE_PUT_NORMAL:
						resultObj = fh.putSync(requestUrl, ajaxParams);
					default:
						break;
					}
				} else {
					Handler handler = new Handler(Looper.getMainLooper());
					handler.post(new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent(MyApplication
									.getAppContext(), MainLoginActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
									| Intent.FLAG_ACTIVITY_CLEAR_TASK);

							MyApplication.getAppContext().startActivity(intent);

						}
					});
				}

				return new JSONObject(resultObj.toString());
			} else if (result.has("success")
					&& result.getString("success").equals("0")
					&& !result.getString("message").equals("err000")) {
				Handler handler = new Handler(Looper.getMainLooper());
				final JSONObject _result = result;
				handler.post(new Runnable() {
					@Override
					public void run() {
						String context = null;
						try {
							String temp = _result.getString("message");
							if (temp.contains("err")) {
								context = ErrHashMap.getErrMessage(_result
										.getString("message"));
							} else if (temp != null && !temp.equals("")) {
								context = "";
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						context = context == null ? MyApplication
								.getAppContext().getString(
										R.string.toast_unknown) : context;
						if (!context.equals("")) {
							Toast.makeText(MyApplication.getAppContext(),
									context, 3).show();
						}
					}
				});
				return result;

			}

			else {
				return result;
			}
		} else {
			if (!NetworkUtils.isNetworkAvailable(MyApplication.getAppContext())) {
				Handler handler = new Handler(Looper.getMainLooper());

				handler.post(new Runnable() {
					@Override
					public void run() {

						Toast.makeText(MyApplication.getAppContext(),
								ErrHashMap.getErrMessage("errFFF"), 5).show();

					}
				});
			}
			JSONObject errObj = new JSONObject();
			errObj.put("success", "0");
			errObj.put("message", "errFFF");
			return errObj;
		}

	}

	public static JSONObject requestSyncForUnchangedParams(String requestUrl,
			AjaxParams params) throws JSONException {
		Object resultObj = fh.postSync(requestUrl, params);
		if (resultObj != null) {
			JSONObject result = new JSONObject(resultObj.toString());
			if (result.getString("success").equals("0")
					&& result.getString("message").equals("err000")) {
				new RegistService(MyApplication.getAppContext()).logOnAction();
				resultObj = fh.postSync(requestUrl, params);

				return new JSONObject(resultObj.toString());
			} else {
				return result;
			}
		} else {
			return null;
		}

	}

	public static FinalHttp getHttpClient() {
		return fh;
	}

	public static void clearHttpCache() {
		fh = new FinalHttp();
		fh.configTimeout(SettingValues.MAX_TIME_OUT * 1000);
	}

	public static void get(String url, AjaxCallBack<Object> callback) {

		FinalHttp fh = new FinalHttp();
		fh.get(url, callback);

	}

}

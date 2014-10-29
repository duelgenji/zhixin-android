package com.qubaopen.utils;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.qubaopen.R;
import com.qubaopen.settings.CurrentUserHelper;
import com.qubaopen.settings.SettingValues;

public class QbpPlatformAction implements PlatformActionListener {
	private int btnId;
	private int shareId;
	private Context context;
	private int keyId;

	private View btn;
	
	public QbpPlatformAction(int btnId, int shareId, int keyId, Context context) {
		this.btnId = btnId;
		this.context = context;
		this.shareId = shareId;
		this.keyId = keyId;
		this.btn = ((Activity)context).findViewById(btnId);
	}

	@Override
	public void onCancel(Platform platform, int action) {
//		Log.i("platform name cancel", platform.getName());
		this.btn.setEnabled(true);
	}

	@Override
	public void onComplete(Platform platform, int action,
			HashMap<String, Object> res) {
//		Log.i("platform name complete", platform.getName());
		try {
			callback();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onError(Platform platform, int action, Throwable t) {
		Log.e("platform name error", platform.getName());
		Log.e("error action", action + "");
		Log.e("error message", t.getMessage());
		t.printStackTrace();
		this.btn.setEnabled(true);
	}

	private void callback() throws JSONException {
		new ShareCallbackTask().execute();
	}

	private class ShareCallbackTask extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... para) {
			try {
				JSONObject params = new JSONObject();
				JSONObject result = null;
				switch (shareId) {
				case QBPShareFunction.SHARE_APP:

					switch (btnId) {
					case R.id.btnShareXlwb:
						params.put("sShare", 1);
						break;
					case R.id.btnShareTxwb:
						params.put("sShare", 2);

						break;
					case R.id.btnShareQqkj:
						params.put("sShare", 4);

						break;
					case R.id.btnShareWx:
						params.put("sShare", 5);

						break;
					case R.id.btnSharePyq:
						params.put("sShare", 3);
						break;
					case R.id.btnShareSMS:

						break;
					default:
						break;
					}
					result = HttpClient
							.requestSync(
									SettingValues.URL_PREFIX
											+ context
													.getString(R.string.share_callback_sharesoft),
									params);

					break;
				case QBPShareFunction.QU_CESHI:
				case QBPShareFunction.QU_DIAOYAN:
					if (shareId == QBPShareFunction.QU_CESHI) {
						params.put("iWjType", 1);
					} else {
						params.put("iWjType", 2);
					}
					params.put("iWjId", keyId);
					switch (btnId) {
					case R.id.btnShareXlwb:
						params.put("iShareType", 1);
						break;
					case R.id.btnShareTxwb:
						params.put("iShareType", 2);

						break;
					case R.id.btnShareQqkj:
						params.put("iShareType", 4);

						break;
					case R.id.btnShareWx:
						params.put("iShareType", 5);

						break;
					case R.id.btnSharePyq:
						params.put("iShareType", 3);
						break;
					case R.id.btnShareSMS:

						break;
					default:
						break;
					}
					result = HttpClient
							.requestSync(
									SettingValues.URL_PREFIX
											+ context
													.getString(R.string.share_callback_xqwj_and_dywj),
									params);

					break;
				case QBPShareFunction.QU_HUATI:
					params.put("iHtId", keyId);
					switch (btnId) {
					case R.id.btnShareXlwb:
						params.put("iShareType", 1);
						break;
					case R.id.btnShareTxwb:
						params.put("iShareType", 2);

						break;
					case R.id.btnShareQqkj:
						params.put("iShareType", 4);

						break;
					case R.id.btnShareWx:
						params.put("iShareType", 5);

						break;
					case R.id.btnSharePyq:
						params.put("iShareType", 3);
						break;
					case R.id.btnShareSMS:

						break;
					default:
						break;
					}
					result = HttpClient.requestSync(SettingValues.URL_PREFIX
							+ context.getString(R.string.share_callback_huati),
							params);

					break;
				case QBPShareFunction.QU_CHOUJIANG:
				case QBPShareFunction.QU_DUIJANG:
					String requestUrl = "";
					if (shareId == QBPShareFunction.QU_CHOUJIANG) {
						requestUrl = SettingValues.URL_PREFIX
								+ context
										.getString(R.string.share_callback_choujiang);

						params.put("lotteryNumber", keyId);
					} else {
						params.put("duijiangNumber", keyId);
						params.put("memberId",
								CurrentUserHelper.getCurrentUserId());
						requestUrl = SettingValues.URL_PREFIX
								+ context
										.getString(R.string.share_callback_duijiang);
					}

					switch (btnId) {
					case R.id.btnShareXlwb:
						params.put("share", 1);
						break;
					case R.id.btnShareTxwb:
						params.put("share", 2);

						break;
					case R.id.btnShareQqkj:
						params.put("share", 4);

						break;
					case R.id.btnShareWx:
						params.put("share", 5);

						break;
					case R.id.btnSharePyq:
						params.put("share", 3);
						break;
					case R.id.btnShareSMS:

						break;
					default:
						break;
					}
					result = HttpClient.requestSync(requestUrl, params);

					break;
				default:
					break;

				}

				return result;

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			if (result != null) {

				try {
					String creditStr = result.has("credit") ? result
							.getString("credit") : "0";
					final StringBuffer shareContentBuffer = new StringBuffer();
					shareContentBuffer.append(context
							.getString(R.string.share_success_str1));
					shareContentBuffer.append(creditStr);
					shareContentBuffer.append(context
							.getString(R.string.share_success_str2));
					Handler handler = new Handler(Looper.getMainLooper());

					handler.post(new Runnable() {
						@Override
						public void run() {

							Toast.makeText(context,
									shareContentBuffer.toString(),
									Toast.LENGTH_SHORT).show();

						}
					});
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			
			btn.setEnabled(true);
		}
	}

}

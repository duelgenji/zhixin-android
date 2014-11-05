package com.qubaopen.activity;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.qubaopen.R;
import com.qubaopen.customui.CardView;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.settings.PhoneHelper;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class MoodControlActivity extends Activity implements OnClickListener {

	private MoodControlActivity _this;

	private QubaopenProgressDialog progressDialog;
	private ImageButton btn_back;
	private TextView txtPageTitle;
	private ImageView moodImageView;
	private WebView moodWebView;
	private TextView moodTips, moodName, moodScore, moodContent;
	private ScrollView moodCardScrollView;
	private CardView moodCardView;
	private ImageView moodImaBg;

	private JSONObject hasMapData = new JSONObject();

	private int windowWidth = PhoneHelper.getPhoneWIDTH();
	private int imgHeight = (int) (windowWidth * 0.6);

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_control);
		_this = this;
		initView();
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		String requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_GET_MAP_MOOD_CONTROL);
		new LoadDataTask().execute(1, requestUrl, null, HttpClient.TYPE_GET);
	}

	private void initView() {

		progressDialog = new QubaopenProgressDialog(this);
		btn_back = (ImageButton) this.findViewById(R.id.backup_btn);
		btn_back.setOnClickListener(this);
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText("情绪管理");
		moodImaBg = (ImageView) this
				.findViewById(R.id.img_xinlimap_mood_no_content_bg);
		moodImaBg.setVisibility(View.GONE);
		moodImageView = (ImageView) this.findViewById(R.id.mood_img);
		moodWebView = (WebView) this.findViewById(R.id.mood_webView);
		moodTips = (TextView) this.findViewById(R.id.mood_lock_tips);
		moodName = (TextView) this.findViewById(R.id.mood_name);
		moodScore = (TextView) this.findViewById(R.id.mood_score);
		moodContent = (TextView) this.findViewById(R.id.mood_content);
		moodCardScrollView = (ScrollView) this.findViewById(R.id.layout_scroll);

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.interest_list_default_image)
				.showImageForEmptyUri(R.drawable.interest_list_default_image)
				.showImageOnFail(R.drawable.interest_list_default_image)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(20)).build();

		LinearLayout.LayoutParams imgLp = (LinearLayout.LayoutParams) moodImageView
				.getLayoutParams();
		imgLp.height = imgHeight;
		moodImageView.setLayoutParams(imgLp);

		LinearLayout.LayoutParams webLp = (LinearLayout.LayoutParams) moodWebView
				.getLayoutParams();
		webLp.height = (int) (1.6 * imgHeight);
		moodWebView.setLayoutParams(webLp);
	}

	// 向服务器请求数据
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					result.put("syncType", syncType);

					Log.i("moodControl", "情绪管理......" + result);
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result.getString("success").equals("1")) {
						if (result.has("data")) {
							if (result.getJSONArray("data").length() == 0) {
								moodImaBg.setVisibility(View.VISIBLE);
							} else {
								moodImaBg.setVisibility(View.GONE);
								hasMapData = result.getJSONArray("data")
										.getJSONObject(0);
								Log.i("moodControl", "有数据......" + hasMapData);
								setMoodView();
								if (progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
							}

						} else {
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							moodImaBg.setVisibility(View.VISIBLE);
						}

					} else {
						Toast.makeText(_this, "获取地图失败", Toast.LENGTH_SHORT)
								.show();
					}

					break;
				default:
					break;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void setMoodView() {
		try {
			if (hasMapData.getBoolean("lock")) {
				moodImageView.setVisibility(View.VISIBLE);
				moodWebView.setVisibility(View.GONE);
				moodName.setVisibility(View.GONE);
				moodScore.setVisibility(View.GONE);
				moodContent.setVisibility(View.GONE);

				moodImageView.setImageResource(R.drawable.xinlimap_lock);
				if (hasMapData.has("tips")
						&& StringUtils.isNotEmpty(hasMapData.getString("tips"))) {
					moodTips.setVisibility(View.VISIBLE);
					moodTips.setText(hasMapData.getString("tips"));
				}

			} else {
				moodTips.setVisibility(View.GONE);
				moodName.setVisibility(View.VISIBLE);
				moodScore.setVisibility(View.VISIBLE);
				moodContent.setVisibility(View.VISIBLE);
				if (hasMapData.has("picPath")
						&& StringUtils.isNotEmpty(hasMapData
								.getString("picPath"))) {
					moodImageView.setVisibility(View.VISIBLE);
					String imgUrl = SettingValues.URL_PREFIX
							+ hasMapData.getString("picPath");
					imageLoader.displayImage(imgUrl, moodImageView, options,
							animateFirstListener);
				} else {
					moodImageView.setVisibility(View.GONE);
				}
				if (hasMapData.has("graphicsType")
						&& StringUtils.isNotEmpty(hasMapData
								.getString("graphicsType"))) {
					moodWebView.setVisibility(View.VISIBLE);
					setWebviewContent();
				} else {
					moodWebView.setVisibility(View.GONE);
				}
				if (hasMapData.has("resultName")
						&& StringUtils.isNotEmpty(hasMapData
								.getString("resultName"))) {
					moodName.setText(hasMapData.getString("resultName"));
				} else {
					moodName.setVisibility(View.GONE);
				}
				if (hasMapData.has("resultScore")
						&& StringUtils.isNotEmpty(hasMapData
								.getString("resultScore"))) {
					moodScore.setText(hasMapData.getString("resultScore"));
				} else {
					moodScore.setVisibility(View.GONE);
				}
				if (hasMapData.has("resultContent")
						&& StringUtils.isNotEmpty(hasMapData
								.getString("resultContent"))) {
					moodContent.setText(hasMapData.getString("resultContent"));
				} else {
					moodContent.setVisibility(View.GONE);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void setWebviewContent() {
		WebSettings settings = moodWebView.getSettings();
		settings.setJavaScriptEnabled(true);

		moodWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(_this, "没网" + errorCode, Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
		moodWebView.loadUrl("http://115.28.176.74/hs.html"
				+ "?height=300&color=1&timestamp=" + new Date().getTime());
		// moodWebView.loadUrl("http://10.0.0.88/hs.html"+
		// "?height=300&timestamp=" + new Date().getTime());

		moodWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				JSONObject mapParamsJson = new JSONObject();
				try {

					mapParamsJson.put("title", hasMapData.getString("mapTitle"));
					mapParamsJson.put("mapMax", hasMapData.getInt("mapMax"));
					mapParamsJson.put("chartType",
							hasMapData.getString("graphicsType"));
					Log.i("moodcontrol",
							">>>>>>" + hasMapData.getJSONArray("chart"));
					mapParamsJson.put("chart", hasMapData.getJSONArray("chart"));

					String mapParams = mapParamsJson.toString();
					moodWebView.loadUrl("javascript:switchChart('" + mapParams
							+ "')");
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

		moodWebView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				}
				return false;
			}
		});
	}

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		case R.id.btn_mood_retest:
			break;
		case R.id.btn_mood_share:
			break;
		default:
			break;
		}

	}
}

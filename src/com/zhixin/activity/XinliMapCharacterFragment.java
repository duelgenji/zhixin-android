package com.zhixin.activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.utils.ConvertData4HighCharts;
import com.zhixin.utils.HttpClient;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.test.LoaderTestCase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//心理地图第一页
public class XinliMapCharacterFragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;

	private WebView webView;

	private RelativeLayout layoutPicture;

	private ImageButton btnSwitchChart;

	private int currentType = 0;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_character,
				container, false);

		btnSwitchChart = (ImageButton) view.findViewById(R.id.btn_switch_chart);
		btnSwitchChart.setOnClickListener(this);

		webView = (WebView) view.findViewById(R.id.webView);
		WebSettings settings = webView.getSettings();
		// settings.setUseWideViewPort(true);
		// settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// settings.setSupportZoom(true);
		// settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);
		// settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// settings.setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(mainActivity, "没网" + errorCode, 3).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); // 在当前的webview中跳转到新的url

				return true;
			}
		});
		webView.loadUrl("http://10.0.0.88/hc_android.html");
		

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);

				new LoadDataTask().execute(1);
			
			}
		});
		// webView.loadUrl("http://www.hcharts.cn/demo/index.php?p=10");

		webView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
				}
				return false;
			}
		});

		return view;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_switch_chart:
			switchChart();
			break;
		default:
			break;
		}
	}

	private void switchChart() {

		if (currentType < 3) {
			webView.loadUrl("javascript:switchChart(" + (++currentType) + ")");
		} else if (currentType == 3) {
			currentType = 4;
			webView.loadUrl("http://10.0.0.88/hs.html");
			webView.loadUrl("javascript:switchChart(0)");

		} else if (currentType == 4) {
			currentType = 0;
			webView.loadUrl("http://10.0.0.88/hc.html");
			webView.loadUrl("javascript:switchChart(" + currentType + ")");

		}
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
//					result = HttpClient.requestSync("http://10.0.0.88:8080/know-heart/mapStatistics/" +
//							"retrieveMapStatistics?type=SDS",
//							null, HttpClient.TYPE_GET);
					result.put("syncType", syncType);
					result.put("success", 1);
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
					if (result != null && result.getInt("success") == 1) {
//						String s = "{"
//								+ "\"categories\":[\"A艺术\",\"S社会\",\"E企业\",\"C常规\",\"R实际\",\"I调研\"],"
//								+ "\"data\":[" + (Math.random() * 100 + 1) + ", "
//								+ (Math.random() * 100 + 1) + ", "
//								+ (Math.random() * 100 + 1) + ", "
//								+ (Math.random() * 100 + 1) + ", "
//								+ (Math.random() * 100 + 1) + ", "
//								+ (Math.random() * 100 + 1) + "],"
//								+ "\"title\":\"霍兰德SDS职业兴趣测试\"}";
						
						String s ="{\"title\" : \"CRE\"," +
								"\"content\" : \"标价员，实验室工作者，广告管理员，自动打字机操作员，电动机装配工程师，缝纫机操作工\"," +
								"\"chart\" : [{\"name\" : \"C\",  \"value\" : 6}, {  \"name\" : \"R\",  \"value\" : 5}, {  \"name\" : \"I\",  \"value\" : 4}, {  \"name\" : \"E\",  \"value\" : 5}, {  \"name\" : \"S\",  \"value\" : 1} ]}";
						
						String r=(new ConvertData4HighCharts().getSpiderData(new JSONObject(s))).toString();
						
						
						webView.loadUrl("javascript:switchChart(2,'" + r + "')");
						
					} else {
						//Toast.makeText(mainActivity, "失败", 3).show();
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

}

package com.zhixin.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.zhixin.R;
import com.zhixin.customui.CardView;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.MapData;
import com.zhixin.logic.MapDataObject;
import com.zhixin.settings.PhoneHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

//情绪管理（心理地图第二页）
public class XinliMapMoodCardFragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;
	private boolean isInit; // 是否可以开始加载数据 
	private boolean mHasLoadedOnce = false; // 是否加载过数据
	
	/** loading */
	private QubaopenProgressDialog progressDialog;

	private ImageView moodImageView;
	private WebView moodWebView;
	private TextView moodTips, moodName, moodScore, moodContent;
	private ScrollView moodCardScrollView;
	private CardView moodCardView;
	private ImageView moodImaBg;

	private DisplayImageOptions options;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private int windowWidth = PhoneHelper.getPhoneWIDTH();
	private int imgHeight = (int) (windowWidth * 0.6);

	private MapData hsMapData = new MapData();

	private View rootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}
	@Override
	public void onResume() {
		super.onResume();
		// 判断当前fragment是否显示  
        if (getUserVisibleHint()) {  
            showData();  
        }  
	}
	 @Override  
	    public void setUserVisibleHint(boolean isVisibleToUser) {  
	        super.setUserVisibleHint(isVisibleToUser);  
	        // 每次切换fragment时调用的方法  
	        if (isVisibleToUser && !mHasLoadedOnce) {  
	            showData();  
	        }  
	    }  
	 /** 
	     * 初始化数据 
	     * @author wzh
	     * @date 2014-10-23
	     */  
	    private void showData() {  
	        if (isInit) {  
	            isInit = false;//加载数据完成  
	            // 加载各种数据  
	            if (!progressDialog.isShowing()) {
					progressDialog.show();
				}
				String requestUrl = SettingValues.URL_PREFIX
						+ getActivity().getString(R.string.URL_GET_MAP)
						+ "?typeId=2";
	
				new LoadDataTask()
						.execute(1, requestUrl, null, HttpClient.TYPE_GET);
	        } 
	        mHasLoadedOnce = true;
	    }  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isInit = true;
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_xinlimap_card_mood,
					container, false);

			initView();

//			if (!progressDialog.isShowing()) {
//				progressDialog.show();
//			}
//			String requestUrl = SettingValues.URL_PREFIX
//					+ getActivity().getString(R.string.URL_GET_MAP)
//					+ "?typeId=2";
//
//			new LoadDataTask()
//					.execute(1, requestUrl, null, HttpClient.TYPE_GET);

		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}

		return rootView;
	}

	private void initView() {
		progressDialog = new QubaopenProgressDialog(mainActivity);
		moodImaBg = (ImageView) rootView
				.findViewById(R.id.img_xinlimap_mood_no_content_bg);
		moodImaBg.setVisibility(View.GONE);
		moodImageView = (ImageView) rootView.findViewById(R.id.mood_img);
		moodWebView = (WebView) rootView.findViewById(R.id.mood_webView);
		moodTips = (TextView) rootView.findViewById(R.id.mood_lock_tips);
		moodName = (TextView) rootView.findViewById(R.id.mood_name);
		moodScore = (TextView) rootView.findViewById(R.id.mood_score);
		moodContent = (TextView) rootView.findViewById(R.id.mood_content);
		moodCardScrollView = (ScrollView) rootView
				.findViewById(R.id.layout_scroll);
		moodCardView = (CardView) rootView.findViewById(R.id.cardview);

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
		webLp.height = imgHeight;
		moodWebView.setLayoutParams(webLp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		default:
			break;
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
					result = HttpClient.requestSync(params[1].toString(), null,
							(Integer) params[3]);
					result.put("syncType", syncType);

					Log.i("地图信息第二页", result + "");
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
								List<MapData> data = new ArrayList<MapData>();
								List<MapData> dataList = new ArrayList<MapData>();
								data = MapDataObject.manageDataFromJson(result);
								Log.i("排序后的数据", data + "");
								for (int i = 0; i < data.size(); i++) {
									if (data.get(i).isMapDataIsSpecial()) {
										hsMapData = data.get(i);
										Log.i("top", "mood===" + hsMapData + "");
									} else {
										dataList.add(data.get(i));
									}
								}
								moodCardView.setMapList(dataList);
								setMoodView();
								Log.i("top", "mood---" + hsMapData + "");
							}

						} else {
							moodImaBg.setVisibility(View.VISIBLE);
						}
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					} else {
						Toast.makeText(mainActivity, "获取地图失败",
								Toast.LENGTH_SHORT).show();
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
		if (hsMapData.isMapDataIsLock()) {
			moodImageView.setVisibility(View.VISIBLE);
			moodWebView.setVisibility(View.GONE);
			moodTips.setVisibility(View.VISIBLE);
			moodName.setVisibility(View.GONE);
			moodScore.setVisibility(View.GONE);
			moodContent.setVisibility(View.GONE);

			moodImageView.setImageResource(R.drawable.xinlimap_lock);
			if (hsMapData.getMapDataTips() != null) {
				moodTips.setText(hsMapData.getMapDataTips());
			}

		} else {
			moodTips.setVisibility(View.GONE);
			moodName.setVisibility(View.VISIBLE);
			moodScore.setVisibility(View.VISIBLE);
			moodContent.setVisibility(View.VISIBLE);
			if (hsMapData.getMapDataPicPath() != null) {
				moodImageView.setVisibility(View.VISIBLE);
				String imgUrl = SettingValues.URL_PREFIX
						+ hsMapData.getMapDataPicPath();
				imageLoader.displayImage(imgUrl, moodImageView, options,
						animateFirstListener);
			} else {
				moodImageView.setVisibility(View.GONE);
			}
			if (hsMapData.getMapDataGraphicsType() != null) {
				moodWebView.setVisibility(View.VISIBLE);
				setWebviewContent();
			} else {
				moodWebView.setVisibility(View.GONE);
			}
			if (hsMapData.getMapDataName() != null) {
				moodName.setText(hsMapData.getMapDataName());
			} else {
				moodName.setText("暂无名称");
			}
			if (hsMapData.getMapDataResultScore() != null) {
				moodScore.setText(hsMapData.getMapDataResultScore());
			} else {
				moodScore.setText("暂无得分");
			}
			if (hsMapData.getMapDataContent() != null) {
				moodContent.setText(hsMapData.getMapDataContent());
			} else {
				moodContent.setText("暂无内容");
			}
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
				Toast.makeText(mainActivity, "没网" + errorCode,
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url); // 在当前的webview中跳转到新的url
				return true;
			}
		});
		moodWebView.loadUrl("http://115.28.176.74/hs.html"
				+ "?timestamp=" + new Date().getTime());
		// moodWebView.loadUrl("http://10.0.0.88/hs.html"+
		// "?height=300&timestamp=" + new Date().getTime());
		moodWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				JSONObject mapParamsJson = new JSONObject();
				try {

					mapParamsJson.put("title", hsMapData.getMapDataTitle());
					mapParamsJson.put("mapMax", hsMapData.getMapDataMax());
					mapParamsJson.put("chartType",
							hsMapData.getMapDataGraphicsType());
					Log.i("moodchart", ">>>>>>" + hsMapData.getMapDataChat());
					mapParamsJson.put("chart", hsMapData.getMapDataChat());

					String mapParams = mapParamsJson.toString();
					Log.i("moodchart", ">>>>>>" + mapParams);
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
}

package com.zhixin.activity;

import com.zhixin.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

//四种动物版，已经取消 2014-08-05
public class XinliMapCharacterFragment extends Fragment implements
		View.OnClickListener {
	
	

	private Activity mainActivity;

	private TextView txtPageTitle;

	private WebView webView;

	private Intent intent;

	private RelativeLayout layoutPicture;
	
	private ImageButton btnSwitchChart;
	
	private int currentType=0;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_character, container,
				false);
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		//txtPageTitle.setText(this.getString(R.string.title_me));
		
		btnSwitchChart= (ImageButton) view.findViewById(R.id.btn_switch_chart);
		btnSwitchChart.setOnClickListener(this);

		webView = (WebView) view.findViewById(R.id.webView);
		WebSettings settings = webView.getSettings(); 
//		settings.setUseWideViewPort(true); 
//		settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//		settings.setSupportZoom(true);
//		settings.setBuiltInZoomControls(true);
		settings.setJavaScriptEnabled(true);
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		settings.setLoadWithOverviewMode(true); 
		webView.setWebViewClient(new WebViewClient() {
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				Toast.makeText(mainActivity, "没网"+errorCode, 3).show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				view.loadUrl(url); // 在当前的webview中跳转到新的url

				return true;
			}
		});
		webView.loadUrl("http://10.0.0.88/hc_android.html");
	
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);	
				String  s ="{" +
                        "\"categories\":[\"A艺术\",\"S社会\",\"E企业\",\"C常规\",\"R实际\",\"I调研\"]," +
                        "\"data\":["+(Math.random()*100+1)+", "+(Math.random()*100+1)+", "+(Math.random()*100+1)+", "+(Math.random()*100+1)+", "+(Math.random()*100+1)+", "+(Math.random()*100+1)+"]," +
                        "\"title\":\"霍兰德SDS职业兴趣测试\"}";
       
				webView.loadUrl("javascript:switchChart(2,'" + s + "')");
			}
		});
		//webView.loadUrl("http://www.hcharts.cn/demo/index.php?p=10");

		webView.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
				if(event.getAction()==MotionEvent.ACTION_DOWN){
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
	
	private void switchChart(){
		
		if(currentType<3){
			webView.loadUrl("javascript:switchChart("+(++currentType)+")");
		}else if(currentType==3){
			currentType=4;
			webView.loadUrl("http://10.0.0.88/hs.html");
			webView.loadUrl("javascript:switchChart(0)");
			
		}else if(currentType==4){
			currentType=0;
			webView.loadUrl("http://10.0.0.88/hc.html");
			webView.loadUrl("javascript:switchChart("+currentType+")");
			
		}
	}


	

}

package com.zhixin.activity;

import net.tsz.afinal.http.AjaxParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhixin.R;
import com.zhixin.customui.CardGroup;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//心理地图第一页
public class XinliMapCard1Fragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;


	private RelativeLayout layoutPicture;

	private ImageButton btnSwitchChart;
	
	private CardGroup cardGroup;
	
	private Button btnAdd;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_xinlimap_card1,
				container, false);
		cardGroup=new CardGroup(view, mainActivity);
		cardGroup.addLayout();
		cardGroup.addLayout();
		cardGroup.addLayout();
		cardGroup.addLayout();
		cardGroup.addLayout();
		cardGroup.addLayout();
		
		btnAdd= (Button)view.findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(this);
		
		//new LoadDataTask().execute(1);

		return view;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			new LoadDataTask().execute(2);
//			Intent intent = new Intent(mainActivity, QuceshiContentActivity.class);
//			startActivity(intent);
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
					result = HttpClient.requestSync("http://10.0.0.88:8080/know-heart/mapStatistics/" +
							"retrieveMapStatistics?type=ALL",
							null, HttpClient.TYPE_GET);
					result.put("syncType", syncType);
					//result.put("success", 1);
					break;
				case 2:
					  AjaxParams obj = new AjaxParams();
						obj.put("selfId", "12");
						obj.put("questionJson", "[{\"questionId\": 270,\"contents\":[{\"id\":827}]}," + "{\"questionId\": 271,\"contents\":[{\"id\":828},{\"id\":829}]},"
								+ "{\"questionId\": 272,\"contents\":[{\"id\":830,\"cnt\":\"你好你\"}," + "{\"id\":831,\"cnt\":\"dddddddd\"}]},{\"questionId\": 273,\"contents\":[{\"id\":832,\"order\":1},"
								+ "{\"id\":833,\"order\":2},{\"id\":834,\"order\":3}]}," + "{\"questionId\": 274,\"contents\":[{\"id\":836}]}]");
						//obj.put("questionJson", "[{\"questionId\": 270,\"contents\":[{\"id\":827}]},{\"questionId\": 271,\"contents\":[{\"id\":828},{\"id\":829}]},{\"questionId\": 272,\"contents\":[{\"id\":830,\"cnt\":\"你好你\"},{\"id\":831,\"cnt\":\"dddddddd\"}]},{\"questionId\": 273,\"contents\":[{\"id\":832,\"order\":1},{\"id\":833,\"order\":2},{\"id\":834,\"order\":3}]},{\"questionId\": 274,\"contents\":[{\"id\":836}]}]");
					result = HttpClient.requestSync("http://10.0.0.11:8081/selfs/calculateSelfResult",
							obj, HttpClient.TYPE_POST_NORMAL);
					result.put("syncType", syncType);
					//result.put("success", 1);
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

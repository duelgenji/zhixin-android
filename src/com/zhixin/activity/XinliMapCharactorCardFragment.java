package com.zhixin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.customui.CardView;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.MapData;
import com.zhixin.logic.MapDataObject;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

//性格分析（心理地图第一页）

public class XinliMapCharactorCardFragment extends Fragment {

	private Activity mainActivity;
	private Context context;

	/** loading */
	private QubaopenProgressDialog progressDialog;

	private ScrollView scrollView;
	private ImageView characterImgBg;
	private CardView cardView;

	private View rootView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(
					R.layout.fragment_xinlimap_card_general, container, false);
			context = getActivity().getApplicationContext();
			progressDialog = new QubaopenProgressDialog(mainActivity);
			characterImgBg = (ImageView) rootView
					.findViewById(R.id.img_xinlimap_general_no_content_bg);
			characterImgBg.setVisibility(View.GONE);
			scrollView = (ScrollView) rootView.findViewById(R.id.layout_scroll);
			cardView = (CardView) rootView.findViewById(R.id.cardview);

			if (!progressDialog.isShowing()) {
				progressDialog.show();
			}
			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_GET_MAP) + "?typeId=1";

			new LoadDataTask()
					.execute(1, requestUrl, null, HttpClient.TYPE_GET);

		} else {
			ViewGroup parent = (ViewGroup) rootView.getParent();
			if (parent != null) {
				parent.removeView(rootView);
			}
		}
		return rootView;
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

					Log.i("地图信息第一页", result + "");
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
								characterImgBg.setVisibility(View.VISIBLE);
							} else {
								characterImgBg.setVisibility(View.GONE);
								try {
									List<MapData> data = new ArrayList<MapData>();
									data = MapDataObject
											.manageDataFromJson(result);
									cardView.setMapList(data);
									Log.i("mapdata", "charactor排序后的数据" + data);

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

						} else {
							characterImgBg.setVisibility(View.VISIBLE);
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

}

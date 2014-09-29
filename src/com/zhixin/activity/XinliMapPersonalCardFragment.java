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
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.customui.CardView;
import com.zhixin.domain.MapData;
import com.zhixin.logic.MapDataObject;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

//个人发展（心理地图第三页）
public class XinliMapPersonalCardFragment extends Fragment implements
		View.OnClickListener {

	private Activity mainActivity;
	private Context context;

	private View rootView;
	private CardView personCardView;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_xinlimap_card,
					container, false);
			personCardView = (CardView) rootView.findViewById(R.id.cardview);

			context = getActivity().getApplicationContext();

			String requestUrl = SettingValues.URL_PREFIX
					+ context.getString(R.string.URL_GET_MAP) + "?typeId=3";

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

					Log.i("地图信息第三页", result + "");

					result.put("syncType", syncType);
					// result.put("success", 1);
					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result != null) {
						if (result.getString("success").equals("1")) {
							List<MapData> data = new ArrayList<MapData>();
							data = MapDataObject.manageDataFromJson(result);
							Log.i("top", "cross排序后的数据" + data + "");
							personCardView.setMapList(data);
						} else {
							rootView.setBackgroundResource(R.drawable.xinlimap_no_map_bg);
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

	@Override
	public void onClick(View v) {

	}

}

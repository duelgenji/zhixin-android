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
import android.widget.ScrollView;
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.R.string;
import com.zhixin.customui.CardView;
import com.zhixin.domain.MapData;
import com.zhixin.logic.MapDataObject;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;

//性格分析（心理地图第一页）

public class XinliMapCharactorCardFragment extends Fragment {

	private Activity mainActivity;
	private Context context;
	private ScrollView scrollView;
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
			rootView = inflater.inflate(R.layout.fragment_xinlimap_character,
					container, false);
			context = getActivity().getApplicationContext();
			scrollView = (ScrollView) rootView.findViewById(R.id.layout_scroll);
			cardView = (CardView) rootView.findViewById(R.id.cardview);
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

		@SuppressWarnings("unused")
		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result != null) {
						if (result.getString("success").equals("1")) {
							try {
								List<MapData> data = new ArrayList<MapData>();
								data = MapDataObject.manageDataFromJson(result);
								cardView.setMapList(data);
//								 {"graphicsType":6,"mapMax":11,"recommendedValue":0,"resultScore":"","lock":false,"resultName":"ABCD","chart":[{"value":87,"name":"AB"},{"value":15,"name":"C"},{"value":36,"name":"D"}],"special":false,"mapTitle":"ABCD测试问卷结果图","resultContent":"","managementType":1},
//								 {"point":{"E":38.49,"N":44.32},"graphicsType":5,"level":3,"mapMax":100,"recommendedValue":1,"resultScore":"","lock":false,"resultName":"EPQ","chart":[],"special":false,"mapTitle":"EPQ测试结果图","resultContent":"","managementType":1},
//								 {"picPath":"\/pic\/1_20140901-151152.png","graphicsType":"","mapMax":10,"recommendedValue":10,"resultScore":69,"lock":false,"resultName":"弹性中等的弹簧","chart":[],"special":false,"mapTitle":"成人抗压力测试","resultContent":"       在压力处境中，你一般会采取积极的策略去应对，较少被困难完全击垮。面对压力时虽然情绪低落，但能作出一定的调整，基本上保持乐观的心态，不丧失信心和原有的信念。","managementType":1},
//								 {"picPath":"\/pic\/1_20140901-151152.png","graphicsType":1,"mapMax":10,"recommendedValue":10,"resultScore":16,"lock":false,"resultName":"D","chart":[{"value":10,"name":"S"},{"value":9,"name":"I"},{"value":16,"name":"D"},{"value":5,"name":"C"}],"special":false,"mapTitle":"DISC性格测试","resultContent":"高D型特质的人可以称为是“天生的领袖”。\\n【情感方面】：D型人一个坚定果敢的人，酷好变化，喜欢控制，干劲十足，独立自主，超级自信。可是，由于比较不会顾及别人的感受，所以显得粗鲁、霸道、没有耐心、穷追不舍、不会放松。D型人不习惯与别人进行感情上的交流，不会恭维人，不喜欢眼泪，匮乏同情心。\\n【工作方面】：D型人是一个务实和讲究效率的人，目标明确，眼光全面，组织力强，行动迅速，解决问题不过夜，果敢坚持到底，在反对声中成长。但是，因为过于强调结果， D型人往往容易忽视细节，处理问题不够细致。爱管人、喜欢支使他人的特点使得D型人能够带动团队进步，但也容易激起同事的反感。\\n【人际关系方面】： D型人喜欢为别人做主，虽然这样能够帮助别人做出选择，但也容易让人有强迫感。由于关注自己的目标， D型人在乎的是别人的可利用价值。喜欢控制别人，不会说对不起。\\n【与之交往】：专业、坦诚、准时，建立信任度。\\n【注意点】：这类型严重者会很独断 霸道，容易让别人感到压力，相处很累。\\n【自我规划】：\\n1、减轻对别人的压力 学会放松 要缓和。\\n2、尝试接受别人的号召和意见 尝试耐心和低调。\\n3、停止争吵 ，让别人也感觉到放松。\\n4、学习包容、学会道歉、学会坦然接受自己的错误、放开胸怀（当一个力量型的人学会承认错误，那么他便成功了）。\\n【描述性词语】：尽快取得结果、马上采取行动、勇于接受挑战、迅速做出决策、质疑现状、有效处理麻烦，克服困难，力争上游，强有力，追根究底，直率，主动，自信。\\n【代表人物】：曹操","managementType":1}
								                           
								Log.i("mapdata", "charactor排序后的数据" + data);

							} catch (JSONException e) {
								e.printStackTrace();
							}
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

}

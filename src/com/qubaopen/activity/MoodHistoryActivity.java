package com.qubaopen.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.calendar.CalendarCardPager;
import com.qubaopen.calendar.CalendarCardPager.OnMonthChangedListener;
import com.qubaopen.calendar.CardGridItem;
import com.qubaopen.calendar.CheckableLayout;
import com.qubaopen.calendar.OnCellItemClick;
import com.qubaopen.calendar.OnItemRender;
import com.qubaopen.domain.MoodInfo;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class MoodHistoryActivity extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private TextView title;
	private TextView btnLastMonth;
	private TextView btnNextMonth;
	private TextView titleMonth;
	private TextView titleYear;
	private int month;
	private int year;

	private List<MoodInfo> moodInfos;

	private Calendar selectedDate;

	private CalendarCardPager calendarCardPager;
	private String requestUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_history);
		 requestUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_GET_MOOD_LIST);
		 Calendar calendar = Calendar.getInstance();
			month = calendar.get(Calendar.MONTH);
			year = calendar.get(Calendar.YEAR);
			LoadDataByMonth(month);
		initView();
	}

	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.backup_btn);
		btnBack.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title_of_the_page);
		title.setText("心情记录");
		btnLastMonth = (TextView) findViewById(R.id.btn_last_month);
		btnLastMonth.setOnClickListener(this);
		titleMonth = (TextView) findViewById(R.id.layout_mood_histor_calendar_title_month);
		titleYear = (TextView) findViewById(R.id.layout_mood_histor_calendar_title_year);
		setTitleByMonth(month, year);
		btnNextMonth = (TextView) findViewById(R.id.btn_next_month);
		btnNextMonth.setOnClickListener(this);
		calendarCardPager = (CalendarCardPager) findViewById(R.id.calendar_view);
		calendarCardPager.setOnCellItemClick(new OnCellItemClick() {

			@Override
			public void onClickOutSide() {

			}

			@Override
			public void onCellClick(View v, CardGridItem item) {
				selectedDate = item.getDate();// 选中的日期
				selectedDate.get(Calendar.DAY_OF_MONTH);
				Log.i("MoodHistoryActivity", "selectedDate...year..."
						+ selectedDate.get(Calendar.YEAR) + "...month..."
						+ selectedDate.get(Calendar.MONTH) + "...day..."
						+ selectedDate.get(Calendar.DAY_OF_MONTH));
			}
		});
		calendarCardPager.setOnItemRender(new OnItemRender() {

			@Override
			public void onRender(CheckableLayout v, CardGridItem item) {
				if (item.isEnabled()) {// 日期可选时，设置背景
					// v.setBackground(background);
				} else {// 日期不可选时，设置背景
					// v.setBackground(background);
				}
			}
		});
		calendarCardPager
				.setOnMonthChangedListener(new OnMonthChangedListener() {

					@Override
					public void monthChanged(Calendar calendar) {
						month = calendar.get(Calendar.MONTH);// 当前界面的月份
						year = calendar.get(Calendar.YEAR);
						Log.i("MoodHistoryActivity", "month......" + month
								+ "year......" + year);
						setTitleByMonth(month, year);
						LoadDataByMonth(month);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		case R.id.btn_last_month:
			calendarCardPager.arrowScroll(1);// 布局中包含Editext用17
			break;
		case R.id.btn_next_month:
			calendarCardPager.arrowScroll(2);// 布局中包含Editext用66
			break;
		default:
			break;
		}
	}
	private void LoadDataByMonth(int month){
		try {
			JSONObject params = new JSONObject();
			Log.i("MoodHistoryActivity", "month......" + month);
			params.put("month", month + 1);
			new LoadMoodHistoryTask().execute(1, requestUrl, params,
					HttpClient.TYPE_POST_FORM);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private class LoadMoodHistoryTask extends
			AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType = (Integer) params[0];
			try {
				switch (syncType) {
				case 1:
					result = HttpClient.requestSync(params[1].toString(),
							params[2], (Integer) params[3]);
					result.put("syncType", syncType);
					Log.i("MoodHistoryActivity", "心情记录：......" + result);

					break;
				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return result;
		}

		@SuppressLint("SimpleDateFormat")
		@Override
		protected void onPostExecute(JSONObject result) {
			try {
				Integer syncType = result.getInt("syncType");
				switch (syncType) {
				case 1:
					if (result.has("success")
							&& result.getString("success").equals("1")) {
						JSONArray data = new JSONArray();
						data = result.getJSONArray("moodList");
						moodInfos = new ArrayList<MoodInfo>();
						for (int i = 0; i < data.length(); i++) {
							MoodInfo moodInfo = new MoodInfo();
							int moodId;
							moodId = data.getJSONObject(i).getInt("mood");
							String moodDate;
							moodDate = data.getJSONObject(i).getString("date");
							String moodMessage;
							moodMessage = data.getJSONObject(i).getString(
									"message");
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									"yyyy-MM-dd");
							Date date = dateFormat.parse(moodDate);
							moodInfo.setMoodId(moodId);
							moodInfo.setMoodDate(date);
							moodInfo.setMoodMessage(moodMessage);
							moodInfos.add(moodInfo);
						}
					} else {
						showToast("获取心情记录失败！");
					}
					break;

				default:
					break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

	}

	private void setTitleByMonth(int month, int year){
		titleYear.setText(year + "");
		if (month == 0) {
			titleMonth.setText("一月");
		}else if (month == 1) {
			titleMonth.setText("二月");
		}else if (month == 2) {
			titleMonth.setText("三月");
		}else if (month == 3) {
			titleMonth.setText("四月");
		}else if (month == 4) {
			titleMonth.setText("五月");
		}else if (month == 5) {
			titleMonth.setText("六月");
		}else if (month == 6) {
			titleMonth.setText("七月");
		}else if (month == 7) {
			titleMonth.setText("八月");
		}else if (month == 8) {
			titleMonth.setText("九月");
		}else if (month == 9) {
			titleMonth.setText("十月");
		}else if (month == 10) {
			titleMonth.setText("十一月");
		}else if (month == 11) {
			titleMonth.setText("十二月");
		}
	}
	private void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
}

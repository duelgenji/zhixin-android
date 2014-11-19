package com.qubaopen.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.calendar.CalendarCardPager;
import com.qubaopen.calendar.CalendarCardPager.OnMonthChangedListener;
import com.qubaopen.calendar.CardGridItem;
import com.qubaopen.calendar.CheckableLayout;
import com.qubaopen.calendar.OnCellItemClick;
import com.qubaopen.calendar.OnItemRender;
import com.qubaopen.daos.UserMoodInfoDao;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.domain.UserMoodInfo;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.HttpClient;

public class MoodHistoryActivity extends Activity implements OnClickListener {
	private ImageButton btnBack;
	private TextView title;
	private TextView btnLastMonth;
	private TextView btnNextMonth;
	private TextView mood;
	private ImageView moodImg;
	private TextView moodMessage;

	/** loading */
	private QubaopenProgressDialog progressDialog;

	private TextView titleMonth;
	private TextView titleYear;
	private int month;
	private int year;

	private UserMoodInfoDao userMoodInfoDao;
	private UserMoodInfo userMoodInfo;
	private Calendar selectedDate;

	private CalendarCardPager calendarCardPager;
	private String requestUrl;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				calendarCardPager.notifyChanged();
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_history);

		userMoodInfoDao = new UserMoodInfoDao();
		userMoodInfo = new UserMoodInfo();
		requestUrl = SettingValues.URL_PREFIX
				+ getString(R.string.URL_GET_MOOD_LIST);
		Calendar calendar = Calendar.getInstance();
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
		progressDialog = new QubaopenProgressDialog(this);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		initView();
		calendarCardPager.requestLayout();
		LoadDataByMonth(month);
	}

	@SuppressLint("SimpleDateFormat")
	private void initView() {
		btnBack = (ImageButton) findViewById(R.id.backup_btn);
		btnBack.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title_of_the_page);
		title.setText("心情日历");
		btnLastMonth = (TextView) findViewById(R.id.btn_last_month);
		btnLastMonth.setOnClickListener(this);
		titleMonth = (TextView) findViewById(R.id.layout_mood_histor_calendar_title_month);
		titleYear = (TextView) findViewById(R.id.layout_mood_histor_calendar_title_year);
		setTitleByMonth(month, year);
		btnNextMonth = (TextView) findViewById(R.id.btn_next_month);
		btnNextMonth.setOnClickListener(this);
		mood = (TextView) findViewById(R.id.tv_mood_history_selected_day_mood);
		moodImg = (ImageView) findViewById(R.id.img_mood_history_selected_day_mood);
		moodMessage = (TextView) findViewById(R.id.mood_history_selected_message);
		calendarCardPager = (CalendarCardPager) findViewById(R.id.calendar_view);

		calendarCardPager.setOnCellItemClick(new OnCellItemClick() {

			@Override
			public void onClickOutSide() {

			}

			@Override
			public void onCellClick(View v, CardGridItem item) {
				selectedDate = item.getDate();// 选中的日期
				String date = (new SimpleDateFormat("yyyy-MM-dd"))
						.format(selectedDate.getTime());

				Log.i("MoodHistoryActivity", "selectedDate......" + date);
				userMoodInfo = userMoodInfoDao.getUserMoodInfo(date + "");
				Log.i("MoodHistoryActivity", "userMoodInfo......"
						+ userMoodInfo);

				if (userMoodInfo != null) {
					moodImg.setVisibility(View.VISIBLE);
					moodMessage.setVisibility(View.VISIBLE);
					if (userMoodInfo.getMoodId() == 1) {
						mood.setText("心情1");
						moodImg.setImageResource(R.drawable.today_mood_face_1);
					} else if (userMoodInfo.getMoodId() == 2) {
						mood.setText("心情2");
						moodImg.setImageResource(R.drawable.today_mood_face_2);
					} else if (userMoodInfo.getMoodId() == 3) {
						mood.setText("心情3");
						moodImg.setImageResource(R.drawable.today_mood_face_3);
					} else if (userMoodInfo.getMoodId() == 4) {
						mood.setText("心情4");
						moodImg.setImageResource(R.drawable.today_mood_face_4);
					} else if (userMoodInfo.getMoodId() == 5) {
						mood.setText("心情5");
						moodImg.setImageResource(R.drawable.today_mood_face_5);
					} else if (userMoodInfo.getMoodId() == 6) {
						mood.setText("心情6");
						moodImg.setImageResource(R.drawable.today_mood_face_6);
					} else if (userMoodInfo.getMoodId() == 7) {
						mood.setText("心情7");
						// moodImg.setImageResource(resId);
					} else if (userMoodInfo.getMoodId() == 8) {
						mood.setText("心情8");
						// moodImg.setImageResource(resId);
					}
					if (StringUtils.isNotEmpty(userMoodInfo.getMoodMessage())) {
						moodMessage.setText(userMoodInfo.getMoodMessage());
					}
				} else {
					mood.setText("无");
					moodImg.setVisibility(View.GONE);
					moodMessage.setVisibility(View.GONE);
				}
			}
		});
		calendarCardPager.setOnItemRender(new OnItemRender() {

			@Override
			public void onRender(CheckableLayout v, CardGridItem item) {
				String date = (new SimpleDateFormat("yyyy-MM-dd")).format(item
						.getDate().getTime());
				userMoodInfo = userMoodInfoDao.getUserMoodInfo(date);
				String currentDate = (new SimpleDateFormat("yyyy-MM-dd"))
						.format(new Date());
				v.setBackgroundDrawable(null);
				if (date.equals(currentDate)) {
					Log.i("MoodHistoryActivity", "date......" + date
							+ "currentDate......" + currentDate);
					 v.setBackgroundResource(R.drawable.card_item_bg_currrent_date);
				} else {
					v.setBackgroundResource(R.drawable.card_item_bg);
					if (userMoodInfo != null) {
						if (userMoodInfo.getMoodId() == 1) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_1);
						} else if (userMoodInfo.getMoodId() == 2) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_2);
						} else if (userMoodInfo.getMoodId() == 3) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_3);
						} else if (userMoodInfo.getMoodId() == 4) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_4);
						} else if (userMoodInfo.getMoodId() == 5) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_5);
						} else if (userMoodInfo.getMoodId() == 6) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_6);
						} else if (userMoodInfo.getMoodId() == 7) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_7);
						} else if (userMoodInfo.getMoodId() == 8) {
							v.setBackgroundResource(R.drawable.card_item_bg_mood_8);
						}
					}

				}

			}
		});
		calendarCardPager
				.setOnMonthChangedListener(new OnMonthChangedListener() {

					@Override
					public void monthChanged(Calendar calendar) {
						month = calendar.get(Calendar.MONTH);// 当前界面的月份
						year = calendar.get(Calendar.YEAR);
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
			calendarCardPager
					.setCurrentItem(calendarCardPager.getCurrentItem() - 1);
			break;
		case R.id.btn_next_month:
			calendarCardPager
					.setCurrentItem(calendarCardPager.getCurrentItem() + 1);
			break;
		default:
			break;
		}
	}

	private void LoadDataByMonth(int month) {
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

						userMoodInfoDao.saveUserMoodInfo(result);
						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);

					} else {
						showToast("获取心情记录失败！");
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

	private void setTitleByMonth(int month, int year) {
		titleYear.setText(year + "");
		if (month == 0) {
			titleMonth.setText("一月");
		} else if (month == 1) {
			titleMonth.setText("二月");
		} else if (month == 2) {
			titleMonth.setText("三月");
		} else if (month == 3) {
			titleMonth.setText("四月");
		} else if (month == 4) {
			titleMonth.setText("五月");
		} else if (month == 5) {
			titleMonth.setText("六月");
		} else if (month == 6) {
			titleMonth.setText("七月");
		} else if (month == 7) {
			titleMonth.setText("八月");
		} else if (month == 8) {
			titleMonth.setText("九月");
		} else if (month == 9) {
			titleMonth.setText("十月");
		} else if (month == 10) {
			titleMonth.setText("十一月");
		} else if (month == 11) {
			titleMonth.setText("十二月");
		}
	}

	private void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
}

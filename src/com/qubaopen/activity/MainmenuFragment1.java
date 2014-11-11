package com.qubaopen.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.datasynservice.MainMenuService;
import com.qubaopen.datasynservice.UserService;
import com.qubaopen.settings.MyApplication;
import com.qubaopen.settings.PhoneHelper;
import com.qubaopen.utils.AnimationUtils;
import com.qubaopen.utils.MatcherUtil;

public class MainmenuFragment1 extends Fragment implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	/***/
	/***/
	// private FragmentManager fm;
	private TextView txtPageTitle;

	private RelativeLayout layoutPickMood;

	private RelativeLayout layoutMoodSwitch;
	/** 主菜单service */
	private MainMenuService mainMenuService;

	/** 动画 */
	AnimationDrawable progressAnimation;
	/***/
	private Activity mainActivity;

	private String designation;
	private Double percent;
	private boolean isChecked;
	private Double deduction;

	private LinearLayout characterPercentLayout;
	private TextView characterPercent;
	private RelativeLayout moodControl;
	private LinearLayout character;
	private LinearLayout communication;
	private LinearLayout career;
	private LinearLayout health;
	private LinearLayout interest;
	private LinearLayout recycle;

	private ImageView imgMoodHistory;
	private ImageView imgMoodArrow;
	private ImageView imgMoodPanel;
	private ImageView imgMoodBackground;
	
	private ImageView imgLastScoreBackground;
	private ImageView imgLastScorePointer;
	private TextView  txtLastScore;
	private TextView  txtCurrentScore;

	private RelativeLayout layoutMoodFace1, layoutMoodFace2, layoutMoodFace3,
			layoutMoodFace4, layoutMoodFace5, layoutMoodFace6;

	private UserService userService;

	private boolean isMoodOpen = false;
	private boolean isMoodFirst = true;

	private int moveDistance = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity_new, container,
				false);

		initView(view);

		return view;
	}

	private void initView(View view) {
		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText("知心");

		characterPercentLayout = (LinearLayout) view
				.findViewById(R.id.layout_character_analysis_num);
		characterPercentLayout.setOnClickListener(this);
		characterPercent = (TextView) view
				.findViewById(R.id.character_analysis_num);
		moodControl = (RelativeLayout) view
				.findViewById(R.id.layout_mood_control);
		moodControl.setOnClickListener(this);

		character = (LinearLayout) view.findViewById(R.id.layout_character);
		character.setOnClickListener(this);
		communication = (LinearLayout) view
				.findViewById(R.id.layout_communication);
		communication.setOnClickListener(this);
		career = (LinearLayout) view.findViewById(R.id.layout_career);
		career.setOnClickListener(this);
		health = (LinearLayout) view.findViewById(R.id.layout_health);
		health.setOnClickListener(this);
		interest = (LinearLayout) view.findViewById(R.id.layout_interest);
		interest.setOnClickListener(this);
		recycle = (LinearLayout) view.findViewById(R.id.layout_recycle);
		recycle.setOnClickListener(this);

		layoutPickMood = (RelativeLayout) view
				.findViewById(R.id.layout_pick_mood);
		layoutMoodSwitch = (RelativeLayout) view
				.findViewById(R.id.layout_mood_switch);
		imgMoodHistory = (ImageView) view.findViewById(R.id.img_mood_history);
		imgMoodHistory.setOnClickListener(this);
		imgMoodBackground = (ImageView) view
				.findViewById(R.id.img_mood_background);
		imgMoodBackground.setOnClickListener(this);
		imgMoodPanel = (ImageView) view
				.findViewById(R.id.img_mood_switch_panel);
		imgMoodPanel.setOnClickListener(this);
		imgMoodArrow = (ImageView) view
				.findViewById(R.id.img_mood_switch_arrow);
		AnimationUtils.startImgBackGround(imgMoodArrow);
		
		imgLastScoreBackground= (ImageView) view
				.findViewById(R.id.img_last_score);
		imgLastScorePointer= (ImageView) view
				.findViewById(R.id.img_last_score_pointer);
		txtLastScore= (TextView) view
				.findViewById(R.id.last_score);
		txtLastScore.setOnClickListener(this);
		txtLastScore= (TextView) view
				.findViewById(R.id.current_score);
		

		layoutMoodFace1 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_1);
		layoutMoodFace2 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_2);
		layoutMoodFace3 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_3);
		layoutMoodFace4 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_4);
		layoutMoodFace5 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_5);
		layoutMoodFace6 = (RelativeLayout) view
				.findViewById(R.id.layout_mood_face_6);
		layoutMoodFace1.setOnClickListener(this);
		layoutMoodFace2.setOnClickListener(this);
		layoutMoodFace3.setOnClickListener(this);
		layoutMoodFace4.setOnClickListener(this);
		layoutMoodFace5.setOnClickListener(this);
		layoutMoodFace6.setOnClickListener(this);

		userService = new UserService(mainActivity);
		calcDistance();
		mainMenuService = new MainMenuService(MyApplication.getAppContext());

		new LoadDataTask().execute(0, 0);

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mainActivity = activity;

	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		Intent intent;
		switch (v.getId()) {
		case R.id.layout_character_analysis_num:
			intent = new Intent(mainActivity, AnalysisCharacterActivity.class);
			intent.putExtra(
					AnalysisCharacterActivity.USER_CHARACTER_DESIGNATION,
					designation);
			intent.putExtra(AnalysisCharacterActivity.USER_CHARACTER_PERCENT,
					percent);
			intent.putExtra(AnalysisCharacterActivity.USER_CHARACTER_ISCHECKED,
					isChecked);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_control:
			startMoodControlActivity();
			v.setEnabled(true);
			break;
		case R.id.layout_character:
			startSelfListActivity(1);
			v.setEnabled(true);
			break;
		case R.id.layout_communication:
			startSelfListActivity(2);
			v.setEnabled(true);
			break;
		case R.id.layout_career:
			startSelfListActivity(3);
			v.setEnabled(true);
			break;
		case R.id.layout_health:
			startSelfListActivity(4);
			v.setEnabled(true);
			break;
		case R.id.layout_interest:
			intent = new Intent(mainActivity, InterestListActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.layout_recycle:
			break;
		case R.id.last_score:
				dashBoardAnim();
				v.setEnabled(true);
			break;
		case R.id.img_mood_switch_panel:

			if (isMoodFirst) {
				isMoodFirst = false;
				int h = (int) (PhoneHelper.getPhoneHeight() * 0.036875);
				imgMoodPanel
						.setBackgroundResource(R.drawable.today_mood_panel_default);
				AnimationUtils.performAnimateMarginBottom(layoutMoodSwitch, 0,
						-h, 333);
			}

			if (!isMoodOpen) {
				isMoodOpen = true;

				imgMoodArrow
						.setBackgroundResource(R.drawable.today_mood_arrow_down);
				AnimationUtils.startImgBackGround(imgMoodArrow);
				AnimationUtils.performAnimateMarginTop(layoutPickMood, 1500,
						moveDistance, 500);
				v.setEnabled(true);
			} else {
				panelClose();
				v.setEnabled(true);

			}
			break;
		case R.id.img_mood_history:
//			panelClose();
			intent = new Intent(mainActivity,MoodHistoryActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		case R.id.img_mood_background:
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_1:
			new LoadDataTask().execute(1, 1);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_2:
			new LoadDataTask().execute(1, 2);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_3:
			new LoadDataTask().execute(1, 3);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_4:
			new LoadDataTask().execute(1, 4);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_5:
			new LoadDataTask().execute(1, 5);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_6:
			new LoadDataTask().execute(1, 6);
			v.setEnabled(true);
			break;
		default:
			v.setEnabled(true);
			break;
		}
	}

	private void startMoodControlActivity() {
		Intent intent = new Intent(mainActivity, MoodControlActivity.class);
		startActivity(intent);
	}

	private void startSelfListActivity(int type) {
		Intent intent = new Intent(mainActivity, SelfListActivity.class);
		intent.putExtra(SelfListActivity.SELF_LIST_TYPE, type);
		startActivity(intent);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}

	private void showToast(String content) {
		Toast.makeText(mainActivity, content, Toast.LENGTH_SHORT).show();
	}
	
	
	//心情指数 仪表盘 动画
	private void dashBoardAnim(){
//		Animation animation4 = new RotateAnimation(0, 270, Animation.RELATIVE_TO_SELF,
//				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		LinearInterpolator lin = new LinearInterpolator();
//		animation4.setInterpolator(lin);
//		animation4.setDuration(666);
//		animation4.setFillEnabled(true);
//		animation4.setFillAfter(true);
//		imgLastScorePointer.startAnimation(animation4);
		AnimationUtils.performAnimateRoration(imgLastScorePointer, 90, 360, 666,txtLastScore);
	}

	// 计算圆盘距离 最笨方法
	private void calcDistance() {

		int h = PhoneHelper.getPhoneHeight();

		int topLabel = (int) (PhoneHelper.getPhoneHeight() * 0.08);
		switch (h) {
		case 800:
			moveDistance = 270 - topLabel;
			break;

		case 1280:
			moveDistance = 460 - topLabel;
			break;

		case 1920:
			moveDistance = 810 - topLabel;
			break;
		default:
			moveDistance = (int) (PhoneHelper.getPhoneHeight() * 0.3)
					- topLabel;
			break;
		}
	}

	// 圆盘 下滑
	private void panelClose() {
		isMoodOpen = false;

		imgMoodArrow.setBackgroundResource(R.drawable.today_mood_arrow_up);
		// AnimationUtils.startImgBackGround(imgMoodArrow);
		AnimationUtils.performAnimateMarginTop(layoutPickMood, moveDistance,
				1500, 500);
		imgMoodPanel.setEnabled(true);
	}

	// 圆盘开关 下移
	private void swtichPanelDown() {
		if (isMoodFirst) {
			isMoodFirst = false;
			int h = (int) (PhoneHelper.getPhoneHeight() * 0.036875);
			imgMoodPanel
					.setBackgroundResource(R.drawable.today_mood_panel_default);
			AnimationUtils.performAnimateMarginBottom(layoutMoodSwitch, 0, -h,
					333);
			imgMoodArrow.setBackgroundResource(R.drawable.today_mood_arrow_up);
		}
	}

	// 用户心情 获取以及设置
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = new JSONObject();
			Integer syncType = (Integer) params[0];
			int type = (Integer) params[1];
			try {
				switch (syncType) {
				case 0:
					// getMood 获取用户心情
					result = userService.getMood();

					result.put("syncType", syncType);
					break;
				case 1:
					// setMood 设置用户
					result = userService.setMood(type);
					result.put("syncType", syncType);
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
				Log.i("getIndexInfo", "mood+others..." + result);
				Integer syncType = result.getInt("syncType");
				if (result != null && result.getInt("success") == 1) {

					switch (syncType) {
					case 0:
						percent = result.getDouble("resolution");
						deduction = result.getDouble("deduction");
						designation = result.getString("userSelfTitle");
						Log.i("percent", "percent..." + percent
								+ "...deduction..." + deduction
								+ "...designation..." + designation);
						characterPercent.setText(percent + "%");
						String lastTime = result.getString("lastTime");
						if (MatcherUtil.isToday(lastTime)) {
							swtichPanelDown();
						} else {
							imgMoodArrow
									.setBackgroundResource(R.drawable.today_mood_arrow_up);
							AnimationUtils.startImgBackGround(imgMoodArrow);
						}
						break;
					case 1:
						showToast(mainActivity
								.getString(R.string.toast_set_mood_success));
						panelClose();
						break;
					default:
						break;
					}
				} else {
					if (syncType == 0) {
						imgMoodArrow
								.setBackgroundResource(R.drawable.today_mood_arrow_up);
						AnimationUtils.startImgBackGround(imgMoodArrow);
					}

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

}

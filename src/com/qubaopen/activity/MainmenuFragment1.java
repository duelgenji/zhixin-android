package com.qubaopen.activity;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qubaopen.R;
import com.qubaopen.activity.MainActivity.MyOnTouchListener;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.datasynservice.MainMenuService;
import com.qubaopen.datasynservice.UserService;
import com.qubaopen.dialog.SetMoodDialog;
import com.qubaopen.domain.UserInfo;
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
	private TextView txtCurrentScore;

	private String designation;
	private Double percent;
	private boolean isJoined;
	private Double deduction;
	private int setRetotion;

	private RelativeLayout layoutMoodFace1, layoutMoodFace2, layoutMoodFace3,
			layoutMoodFace4, layoutMoodFace5, layoutMoodFace6;
	private SetMoodDialog setMoodDialog;
	private boolean isMoodOpen = false;
	private boolean isMoodFirst = true;

	private UserService userService;
	private boolean isDone = false;

	private int moveDistance = 0;

	private UserInfo userInfo;
	private UserInfoDao userInfoDao;
	
	private LinearLayout layouMainragment;

	private MyOnTouchListener myOnTouchListener = new MyOnTouchListener() {

		@Override
		public void onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				if (isMoodOpen) {
					panelClose();
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity_new, container,
				false);

		initView(view);

		return view;
	}

	
	@SuppressLint("ClickableViewAccessibility")
	private void initView(View view) {
		((MainActivity) this.getActivity())
				.registMyOnToucherListener(myOnTouchListener);
		
		userInfoDao = new UserInfoDao();
		userInfo = userInfoDao.getCurrentUser();

		txtPageTitle = (TextView) view.findViewById(R.id.title_of_the_page);
		txtPageTitle.setText("知心");

		layouMainragment= (LinearLayout)view.findViewById(R.id.layout_main_fragment);
		//layouMainragment.setOnClickListener(this);
		//layouMainragment.setEnabled(false);
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

		imgLastScoreBackground = (ImageView) view
				.findViewById(R.id.img_last_score);
		imgLastScoreBackground.setOnClickListener(this);
		imgLastScorePointer = (ImageView) view
				.findViewById(R.id.img_last_score_pointer);
		txtCurrentScore = (TextView) view.findViewById(R.id.current_score);

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
		if (userInfo != null) {
			if (userInfo.getTodayMood() != 0) {

			} else {
				if (!isDone) {
					isDone = true;
					new LoadDataTask().execute(0, 0);
				}

			}
			if (userInfo.getLastTime() != new Date()) {
				if (!isDone) {
					isDone = true;
					new LoadDataTask().execute(0, 0);
				}
			}
			if (userInfo.getDesignation() != null) {
				designation = userInfo.getDesignation();
			} else {
				if (!isDone) {
					isDone = true;
					new LoadDataTask().execute(0, 0);
				}

			}
			if (userInfo.getDeduction() != null) {
				deduction = userInfo.getDeduction();
			} else {
				if (!isDone) {
					isDone = true;
					new LoadDataTask().execute(0, 0);
				}

			}
			if (userInfo.getResolution() != null) {
				percent = userInfo.getResolution();
			} else {
				if (!isDone) {
					isDone = true;
					new LoadDataTask().execute(0, 0);
				}

			}
			isJoined = userInfo.isJoined();
		}

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
			intent.putExtra(AnalysisCharacterActivity.USER_CHARACTER_ISJOINED,
					isJoined);
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
		case R.id.img_last_score:
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
				setAllEnabled(false);

				imgMoodArrow
						.setBackgroundResource(R.drawable.today_mood_arrow_down);
				AnimationUtils.startImgBackGround(imgMoodArrow);
				AnimationUtils.performAnimateMarginTop(layoutPickMood, 1500,
						moveDistance, 500);
				//v.setEnabled(true);
			} else {
				isMoodOpen = false;
				panelClose();
			}
			break;
		case R.id.img_mood_history:
			// panelClose();
			intent = new Intent(mainActivity, MoodHistoryActivity.class);
			final Intent fi=intent;
			final View fv=v;
			new Handler().postDelayed(new Runnable(){    
			    public void run() {  
					startActivity(fi);
					fv.setEnabled(true);
			    }    
			 }, 300);   
			break;
		case R.id.img_mood_background:
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_1:
			// new LoadDataTask().execute(1, 1);
			setMoodDialog = new SetMoodDialog(mainActivity, 1);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_2:
			// new LoadDataTask().execute(1, 2);
			setMoodDialog = new SetMoodDialog(mainActivity, 2);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_3:
			// new LoadDataTask().execute(1, 3);
			setMoodDialog = new SetMoodDialog(mainActivity, 3);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_4:
			// new LoadDataTask().execute(1, 4);
			setMoodDialog = new SetMoodDialog(mainActivity, 4);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_5:
			setMoodDialog = new SetMoodDialog(mainActivity, 5);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			// new LoadDataTask().execute(1, 5);
			v.setEnabled(true);
			break;
		case R.id.layout_mood_face_6:
			setMoodDialog = new SetMoodDialog(mainActivity, 6);
			if (!setMoodDialog.isShowing()) {
				setMoodDialog.show();
			}
			// panelClose();
			// new LoadDataTask().execute(1, 6);
			v.setEnabled(true);
			break;
		case R.id.layout_main_fragment:
			panelClose();
			break;
		default:
			v.setEnabled(true);
			break;
		}
		v.getParent().requestDisallowInterceptTouchEvent(true);
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

	// 心情指数 仪表盘 动画
	private void dashBoardAnim() {
		setRetotion = (int) (270 * deduction / 100);
		if (Build.VERSION.SDK_INT < 11) {
			Animation animation4 = new RotateAnimation(90, 360,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			LinearInterpolator lin = new LinearInterpolator();
			animation4.setInterpolator(lin);
			animation4.setDuration(666);
			animation4.setFillEnabled(true);
			animation4.setFillAfter(true);
			animation4.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					Log.i("setRetotion", "setRetotion....." + setRetotion);
					Animation animation5 = new RotateAnimation(360,
							360 - setRetotion, Animation.RELATIVE_TO_SELF,
							0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

					LinearInterpolator lin = new LinearInterpolator();
					animation5.setInterpolator(lin);
					animation5.setDuration(666);
					animation5.setFillEnabled(true);
					animation5.setFillAfter(true);
					imgLastScorePointer.startAnimation(animation5);
				}
			});
			imgLastScorePointer.startAnimation(animation4);
			txtCurrentScore.setText(((int) (100 - deduction)) + "分");

		} else {
			AnimationUtils.performAnimateRoration(imgLastScorePointer, 90, 360,
					666, txtCurrentScore, 0);
			AnimationUtils.performAnimateRoration(imgLastScorePointer, 360,
					360 - setRetotion, 666, txtCurrentScore, 667);
		}

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
		
		new Handler().postDelayed(new Runnable(){    
		    public void run() {  
				setAllEnabled(true);
		    }    
		 }, 300);   
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
					result = userService.getIndexInfo();

					result.put("syncType", syncType);
					break;
				// case 1:
				// // setMood 设置用户
				// result = userService.setMood(type);
				// result.put("syncType", syncType);
				// break;
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
						userInfoDao.saveUserIndexInfo(result);
						percent = result.getDouble("resolution");
						deduction = result.getDouble("deduction");
						designation = result.getString("userSelfTitle");
						isJoined = result.getBoolean("isJoined");
						Log.i("MainmenuFragment1", "percent..." + percent
								+ "...deduction..." + deduction
								+ "...designation..." + designation
								+ "...isJoined..." + isJoined);
						characterPercent.setText(percent + "%");
						dashBoardAnim();
						String lastTime = result.getString("lastTime");
						if (MatcherUtil.isToday(lastTime)) {
							swtichPanelDown();
						} else {
							imgMoodArrow
									.setBackgroundResource(R.drawable.today_mood_arrow_up);
							AnimationUtils.startImgBackGround(imgMoodArrow);
						}
						break;
					// case 1:
					// showToast(mainActivity
					// .getString(R.string.toast_set_mood_success));
					// panelClose();
					// break;
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

	private void showToast(String content) {
		Toast.makeText(mainActivity, content, Toast.LENGTH_SHORT).show();
	}
	
	
	private void setAllEnabled(boolean enabled){
		characterPercentLayout.setEnabled(enabled);
		moodControl.setEnabled(enabled);
		character.setEnabled(enabled);
		communication.setEnabled(enabled);
		career.setEnabled(enabled);
		health.setEnabled(enabled);
		interest.setEnabled(enabled);
		recycle.setEnabled(enabled);
		imgMoodBackground.setEnabled(enabled);
		imgLastScoreBackground.setEnabled(enabled);
		imgMoodPanel.setEnabled(enabled);

		

	}
}

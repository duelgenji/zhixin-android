package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.dialog.TimePickerDialog;
import com.zhixin.domain.UserSettings;
import com.zhixin.service.UserSettingIntentService;
import com.zhixin.utils.SqlCursorLoader;

public class MoreSetting extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
DialogInterface.OnDismissListener{
	private TextView txtPageTitle;
	private ImageButton iBtnPageBack;

	private ToggleButton tBtnNewMessage_MoreOption;
	private TextView txtReceiveTime_MoreOption;
	private ToggleButton tBtnEconomize_MoreOption;
	private ToggleButton tBtnOpenToFriendAnswer_MoreOption;
	private ToggleButton tBtnOpenToFriendNews_MoreOption;
	private ToggleButton tBtnOpenToMasterAnswer_MoreOption;

	private TimePickerDialog timePickerDialog;

	private String startTime;
	private String endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);

		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_more_option));

		tBtnNewMessage_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnNewMessage_MoreOption);
		tBtnEconomize_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnEconomize_MoreOption);
		tBtnOpenToFriendAnswer_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnOpenToFriendAnswer_MoreOption);
//		tBtnOpenToFriendNews_MoreOption = (ToggleButton) this
//				.findViewById(R.id.tBtnOpenToFriendNews_MoreOption);
//		tBtnOpenToMasterAnswer_MoreOption = (ToggleButton) this
//				.findViewById(R.id.tBtnOpenToMasterAnswer_MoreOption);
		txtReceiveTime_MoreOption = (TextView) this
				.findViewById(R.id.txtReceiveTime_MoreOption);
		txtReceiveTime_MoreOption.setOnClickListener(this);

		// 加载数据
		getSupportLoaderManager().restartLoader(0, null, this);
		

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new SqlCursorLoader(this,
				"select * from user_settings limit 1;", UserSettings.class);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// 离开页面传数据
		try {
			Intent intent = new Intent(this, UserSettingIntentService.class);
			Integer iMode = tBtnEconomize_MoreOption.isChecked() ? 1 : 0;
			Integer iOA2F = tBtnOpenToFriendAnswer_MoreOption.isChecked() ? 1
					: 0;
			Integer iOA2M = tBtnOpenToMasterAnswer_MoreOption.isChecked() ? 1
					: 0;
			Integer iON2F = tBtnOpenToFriendNews_MoreOption.isChecked() ? 1 : 0;
			Integer iNewMsg = tBtnNewMessage_MoreOption.isChecked() ? 1 : 0;
			

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("iHygkwj", iOA2F);
			jsonObject.put("iGkdt", iON2F);
	//		jsonObject.put("iMzgkwj", iOA2M);
			jsonObject.put("iSllms", iMode);

			jsonObject.put("sStartTime", startTime);
			jsonObject.put("sEndTime", endTime);
			jsonObject.put("isTs", iNewMsg);
			intent.putExtra("json", jsonObject.toString());
			this.startService(intent);

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

		if (data.getCount() == 1) {
			data.moveToFirst();
			String receiveTime = "";
			//新消息通知
			Boolean bNewMsg = data.getInt(data.getColumnIndex("isTs")) == 1;
			//省流量模式
			Boolean bMode = data.getInt(data.getColumnIndex("isSllms")) == 1;
			//是否向好友公开问卷
			Boolean bOA2F = data.getInt(data.getColumnIndex("isHygkwj")) == 1;
			//是否向盟主公开问卷
			Boolean bOA2M = data.getInt(data.getColumnIndex("isMzgkwj")) == 1;
			//是否公开答题
			Boolean bON2F = data.getInt(data.getColumnIndex("isGkdt")) == 1;
			//接收通知的开始时间
			startTime = data.getString(data.getColumnIndex("kssj"));
			//接收通知的结束时间
			endTime = data.getString(data.getColumnIndex("jssj"));
			receiveTime = startTime + "-" + endTime;

			txtReceiveTime_MoreOption.setText(receiveTime);
			tBtnEconomize_MoreOption.setChecked(bMode);
			tBtnNewMessage_MoreOption.setChecked(bNewMsg);
			tBtnOpenToFriendAnswer_MoreOption.setChecked(bOA2F);
			tBtnOpenToFriendNews_MoreOption.setChecked(bON2F);
			tBtnOpenToMasterAnswer_MoreOption.setChecked(bOA2M);

			timePickerDialog = new TimePickerDialog(this,
					android.R.style.Theme_Translucent_NoTitleBar, startTime,
					endTime);
			Window window = timePickerDialog.getWindow();
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.dimAmount = 0.7f;
			timePickerDialog.getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_DIM_BEHIND);
			window.setAttributes(lp);

			txtReceiveTime_MoreOption.setOnClickListener(this);
			timePickerDialog.setOnDismissListener(this);
		}

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	@Override
	public void onClick(View v) {
		v.setEnabled(false);
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
			break;

		case R.id.txtReceiveTime_MoreOption:

			if (!timePickerDialog.isShowing()) {
				timePickerDialog.show();
			}
			break;
		default:
			break;

		}
		v.setEnabled(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (timePickerDialog.isLeaveWithConfirm()) {
			StringBuffer startTimeBuffer = new StringBuffer();
			startTimeBuffer.append(timePickerDialog.getStartHour() < 10 ? "0"
					+ String.valueOf(timePickerDialog.getStartHour()) : String
					.valueOf(timePickerDialog.getStartHour()));
			startTimeBuffer.append(":");
			startTimeBuffer.append(timePickerDialog.getStartMinute() < 10 ? "0"

			+ String.valueOf(timePickerDialog.getStartMinute()) : String
					.valueOf(timePickerDialog.getStartMinute()));

			startTime = startTimeBuffer.toString();

			StringBuffer endTimeBuffer = new StringBuffer();
			endTimeBuffer.append(timePickerDialog.getEndHour() < 10 ? "0"
					+ String.valueOf(timePickerDialog.getEndHour()) : String
					.valueOf(timePickerDialog.getEndHour()));
			endTimeBuffer.append(":");
			endTimeBuffer.append(timePickerDialog.getEndMinute() < 10 ? "0"

			+ String.valueOf(timePickerDialog.getEndMinute()) : String
					.valueOf(timePickerDialog.getEndMinute()));
			endTime = endTimeBuffer.toString();
			txtReceiveTime_MoreOption.setText(startTime + "-" + endTime);
		}

	}

}

package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.dialog.TimePickerDialog;
import com.zhixin.domain.UserSettings;
import com.zhixin.service.UserSettingIntentService;
import com.zhixin.settings.CurrentUserHelper;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
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
	
	private Button logOutBtn;

	private TimePickerDialog timePickerDialog;
	private Activity _this;

	private String startTime;
	private String endTime;
	private Boolean push;
	private Boolean saveFlow;
	private Boolean publicAnswersToFriend;
	
	boolean ischecked = false;
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			String receiveTime = "";
			switch (msg.what) {
			case 0:
				tBtnNewMessage_MoreOption.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
	//					if (push.equals("true")) {
							if (push==true) {	
							ischecked = true;
						}else {
							ischecked = false;
						}
					}
				});
				tBtnEconomize_MoreOption.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if (saveFlow.equals("true")) {
						if (saveFlow == true) {
							ischecked = true;
						}else {
							ischecked = false;
						}
					}
				});
				tBtnOpenToFriendAnswer_MoreOption.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if (publicAnswersToFriend.equals("true")) {
						if (publicAnswersToFriend   == true) {
							ischecked = true;
						}else {
							ischecked = false;
						}
					}
				});
				receiveTime = startTime + "-" + endTime;
				txtReceiveTime_MoreOption.setText(receiveTime);
				
				break;
/**				
				if(push.equals("true")){
					tBtnNewMessage_MoreOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView,
                                boolean isChecked) {
                        	tBtnNewMessage_MoreOption.setChecked(isChecked);
                        }
					});
				}else{
					tBtnNewMessage_MoreOption.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView,
                                boolean isChecked) {
                        	tBtnNewMessage_MoreOption.setChecked(isChecked);
                        }
					});
				}
					*/
//				if(saveFlow.equals("true")){
//					tBtnEconomize_MoreOption.setChecked(bMode);
//				}else{
//					tBtnEconomize_MoreOption.setChecked(bMode);
//				}
//				if(publicAnswersToFriend.equals("true")){
//					tBtnOpenToFriendAnswer_MoreOption.setChecked(bOA2F);
//				}else{
//					tBtnOpenToFriendAnswer_MoreOption.setChecked(bOA2F);
//				}
				
			
			default:
				break;
			}
		};
	};
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_setting);
		_this = this;
		txtPageTitle = (TextView) this.findViewById(R.id.title_of_the_page);
		iBtnPageBack = (ImageButton) this.findViewById(R.id.backup_btn);
		iBtnPageBack.setOnClickListener(this);
		txtPageTitle.setText(this.getString(R.string.title_more_option));

		logOutBtn = (Button) this.findViewById(R.id.logOutBtn);
		logOutBtn.setOnClickListener(this);
		
		tBtnNewMessage_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnNewMessage_MoreOption);
		tBtnNewMessage_MoreOption.setOnClickListener(this);
		tBtnEconomize_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnEconomize_MoreOption);
		tBtnEconomize_MoreOption.setOnClickListener(this);
		tBtnOpenToFriendAnswer_MoreOption = (ToggleButton) this
				.findViewById(R.id.tBtnOpenToFriendAnswer_MoreOption);
		tBtnOpenToFriendAnswer_MoreOption.setOnClickListener(this);
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
			Integer iNewMsg = tBtnNewMessage_MoreOption.isChecked() ? 1 : 0;
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("iHygkwj", iOA2F);
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
			//接收通知的开始时间
			startTime = data.getString(data.getColumnIndex("kssj"));
			//接收通知的结束时间
			endTime = data.getString(data.getColumnIndex("jssj"));
			receiveTime = startTime + "-" + endTime;

			txtReceiveTime_MoreOption.setText(receiveTime);
			tBtnEconomize_MoreOption.setChecked(bMode);
			tBtnNewMessage_MoreOption.setChecked(bNewMsg);
			tBtnOpenToFriendAnswer_MoreOption.setChecked(bOA2F);

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
		case R.id.tBtnEconomize_MoreOption:
			tBtnEconomize_MoreOption.setChecked(true);
			break;
		case R.id.logOutBtn:
			logOut();
			break;
		default:
			break;

		}
		v.setEnabled(true);
	}

	private void logOut() {
		// TODO Auto-generated method stub
		CurrentUserHelper.clearCurrentPhone();
		Intent intent = new Intent(MoreSetting.this, InitialActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		this.finish();
		HttpClient.clearHttpCache();
	}
	@Override
	protected void onResume() {
		super.onResume();
		StatService.onResume(this);
//		long userId = CurrentUserHelper.getCurrentUserId();
		 String requestUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_USER_SETTING);
//	        requestUrl+="/"+userId;
		new LoadDataTask().execute(1,requestUrl,null,HttpClient.TYPE_GET);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}
	
	private class LoadDataTask extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result=null;
			Integer syncType=(Integer)params[0];
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST
					result = HttpClient.requestSync(params[1].toString(), null,(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				case 2:
					result = HttpClient.requestSync(params[1].toString(), (JSONObject)params[2],(Integer)params[3]);
					result.put("syncType", syncType);
					break;
				default :
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
				Integer syncType=result.getInt("syncType");
				switch(syncType){
				case 1:
					if (result != null && result.getInt("success") == 1) {
		                //。。。。。。。。。
						Toast.makeText(_this, "获取设置成功！", Toast.LENGTH_SHORT).show();
						
						push = result.getBoolean("push");
						startTime = result.getString("startTime");
						endTime = result.getString("endTime");
						saveFlow = result.getBoolean("saveFlow");
						publicAnswersToFriend = result.getBoolean("publicAnswersToFriend");
						Message msg = Message.obtain();
						msg.what = 0;
						handler.sendMessage(msg);
						
						
					}else {
						Toast.makeText(_this, "获取设置失败！", Toast.LENGTH_SHORT).show();
					}
					break;
				case 2:
					if (result != null && result.getInt("success") == 1) {
						//。。。。。。。。。
						Toast.makeText(_this, "修改个人资料成功！", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(_this, "修改数据失败！", Toast.LENGTH_SHORT).show();
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

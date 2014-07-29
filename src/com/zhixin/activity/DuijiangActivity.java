package com.zhixin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhixin.R;
import com.zhixin.adapter.QuduijiangListAdapter;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.settings.SettingValues;

public class DuijiangActivity extends Activity implements
LoaderManager.LoaderCallbacks<Cursor>,
CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private GridView gridView;
	private ImageButton backup_btn;
	private DuijiangActivity mainActivity;
	
	private QuduijiangListAdapter adapter;
	//跳转到兑奖历史的按钮
	private ImageView duijianggift;
	private QubaopenProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_user_info_duijiang);
    	//页面名称
    	((TextView)findViewById(R.id.title_of_the_page))
		.setText(getString(R.string.title_quduijiang));
    	
    	gridView = (GridView)findViewById(R.id.duijiangList);
		backup_btn = (ImageButton)findViewById(R.id.backup_btn);
		backup_btn.setVisibility(View.INVISIBLE);
		duijianggift = (ImageView) findViewById(R.id.duijianggift);
		duijianggift.setOnClickListener(this);
		
//		参数添加错误
		progressDialog = new QubaopenProgressDialog(this);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				//连接后台数据接口
				SharedPreferences sharedPref = mainActivity
						.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
								Context.MODE_PRIVATE);
				return sharedPref.getBoolean(
						SettingValues.INSTRUCTION_DUIJIANG, true);
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					//是否刷新UI
					new InstructionDialog(mainActivity,
							SettingValues.INSTRUCTION_DUIJIANG).show();
				}
			}

		}.execute();
		
		
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.backup_btn:
			
			break;
		case R.id.duijianggift:
			Intent intent = new Intent(DuijiangActivity.this, DuijiangHistoryActivity.class);
			startActivity(intent);
			v.setEnabled(true);
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		// TODO Auto-generated method stub
		adapter = new QuduijiangListAdapter(mainActivity, cursor);
		gridView.setAdapter(adapter);
		adapter.changeCursor(cursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		adapter.changeCursor(null);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		progressDialog.dismiss();
	}
}

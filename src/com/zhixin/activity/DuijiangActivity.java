package com.zhixin.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.settings.SettingValues;
/**
 * 兑奖历史的页面，页头的状态还未添加
 * @author Administrator
 *
 */
public class DuijiangActivity extends Activity implements
LoaderManager.LoaderCallbacks<Cursor>,
CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private GridView gridView;
	private ImageButton backup_btn;
	private DuijiangActivity mainActivity;
	
	private Context context;
	
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
		context = this.getApplicationContext();
		SharedPreferences sharedPref = this.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		
//		参数添加错误
		progressDialog = new QubaopenProgressDialog(this);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
/**		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
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
 */
    }
	@Override
	public void onClick(View v) {
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
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter = new QuduijiangListAdapter(mainActivity, cursor);
		gridView.setAdapter(adapter);
		adapter.changeCursor(cursor);
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.changeCursor(null);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		progressDialog.dismiss();
	}
}

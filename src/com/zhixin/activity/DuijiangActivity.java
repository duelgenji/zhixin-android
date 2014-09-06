package com.zhixin.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhixin.R;
import com.zhixin.adapter.QuduijiangListAdapter;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.HttpClient;
/**
 * 兑奖历史的页面，页头的状态还未添加
 * @author Administrator
 *
 */
public class DuijiangActivity extends FragmentActivity implements
LoaderManager.LoaderCallbacks<Cursor>,
CompoundButton.OnCheckedChangeListener, View.OnClickListener {
	private GridView gridView;
	private ImageButton backup_btn;
	private TextView pageTitle;
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
    	initView();
		context = this.getApplicationContext();
		SharedPreferences sharedPref = this.getSharedPreferences(SettingValues.FILE_NAME_SETTINGS,
						Context.MODE_PRIVATE);
		 String requestUrl = SettingValues.URL_PREFIX
					+ getString(R.string.URL_GET_GIFT_LIST);
		new LoadDataTask().execute(1,requestUrl,null,HttpClient.TYPE_GET);
    }
    
    private void initView(){
    	pageTitle = (TextView)findViewById(R.id.title_of_the_page);
    	pageTitle.setText(getString(R.string.title_quduijiang));
    	
    	backup_btn = (ImageButton)findViewById(R.id.backup_btn);
    	backup_btn.setOnClickListener(this);
    	
    	duijianggift = (ImageView) findViewById(R.id.duijianggift);
    	duijianggift.setOnClickListener(this);
    	
    	gridView = (GridView)findViewById(R.id.duijianglist);
    	
    	progressDialog = new QubaopenProgressDialog(this);
    	if (!progressDialog.isShowing()) {
    		progressDialog.show();
    	}
    }
    
    private class LoadDataTask extends AsyncTask<Object, Void, JSONObject>{

		@Override
		protected JSONObject doInBackground(Object... params) {
			JSONObject result = null;
			Integer syncType=(Integer)params[0];
			try {
				switch(syncType){
				case 1:
					//null。。。。传参方式是get
					//(Integer)params[3]对应上面的HttpClient.TYPE_POST
					result = HttpClient.requestSync(params[1].toString(), null,(Integer)params[3]);
					Log.i("兑奖列表请求结果", result+"");
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
					if (result != null && result.getString("success").equals("1")) {
		                //。。。。。。。。。
						
						Toast.makeText(mainActivity, "获取兑奖列表成功！", Toast.LENGTH_SHORT).show();
						
					}else {
						Toast.makeText(mainActivity, "获取兑奖列表失败！", Toast.LENGTH_SHORT).show();
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
    protected void onStart() {
    	super.onStart();
    	getSupportLoaderManager().restartLoader(0, null, this);
    	progressDialog.dismiss();
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			this.onBackPressed();
			v.setEnabled(true);
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
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}

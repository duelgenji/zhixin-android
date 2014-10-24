package com.zhixin.activity;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.SelfListAdapter;
import com.zhixin.daos.UserInfoDao;
import com.zhixin.datasynservice.SelfListService;
import com.zhixin.dialog.InstructionDialog;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.SelfList;
import com.zhixin.domain.UserInfo;
import com.zhixin.settings.SettingValues;
import com.zhixin.utils.SqlCursorLoader;

public class SelfListActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	/**趣测试列表 */
	private ListView selfList;
	
	private RelativeLayout layoutSelfListEmpty;
	/**loading */
	private QubaopenProgressDialog progressDialog;
	
	private SelfListAdapter adapter;
	
	private SelfListService selfListService;

	private SelfListActivity _this;


	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	/**下拉刷新*/
	private SwipeRefreshLayout selfListParent;

	private class LoadDataTask extends AsyncTask<Integer, Void, String> {
		private boolean refreshFlag;

		public LoadDataTask(boolean refreshFlag) {
			super();
			this.refreshFlag = refreshFlag;
		}

		@Override
		protected String doInBackground(Integer... params) {
			return selfListService.requestSelfList(refreshFlag);
		}

		@Override
		protected void onPostExecute(String params) {
			Bundle bundle=new Bundle();
			if(params==null){
				params="";
			}
			bundle.putString("ids", params);
			getSupportLoaderManager().restartLoader(0, bundle, _this);
			if (selfListParent.isRefreshing()){
				selfListParent.setRefreshing(false);
			}
			
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (selfListParent.isRefreshing()){
				selfListParent.setRefreshing(false);
			}
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_list);
		_this = this;
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		return new SqlCursorLoader(this, SelfListService.SelfListSqlMaker.makeSql(bundle.getString("ids")), SelfList.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if(cursor.getCount()==0){
			selfListParent.setVisibility(View.GONE);
			layoutSelfListEmpty.setVisibility(View.VISIBLE);
		}else{
			selfListParent.setVisibility(View.VISIBLE);
			layoutSelfListEmpty.setVisibility(View.GONE);
		}
		if (adapter == null) {
			adapter = new SelfListAdapter(this, cursor);
			selfList.setAdapter(adapter);
		} else {
			adapter.changeCursor(cursor);
		}

		selfList.setOnScrollListener(this);

		if (progressDialog.isShowing()
				&& refreshDataTask.getStatus() == AsyncTask.Status.FINISHED) {
			progressDialog.dismiss();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		if (adapter != null) {
			adapter.changeCursor(null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backup_btn:
			finish();
			break;
		default:
			break;
		}

	}

	private void init() {
		selfListService = new SelfListService(this);
		progressDialog = new QubaopenProgressDialog(this);
		selfListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.selfListParent);
		layoutSelfListEmpty =(RelativeLayout) this.findViewById(R.id.layoutSelfListEmpty);
		selfListParent.setColorScheme(R.color.know_heart_theme,
				R.color.general_activity_background, R.color.know_heart_theme,
				R.color.general_activity_background);
		selfListParent.setOnRefreshListener(this);
	
		((TextView)findViewById(R.id.title_of_the_page))
		.setText("心理自测");

		findViewById(R.id.backup_btn).setOnClickListener(this);
		selfList = (ListView) this.findViewById(R.id.selfList);

		refreshDataTask = new LoadDataTask(false);
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute();

		//app第一次到此页面     有教学图片
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				SharedPreferences sharedPref = _this.getSharedPreferences(
						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				UserInfo userInfo =new UserInfoDao().getCurrentUser();
				
				
				if(userInfo!=null && (userInfo.getSex()==2 || StringUtils.isEmpty(userInfo.getBirthDay()))){
					return 2;
				}else if(userInfo==null){
					return 2;
				}else if(sharedPref.getBoolean(
						SettingValues.INSTRUCTION_SELF_LIST, true)){
					return 1;
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer result) {
				if (result==1) {
					InstructionDialog instructionDialog = new InstructionDialog(
							_this, SettingValues.INSTRUCTION_SELF_LIST);
					instructionDialog.show();
				}else if (result==2){
					Intent intent = new Intent(SelfListActivity.this, SelectAgeSexActivity.class);
					startActivity(intent);
				}
			}
		}.execute();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		this.currentFirstVisibleItem = firstVisibleItem;
		this.currentVisibleItemCount = visibleItemCount;
		this.currentTotalItemCount = totalItemCount;
	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.currentScrollState = scrollState;
		this.isScrollCompletedAndTouchedBottom();

	}

	private void isScrollCompletedAndTouchedBottom() {
		if (this.currentVisibleItemCount > 0
				&& this.currentScrollState == SCROLL_STATE_IDLE) {
			if ((this.currentFirstVisibleItem + this.currentVisibleItemCount) == this.currentTotalItemCount) {
				if (refreshDataTask.getStatus() != AsyncTask.Status.RUNNING) {
					refreshDataTask = new LoadDataTask(true);
					if (!progressDialog.isShowing()) {
						progressDialog.show();
					}
					refreshDataTask.execute();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
		StatService.onPause(this);
	}

	@Override
	public void onRefresh() {
		if (refreshDataTask.getStatus() != AsyncTask.Status.RUNNING) {
			refreshDataTask = new LoadDataTask(true);
			// if (!progressDialog.isShowing()) {
			// progressDialog.show();
			// }
			refreshDataTask.execute();
		}

	}
}

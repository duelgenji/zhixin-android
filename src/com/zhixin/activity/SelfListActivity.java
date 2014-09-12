package com.zhixin.activity;

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
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.zhixin.R;
import com.zhixin.adapter.SelfListAdapter;
import com.zhixin.datasynservice.SelfListService;
import com.zhixin.dialog.QubaopenProgressDialog;
import com.zhixin.domain.SelfList;
import com.zhixin.utils.SqlCursorLoader;

public class SelfListActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	/**趣测试列表 */
	private ListView selfList;
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
			this.onBackPressed();
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
//		new AsyncTask<Void, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Void... params) {
//				SharedPreferences sharedPref = _this.getSharedPreferences(
//						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
//				return sharedPref.getBoolean(
//						SettingValues.INSTRUCTION_QUCESHI_LIST1, true);
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				if (result) {
//					InstructionDialog qushouyeFirst = new InstructionDialog(
//							_this, SettingValues.INSTRUCTION_QUCESHI_LIST1);
//					qushouyeFirst.setOnDismissListener(new OnDismissListener() {
//						@Override
//						public void onDismiss(DialogInterface dialog) {
//							new InstructionDialog(_this,
//									SettingValues.INSTRUCTION_QUCESHI_LIST2)
//									.show();
//						}
//					});
//					qushouyeFirst.show();
//				}
//			}
//		}.execute();
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

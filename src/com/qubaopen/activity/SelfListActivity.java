package com.qubaopen.activity;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.qubaopen.R;
import com.qubaopen.adapter.SelfListAdapter;
import com.qubaopen.daos.UserInfoDao;
import com.qubaopen.datasynservice.SelfListService;
import com.qubaopen.dialog.InstructionDialog;
import com.qubaopen.dialog.QubaopenProgressDialog;
import com.qubaopen.domain.SelfList;
import com.qubaopen.domain.UserInfo;
import com.qubaopen.settings.SettingValues;
import com.qubaopen.utils.SqlCursorLoader;

public class SelfListActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener,
		OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
	/** 列表类型 */
	public static String SELF_LIST_TYPE = "selfListType";
	private int type;
	/** 测试列表 */
	private ListView selfList;

	private RelativeLayout layoutSelfListEmpty;
	/** loading */
	private QubaopenProgressDialog progressDialog;

	private SelfListAdapter adapter;
	private SelfListService selfListService;

	private SelfListActivity _this;

	private TextView title;
	private int currentFirstVisibleItem;
	private int currentVisibleItemCount;
	private int currentTotalItemCount;
	private int currentScrollState;

	private LoadDataTask refreshDataTask;

	/** 下拉刷新 */
	private SwipeRefreshLayout selfListParent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_self_list);
		_this = this;
		initIntent();
		init();
	}

	private void init() {
		selfListService = new SelfListService(this);
		progressDialog = new QubaopenProgressDialog(this);
		selfListParent = (SwipeRefreshLayout) this
				.findViewById(R.id.selfListParent);
		layoutSelfListEmpty = (RelativeLayout) this
				.findViewById(R.id.layoutSelfListEmpty);
		selfListParent.setColorScheme(R.color.know_heart_theme,
				R.color.general_activity_background, R.color.know_heart_theme,
				R.color.general_activity_background);
		selfListParent.setOnRefreshListener(this);

		title = (TextView) findViewById(R.id.title_of_the_page);
		if (type == 1) {
			title.setText(getString(R.string.self_list_title_1));
		}else if (type == 2) {
			title.setText(getString(R.string.self_list_title_2));
		}else if (type == 3) {
			title.setText(getString(R.string.self_list_title_3));
		}else if (type == 4) {
			title.setText(getString(R.string.self_list_title_4));
		}

		findViewById(R.id.backup_btn).setOnClickListener(this);
		selfList = (ListView) this.findViewById(R.id.selfList);
		// //原方法（带刷新）
		// refreshDataTask = new LoadDataTask(false);
		// 新方法
		refreshDataTask = new LoadDataTask();
		if (!progressDialog.isShowing()) {
			progressDialog.show();
		}
		refreshDataTask.execute();

		// app第一次到此页面 有教学图片
		new AsyncTask<Void, Void, Integer>() {
			@Override
			protected Integer doInBackground(Void... params) {
				SharedPreferences sharedPref = _this.getSharedPreferences(
						SettingValues.FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
				UserInfo userInfo = new UserInfoDao().getCurrentUser();

				if (userInfo != null
						&& (userInfo.getSex() == 2 || StringUtils
								.isEmpty(userInfo.getBirthDay()))) {
					return 2;
				} else if (userInfo == null) {
					return 2;
				} else if (sharedPref.getBoolean(
						SettingValues.INSTRUCTION_SELF_LIST, true)) {
					return 1;
				}
				return 0;
			}

			@Override
			protected void onPostExecute(Integer result) {
				if (result == 1) {
					InstructionDialog instructionDialog = new InstructionDialog(
							_this, SettingValues.INSTRUCTION_SELF_LIST);
					instructionDialog.show();
				} else if (result == 2) {
					Intent intent = new Intent(SelfListActivity.this,
							SelectAgeSexActivity.class);
					startActivity(intent);
				}
			}
		}.execute();
	}

	private void initIntent() {
		type = getIntent().getIntExtra(SELF_LIST_TYPE, 0);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	private class LoadDataTask extends AsyncTask<Integer, Void, String> {
		// private boolean refreshFlag;
		//
		// public LoadDataTask(boolean refreshFlag) {
		// super();
		// this.refreshFlag = refreshFlag;
		// }

		@Override
		protected String doInBackground(Integer... params) {
			// //原方法（带刷新）
			// return selfListService.requestSelfList(refreshFlag,type);
			// 新方法
			return selfListService.requestSelfList(type);
		}

		@Override
		protected void onPostExecute(String params) {
			Bundle bundle = new Bundle();
			if (params == null) {
				params = "";
			}
			bundle.putString("ids", params);
			getSupportLoaderManager().restartLoader(0, bundle, _this);
			if (selfListParent.isRefreshing()) {
				selfListParent.setRefreshing(false);
			}

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
			if (selfListParent.isRefreshing()) {
				selfListParent.setRefreshing(false);
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

		return new SqlCursorLoader(this,
				SelfListService.SelfListSqlMaker.makeSql(bundle
						.getString("ids")), SelfList.class);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		if (cursor.getCount() == 0) {
			selfListParent.setVisibility(View.GONE);
			layoutSelfListEmpty.setVisibility(View.VISIBLE);
		} else {
			selfListParent.setVisibility(View.VISIBLE);
			layoutSelfListEmpty.setVisibility(View.GONE);
		}
		if (adapter == null) {
			adapter = new SelfListAdapter(this, cursor,type);
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
					// //原方法（带刷新）
					// refreshDataTask = new LoadDataTask(false);
					// 新方法
					refreshDataTask = new LoadDataTask();
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
			// //原方法（带刷新）
			// refreshDataTask = new LoadDataTask(false);
			// 新方法
			refreshDataTask = new LoadDataTask();
			// if (!progressDialog.isShowing()) {
			// progressDialog.show();
			// }
			refreshDataTask.execute();
		}

	}
}
